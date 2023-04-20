package com.example.campusdianping.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campusdianping.entity.blog.Blog;
import com.example.campusdianping.mapper.BlogMapper;
import com.example.campusdianping.service.ILikeTodbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

import static com.example.campusdianping.common.constant.RedisConstants.BLOG_LIKED_COUNT_KEY;

/**
 * @Description blog点赞数写入db定时任务实现类
 * @auther j2-yizhiyang
 * @date 2023/4/19 16:24
 */
@Slf4j
@Service
public class LikeTodbServiceImpl extends ServiceImpl<BlogMapper, Blog> implements ILikeTodbService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private LikeTodbServiceImpl likeTodbService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public void LikeCountTodb() {

        Map<Object, Object> map = redisTemplate.opsForHash().entries(BLOG_LIKED_COUNT_KEY);
        //redis里面整个键值对（手动移除，这样做真的效率不行吧,如果正在删除的时候要写入怎么办）
        //一个key一个key的删，删之前二次判断（全部一次性删除，可能时间太久，影响点赞）
        //stringRedisTemplate.opsForHash().delete(BLOG_LIKED_COUNT_KEY);
        //用代理对象（自己注入自己）否则事务会失效
        for (Object key : map.keySet()) {
            Integer value = (Integer) map.get(key);
            Long blogId = (Long) key;
            log.info("value"+value);
            likeTodbService.Saveall(blogId, value);
        }
    }
    //二次检查，然后删除
    @Transactional
    public void Saveall(Object blogId,Object value){
            //删除（删除之前再次查询是否和获得的点赞数一致）
            Long blog_id = (Long) blogId;
            Integer count = (Integer) value;
            Integer now_count = (Integer) redisTemplate.opsForHash().get(BLOG_LIKED_COUNT_KEY,blogId);
           if(!now_count.equals(count)){
               count = now_count;
               redisTemplate.opsForHash().delete(BLOG_LIKED_COUNT_KEY,blogId);
           }
           else {
               redisTemplate.opsForHash().delete(BLOG_LIKED_COUNT_KEY,blogId);
           }
            UpdateWrapper<Blog> blogUpdateWrapper = new UpdateWrapper<>();
            blogUpdateWrapper.set("liked",count);
            blogUpdateWrapper.eq("id",blog_id);
            update(blogUpdateWrapper);
    }
}
