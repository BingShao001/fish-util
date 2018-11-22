package com.yb.fish.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class RedissionLock implements InterfaceRedisLock {
    Redisson redisson = null;

    {
        redisson = (Redisson) Redisson.create();
    }
    /**
     * @param waitTime  尝试加锁最多等待时间
     * @param leaseTime 上锁以后解锁时间
     */
    @Override
    public boolean lock(String bussinessKey, int waitTime, int leaseTime) throws InterruptedException {
        RLock lock = redisson.getLock(bussinessKey);
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * @param waitTime 上锁以后解锁时间(避免产生死锁)
     */
    @Override
    public boolean lock(String bussinessKey, int waitTime) throws InterruptedException {
        RLock lock = redisson.getLock(bussinessKey);
        return lock.tryLock(waitTime, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String bussinessKey) {
        Redisson redisson = (Redisson) Redisson.create();
        RLock lock = redisson.getLock(bussinessKey);
        lock.lock();
    }

    @Override
    public void unLock(String bussinessKey) {
        RLock lock = redisson.getLock(bussinessKey);
        lock.unlock();
    }
}
