package com.jtronlabs.to_the_moon.views;
  
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;

public class ShootingView extends GravityView{
	
	private double bulletFreq;
	private double bulletDamageRatio=1,bulletSpeedRatio=1;
	private static double BULLET_ONE_DMG=5,BULLET_ONE_SPEED=7;
	public ArrayList<ProjectileView> myBullets;
	private boolean shootingUp=true,autoSpawnBullets=false;
	
    Runnable spawnBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnLevelOneCenteredBullet();
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
	
	public ShootingView(Context context,int scoreValue,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		myBullets= new ArrayList<ProjectileView>();
		post(moveMyBulletsRunnable);
	}
	
	public ShootingView(Context context,AttributeSet at,int scoreValue,double projectileSpeedUp,double projectileSpeedDown,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,at,scoreValue,projectileSpeedUp,projectileSpeedDown,projectileSpeedX,projectileDamage,projectileHealth);
		
		myBullets = new ArrayList<ProjectileView>();
		post(moveMyBulletsRunnable);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		stopShooting();
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
		shootingUp=false;
		bulletFreq=bulletSpawningFrequency;
		autoSpawnBullets=true;
	}
	
	/**
	 * Create a bullet and add it to the screen
	 */
	public void spawnLevelOneCenteredBullet(){
		ProjectileView bullet = createBullet(ctx,BULLET_ONE_SPEED,BULLET_ONE_DMG);
		
		//initially position the Bullet View to avg of shooter's left & right, and at top if shooting up (or at bottom if shooting down)
		float xAvg = (ShootingView.this.getX()+ShootingView.this.getX()+ShootingView.this.getWidth())/2;
		android.view.ViewGroup.LayoutParams params = bullet.getLayoutParams();//bullet.getWidth() returns 0 as it is not yet drawn. The params are set though, and thus this method works to find the width
		bullet.setX(xAvg-params.width/2);
		
		if(shootingUp){
			bullet.setBackgroundResource(R.drawable.laser1_hero);
			bullet.setY(ShootingView.this.getY());//middle and top of ShootingView
			bullet.highestPositionThreshold=-bullet.getHeight();
		}else{
			bullet.setBackgroundResource(R.drawable.laser1_enemy);
			bullet.setY(ShootingView.this.getY()+ShootingView.this.getHeight());//middle and bottom of ShootingView
			bullet.lowestPositionThreshold=heightPixels;
		}
		
		((RelativeLayout)this.getParent()).addView(bullet,1);

		myBullets.add(bullet);
	}
	
	private ProjectileView createBullet(Context context,double bulletsSpeed,double bulletsDamage) {
		ProjectileView bullet = new ProjectileView(context,0,bulletsSpeed*bulletSpeedRatio,bulletsSpeed*bulletSpeedRatio,1,bulletsDamage*this.bulletDamageRatio,0.1);

		//give bullet a width and height
		int width=(int)ctx.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)ctx.getResources().getDimension(R.dimen.bullet_height);
		bullet.setLayoutParams(new LayoutParams(width,height));
		
		return bullet;
	}
	
	/**
	 * The damage of every new bullet spawned will be multiplied by the passed parameter
	 * @param ratio The amount multiplied by default bullet damage values
	 */
	public void changeNewBulletDamage(double ratio){
		bulletDamageRatio=ratio;
	}
	/**
	 * The speed of every new bullet spawned will be multiplied by the passed parameter
	 * @param ratio The amount multiplied by default bullet speed values
	 */
	public void changeNewBulletSpeed(double ratio){
		bulletSpeedRatio=ratio;
	}
	
}
