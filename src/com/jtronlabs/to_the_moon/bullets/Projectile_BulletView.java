package com.jtronlabs.to_the_moon.bullets;
  
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Projectile_BulletView extends ProjectileView{

	public static final double DEFAULT_HEALTH=0.1;
	public static final int DEFAULT_SCORE=0;
	
	private Gravity_ShootingView theOneWhoShotMe;
	
	protected boolean shootingUp;
	
	Runnable moveBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		//ensure view is not removed before running
    		if( ! Projectile_BulletView.this.isRemoved()){
	    		boolean atThreshold=false;
	    		//move up and down
	    		if(shootingUp){
	    			atThreshold=Projectile_BulletView.this.move(ProjectileView.UP);
	    		}else{
	    			atThreshold=Projectile_BulletView.this.move(ProjectileView.DOWN);
	    		}
	    		
	    		if(atThreshold || Projectile_BulletView.this.getHealth()<=0){//off screen
	    			Projectile_BulletView.this.removeView(false);
	    			Projectile_BulletView.this.removeCallbacks(null);
	    		}else{
	    			//move left and right is xSpeed != 0
	    			if( (Math.abs(Projectile_BulletView.this.getSpeedX())>0.0001)){
	            		if(Projectile_BulletView.this.getSpeedX()>0){
	            			atThreshold=Projectile_BulletView.this.move(ProjectileView.RIGHT);
	            		}else{
	            			atThreshold=Projectile_BulletView.this.move(ProjectileView.LEFT);
	            		}
	        		}
	    			Projectile_BulletView.this.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
	    		}
    		}
    	}
	};
	
	public Projectile_BulletView(Context context,Gravity_ShootingView shooter,boolean shootBulletUp,
			double projectileSpeedVertical,double projectileSpeedX, double projectileDamage,
			int bulletWidth, int bulletHeight, double positionOnShooterAsAPercentage) {
		super(context,DEFAULT_SCORE,projectileSpeedVertical,projectileSpeedVertical,
				projectileSpeedX,projectileDamage,DEFAULT_HEALTH,0);
		
		//set thresholds to off screen
		this.highestPositionThreshold=(int) -(MainActivity.getScreenDens() * 50);
		this.lowestPositionThreshold=(int) (MainActivity.getHeightPixels());
	
		//set instance variables
		theOneWhoShotMe=shooter;
		shootingUp=shootBulletUp;

		this.setLayoutParams(new LayoutParams(bulletWidth,bulletHeight));

		//set initial positions. Width and Height need to be already initialized for this to work
		final double posOnShooter= theOneWhoShotMe.getX()+ theOneWhoShotMe.getWidth() * 
				positionOnShooterAsAPercentage/100.0;
		final float middleOfBulletOnShootingPos = (float) (posOnShooter-bulletWidth/2.0);
		this.setX(middleOfBulletOnShootingPos);
		
		if(shootBulletUp){
			this.setY(theOneWhoShotMe.getY());			
		}else{
			this.setY(theOneWhoShotMe.getY()+theOneWhoShotMe.getHeight());			
		}

		//set bullet rotation and post the move runnable
		this.setBulletRotation();
		this.post(moveBulletRunnable);
	}
	
	public void setBulletRotation(){		
		//set bullet rotation
		if( ! (Math.abs(this.getSpeedX())<0.00001)){
			double arcTan;
			if(shootingUp){
				arcTan = Math.atan(this.getSpeedX()/this.getSpeedY());//Use trig to find rotation values of bullets
			}else{
				arcTan = Math.atan(-1*this.getSpeedX()/this.getSpeedY());//Multiply xSpeed by negative if shooting down
				arcTan = Math.PI + arcTan; 
			}
			final float rotVal = (float) Math.toDegrees(arcTan);
			this.setRotation(rotVal);
		}
	}
	
	/**
	 * Clean up threads. Remove bullet from Shooter's list of bullets. Check if Shooter is dead and all bullets are gone, if so remove Shooter from GameActivity.enemies. call super
	 */
	public int removeView(boolean showExplosion){
		theOneWhoShotMe.myGun.myBullets.remove(this);
		if(theOneWhoShotMe.getHealth()<=0 && theOneWhoShotMe.myGun.myBullets.size()==0){
			GameActivity.enemies.remove(theOneWhoShotMe);
		}
		return super.removeView(showExplosion);
	}
}
