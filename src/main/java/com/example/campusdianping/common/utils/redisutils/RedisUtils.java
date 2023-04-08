package com.example.campusdianping.common.utils.redisutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis工具类
 * @auther j2-yizhiyang
 * @date 2023/4/3 19:48
 */
@Slf4j
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //获取对象
    public Object get(String key) {
        Object value = null;
        try {
            value = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisUtils#get fail! e:{" + e.getStackTrace().toString() + "}");
        }
        return value;
    }
    //判断是否存在
    public boolean isExist(String key) {
        if (get(key) != null) {
            return true;
        }
        return false;
    }
    /**
     *
     * 添加元素，并且设置过期时间
     * */
    public int add(String key, Object value, Long timeout, TimeUnit timeUnit) {
        try {
            if (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit))) {
                // 添加失败
                return -1;
            }

        } catch (Exception e) {
            log.error("RedisUtils#add fail! e:{" + e.getStackTrace().toString() + "}");
            return -1;
        }
        return 0;
    }
    /**
     *
     * 添加元素，没有设置过期时间
     * */
    public int add(String key, Object value) {
        try {
            if (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, value))) {
                // 添加失败
                log.error("RedisUtils#add fail");
                return -1;
            }

        } catch (Exception e) {
            log.error("RedisUtils#add fail! e:{" + e.getStackTrace().toString() + "}");
            return -1;
        }
        return 0;
    }
    /**
     *
     * 根据key删除元素
     * */
    public int del(String key) {
        try {
            if (Boolean.FALSE.equals(redisTemplate.delete(key))) {
                return -1;
            }
        } catch (Exception e) {
            log.error("RedisUtils#del fail! e:{" + e.getStackTrace().toString() + "}");
            return -1;
        }

        return 0;
    }
    /**
     *
     * 获取对应key的过期时间
     * */
    public Long ttl(String key) {
        Long expiredTime;
        try {
            expiredTime = redisTemplate.getExpire(key);
        } catch (Exception e) {
            log.error("RedisUtils#setEx fail! e:{" + e.getStackTrace().toString() + "}");
            return -1L;
        }
        return expiredTime;
    }
}
