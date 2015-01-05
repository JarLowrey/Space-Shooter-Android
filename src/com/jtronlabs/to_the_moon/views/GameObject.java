package com.jtronlabs.to_the_moon.views;

public interface GameObject {

	public void restartThreads();
	public void cleanUpThreads();
	public int removeView(boolean showExplosion);
}
