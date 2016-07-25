package com.zhengweihao.lock.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ZhengWeihao on 16/7/25.
 */
public class TicketDispenser {

    private static ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final int windows = 10;
    private static final int ticketsPerWindow = 1000;
    private static CountDownLatch latch = new CountDownLatch(windows);

    private static List<Integer> tickets;
    private void initTickets() {
        tickets = new ArrayList<Integer>();
        for(int i=0; i<windows; i++) {
            tickets.add(ticketsPerWindow);
        }
    }
    private void checkTickets() {
        if(tickets == null || tickets.size() <= 0) {
            return;
        }

        for(int i=0; i<tickets.size(); i++) {
            System.out.println("窗口:" + i + " 的余票量:" + tickets.get(i));
        }
    }

    @Test
    public void testReadWriteLock() {
        initTickets();

        rwl.readLock().lock();
        checkTickets();
        rwl.readLock().unlock();

        rwl.writeLock().lock();

        rwl.readLock().unlock();
    }

}
