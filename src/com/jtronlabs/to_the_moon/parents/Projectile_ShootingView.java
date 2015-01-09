package com.jtronlabs.to_the_moon.parents;
  
import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserDefault;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable;

/**
 * Parent class for all shooters. A shooter has a gun and bullet type and floats down to its initial position (hence extending Gravity class)
 * @author lowre_000
 *
 */
public class Projectile_ShootingView extends Moving_ProjectileView{

	//myGun needs to be set in a specific View's class
	public Gun myGun;
	public Bullet myBulletType;
	
	
	public Projectile_ShootingView(Context context,boolean shootingUpwards,double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context, projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);
		
		myBulletType = new Bullet_LaserDefault();
	}
	
	public Projectile_ShootingView(Context context,AttributeSet at,boolean shootingUpwards,double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,at, projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);	

		myBulletType = new Bullet_LaserDefault();
	}

	@Override
	public void removeGameObject(){
		if(myGun!=null){
//			myGun.stopShooting();	
		}
		
		super.removeGameObject();
	}
	
	public void restartThreads(){
//		this.myGun.startShooting();
		super.restartThreads();
	}
	
	/**
	 * Takes care of upgrading or downgrading this instance's Gun_Upgradeable
	 * @param upgrade True to upgrade gun, False to downgrade
	 */
	public void upgradeOrDowngradeGun(boolean upgrade){
		//create new upgrade
		Gun_Upgradeable nextGun;
		if(upgrade){
			nextGun = this.myGun.getMostRecentUpgradeableGun().getUpgradeGun();
		}else{
			nextGun = this.myGun.getMostRecentUpgradeableGun().getDowngradedGun();			
		}
		
		if(this.myGun instanceof Gun_Special){
			this.myGun.setPreviousUpgradeableGun(nextGun);//set it up so once special disappears, new gun will take over
		}else if( ! this.myGun.getClass().equals(nextGun.getClass())){
			this.myGun=nextGun;
		}
	}
	
	/**
	 * Takes care of changing this instance's gun to a given Special Gun with a set amount of amunition
	 * @param newGun Special Gun to switch to
	 * @param ammo Amount of ammo new special gun will have
	 */
	public void giveSpecialGun(Gun_Special newGun, int ammo){
		this.myGun.transferGunProperties(newGun);
		this.myGun=newGun;
		newGun.setAmmo(ammo);
	}
}
