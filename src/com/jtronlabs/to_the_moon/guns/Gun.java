package com.jtronlabs.to_the_moon.guns;
  
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public abstract class Gun {
	
	protected double bulletSpeedY=12,bulletFreq,bulletDamage=5;
	protected Gun_Upgradeable previousUpgradeableGun;
	protected boolean shootingUp=false;

	Gravity_ShootingView shooter;
	Context ctx;
	
	public ArrayList<BulletView> myBullets;
	
	Handler myShootingHandler = new Handler();
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		boolean outOfAmmoGun = spawnMyBullet();
    		if(outOfAmmoGun==true){
    			Gun.this.stopShooting();//stop spawning bullets with this gun
    			shooter.myGun=shooter.myGun.previousUpgradeableGun;//set shooter's gun to previous gun
//    			if(shooter.myGun==null){Log.d("lowrey","this should not be null");}
    			shooter.myGun.myBullets=Gun.this.myBullets;
    		}else{
    			myShootingHandler.postDelayed(this, (long) bulletFreq);
    		}
    	}
	};

	/**
	 * 
	 * @return true if gun is a special and is out of ammo. False otherwise
	 */
	public abstract boolean spawnMyBullet();	
	
	 	
	public Gun(Context context,Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamageAmt,double bulletFrequency) {
		ctx=context;
		shooter=theShooter;
		
		shootingUp= shootingUpwards;
		bulletSpeedY=bulletSpeedVertical;
		bulletDamage=bulletDamageAmt;
		bulletFreq=bulletFrequency;
		previousUpgradeableGun=(Gun_Upgradeable) this;
		
		//set myBullets to empty arraylist
		myBullets=new ArrayList<BulletView>();
		if(previousUpgradeableGun==null){Log.d("lowrey","this should not be null111111111");}
	}
	
	public Gun(Context context,Gravity_ShootingView theShooter,Gun_Upgradeable previousGunUpgrade) {
		ctx=context;
		
		shooter=theShooter;
		previousUpgradeableGun = previousGunUpgrade;

		transferGunProperties(this);
	} 
	
	public void transferGunProperties(Gun newGun){
		this.stopShooting();
		
		newGun.setBulletDamage(previousUpgradeableGun.bulletDamage);
		newGun.setBulletFreq(previousUpgradeableGun.bulletFreq);
		newGun.setBulletSpeedY(previousUpgradeableGun.bulletSpeedY);
		newGun.setShootingUp(previousUpgradeableGun.shootingUp);
		
		newGun.myBullets=previousUpgradeableGun.myBullets;
	}
	
	public void startShooting(double bulletSpawningFrequency){
		setBulletFreq(bulletSpawningFrequency);
		myShootingHandler.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	public void startShootingImmediately(double bulletSpawningFrequency){
		setBulletFreq(bulletSpawningFrequency);
		myShootingHandler.post(spawnBulletRunnable);
	}
	public void startShooting(){
		myShootingHandler.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	public void startShootingImmediately(){
		myShootingHandler.post(spawnBulletRunnable);
	}
	public void stopShooting(){
		myShootingHandler.removeCallbacks(spawnBulletRunnable);		
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
	

	public Gun_Upgradeable getMostRecentUpgradeableGun(){
		boolean isUpgradeable = this instanceof Gun_Upgradeable;
		if(isUpgradeable){
			return (Gun_Upgradeable) this;
		}else{
			return this.previousUpgradeableGun;
		}
	}
}
