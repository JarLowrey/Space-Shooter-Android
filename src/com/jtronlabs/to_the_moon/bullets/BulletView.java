package com.jtronlabs.to_the_moon.bullets;
  
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class BulletView extends Moving_ProjectileView{

	
	private Shooter theOneWhoShotMe;
	
	protected boolean shootingUp;
	
	Runnable moveBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		//ensure view is not removed before running
    		if( ! BulletView.this.isRemoved()){
    			
	    		//move up and down
	    		if(shootingUp){
	    			BulletView.this.moveDirection(Moving_ProjectileView.UP);
	    		}else{
	    			BulletView.this.moveDirection(Moving_ProjectileView.DOWN);
	    		}
	    		
	    		//move sideways only if horizontal speed is not 0. This is not needed (since 0 horizontal speed results in 0 movement),
	    		//but I'd like to think it saves some resources
    			if( (Math.abs(BulletView.this.getSpeedX())>0.0001)){
    				//move sideways
            		if(BulletView.this.getSpeedX()>0){
            			BulletView.this.moveDirection(Moving_ProjectileView.RIGHT);
            		}else{
            			BulletView.this.moveDirection(Moving_ProjectileView.LEFT);
            		}
        		}
	    		
    			BulletView.this.postDelayed(this,HOW_OFTEN_TO_MOVE);
    		}
    	}
	};
	
	public BulletView(Context context,Shooter shooter,boolean shootBulletUp,
			double projectileSpeedVertical,double projectileSpeedX, double projectileDamage,
			int bulletWidth, int bulletHeight, double positionOnShooterAsAPercentage) {
		super(context,projectileSpeedVertical,
				projectileSpeedX,projectileDamage,1);
	
		//set instance variables
		theOneWhoShotMe=shooter;
		shootingUp=shootBulletUp;
		shooter.getMyBullets().add(this);
		
		this.setLayoutParams(new LayoutParams(bulletWidth,bulletHeight));

		//set initial positions. Width and Height need to be already initialized for this to work
		final double posRelativeToShooter= theOneWhoShotMe.getWidth() * 	positionOnShooterAsAPercentage/100.0;
		final float middleOfBulletOnShootingPos = (float) (posRelativeToShooter+theOneWhoShotMe.getX()-bulletWidth/2.0);
		this.setX(middleOfBulletOnShootingPos);
		
		if(shootBulletUp){
			this.setY(theOneWhoShotMe.getY()+theOneWhoShotMe.getHeight());//bottom			
		}else{
			this.setY(theOneWhoShotMe.getY());//top
		}

		//set bullet rotation and post the move runnable
		this.setBulletRotation();
		this.post(moveBulletRunnable);
	}
	
	public void setBulletRotation(){		
		//set bullet rotation
		if( ! (Math.abs(this.getSpeedX())<0.00001)){  //save some resources by not rotating if xSpeed is 0
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
	 * Remove bullet from Shooter's list of bullets. Check if Shooter is dead and all bullets are gone, if so remove Shooter from GameActivity.enemies. call super
	 */
	public void removeGameObject(){
		theOneWhoShotMe.getMyBullets().remove(this);
		if(theOneWhoShotMe.isDead() && theOneWhoShotMe.getMyBullets().size()==0){
			GameActivity.enemies.remove(theOneWhoShotMe);
		}
		
		super.removeGameObject();
	}
}
