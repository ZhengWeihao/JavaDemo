package com.zhengweihao.lock.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ZhengWeihao on 16/7/25.
 */
public class TicketDispenser {

    private static ExecutorService es = Executors.newFixedThreadPool(12);
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final int windows = 10;
    private static final int ticketsPerWindow = 100;
    private static CountDownLatch latch = new CountDownLatch(windows);
    private static AtomicInteger sell = new AtomicInteger();

    private static List<Integer> tickets;

    private void initTickets() {
        tickets = new ArrayList<Integer>();
        for (int i = 0; i < windows; i++) {
            tickets.add(ticketsPerWindow);
        }
    }

    private void checkTickets() {
        if (tickets == null || tickets.size() <= 0) {
            return;
        }

        for (int i = 0; i < tickets.size(); i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\t窗口:" + i + " 的余票量:" + tickets.get(i));
        }
    }

    @Test
    public void testReadWriteLock() throws InterruptedException {
        initTickets();

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < windows; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int a = 0; a < ticketsPerWindow * 1.2; a++) {
                        rwl.readLock().lock();
                        checkTickets();
                        rwl.readLock().unlock();

                        rwl.writeLock().lock();
                        for (int w = 0; w < windows; w++) {
                            Integer ticketWindow = tickets.get(w);
                            if (ticketWindow > 0) {
                                try {
                                    TimeUnit.MILLISECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ticketWindow -= 1;
                                tickets.set(w, ticketWindow);
                                sell.incrementAndGet();
                                break;
                            }
                        }
                        rwl.writeLock().unlock();
                    }
                    latch.countDown();

                }
            });
        }

        latch.await();
        System.out.println("售票完成,结果:");
        checkTickets();
        System.out.println("总票量:" + (windows * ticketsPerWindow) + ";卖出票量:" + sell.get());

        long runMillis = System.currentTimeMillis() - startMillis;
        System.out.println("运行时间:" + runMillis + "(ms)");
    }

    private Object obj = new Object();

    @Test
    public void testSynchronized() throws InterruptedException {
        initTickets();

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < windows; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int a = 0; a < ticketsPerWindow * 1.2; a++) {
                        synchronized (obj) {
                            checkTickets();

                            for (int w = 0; w < windows; w++) {
                                Integer ticketWindow = tickets.get(w);
                                if (ticketWindow > 0) {
                                    try {
                                        TimeUnit.MILLISECONDS.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    ticketWindow -= 1;
                                    tickets.set(w, ticketWindow);
                                    sell.incrementAndGet();
                                    break;
                                }
                            }
                        }
                    }
                    latch.countDown();

                }
            });
        }

        latch.await();
        System.out.println("售票完成,结果:");
        checkTickets();
        System.out.println("总票量:" + (windows * ticketsPerWindow) + ";卖出票量:" + sell.get());

        long runMillis = System.currentTimeMillis() - startMillis;
        System.out.println("运行时间:" + runMillis + "(ms)");
    }

    private ReentrantLock rl = new ReentrantLock();

    @Test
    public void testReentrantLock() throws InterruptedException {
        initTickets();

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < windows; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int a = 0; a < ticketsPerWindow * 1.2; a++) {
                        rl.lock();
                        checkTickets();

                        for (int w = 0; w < windows; w++) {
                            Integer ticketWindow = tickets.get(w);
                            if (ticketWindow > 0) {
                                try {
                                    TimeUnit.MILLISECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ticketWindow -= 1;
                                tickets.set(w, ticketWindow);
                                sell.incrementAndGet();
                                break;
                            }
                        }
                        rl.unlock();
                    }
                    latch.countDown();

                }
            });
        }

        latch.await();
        System.out.println("售票完成,结果:");
        checkTickets();
        System.out.println("总票量:" + (windows * ticketsPerWindow) + ";卖出票量:" + sell.get());

        long runMillis = System.currentTimeMillis() - startMillis;
        System.out.println("运行时间:" + runMillis + "(ms)");
    }

}
