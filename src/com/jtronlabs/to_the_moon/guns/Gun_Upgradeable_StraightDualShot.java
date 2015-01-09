package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Gun_Upgradeable_StraightDualShot extends Gun_Upgradeable {
	
	private static final double STRAIGHT_BULLET=0;
	
	public Gun_Upgradeable_StraightDualShot(Context context,
			Shooter theShooter) {
		super(context,theShooter);
	}
	public boolean shoot(){
		//create 2 bullets at edges of shooter shoot vertically straight
		BulletView bulletLeft = shooter.getBulletType().getBullet(ctx, shooter,STRAIGHT_BULLET, Bullet.BULLET_LEFT);
		BulletView bulletRight = shooter.getBulletType().getBullet(ctx, shooter,STRAIGHT_BULLET, Bullet.BULLET_RIGHT);

		//add bullets to layout
		shooter.getMyScreen().addView(bulletLeft,1);
		shooter.getMyScreen().addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);

		return false;
	}
	@Override
	public Gun_Upgradeable getUpgradeGun() {
		//no upgrade currently, so return this
		return this;
	}
	
	@Override
	public Gun_Upgradeable getDowngradedGun() {
		//return the Single StraightShot
		shooter.stopShooting();
		Gun_Upgradeable_StraightSingleShot newGun = new Gun_Upgradeable_StraightSingleShot(ctx,shooter);
		this.transferGunProperties(newGun);
		return newGun;
	}
	
}
