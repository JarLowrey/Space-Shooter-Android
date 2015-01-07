package com.jtronlabs.to_the_moon.ship_views;
  
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullet_views.Bullet_LaserOne;
import com.jtronlabs.to_the_moon.bullet_views.BulletView;

public class Gravity_ShootingView extends GravityView{
	
	public static final int LASER_ONE=0;
	boolean triShot=false;
	double bulletSpeedY=10,
			bulletSpeedX=-10,
			bulletDamage=5;
	
	private int myBulletType=LASER_ONE;
	
	private double bulletFreq;
	public ArrayList<BulletView> myBullets;
	private boolean shootingUp=false;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnMyBullet(BulletView.BULLET_MIDDLE);
    		if(triShot){
    			spawnMyBullet(BulletView.BULLET_LEFT);
    			spawnMyBullet(BulletView.BULLET_RIGHT);
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
	
	public void startShooting(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		this.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
	}
	
	public void startShootingImmediately(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		this.post(spawnBulletRunnable);
	}
	public void setMyBulletProperties(int whichBullet,double bulletSpeedVertical,
			double bulletSpeedHorizontal,double bulletDamage) throws IllegalArgumentException{
		if(whichBullet!=LASER_ONE){
			throw new IllegalArgumentException("Unknown bullet type");
		}else{
			myBulletType=whichBullet;
			bulletSpeedY=10;
			bulletSpeedX=-10;
			bulletDamage=5;
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
}
