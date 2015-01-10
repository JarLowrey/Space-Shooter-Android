package com.jtronlabs.to_the_moon.bullets;

import android.content.Context;

import com.jtronlabs.to_the_moon_interfaces.Shooter;
  

public abstract class Bullet_NotFancy extends Bullet{
	
	//no threads needed for a default bullet
	public void stopBulletThreads(){}
	
	public abstract BulletView getBullet(Context context,Shooter shooter);
	
}
