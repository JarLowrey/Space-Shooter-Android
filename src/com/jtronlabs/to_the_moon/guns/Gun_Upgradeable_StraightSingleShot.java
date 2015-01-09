package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Gun_Upgradeable_StraightSingleShot extends Gun_Upgradeable {
	
	public Gun_Upgradeable_StraightSingleShot(Context context,Shooter theShooter) {
		super(context,theShooter);
	}
	public boolean shoot(){
		//create one bullet at center of shooter
		BulletView bulletMid = shooter.getBulletType().getBullet(ctx, shooter, Bullet.BULLET_TRAVELS_STRAIGHT,Bullet.BULLET_MIDDLE);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletMid,1);
		
		return false;
	}
	/**
	 * Upgrade is a dual shot
	 */
	@Override
	public Gun_Upgradeable getUpgradeGun() {
		//return the Dual StraightShot
		shooter.stopShooting();
		Gun_Upgradeable_StraightDualShot newGun = new Gun_Upgradeable_StraightDualShot(ctx,shooter);
		newGun.setPreviousUpgradeableGun(this);
		return newGun;
	}
	@Override
	public Gun_Upgradeable getDowngradedGun() {
		//this is lowest gun (there is no downgrade), so return this
		return this;
	}
	
}
