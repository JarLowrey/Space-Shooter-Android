package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.new_proj.GameActivity;
import com.jtronlabs.new_proj.R;

public class ShootingView extends GravityView{
	
	int bulletImgId;
	double bulletDamage,bulletSpeed,bulletFreq;
	
	ArrayList<ProjectileView> myBullets = new ArrayList<ProjectileView>();
	private boolean shootingDown=true;
	private final int bulletMovingFreq=50;
	
	Handler shooterHandler = new Handler();
    Runnable shootBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		ProjectileView bullet = new ProjectileView(ctx,bulletSpeed,1,bulletDamage,0.1);
    		
    		//add bullet to layout
    		bullet.setBackgroundResource(bulletImgId);
    		int width=(int)ctx.getResources().getDimension(R.dimen.bullet_width);
    		int height=(int)ctx.getResources().getDimension(R.dimen.bullet_height);
    		bullet.setLayoutParams(new LayoutParams(width,height));
    		
    		//position Bullet View
    		float xAvg = (ShootingView.this.getX()+ShootingView.this.getX()+ShootingView.this.getWidth())/2;
    		bullet.setX(xAvg);
    		if(shootingDown){
    			bullet.setY(ShootingView.this.getY()+ShootingView.this.getHeight());//middle and bottom of ShootingView
    		}else{
    			bullet.setY(ShootingView.this.getY());//middle and top of ShootingView
    		}
    		GameActivity.gameScreen.addView(bullet,1);
    		GameActivity.dangerousObjects.add(bullet);

    		myBullets.add(bullet);
    		shooterHandler.postDelayed(this, (long) bulletFreq);
    	}
	};
	
	Runnable moveTheBulletsRunnable = new Runnable(){
    	@Override
        public void run() {
    		for(int i=0;i<myBullets.size();i++){
    			ProjectileView bullet = myBullets.get(i);
				float y = bullet.getY();
	    		if(shootingDown){
	    			y-=bullet.speedY;
	    		}else{
	    			y+=bullet.speedY;
	    		}
				bullet.setY(y);
    		}
			shooterHandler.postDelayed(shootBulletRunnable, bulletMovingFreq);
			
    	}
	};
	
	public ShootingView(Context context,double projectileSpeedY,double projectileSpeedX, double projectileDamage,double projectileHealth,double bulletSpd,double bulletDmg, double bulletFrequency,int bulletBackground) {
		super(context,projectileSpeedY,projectileSpeedX,projectileDamage,projectileHealth);
		
		bulletFreq=bulletFrequency;
		bulletDamage=bulletDmg;
		bulletSpeed=bulletSpd;
		bulletImgId=bulletBackground;
		
		shooterHandler.postDelayed(shootBulletRunnable,(long) bulletFreq);
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
	public void changeBullerSpeed(double newBulletSpeed){
		bulletSpeed=newBulletSpeed;
	}
	public void changeBulletDamage(double newBulletDamage){
		bulletDamage=newBulletDamage;
	}
	
	/**
	 * By default, a ShootingView shoots downward to the bottom of the screen. This method makes the 
	 * bullets travel upwards, to the top of the screen
	 */
	public void switchShootingDirection(){
		shootingDown=!shootingDown;
	}
	
}
