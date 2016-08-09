package com.zhengweihao.lock.reentrantlock;

import com.zhengweihao.lock.example.Mutex;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ZhengWeihao on 16/8/9.
 */
public class ReentrantTest {
    @Test
    public void synchronizedTest() {
        Object obj = new Object();
        synchronized (obj) {
            System.out.println("进入锁1");
            synchronized (obj) {
                System.out.println("进入锁2");
            }
        }
        System.out.println("结束示范");
    }

    @Test
    public void mutexTest() {
        Mutex m = new Mutex();

        m.lock();
        System.out.println("进入锁1");

        m.lock();
        System.out.println("进入锁2");

        System.out.println("结束示范");
    }

    @Test
    public void reentrantLockTest() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        System.out.println("进入锁1");

        lock.lock();
        System.out.println("进入锁2");

        System.out.println("结束示范");
    }
}
