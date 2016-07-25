package com.zhengweihao.lock.example;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家进餐问题
 *
 * @author zhengweihao
 */
public class DinningPhilosophersProblem {

    @Test
    public void dining() {
        // 实例化五双筷子
        List<Chopstick> chopsticks = new ArrayList<Chopstick>();
        for (int i = 0; i < 5; i++) {
            chopsticks.add(new Chopstick(i));
        }

        // 为哲学家分配筷子并运行
        for (int i = 0; i < 5; i++) {
            int leftI = i;
            int rightI = i + 1;
            if (rightI >= 5) {
                rightI = 0;
            }

            new Philosopher(String.valueOf(i), chopsticks.get(leftI), chopsticks.get(rightI)).start();
        }

        while (true) ;
    }

    @Test
    public void orderedDining() {
        System.out.println("全局顺序方式");

        // 实例化五双筷子
        List<Chopstick> chopsticks = new ArrayList<Chopstick>();
        for (int i = 0; i < 5; i++) {
            chopsticks.add(new Chopstick(i));
        }

        // 为哲学家分配筷子并运行
        for (int i = 0; i < 5; i++) {
            int leftI = i;
            int rightI = i + 1;
            if (rightI >= 5) {
                rightI = 0;
            }

            new OrderedPhilosopher(String.valueOf(i), chopsticks.get(leftI), chopsticks.get(rightI)).start();
        }

        while (true) ;
    }

    @Test
    public void tryLockDining() {
        System.out.println("tryLock方式:");

        // 实例化五双筷子
        List<ReentrantLock> chopsticks = new ArrayList<ReentrantLock>();
        for (int i = 0; i < 5; i++) {
            chopsticks.add(new ReentrantLock());
        }

        // 为哲学家分配筷子并运行
        for (int i = 0; i < 5; i++) {
            int leftI = i;
            int rightI = i + 1;
            if (rightI >= 5) {
                rightI = 0;
            }

            new TryLockPhilosopher(String.valueOf(i), chopsticks.get(leftI), chopsticks.get(rightI)).start();
        }

        while (true) ;
    }

    @Test
    public void lockInterruptDining() throws InterruptedException {
        System.out.println("lockInterrupt方式:");

        // 实例化五双筷子
        List<ReentrantLock> chopsticks = new ArrayList<ReentrantLock>();
        for (int i = 0; i < 5; i++) {
            chopsticks.add(new ReentrantLock());
        }

        // 为哲学家分配筷子并运行
        List<InterruptPhilosopher> ips = new ArrayList<InterruptPhilosopher>();
        for (int i = 0; i < 5; i++) {
            int leftI = i;
            int rightI = i + 1;
            if (rightI >= 5) {
                rightI = 0;
            }

            InterruptPhilosopher ip = new InterruptPhilosopher(String.valueOf(i), chopsticks.get(leftI), chopsticks.get(rightI));
            ips.add(ip);
            ip.start();
        }

        boolean interrupted = false;
        while (!interrupted) {
            Date lastDiningDate = ips.get(0).getLastDiningDate();
            if(lastDiningDate != null && new Date().getTime() - lastDiningDate.getTime() > 5000) {
                for(InterruptPhilosopher ip : ips) {
                    ip.interrupt();
                }
                interrupted = true;
            }
        }
    }
}

/**
 * 筷子
 *
 * @author zhengweihao
 */
class Chopstick {

    private int id;

