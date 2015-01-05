package com.jtronlabs.to_the_moon.views;
  
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;

public class ShootingView extends GravityView{
	
	private int bulletImgId;
	private double bulletDamage,bulletSpeed,bulletFreq;
	
	public ArrayList<ProjectileView> myBullets;
	private boolean shootingUp=true,autoSpawnBullets=false;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnCenteredBullet();
    		ShootingView.this.postDelayed(this, (long) bulletFreq);
    	}
	};
	
	Runnable moveMyBulletsRunnable = new Runnable(){
    	@Override
        public void run() {
    		for(int i=0;i<myBullets.size();i++){
        		boolean atThreshold=false;
    			ProjectileView bullet = myBullets.get(i);
	    		if(shootingUp){
	    			atThreshold=bullet.move(ProjectileView.UP);
	    		}else{
	    			atThreshold=bullet.move(ProjectileView.DOWN);
	    		}
	    		if(atThreshold){
	    			bullet.removeView(false);
	    			myBullets.remove(i);
	    		}
    		}
    		if(ShootingView.this.getHealth()<=0 && myBullets.size()==0){
    			ShootingView.this.removeCallbacks(this);				
			}else{
    			ShootingView.this.postDelayed(moveMyBulletsRunnable, HOW_OFTEN_TO_MOVE);
    		}
    	}
	};
	

	public int removeView(boolean showExplosion){
		return super.removeView(showExplosion);
	}
	
	public ShootingView(Context context,int scoreValue,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		myBullets= new ArrayList<ProjectileView>();
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
		post(moveMyBulletsRunnable);
	}
	
	public ShootingView(Context context,AttributeSet at,int scoreValue,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg,int bulletBackground) {
		super(context,at,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		myBullets = new ArrayList<ProjectileView>();
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd*screenDens;
		bulletImgId=bulletBackground;
		post(moveMyBulletsRunnable);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
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
		shootingUp=false;
		autoSpawnBullets=true;
		bulletFreq=bulletSpawningFrequency;
	}
	
	/**
	 * Create a bullet and add it to the screen
	 */
	public void spawnCenteredBullet(){
		ProjectileView bullet = new ProjectileView(ctx,0,bulletSpeed,bulletSpeed,1,bulletDamage,0.1);
		
		//add bullet to layout
		bullet.setBackgroundResource(bulletImgId);
		int width=(int)ctx.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)ctx.getResources().getDimension(R.dimen.bullet_height);
		bullet.setLayoutParams(new LayoutParams(width,height));
		
		//initially position the Bullet View to avg of shooter's left & right, and at top if shooting up (or at bottom if shooting down)
		float xAvg = (ShootingView.this.getX()+ShootingView.this.getX()+ShootingView.this.getWidth())/2;
		bullet.setX(xAvg);
		if(shootingUp){
			bullet.setY(ShootingView.this.getY());//middle and top of ShootingView
			bullet.highestPositionThreshold=-bullet.getHeight();
		}else{
			bullet.setY(ShootingView.this.getY()+ShootingView.this.getHeight());//middle and bottom of ShootingView
			bullet.lowestPositionThreshold=heightPixels;
		}
		
		((RelativeLayout)this.getParent()).addView(bullet,1);

		myBullets.add(bullet);
	}
	
}
