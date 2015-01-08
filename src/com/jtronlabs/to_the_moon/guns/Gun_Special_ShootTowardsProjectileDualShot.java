package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.util.Log;
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
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
		
		shootTowardsMe = shootingAtMe;
	}
	public boolean spawnMyBullet(){
		//create left and right bullets that travel at same angles, such that left bullet will hit View that is being shot at (if it were stationary)
		double diffYAbs = (shootingUp) ? Math.abs(shooter.getY() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) ) ://top of shooter - bottom of ShotAt
			Math.abs( ( shooter.getY()+shooter.getHeight() ) - shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
			
		final double diffX = shooter.getX() - ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2;//from left side of shooter to middle of ShotAt
		final double angleRadians = Math.atan(Math.abs(diffX)/diffYAbs);
		Log.d("lowrey",""+angleRadians);
		double bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(angleRadians);
		
		bulletSpeedX = (diffX>0) ? -bulletSpeedX : bulletSpeedX;
		
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
