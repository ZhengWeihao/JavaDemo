package com.zhengweihao.lock.example;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ZhengWeihao on 16/7/24.
 */
public class Compare {

    private static final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final int threadNum = 10;
    private static final int addTime = 100000;
    private static final CountDownLatch latch = new CountDownLatch(threadNum * addTime);
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Random r = new Random();

    private Object obj = new Object();
    private static Integer result = 500000;

    @Test
    public void addBySynchronized() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int t = 0; t < addTime; t++) {
                        int amount = r.nextInt(100);
                        synchronized (obj) {
                            if (result >= amount) {
                                result -= amount;
                            }
                            latch.countDown();
                        }
                    }
                }
            });
        }

        latch.await();
        System.out.println("result:" + result);
        System.out.println("run time:" + (System.currentTimeMillis() - startMillis) + "(ms)");
    }

    @Test
    public void addByReadWriteLock() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        for (int i = 0; i < threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int t = 0; t < addTime; t++) {
                        int amount = r.nextInt(100);

                        rwl.readLock().lock();
                        boolean amountEnough = result >= amount;

                        if (amountEnough) {
                            rwl.readLock().unlock();
                            rwl.writeLock().lock();
                            rwl.readLock().lock();
                            result -= amount;
                            rwl.writeLock().unlock();
                        }
                        rwl.readLock().unlock();

                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        System.out.println("result:" + result);
        System.out.println("run time:" + (System.currentTimeMillis() - startMillis) + "(ms)");
    }

}
