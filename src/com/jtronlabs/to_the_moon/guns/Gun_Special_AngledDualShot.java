package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public  class Gun_Special_AngledDualShot extends Gun_Special {
	
	private static final double DEFAULT_ANGLE=8;
	
	public Gun_Special_AngledDualShot(Context context,
			Shooter theShooter) {
		super(context,theShooter);
	}
	public boolean shoot(){
		//travel horizontally at a speed such that the bullets will move in DEFAULT_ANGLE direction
		double bulletSpeedX = shooter.getBulletSpeedY() * Math.tan(Math.toRadians(DEFAULT_ANGLE));

		//create left and right bullets at center of shooter
		BulletView bulletLeft = shooter.getBulletType().getBullet(ctx, shooter,	-1 * bulletSpeedX, Bullet.BULLET_MIDDLE);
		BulletView bulletRight = shooter.getBulletType().getBullet(ctx, shooter,bulletSpeedX, Bullet.BULLET_MIDDLE);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletLeft,1);
		shooter.getMyScreen().addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);
		
		//decrement special ammo and return result
		return decrementAmmo();
	}
}
