package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
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
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
		
		shootTowardsMe = shootingAtMe;
	}
	public boolean shoot(){
		//create left and right bullets that travel at same angles, such that left bullet will hit View that is being shot at (if it were stationary)
		double diffYAbs = (shootingUp) ? 
			Math.abs(shooter.getY() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) ) ://top of shooter - bottom of ShotAt
			Math.abs( ( shooter.getY()+shooter.getHeight() ) - shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
			
		final double diffX = shooter.getX() - ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2;//from left side of shooter to middle of ShotAt
		final double angleRadians = Math.atan(Math.abs(diffX)/diffYAbs);
		double bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(angleRadians);
		
		bulletSpeedX = (diffX>0) ? -bulletSpeedX : bulletSpeedX;//adjust for shooter to being on one side or other of target
		//limit the x speed ( and thus the angle) by the y speed. Otherwise at the bottom of the screen, bullets are ridiculously quick in X direction
		bulletSpeedX = (bulletSpeedX>bulletSpeedY) ? bulletSpeedY * (bulletSpeedX/bulletSpeedX) : bulletSpeedX;
		
		Projectile_BulletView bulletLeft = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				bulletSpeedX,bulletDamage, Bullet.BULLET_LEFT);
		Projectile_BulletView bulletRight = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				bulletSpeedX,bulletDamage, Bullet.BULLET_RIGHT);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getParent()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}
