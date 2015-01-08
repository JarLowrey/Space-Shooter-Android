package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public  class Gun_Special_ShootTowardsProjectileDualShot extends Gun_Special {
	
	private ProjectileView shootTowardsMe;
	
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,ProjectileView shootingAtMe,
			Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
		
		 shootTowardsMe = shootingAtMe;
	}
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,ProjectileView shootingAtMe,
			Gun_Upgradeable previousGunObject,
			Gravity_ShootingView theShooter) {
		super(context,previousGunObject,theShooter);
		
		shootTowardsMe = shootingAtMe;
	}
	public boolean spawnMyBullet(){
		//create left and right bullets that travel at same angles, such that left bullet will hit View that is being shot at (if it were stationary)
		final double diffYAbs = Math.abs(shooter.getY() - shootTowardsMe.getY());
		final double diffX = shooter.getX() - shootTowardsMe.getX();
		final double angle = Math.atan(Math.abs(diffX)/diffYAbs);
		
		double bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(Math.toRadians(angle));
		
		BulletView bulletLeft= new BulletView(ctx, shooter, shootingUp,BulletView.BULLET_LEFT,
				bulletSpeedY, bulletSpeedX, bulletDamage);
		BulletView bulletRight= new BulletView(ctx, shooter, shootingUp,BulletView.BULLET_RIGHT,
				bulletSpeedY, bulletSpeedX, bulletDamage);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getParent()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}
