package com.jtronlabs.to_the_moon.views;

public interface GameObject {

	public void restartThreads();
	public void cleanUpThreads();
	public void removeView(boolean showExplosion);
}
