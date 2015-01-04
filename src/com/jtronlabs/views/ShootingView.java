package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.new_proj.GameActivity;
import com.jtronlabs.new_proj.R;

public class ShootingView extends GravityView{
	
	private int bulletImgId;
	private double bulletDamage,bulletSpeed,bulletFreq;
	
	ArrayList<ProjectileView> myBullets = new ArrayList<ProjectileView>();
	private boolean shootingUp=true;
	private final int bulletMovingFreq=50;
	
	Handler shooterHandler = new Handler();
    Runnable shootBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnBullet();
    		shooterHandler.postDelayed(this, (long) bulletFreq);
    	}
	};
	
	Runnable moveTheBulletsRunnable = new Runnable(){
    	@Override
        public void run() {
    		for(int i=0;i<myBullets.size();i++){
    			ProjectileView bullet = myBullets.get(i);
				float y = bullet.getY();
	    		if(shootingUp){
	    			y-=bullet.speedY;
	    		}else{
	    			y+=bullet.speedY;
	    		}
				bullet.setY(y);
    		}
			shooterHandler.postDelayed(shootBulletRunnable, bulletMovingFreq);
			
    	}
	};
	
	public ShootingView(Context context,double projectileSpeedY,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,projectileSpeedY,projectileSpeedX,projectileDamage,projectileHealth);
		
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
		
		shooterHandler.post(moveTheBulletsRunnable);
	}
	
	public ShootingView(Context context,AttributeSet at,double projectileSpeedY,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,projectileSpeedY,projectileSpeedX,projectileDamage,projectileHealth);
		
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
		
		shooterHandler.post(moveTheBulletsRunnable);
	}
	
	/**
	 * Assign default values to bullet damage, speed, and background Id. Post runnable in charge of moving bullets
	 */
	private void initShootingView(){
		bulletDamage=10;
		bulletSpeed=25*screenDens;
		bulletImgId=R.drawable.laser1;
		
		shooterHandler.post(moveTheBulletsRunnable);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		shooterHandler.removeCallbacks(shootBulletRunnable);
		shooterHandler.removeCallbacks(moveTheBulletsRunnable);
	}
	
	public void restartThreads(){
		super.restartThreads();
		shooterHandler.postDelayed(shootBulletRunnable,(long) bulletFreq);		
		shooterHandler.post(moveTheBulletsRunnable);
	}
	
	public void changeBulletFrequency(double newBulletFreq){
		bulletFreq=newBulletFreq;
	}
	public void changeBulletBackground(int newBackgroundResourceId){
		bulletImgId=newBackgroundResourceId;
	}
	public void changeBulletSpeed(double newBulletSpeed){
		bulletSpeed=newBulletSpeed;
	}
	public void changeBulletDamage(double newBulletDamage){
		bulletDamage=newBulletDamage;
	}
	/**
	 * set View to automatically create bullets. These bullets will be shot downwards, toward the protagonist
	 * @param bulletSpawningFrequency-frequency at which bullets are automatically spawned
	 */
	public void spawnBulletsAutomatically(double bulletSpawningFrequency){
		bulletFreq=bulletSpawningFrequency;
		shooterHandler.postDelayed(shootBulletRunnable,(long) bulletFreq);
		shootingUp=false;
	}
	
	/**
	 * Create a bullet and add it to the screen
	 */
	public void spawnBullet(){
		ProjectileView bullet = new ProjectileView(ctx,bulletSpeed,1,bulletDamage,0.1);
		
		//add bullet to layout
		bullet.setBackgroundResource(bulletImgId);
		int width=(int)ctx.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)ctx.getResources().getDimension(R.dimen.bullet_height);
		bullet.setLayoutParams(new LayoutParams(width,height));
		
		//position Bullet View
		float xAvg = (ShootingView.this.getX()+ShootingView.this.getX()+ShootingView.this.getWidth())/2;
		bullet.setX(xAvg);
		if(shootingUp){
			bullet.setY(ShootingView.this.getY()+ShootingView.this.getHeight());//middle and bottom of ShootingView
		}else{
			bullet.setY(ShootingView.this.getY());//middle and top of ShootingView
		}
		GameActivity.gameScreen.addView(bullet,1);
		GameActivity.dangerousObjects.add(bullet);

		myBullets.add(bullet);
	}
	
}
