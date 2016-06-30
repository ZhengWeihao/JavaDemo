package com.zhengweihao.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 哲学家进餐问题
 * 
 * @author zhengweihao
 *
 */
public class DinningPhilosophersProblem {

	public static void main(String[] args) {
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
	}
}

/**
 * 筷子
 * 
 * @author zhengweihao
 *
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
 *
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
		int scope = 5;
		Random r = new Random();

		try {
			while (true) {
				System.out.println("the philosopher:" + this.name + " is in thinking ..");
				Thread.sleep(r.nextInt(scope));

				/*
				 * // 死锁案例 synchronized (left) {
				 * System.out.println("the philosopher:" + this.name +
				 * " get the left chopstick .."); synchronized (right) {
				 * System.out.println("the philosopher:" + this.name +
				 * " get the right chopstick ..");
				 * System.out.println("the philosopher:" + this.name +
				 * " is in dining .."); Thread.sleep(r.nextInt(scope)); } }
				 */

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
					System.out.println("the philosopher:" + this.name + " get the first chopstick("
							+ firstChopstick.getId() + ") ..");
					synchronized (secondChopstick) {
						System.out.println("the philosopher:" + this.name + " get the second chopstick("
								+ secondChopstick.getId() + ") ..");
						System.out.println("the philosopher:" + this.name + " is in dining ..");
						Thread.sleep(r.nextInt(scope));
					}
				}

				System.out.println("the philosopher:" + this.name + " put down the chopsticks ..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
