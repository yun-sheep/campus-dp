package com.example.campusdianping.common.utils.lock;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Description 分布式锁实现类
 * @auther j2-yizhiyang
 * @date 2023/4/18 18:36
 */
public class SimpleRedisLock implements Ilock{
    private String name;
    private StringRedisTemplate stringRedisTemplate;
    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";
     //
    @Override
    public boolean tryLock(long timeoutSec) {
        long threadId = Thread.currentThread().getId();
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX+name,
                threadId+"",timeoutSec, TimeUnit.SECONDS);
        //拆箱，可能出现空指针
        return BooleanUtil.isTrue(success);
    }

    @Override
    public void unlock() {

    }



}
