package ddt.utils;

// Used for get run time of a series of functions.
public abstract class Timer {
	abstract public void run();

	public long getRunTime() {
		final long start = System.currentTimeMillis();
		run();
		final long end = System.currentTimeMillis();
		return end - start;
	}
}
