package com.jtronlabs.to_the_moon.misc;

public interface GameObject {

	public void restartThreads();
	public void cleanUpThreads();
	public int removeView(boolean showExplosion);
}
