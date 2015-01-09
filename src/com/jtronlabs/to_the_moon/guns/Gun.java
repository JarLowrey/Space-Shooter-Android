package com.jtronlabs.to_the_moon.guns;
  
import java.util.ArrayList;

import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public abstract class Gun {
		
	protected double bulletSpeedY=12,bulletFreq,bulletDamage=5;
	protected Gun_Upgradeable previousUpgradeableGun;
	protected boolean shootingUp=false;

	Gravity_ShootingView shooter;
	Context ctx;
	
	public ArrayList<Projectile_BulletView> myBullets;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		//ensure shooter is not removed before running
    		if(! shooter.isRemoved()){
    			boolean outOfAmmoGun = shoot();
	    		if(outOfAmmoGun==true){
	    			Gun.this.stopShooting();//stop spawning bullets with this gun
	    			shooter.myGun=shooter.myGun.previousUpgradeableGun;//set shooter's gun to previous gun
	//    			if(shooter.myGun==null){Log.d("lowrey","this should not be null");}
	    			shooter.myGun.myBullets=Gun.this.myBullets;
	    		}else{
	    			shooter.postDelayed(this, (long) bulletFreq);
	    		}
    		}
    	}
	};

	/**
	 * 
	 * @return true if gun is a special and is out of ammo. False otherwise
	 */
	public abstract boolean shoot();	
	
	 	
	public Gun(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamageAmt,double bulletFrequency) {
		ctx=context;
		shooter=theShooter;
		
		shootingUp= shootingUpwards;
		bulletSpeedY=bulletSpeedVertical;
		bulletDamage=bulletDamageAmt;
		bulletFreq=bulletFrequency;
		previousUpgradeableGun=getMostRecentUpgradeableGun();
		
		//set myBullets to empty arraylist
		myBullets=new ArrayList<Projectile_BulletView>();
		startShooting(bulletFrequency);
	}
	
	public Gun(Context context,Gravity_ShootingView theShooter) {
		ctx=context;
		
		shooter=theShooter;
		transferGunProperties(this);
	} 
	
	public void transferGunProperties(Gun newGun){
		this.stopShooting();

		newGun.setPreviousUpgradeableGun(getMostRecentUpgradeableGun());
		
		//no previous upgrade means this gun is the base
		if(previousUpgradeableGun==null){
			newGun.setBulletDamage(bulletDamage);
			newGun.setBulletFreq(bulletFreq);
			newGun.setBulletSpeedY(bulletSpeedY);
			newGun.myBullets=myBullets;
		}else{
			newGun.setBulletDamage(previousUpgradeableGun.bulletDamage);
			newGun.setBulletFreq(previousUpgradeableGun.bulletFreq);
			newGun.setBulletSpeedY(previousUpgradeableGun.bulletSpeedY);
			newGun.setShootingUp(previousUpgradeableGun.shootingUp);
			newGun.myBullets=previousUpgradeableGun.myBullets;
		}
	}
	
	
	public void startShooting(double bulletSpawningFrequency){
		setBulletFreq(bulletSpawningFrequency);
		shooter.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	public void startShootingImmediately(double bulletSpawningFrequency){
		setBulletFreq(bulletSpawningFrequency);
		shooter.post(spawnBulletRunnable);
	}
	public void startShooting(){
		shooter.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	public void startShootingImmediately(){
		shooter.post(spawnBulletRunnable);
	}
	public void stopShooting(){
		shooter.removeCallbacks(spawnBulletRunnable);		
	}	
	
	public double getBulletSpeedY(){
		return this.bulletSpeedY;
	}
	public double getBulletDamage(){
		return this.bulletDamage;
	}
	public double getBulletFreq(){
		return this.bulletFreq;
	}
	public boolean isShootingUp(){
		return shootingUp;
	}
	public void setBulletFreq(double freq){
		bulletFreq=freq;
	}
	public void setBulletSpeedY(double newSpeed){
		this.bulletSpeedY=newSpeed;
	}
	public void setBulletDamage(double newDamage){
		this.bulletDamage=newDamage;
	}
	public void setShootingUp(boolean gunIsShootingUpwards){
		shootingUp=gunIsShootingUpwards;
	}
	public void setPreviousUpgradeableGun(Gun_Upgradeable gun){
		this.previousUpgradeableGun=gun;
	}
	

	public Gun_Upgradeable getMostRecentUpgradeableGun(){
		boolean isUpgradeable = this instanceof Gun_Upgradeable;
		if(isUpgradeable){
			return (Gun_Upgradeable) this;
		}else{
			return this.previousUpgradeableGun;
		}
	}
}
