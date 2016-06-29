package com.zhengweihao.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ��ѧ�ҽ�������
 * 
 * @author zhengweihao
 *
 */
public class DinningPhilosophersProblem {

	public static void main(String[] args) {
		// ʵ������˫����
		List<Chopstick> chopsticks = new ArrayList<Chopstick>();
		for (int i = 0; i < 5; i++) {
			chopsticks.add(new Chopstick(i));
		}

		// Ϊ��ѧ�ҷ�����Ӳ�����
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
 * ����
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
 * ��ѧ��
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
		int scope = 2;
		Random r = new Random();

		try {
			while (true) {
				System.out.println("the philosopher:" + this.name + " is in thinking ..");
				Thread.sleep(r.nextInt(scope));

				/*
				 * synchronized (left) { System.out.println("the philosopher:" +
				 * this.name + " get the left schopstick .."); synchronized
				 * (right) { System.out.println("the philosopher:" + this.name +
				 * " get the right schopstick ..");
				 * System.out.println("the philosopher:" + this.name +
				 * " is in dining .."); Thread.sleep(r.nextInt(scope)); } }
				 */

				// �����ӱ������˳��ȡ����
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
					System.out.println("the philosopher:" + this.name + " get the first schopstick("
							+ firstChopstick.getId() + ") ..");
					synchronized (secondChopstick) {
						System.out.println("the philosopher:" + this.name + " get the second schopstick("
								+ secondChopstick.getId() + ") ..");
						System.out.println("the philosopher:" + this.name + " is in dining ..");
						Thread.sleep(r.nextInt(scope));
					}
				}

				System.out.println("the philosopher:" + this.name + " put down the schopsticks ..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
