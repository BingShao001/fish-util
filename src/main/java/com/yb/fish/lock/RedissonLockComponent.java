package com.yb.fish.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockComponent {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 带返回参数的锁
     *
     * @param lockKey
     * @param lockTime  锁时间
     * @param leaseTime 持锁最长时间(为了防止死锁时间过长，源代码默认30S)
     * @param timeUnit
     * @param processor
     * @param <T>
     * @return
     */
    public <T> T commonLock(String lockKey, long lockTime, long leaseTime, TimeUnit timeUnit, LockProcessorWithReturn<T> processor) {
        RLock rlock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            if (rlock.tryLock(lockTime, leaseTime, timeUnit)) {
                return processor.process();
            }
            throw new RuntimeException("Could not acquire lock for key: " + lockKey);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while acquiring lock for key: " + lockKey, e);
        } finally {
            if (locked && rlock.isHeldByCurrentThread()) {
                rlock.unlock();
            }
        }

    }
    /**
     * 无返回参数的锁
     *
     * @param lockKey
     * @param lockTime  锁时间
     * @param leaseTime 持锁最长时间(为了防止死锁时间过长，源代码默认30S)
     * @param timeUnit
     * @param processor
     * @return
     */
    public void commonLock(String lockKey, long lockTime, long leaseTime, TimeUnit timeUnit, LockProcessor processor) {
        RLock rlock = redissonClient.getLock(lockKey);
        try {
            if (rlock.tryLock(lockTime, leaseTime, timeUnit)) {
                processor.process();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 重置中断状态
            throw new RuntimeException("Interrupted while acquiring lock for key: " + lockKey, e);
        } finally {
            if (rlock.isHeldByCurrentThread()) {
                rlock.unlock();
            }
        }
    }
}
