package com.yb.fish.lock;

public interface InterfaceRedisLock {

    public boolean lock(String bussinessKey, int waitTime, int leaseTime) throws InterruptedException;

    public boolean lock(String bussinessKey, int waitTime) throws InterruptedException;

    public void lock(String bussinessKey);

    public void unLock(String bussinessKey);
}
