package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon_interfaces.Shooter;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class Gun {
	protected Gun_Upgradeable previousUpgradeableGun;

	Shooter shooter;
	Context ctx;
	/**
	 * Create bullets of the shooter's type at the shooter's position. Properties such as number of bullets, 
	 * direction of bullets, initial position of bullets, and more may be different
	 * @return true if gun is a special and is out of ammo. False otherwise.
	 */
	public abstract boolean shoot();	
	
	
	public Gun(Context context,Shooter theShooter) {
		ctx=context;
		

		this.setPreviousUpgradeableGun(getMostRecentUpgradeableGun());
		shooter=theShooter;
		shooter.startShooting();
	} 

	public Gun_Upgradeable getMostRecentUpgradeableGun(){
		boolean isUpgradeable = this instanceof Gun_Upgradeable;
		if(isUpgradeable){
			return (Gun_Upgradeable) this;
		}else{
			return this.previousUpgradeableGun;
		}
	}
	

	public void setPreviousUpgradeableGun(Gun_Upgradeable gun){
		this.previousUpgradeableGun=gun;
	}
	
//	public ArrayList<BulletView> getBullets(){
//		return myBullets;
//	}
}
