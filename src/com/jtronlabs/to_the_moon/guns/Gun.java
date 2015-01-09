package com.jtronlabs.to_the_moon.guns;
  
import java.util.ArrayList;

import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.BulletView;
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
//	
//	protected ArrayList<BulletView> myBullets;

	/**
	 * Create bullets of the shooter's type at the shooter's position. Properties such as number of bullets, 
	 * direction of bullets, initial position of bullets, and more may be different
	 * @return true if gun is a special and is out of ammo. False otherwise.
	 */
	public abstract boolean shoot();	
	
	
	public Gun(Context context,Shooter theShooter) {
		ctx=context;
		
//		myBullets = new ArrayList<BulletView>();
		shooter=theShooter;
		transferGunProperties(this);
		shooter.startShooting();
	} 
	
	/**
	 * Set newGun's properties to the most recently upgradeable gun's properties. Stop shooting the gun that is being called (this)
	 * @param newGun 
	 */
	public void transferGunProperties(Gun newGun){
		
//		this.stopShooting();
		newGun.setPreviousUpgradeableGun(getMostRecentUpgradeableGun());
		
//		//no previous upgrade means this current gun is the base. So, this Gun's properties must be passed to the next gun
//		if(previousUpgradeableGun==null){
//			newGun.myBullets=myBullets;
//		}else{
//			newGun.myBullets=previousUpgradeableGun.myBullets;
//		}
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
