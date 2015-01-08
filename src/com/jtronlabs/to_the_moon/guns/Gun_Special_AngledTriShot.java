package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public  class Gun_Special_AngledTriShot extends Gun_Special {
	
	private static final double DEFAULT_ANGLE=8;
	
	public Gun_Special_AngledTriShot(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
	}
	public Gun_Special_AngledTriShot(Context context,
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
	}
	public boolean spawnMyBullet(){
		//create 3 bullets at center of shooter, with left and right bullets traveling X at speed speedY*tan(DEFAULT_ANGLE)
		double bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(Math.toRadians(DEFAULT_ANGLE));
		
		BulletView bulletLeft= new BulletView(ctx, shooter, shootingUp,BulletView.BULLET_MIDDLE,
				bulletSpeedY, -1*bulletSpeedX, bulletDamage);
		BulletView bulletMid= new BulletView(ctx, shooter, shootingUp,BulletView.BULLET_MIDDLE,
				bulletSpeedY, 0, bulletDamage);
		BulletView bulletRight= new BulletView(ctx, shooter, shootingUp,BulletView.BULLET_MIDDLE,
				bulletSpeedY, bulletSpeedX, bulletDamage);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getParent()).addView(bulletMid,1);
		((RelativeLayout)shooter.getParent()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletMid);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}
