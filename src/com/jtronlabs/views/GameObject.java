package com.jtronlabs.views;

public interface GameObject {

	public void restartThreads();
	public void cleanUpThreads();
	public void removeView(boolean showExplosion);
}