    public Chopstick(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

/**
 * 哲学家
 *
 * @author zhengweihao
 */
class Philosopher extends Thread {

    private String name;
    private Chopstick left;
    private Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int scope = 2;
        Random r = new Random();

        try {
            while (true) {
                System.out.println("哲学家:" + this.name + " 正在思考 ..");
                Thread.sleep(r.nextInt(scope));

                synchronized (left) {
                    System.out.println("哲学家:" + this.name + " 拿起左边筷子 ..");
                    synchronized (right) {
                        System.out.println("哲学家:" + this.name + " 拿起右边筷子 ..");
                        System.out.println("哲学家:" + this.name + " 正在用餐 ..");
                        Thread.sleep(r.nextInt(scope));
                    }
                }
                System.out.println("哲学家:" + this.name + " 放下筷子 ..");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 进餐有序的哲学家
 *
 * @author zhengweihao
 */
class OrderedPhilosopher extends Thread {

    private String name;
    private Chopstick left;
    private Chopstick right;

    public OrderedPhilosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int scope = 2;
        Random r = new Random();

        try {
            while (true) {
                System.out.println("哲学家:" + this.name + " 正在思考 ..");
                Thread.sleep(r.nextInt(scope));

                // 按筷子编号升序顺序取筷子
                Chopstick firstChopstick = null;
                Chopstick secondChopstick = null;
                if (left.getId() > right.getId()) {
                    firstChopstick = right;
                    secondChopstick = left;
                } else {
                    firstChopstick = left;
                    secondChopstick = right;
                }

                synchronized (firstChopstick) {
                    System.out.println("哲学家:" + this.name + " 拿起第一只筷子(" + firstChopstick.getId() + ") ..");
                    synchronized (secondChopstick) {
                        System.out.println("哲学家:" + this.name + " 拿起第二只筷子(" + secondChopstick.getId() + ") ..");
                        System.out.println("哲学家:" + this.name + " 开始用餐 ..");
                        Thread.sleep(r.nextInt(scope));
                    }
                }

                System.out.println("哲学家:" + this.name + " 放下筷子 ..");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TryLockPhilosopher extends Thread {
    private String name;
    private ReentrantLock left;
    private ReentrantLock right;

    public TryLockPhilosopher(String name, ReentrantLock left, ReentrantLock right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int scope = 2;
        Random r = new Random();

        try {
            while (true) {
                int tryTime = 10;
                System.out.println("哲学家:" + this.name + " 正在思考 ..");
                Thread.sleep(r.nextInt(scope));

                if (left.tryLock(tryTime, TimeUnit.SECONDS)) {
                    System.out.println("哲学家:" + this.name + " 拿起左边筷子 ..");
                    if (right.tryLock(tryTime, TimeUnit.SECONDS)) {
                        System.out.println("哲学家:" + this.name + " 拿起右边筷子 ..");
                        try {
                            System.out.println("哲学家:" + this.name + " 开始用餐 ..");
                            Thread.sleep(r.nextInt(scope));
                        } finally {
                            left.unlock();
                            right.unlock();
                            System.out.println("哲学家:" + this.name + " 放下筷子 ..");
                        }
                    } else {
                        System.out.println("哲学家:" + this.name + " 无法拿起右边筷子, 继续思考 ..");
                    }
                } else {
                    System.out.println("哲学家:" + this.name + " 无法拿起左边筷子, 继续思考 ..");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class InterruptPhilosopher extends Thread {
    private String name;
    private ReentrantLock left;
    private ReentrantLock right;
    private Date lastDiningDate;

    public InterruptPhilosopher(String name, ReentrantLock left, ReentrantLock right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        int scope = 2;
        Random r = new Random();

        try {
            while (true) {
                try {
                    System.out.println("哲学家:" + this.name + " 正在思考 ..");
                    Thread.sleep(r.nextInt(scope));

                    left.lockInterruptibly();
                    System.out.println("哲学家:" + this.name + " 拿起左边筷子 ..");
                    right.lockInterruptibly();
                    System.out.println("哲学家:" + this.name + " 拿起右边筷子 ..");
                    lastDiningDate = new Date();
                    System.out.println("哲学家:" + this.name + " 开始用餐 ..");
                    Thread.sleep(r.nextInt(scope));
                } catch(InterruptedException e) {
                    System.out.println("interrupted");
                    e.printStackTrace();
                } finally {
                    left.unlock();
                    right.unlock();
                    System.out.println("哲学家:" + this.name + " 放下筷子 ..");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReentrantLock getLeft() {
        return this.left;
    }

    public ReentrantLock getRight() {
        return this.right;
    }

    public Date getLastDiningDate() {
        return this.lastDiningDate;
    }
}
