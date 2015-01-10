package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public  class Gun_AngledTriShot extends Gun {
	
	private static final double DEFAULT_ANGLE=8;
	
	public Gun_AngledTriShot(Context context,
			Shooter theShooter,Bullet bulletType) {
		super(context,theShooter,bulletType);
	}
	public boolean shoot(){
		//travel horizontally at a speed such that the bullets will move in DEFAULT_ANGLE direction
		double bulletSpeedX = shooter.getBulletSpeedY() * Math.tan(Math.toRadians(DEFAULT_ANGLE));

		//create 3 bullets at center of shooter
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter);
		BulletView bulletMid = myBulletType.getBullet(ctx, shooter);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter);
		
		//set left and right bullets to bulletSpeedX
		bulletLeft.setSpeedX(bulletSpeedX * -1);
		bulletRight.setSpeedX(bulletSpeedX);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletLeft,1);
		shooter.getMyScreen().addView(bulletMid,1);
		shooter.getMyScreen().addView(bulletRight,1);

		return false;
	}
}
