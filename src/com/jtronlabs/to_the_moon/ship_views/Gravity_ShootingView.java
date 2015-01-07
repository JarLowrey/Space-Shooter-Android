package com.jtronlabs.to_the_moon.ship_views;
  
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullet_views.BulletView;
import com.jtronlabs.to_the_moon.bullet_views.Bullet_LaserOne;

public class Gravity_ShootingView extends Projectile_GravityView{
	
	public static final int LASER_ONE=0;
	boolean triShot=false,dualShot=false;
	double bulletSpeedY=10,
			bulletSpeedX=-10,
			bulletDamage=5;
//	private double oldBulletSpeedY,
//			oldBulletSpeedX,
//			oldBulletDamage;
	
	private int myBulletType=LASER_ONE,ammo=-1;
	
	private double bulletFreq;
	public ArrayList<BulletView> myBullets;
	private boolean shootingUp=false;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		if(triShot){
    			spawnMyBullet(BulletView.BULLET_LEFT);
    			spawnMyBullet(BulletView.BULLET_RIGHT);
        		spawnMyBullet(BulletView.BULLET_MIDDLE);
    		}else if (dualShot){
    			spawnMyBullet(BulletView.BULLET_LEFT);
    			spawnMyBullet(BulletView.BULLET_RIGHT);    			
    		}else{
        		spawnMyBullet(BulletView.BULLET_MIDDLE);    			
    		}
    		if(ammo>0){
    			decrementAmmo();
    		}
    		Gravity_ShootingView.this.postDelayed(this, (long) bulletFreq);
    	}
	};

	public int removeView(boolean showExplosion){
		cleanUpThreads();
		return super.removeView(showExplosion);
	}
	
	public Gravity_ShootingView(Context context,boolean shootingUpwards,int scoreValue,double projectileSpeedUp,
			double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,
				projectileDamage,projectileHealth);
		
		shootingUp= shootingUpwards;
		myBullets= new ArrayList<BulletView>();
	}
	
	public Gravity_ShootingView(Context context,AttributeSet at,boolean shootingUpwards,int scoreValue,double projectileSpeedUp,
			double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,at,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,
				projectileDamage,projectileHealth);

		shootingUp= shootingUpwards;
		myBullets = new ArrayList<BulletView>();
	}
	
	public void cleanUpThreads(){
		stopShooting();
		super.cleanUpThreads();
	}
	
	public void stopShooting(){
		this.removeCallbacks(spawnBulletRunnable);		
	}
	
	public void restartThreads(){
		super.restartThreads();
		startShooting(bulletFreq);
	}
	/**
	 * shoot 3 bullets, one in middle of ship, one on the left, and one on the right. 
	 * The left and right bullets will travel in the X direction based upon their speedX, the center bullet will only go straight
	 * @param set a 
	 */
	public void setTriShot(double newBulletSpeedX){
		dualShot=false;
		triShot=true;
		bulletSpeedX= newBulletSpeedX;
	}
	public boolean isTriShot(){
		return triShot;
	}
	/**
	 * shoot 2 bullets, one on the left of the ship and one on the right. 
	 * The left and right bullets will travel in the X direction based upon their speedX, the center bullet will only go straight
	 */
	public void setDualShot(double newBulletSpeedX){
		dualShot=true;
		triShot=false;
		bulletSpeedX=newBulletSpeedX;
	}
	public boolean isDualShot(){
		return dualShot;
	}
	/**
	 * shoot 1 bullet in center of ship
	 */
	public void setSingleShot(){
		dualShot=false;
		triShot=false;
		bulletSpeedX=-10;
	}
	
	public void startShooting(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		this.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	
	public void startShootingImmediately(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		this.post(spawnBulletRunnable);
	}
	public void setBulletProperties(int whichBullet,double bulletSpeedVertical,
			double bulletSpeedHorizontal,double newBulletDamage) throws IllegalArgumentException{
		if(whichBullet!=LASER_ONE){
			throw new IllegalArgumentException("Unknown bullet type");
		}else{
			myBulletType=whichBullet;
			bulletSpeedY=bulletSpeedVertical;
			bulletSpeedX=bulletSpeedHorizontal;
			bulletDamage=newBulletDamage;
		}
		
	}
	/**
	 * 
	 * @param whichSideBulletIsOn
	 * @param ySpeed
	 * @param xSpeed
	 * @param damage
	 */
	private void spawnMyBullet(int whichSideBulletIsOn){
		BulletView bullet=null;
		switch(this.myBulletType){
		case LASER_ONE:
			bullet = new Bullet_LaserOne(ctx, this, shootingUp,whichSideBulletIsOn, bulletSpeedY, bulletSpeedX, bulletDamage);
			break;
		}
		((RelativeLayout)this.getParent()).addView(bullet,1);

		myBullets.add(bullet);
	}
	
	public double getBulletSpeedX(){
		return this.bulletSpeedX;
	}
	public double getBulletSpeedY(){
		return this.bulletSpeedY;
	}
	public double getBulletDamage(){
		return this.bulletDamage;
	}
	public int getBulletType(){
		return myBulletType;
	}

	/**
	 * upgrade gun type if current gun type is worse than the max
	 * @return True if gun was upgraded
	 */
	public boolean upgradeGun(){
		if(myBulletType < LASER_ONE){
			myBulletType++;
			return true;
		}
		return false;
	}
	/**
	 * Set the amount of special ammo the rocket has. New value must be positive
	 * @param newAmmoAmount A postive, nonzero number representing this RocketView's store of special ammunition
	 * @return True if newAmmoAmount was >0 and thus ammo was properly set. False otherwise
	 */
	public boolean setAmmo(int newAmmoAmount){
		if(newAmmoAmount>0){
			ammo=newAmmoAmount;
//			oldBulletSpeedY=bulletSpeedY;
//			oldBulletSpeedX=bulletSpeedX;
//			oldBulletDamage=bulletDamage;
			return true;
		}
		return false;
	}
	/**
	 * Decrement the amount of special ammo the View has. If View is out of ammo, revert to single shot and previous damage attributes
	 * @param newAmmoAmount 
	 * @return True if the rocket has run out of ammo, false otherwise
	 */
	private boolean decrementAmmo(){
		boolean outOfAmmo=false;
		ammo--;
		if(ammo<=0){
			outOfAmmo=true;
			this.setSingleShot();
//			this.setBulletProperties(myBulletType,oldBulletSpeedY,oldBulletSpeedX,oldBulletDamage);
		}
		return outOfAmmo;
		
	}
	public int getAmmo(){
		return ammo;
	}
}
