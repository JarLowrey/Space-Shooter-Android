package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public abstract class Gun_Upgradeable extends Gun {
	
	public Gun_Upgradeable(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
	}
	public Gun_Upgradeable(Context context,Gravity_ShootingView theShooter) {
		super(context,theShooter);
	}
	
	public abstract Gun_Upgradeable upgradeGun();
	public abstract Gun_Upgradeable downgradeGun();
	
}
