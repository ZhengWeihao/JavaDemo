package com.zhengweihao.lock.readwritelock;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ZhengWeihao on 16/7/25.
 */
public class Demo {
    private static final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final CountDownLatch latch = new CountDownLatch(2);

    @Test
    public void testReadRead() throws InterruptedException {
        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.readLock().lock();
                log("readLock 1 locked");

                sleep(2000);

                rwl.readLock().unlock();
                log("readLock 1 unlocked");

                latch.countDown();
            }
        });

        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.readLock().lock();
                log("readLock 2 locked");

                sleep(2000);

                rwl.readLock().unlock();
                log("readLock 2 unlocked");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void testReadWrite() throws InterruptedException {
        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.readLock().lock();
                log("readLock 1 locked");

                sleep(2000);

                rwl.readLock().unlock();
                log("readLock 1 unlocked");

                latch.countDown();
            }
        });

        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.writeLock().lock();
                log("writeLock 2 locked");

                sleep(2000);

                rwl.writeLock().unlock();
                log("writeLock 2 unlocked");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void testWriteRead() throws InterruptedException {
        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.writeLock().lock();
                log("writeLock 1 locked");

                sleep(2000);

                rwl.writeLock().unlock();
                log("writeLock 1 unlocked");

                latch.countDown();
            }
        });

        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.readLock().lock();
                log("readLock 2 locked");

                sleep(2000);

                rwl.readLock().unlock();
                log("readLock 2 unlocked");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void testWriteWrite() throws InterruptedException {
        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.writeLock().lock();
                log("writeLock 1 locked");

                sleep(2000);

                rwl.writeLock().unlock();
                log("writeLock 1 unlocked");

                latch.countDown();
            }
        });

        es.execute(new Runnable() {
            @Override
            public void run() {
                rwl.writeLock().lock();
                log("writeLock 2 locked");

                sleep(2000);

                rwl.writeLock().unlock();
                log("writeLock 2 unlocked");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void testReadInWrite() {
        rwl.writeLock().lock();
        log("writeLock locked");

        rwl.readLock().lock();
        log("readLock locked");

        rwl.readLock().unlock();
        log("readLock unlocked");

        rwl.writeLock().unlock();
        log("writeLock unlocked");
    }

    @Test
    public void testWriteInRead() {
        rwl.readLock().lock();
        log("readLock locked");

        rwl.writeLock().lock();
        log("writeLock locked");

        rwl.writeLock().unlock();
        log("writeLock unlocked");

        rwl.readLock().unlock();
        log("readLock unlocked");
    }

    private void log(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + ":\n" + msg);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
