package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Gun_Upgradeable_StraightSingleShot extends Gun_Upgradeable {
	
	public Gun_Upgradeable_StraightSingleShot(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
	}
	public Gun_Upgradeable_StraightSingleShot(Context context,Gravity_ShootingView theShooter) {
		super(context,theShooter);
	}
	public boolean shoot(){

		Projectile_BulletView bulletMid = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				Bullet.BULLET_TRAVELS_STRAIGHT,bulletDamage, Bullet.BULLET_MIDDLE);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletMid,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletMid);
		
		return false;
	}
	/**
	 * Upgrade is a dual shot
	 */
	@Override
	public Gun_Upgradeable upgradeGun() {
		this.stopShooting();
		Gun_Upgradeable_StraightDualShot newGun = new Gun_Upgradeable_StraightDualShot(ctx,shooter);
		this.transferGunProperties(newGun);
		return newGun;
	}
	@Override
	public Gun_Upgradeable downgradeGun() {
		return this;
	}
	
}
