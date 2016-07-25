package com.zhengweihao.lock.example;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZhengWeihao on 16/7/24.
 */
public class Compare {

    private static final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final int threadNum = 5;
    private static final int addTime = 100000;
    private static final CountDownLatch latch = new CountDownLatch(threadNum * addTime);

    private static Integer result = 0;

    @Test
    public void addBySynchronized() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        for(int i=0; i<threadNum; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for(int t=0; t<addTime; t++) {
                        synchronized (result.getClass()) {
                            result += 1;
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

}
