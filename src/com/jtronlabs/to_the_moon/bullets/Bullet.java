package com.jtronlabs.to_the_moon.bullets;

import android.content.Context;

import com.jtronlabs.to_the_moon_interfaces.Shooter;
  

public abstract class Bullet{
	
	public final static int BULLET_LEFT=0,
			BULLET_MIDDLE=50,
			BULLET_RIGHT=100;
	public static final double BULLET_TRAVELS_STRAIGHT=0;
	
	public abstract void stopBulletThreads();
	public abstract BulletView getBullet(Context context,Shooter shooter);
	
}
