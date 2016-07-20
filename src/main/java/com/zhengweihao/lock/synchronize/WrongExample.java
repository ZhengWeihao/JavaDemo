package com.zhengweihao.lock.synchronize;

/**
 * Created by ZhengWeihao on 16/7/17.
 */
public class WrongExample {

    private static Integer num = 0;

    private static class AddThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 50000; i++) {
                synchronized (num) {
                    num++;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread t1 = new AddThread();
        AddThread t2 = new AddThread();

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(num);
    }
}

