package com.yb.fish.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author bing.zhang
 * @title: SemaphoreMain
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/9上午10:35
 */
public class SemaphoreMain {
    static Semaphore semaphore = SemaphoreUtils.getSemaphoreInstance(1);
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    runWork();
                }

            });

        }
        try {
            countDownLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void runWork() {

        try {
            semaphore.acquire(1);
            Thread.sleep(1500);
            System.out.println("work thread : " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
            semaphore.release();
        } finally {
            semaphore.release();
        }
    }
}
