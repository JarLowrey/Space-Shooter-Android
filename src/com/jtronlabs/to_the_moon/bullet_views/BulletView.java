package com.jtronlabs.to_the_moon.bullet_views;
  
import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.misc.ProjectileView;
import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;

public class BulletView extends ProjectileView{
	
	public static final double DEFAULT_HEALTH=0.1;
	public static final int DEFAULT_SCORE=0;
	private Gravity_ShootingView theOneWhoShotMe;
	
	private boolean shootingUp,shootingRight;
	
	Runnable moveBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		boolean atThreshold=false;
    		if(shootingUp){
    			atThreshold=BulletView.this.move(ProjectileView.UP);
    		}else{
    			atThreshold=BulletView.this.move(ProjectileView.DOWN);
    		}
    		if(BulletView.this.getSpeedX()>0){
        		if(shootingRight){
        			atThreshold=BulletView.this.move(ProjectileView.RIGHT);
        		}else{
        			atThreshold=BulletView.this.move(ProjectileView.LEFT);
        		}
    		}
    		if(atThreshold || BulletView.this.getHealth()<=0){
    			BulletView.this.removeView(false);
    			BulletView.this.removeCallbacks(this);
    		}else{
    			BulletView.this.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
    		}
    	}
	};
	
	public BulletView(Context context,Gravity_ShootingView shooter,boolean shootBulletUp,boolean shootBulletRight,float bulletHeight,float startingPixelPositionY,
			double projectileSpeedVertical,double projectileSpeedX, double projectileDamage) {
		super(context,DEFAULT_SCORE,projectileSpeedVertical,projectileSpeedVertical,
				projectileSpeedX,projectileDamage,DEFAULT_HEALTH);
		
		theOneWhoShotMe=shooter;
		
		//set thresholds to off screen
		this.highestPositionThreshold=-bulletHeight;
		this.lowestPositionThreshold=heightPixels+bulletHeight;
		
		//set Y position and the instance boolean for whether or not bullet is traveling up or down, left or right
		this.setY(startingPixelPositionY);
		shootingUp=shootBulletUp;
		shootingRight=shootBulletRight;
		
		//post the movement runnable
		this.post(moveBulletRunnable);
	}
	
	public BulletView(Context context,AttributeSet at,Gravity_ShootingView shooter,boolean shootBulletUp, boolean shootBulletRight,float bulletHeight,
			float startingPixelPositionY,double projectileSpeedVertical,double projectileSpeedX, 
			double projectileDamage) {
		super(context,at,DEFAULT_SCORE,projectileSpeedVertical,projectileSpeedVertical,
				projectileSpeedX,projectileDamage,DEFAULT_HEALTH);

		theOneWhoShotMe=shooter;
		
		//set thresholds to off screen
		this.highestPositionThreshold=-bulletHeight;
		this.lowestPositionThreshold=heightPixels+bulletHeight;
		
		//set Y position and the instance boolean for whether or not bullet is traveling up or down,left or right
		this.setY(startingPixelPositionY);
		shootingUp=shootBulletUp;
		shootingRight=shootBulletRight;
				
		//post the movement runnable
		this.post(moveBulletRunnable);
	}
	
	public int removeView(boolean showExplosion){
		cleanUpThreads();
		theOneWhoShotMe.myBullets.remove(this);
		return super.removeView(showExplosion);
	}
	public void cleanUpThreads(){
		this.removeCallbacks(moveBulletRunnable);
		super.cleanUpThreads();
	}
	
	public void restartThreads(){
		this.post(moveBulletRunnable);
		super.restartThreads();
	}
	
//	/**
//	 * Create a bullet and add it to the screen
//	 */
//	public void spawnLevelOneCenteredBullet(){
//		ProjectileView bullet = createBullet(ctx,BULLET_ONE_SPEED,BULLET_ONE_DMG);
//		
//		//initially position the Bullet View to avg of shooter's left & right, and at top if shooting up (or at bottom if shooting down)
//		float xAvg = (BulletView.this.getX()+BulletView.this.getX()+BulletView.this.getWidth())/2;
//		android.view.ViewGroup.LayoutParams params = this.getLayoutParams();//bullet.getWidth() returns 0 as it is not yet drawn. The params are set though, and thus this method works to find the width
//		bullet.setX(xAvg-params.width/2);
//		
//		if(shootingUp){
//			bullet.setBackgroundResource(R.drawable.laser1_hero);
//			bullet.setY(BulletView.this.getY());//middle and top of ShootingView
//		}else{
//			bullet.setBackgroundResource(R.drawable.laser1_enemy);
//			bullet.setY(BulletView.this.getY()+BulletView.this.getHeight());//middle and bottom of ShootingView
//		}
//		
//		((RelativeLayout)this.getParent()).addView(bullet,1);
//
//	}	
}
