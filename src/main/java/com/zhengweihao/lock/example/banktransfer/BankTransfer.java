package com.zhengweihao.lock.example.banktransfer;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ZhengWeihao on 16/7/10.
 */
public class BankTransfer {

    private static final Executor executor = Executors.newFixedThreadPool(10);
    private static Integer accountA = 10000;// 账户A余额
    private static Integer accountB = 10000;// 账户B余额
    private static final int transferTime = 50000;

    private static final CountDownLatch latch = new CountDownLatch(transferTime);
    private static final boolean debug = false;

    Runnable unlockTransfer = new Runnable() {
        @Override
        public void run() {
            Random r = new Random();
            int transferAmount = r.nextInt(500) - 250;
            if (accountA + transferAmount >= 0 && accountB - transferAmount >= 0) {
                accountA += transferAmount;
                accountB -= transferAmount;

                if (debug) {
                    System.out.println("发生一笔转账,A账户:" + transferAmount + ";B账户:" + (transferAmount * -1));
                }
            }

            latch.countDown();
        }
    };

    @Test
    public void unlockTransfer() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        for (int i = 0; i < transferTime; i++) {
            executor.execute(unlockTransfer);
        }

        latch.await();// 等待至所有线程都完成
        System.out.println("账户A余额:" + accountA + ";账户B余额:" + accountB + ";总额:" + (accountA + accountB));

        long stopMillis = System.currentTimeMillis();
        System.out.println("执行时长:" + (stopMillis - startMillis) + "ms");
    }

    Runnable syncTransfer = new Runnable() {
        @Override
        public void run() {
            Random r = new Random();
            int transferAmount = r.nextInt(500) - 250;

            synchronized (this) {
                if (accountA + transferAmount >= 0 && accountB - transferAmount >= 0) {
                    accountA += transferAmount;
                    accountB -= transferAmount;

                    if (debug) {
                        System.out.println("发生一笔转账,A账户:" + transferAmount + ";B账户:" + (transferAmount * -1));
                    }
                }
            }

            latch.countDown();
        }
    };

    @Test
    public void syncTransfer() throws InterruptedException {
        long startMillis = System.currentTimeMillis();

        for (int i = 0; i < transferTime; i++) {
            executor.execute(syncTransfer);
        }

        latch.await();// 等待至所有线程都完成
        System.out.println("账户A余额:" + accountA + ";账户B余额:" + accountB + ";总额:" + (accountA + accountB));

        long stopMillis = System.currentTimeMillis();
        System.out.println("执行时长:" + (stopMillis - startMillis) + "ms");
    }

}