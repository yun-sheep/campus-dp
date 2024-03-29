package com.example.campusdianping.common.utils.lock;

/**
 * @Description 分布式锁接口
 * @auther j2-yizhiyang
 * @date 2023/4/18 18:34
 */
public interface Ilock {
    /**
     * 尝试获取锁
     * @param timeoutSec 锁持有的超时时间，过期后自动释放
     * @return true代表获取锁成功; false代表获取锁失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();

}
