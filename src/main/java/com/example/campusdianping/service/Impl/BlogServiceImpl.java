package com.example.campusdianping.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campusdianping.common.constant.RedisConstants;
import com.example.campusdianping.common.constant.SystemConstants;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.common.domian.UserHolder;
import com.example.campusdianping.common.domian.blog.BlogVO;
import com.example.campusdianping.common.domian.user.UserVO;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import com.example.campusdianping.entity.SecurityUser;
import com.example.campusdianping.entity.User;
import com.example.campusdianping.entity.blog.Blog;
import com.example.campusdianping.entity.follow.Follow;
import com.example.campusdianping.mapper.BlogMapper;
import com.example.campusdianping.service.IBlogService;
import com.example.campusdianping.service.IFollowService;
import com.example.campusdianping.service.IUserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.campusdianping.common.constant.RedisConstants.BLOG_LIKED_KEY;
import static com.example.campusdianping.common.constant.RedisConstants.FEED_KEY;

/**
 * @Description blog服务实现类
 * @auther j2-yizhiyang
 * @date 2023/4/3 19:48
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IFollowService followService;

    @Resource
    private  RedisUtils redisUtils;


    /**
     * 首页查询热门blog，按照点赞高的返回
     * */
    //TODO 这里可以进行优化（比如：刷新之后数据怎么推送的，每隔一个小时统计点赞数飙升的？）
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        List<BlogVO> list = new ArrayList<>();
        for(Blog blog:records){
            BlogVO blogVO = BeanUtil.copyProperties(blog,BlogVO.class);
            this.queryBlogUser(blogVO);
            this.isBlogLiked(blogVO);
            list.add(blogVO);
        }
        return Result.ok(list);
    }
    /**
     * 根据查询某个blog的详情信息
     * @param id blog id
     * @return Result 携带blogVO的返回格式
     */
    @Override
    public Result queryBlogById(Long id) {
        // 1.查询blog（先查redis中，不存在则查数据库，然后放到redis中，设置过期时间
        //   在redis中发现过期时间小于阈值，则刷新过期时间
        String key = RedisConstants.BLOG_KEY+id;
        Blog blog;
        if (!redisUtils.isExist(key)) {
           blog = getById(id);
            redisUtils.add(key,blog,60*60L, TimeUnit.SECONDS);
        }
        else {
            blog  = (Blog) redisUtils.get(key);
            if(redisUtils.ttl(key)<60L){
                //TODO 刷新过期时间（写在redis工具类里面的了）
                int i = 0;
            }
        }
        if (blog == null) {
            //TODO 设置null对象（防止穿透）
            return Result.fail("笔记不存在！");
        }
        //转换成blogVo
        BlogVO blogVO = BeanUtil.copyProperties(blog,BlogVO.class);
        // 2.查询blog有关的用户
        queryBlogUser(blogVO);
        // 3.查询blog是否被点赞
        isBlogLiked(blogVO);
        return Result.ok(blogVO);
    }
    /**
     * 当前用户是否点赞过该blog
     * @param blogvo
     *
    */
    private void isBlogLiked(BlogVO blogvo) {
        // 1.获取登录用户
        SecurityUser user = UserHolder.getUser();
        if (user == null) {
            // 用户未登录，无需查询是否点赞
            return;
        }
        Long userId = user.getId();
        // 2.判断当前登录用户是否已经点赞
        String key = "blog:liked:" + blogvo.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        //如果已经点赞了就直接取消点赞
        blogvo.setIsLike(score != null);
    }
    /**
     * 查询当前blog的对应的用户信息
     * @param blogvo 当前博客信息
     *
    */
    private void queryBlogUser(BlogVO blogvo) {
        Long userId = blogvo.getUserId();
        User user = userService.getById(userId);
        blogvo.setName(user.getNickName());
        blogvo.setIcon(user.getIcon());
    }

     /** 用户点赞blog
     */
    @Override
    public Result likeBlog(Long id) {
        // 1.获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 2.判断当前登录用户是否已经点赞
        String key = BLOG_LIKED_KEY + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if (score == null) {
            // 3.如果未点赞，可以点赞
            // 3.1.数据库点赞数 + 1
            //TODO 定时任务将redis里面的点赞写入数据库
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2.保存用户到Redis的set集合  zadd key value score
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        } else {
            // 4.如果已点赞，取消点赞
            // 4.1.数据库点赞数 -1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            // 4.2.把用户从Redis的set集合移除
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return Result.ok();
    }

    @Override
    public Result queryBlogLikes(Long id) {
        String key = BLOG_LIKED_KEY + id;
        // 1.查询top5的点赞用户 zrange key 0 4
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5 == null || top5.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
        // 2.解析出其中的用户id
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        // 3.根据用户id查询用户 WHERE id IN ( 5 , 1 ) ORDER BY FIELD(id, 5, 1)
        List<UserVO> userDTOS = userService.query()
                .in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserVO.class))
                .collect(Collectors.toList());
        // 4.返回
        return Result.ok(userDTOS);
    }
    /**
     * 发布保存blog
     */

    @Override
    public Result saveBlog(Blog blog) {
        // 1.获取登录用户
        SecurityUser user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 2.保存探店笔记
        boolean isSuccess = save(blog);
        if(!isSuccess){
            return Result.fail("新增笔记失败!");
        }
        // 3.查询笔记作者的所有粉丝 select * from tb_follow where follow_user_id = ?
        List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
        // TODO 可以写成异步的（消息队列来做）
        for (Follow follow : follows) {
            // 4.1.获取粉丝id
            Long userId = follow.getUserId();
            // 4.2.推送
            String key = FEED_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
        }
        // 5.返回id
        return Result.ok(blog.getId());
    }
    /**
     * 从收件箱中查询出用户关注发布的blog(Feed()流中的推模式）
     * */
    @Override
    public Result queryBlogOfFollow(Long max, Integer offset) {
        // 1.获取当前用户
        /*Long userId = UserHolder.getUser().getId();
        // 2.查询收件箱 ZREVRANGEBYSCORE key Max Min LIMIT offset count
        String key = FEED_KEY + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 2);
        // 3.非空判断
        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.ok();
        }
        // 4.解析数据：blogId、minTime（时间戳）、offset
        List<Long> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0; // 2
        int os = 1; // 2
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) { // 5 4 4 2 2
            // 4.1.获取id
            ids.add(Long.valueOf(tuple.getValue()));
            // 4.2.获取分数(时间戳）
            long time = tuple.getScore().longValue();
            if (time == minTime) {
                os++;
            } else {
                minTime = time;
                os = 1;
            }
        }

        // 5.根据id查询blog
        String idStr = StrUtil.join(",", ids);
        List<Blog> blogs = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();

        for (Blog blog : blogs) {
            // 5.1.查询blog有关的用户
            queryBlogUser(blog);
            // 5.2.查询blog是否被点赞
            isBlogLiked(blog);
        }

        // 6.封装并返回
        ScrollResult r = new ScrollResult();
        r.setList(blogs);
        r.setOffset(os);
        r.setMinTime(minTime);*/
        return null;
    }

        //return Result.ok(null);



}
