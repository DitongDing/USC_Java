package test.dejavu;

import ddt.utils.Timer;

public class TestTimer {
	public static void main(String[] args) {
		final int second = 2;

		Timer timer = new Timer() {
			public void run() {
				TestTimer.run(second);
			}
		};
		System.out.println(timer.getRunTime());
	}

	public static void run(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
