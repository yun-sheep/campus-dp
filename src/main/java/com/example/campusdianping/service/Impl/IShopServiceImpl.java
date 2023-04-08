package com.example.campusdianping.service.Impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campusdianping.common.constant.RedisConstants;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.RedisData;
import com.example.campusdianping.entity.shop.Shop;
import com.example.campusdianping.mapper.ShopMapper;
import com.example.campusdianping.service.IShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Description shop的实现类
 * @auther j2-yizhiyang
 * @date 2023/4/5 11:27
 */
@Service
public class IShopServiceImpl  extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //创建线程池
    private static final ExecutorService CACHE_REBUID_EXECUTOR = Executors.newFixedThreadPool(10);
    @Override
    public Result queryById(Long id) {
        return null;
    }
    //
    @Override
    @Transactional
    public Result update(Shop shop) {
        return null;
    }
    //这里可以优化一下
    @Override
    public Result queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        return null;
    }
    /**
     * 预热缓存key
     * */
    @PostConstruct
    public void sortShopType(){

    }
    /**
     * 使用逻辑过期来解决缓存击穿的问题
     * */
    public Shop queryLogicalExpire(Long id){
        String key = id.toString();
        //1、从redis中查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        //2、判断是否存在
        if(StrUtil.isBlank(shopJson)){
            return  null;
        }
        RedisData redisData = JSONUtil.toBean(shopJson,RedisData.class);
        Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class );
        LocalDateTime expireTime = redisData.getExpireTime();
        if(expireTime.isAfter(LocalDateTime.now())){
            return shop;
        }
        //重建缓存
        String cacheKey = RedisConstants.LOCK_SHOP_KEY;
        boolean islocak = trylocak(cacheKey);
        if(islocak){
            //获取到锁了，要再次进行一个doublechek
            RedisData newredisData = JSONUtil.toBean(shopJson,RedisData.class);
            LocalDateTime newexpireTime = redisData.getExpireTime();
            if(expireTime.isAfter(LocalDateTime.now())){
                return JSONUtil.toBean((JSONObject) redisData.getData(),Shop.class );
            }
            //开启独立线程去做缓存重建
            CACHE_REBUID_EXECUTOR.submit(()->{

                try {
                    //TODO 重建缓存，释放锁
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    //TODO 释放锁
                }

            });
        }
        return  null;
        //fastjson支持泛型RedisData<Shop> Data = JSON.parseObject(shopJson,new TypeReference<RedisData<Shop>>(){})
        //RedisData<Shop> Data = JSON.parseObject(shopJson,new TypeReference<RedisData<Shop>>(){})
    }
    //使用redis锁的问题
    private boolean trylocak(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }
}
