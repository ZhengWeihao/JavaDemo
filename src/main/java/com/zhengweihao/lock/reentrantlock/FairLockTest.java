package com.zhengweihao.lock.reentrantlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhengWeihao on 16/8/7.
 */
public class FairLockTest {

    private static final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static int threadNum = 50;
    private static final CountDownLatch cdl = new CountDownLatch(threadNum);
    private static final ReentrantLock lock = new ReentrantLock(false);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < threadNum; i++) {
            final int threadId = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    lock.lock();

                    System.out.println("线程:" + threadId + "已运行");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException expect) {
                    }
                    cdl.countDown();

                    lock.unlock();
                }
            });
        }

        cdl.await();
    }

}
