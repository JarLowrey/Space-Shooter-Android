package com.jtronlabs.to_the_moon.ship_views;
  
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.bullet_views.BulletView;
import com.jtronlabs.to_the_moon.bullet_views.BulletViewOne;

public class ShootingView extends GravityView{
	
	public static final int BULLET_ENEMY_ONE=0,BULLET_FRIENDLY_ONE=1;
	private int myBulletType=BULLET_ENEMY_ONE;
	
	private double bulletFreq;
	public ArrayList<BulletView> myBullets;
	private boolean autoSpawnBullets=false;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnMyBullet();
    		ShootingView.this.postDelayed(this, (long) bulletFreq);
    	}
	};

	public int removeView(boolean showExplosion){
		cleanUpThreads();
		return super.removeView(showExplosion);
	}
	
	public ShootingView(Context context,int scoreValue,double projectileSpeedUp,
			double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,
				projectileDamage,projectileHealth);
		
		myBullets= new ArrayList<BulletView>();
	}
	
	public ShootingView(Context context,AttributeSet at,int scoreValue,double projectileSpeedUp,
			double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,at,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,
				projectileDamage,projectileHealth);
		
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
		if(autoSpawnBullets){
			this.postDelayed(spawnBulletRunnable,(long) bulletFreq);		
		}
	}
	
	public void changeBulletFrequency(double newBulletFreq){
		bulletFreq=newBulletFreq;
	}
	
	public void startShooting(double bulletSpawningFrequency){
		autoSpawnBullets=true;
		bulletFreq=bulletSpawningFrequency;
		this.post(spawnBulletRunnable);		
	}
	/**
	 * set View to automatically create bullets. These bullets will be shot downwards, toward the protagonist
	 * @param bulletSpawningFrequency-frequency at which bullets are automatically spawned
	 */
	public void spawnBulletsAutomatically(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		autoSpawnBullets=true;
	}
	
	public void setMyBulletType(int whichBullet) throws IllegalArgumentException{
		if(whichBullet!=BULLET_ENEMY_ONE &&  whichBullet != BULLET_FRIENDLY_ONE){
			throw new IllegalArgumentException("Unknown bullet type");
		}
		myBulletType=whichBullet;
	}
	
	private void spawnMyBullet(){
		BulletView bullet=null;
		switch(this.myBulletType){
		case BULLET_ENEMY_ONE:
			bullet = new BulletViewOne(super.ctx,this,false, this.getY()+this.getHeight(), this.getX(), this.getX()+this.getWidth());
			break;
		case BULLET_FRIENDLY_ONE:
			bullet = new BulletViewOne(super.ctx,this,true, this.getY(), this.getX(), this.getX()+this.getWidth());
			break;
		}
		((RelativeLayout)this.getParent()).addView(bullet,1);

		myBullets.add(bullet);
	}
}
