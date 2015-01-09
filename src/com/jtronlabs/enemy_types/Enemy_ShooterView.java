package com.jtronlabs.enemy_types;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserDefault;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Enemy_ShooterView extends EnemyView implements Shooter{
	
	//myGun needs to be set in a specific View's class
	public Gun myGun;
	public Bullet myBulletType;
	
	private double bulletFreq,bulletSpeedY,bulletDamage;
	

  Runnable shootingRunnable = new Runnable(){
  	@Override
      public void run() {
  		//ensure shooter is not removed before running
  		if( ! isRemoved()){
  			boolean outOfAmmoGun = myGun.shoot();
    		if(outOfAmmoGun==true){
    			Enemy_ShooterView.this.stopShooting();//stop spawning bullets with this gun
    			myGun=myGun.getMostRecentUpgradeableGun();//set shooter's gun to previous gun
    			myGun.myBullets=Enemy_ShooterView.this.myGun.myBullets;//transfer bullets to previous gun so they will continue to be hit detected
    		}else{
    			Enemy_ShooterView.this.postDelayed(this, (long) Enemy_ShooterView.this.getBulletFreq());
    		}
  		}
  	}
	};
	
	public Enemy_ShooterView(Context context,int scoreForKilling, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth,double probSpawnBonusOnDeath,
			double bulletFrequency,double bulletDmg,double bulletVerticalSpeed) {
		super(context, scoreForKilling,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth,probSpawnBonusOnDeath);

		bulletFreq=bulletFrequency;
		bulletDmg=bulletDamage;
		bulletSpeedY=bulletVerticalSpeed;
		myGun = new Gun_Upgradeable_StraightSingleShot(context,this);
		myBulletType = new Bullet_LaserDefault();
	}
	
	@Override
	public void removeGameObject(){
		stopShooting();
		super.removeGameObject();
	}
	
	@Override
	public void restartThreads(){
		startShooting();
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

	@Override
	public Gun getGun() {
		return myGun;
	}

	@Override
	public Bullet getBulletType() {
		return myBulletType;
	}

	@Override
	public void setMyBullets(ArrayList<BulletView> bullets) {
		myGun.myBullets=bullets;
	}

	@Override
	public ArrayList<BulletView> getMyBullets() {
		return myGun.myBullets;
	}

	@Override
	public void startShooting() {
		this.postDelayed(shootingRunnable,(long) getBulletFreq());
	}

	@Override
	public void stopShooting() {
		this.removeCallbacks(shootingRunnable);
	}
	
	@Override
	public ViewGroup getMyScreen() {
		return (ViewGroup)this.getParent();
	}

	@Override
	public boolean isFriendly() {
		return false;
	}

	@Override
	public double getBulletSpeedY() {
		return bulletSpeedY;
	}

	@Override
	public double getBulletDamage() {
		return bulletDamage;
	}

	@Override
	public double getBulletFreq() {
		return bulletFreq;
	}
	
	@Override
	public void setBulletFreq(double freq) {
		bulletFreq=freq;
	}

	@Override
	public void setBulletSpeedY(double newSpeed) {
		bulletSpeedY=newSpeed;
	}

	@Override
	public void setBulletDamage(double newDamage) {
		bulletDamage = newDamage;
	}

	@Override
	public void setBulletType(Bullet newBulletType) {
		this.myBulletType=newBulletType;
	}
}
