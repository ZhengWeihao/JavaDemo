package com.zhengweihao.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhengWeihao on 16/8/10.
 */
public class Demo {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    private static Runnable conditionWait = new Runnable() {
        @Override
        public void run() {
            lock.lock();
            System.out.println("wait线程获取到锁");

            try {
                System.out.println("wait线程进入等待");
                condition.await();
                System.out.println("wait线程完成等待");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("wait线程释放锁");
                lock.unlock();
            }
        }
    };

    private static Runnable conditionSignal = new Runnable() {
        @Override
        public void run() {
            lock.lock();
            System.out.println("signal线程获取到锁");

            try {
                System.out.println("signal线程唤醒wait线程");
                condition.signal();
                System.out.println("signal线程完成唤醒");
            } finally {
                System.out.println("signal线程释放锁");
                lock.unlock();
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        new Thread(conditionWait).start();
        Thread.sleep(1000);
        new Thread(conditionSignal).start();
    }

}
