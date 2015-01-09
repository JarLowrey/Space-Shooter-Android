package com.jtronlabs.to_the_moon.bullets;

import java.util.ArrayList;

import android.util.Log;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.parents.MovingView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;
  

// 					NOT WORKING			
public abstract class Bullet_TrackingView extends Bullet{

	public static final double MAX_TRACKING_SPEED=20*MainActivity.getScreenDens();
	public static final double DEFAULT_TRACKING_SPEED=10;
	
	private double trackingSpeed;
	private MovingView viewTracking;
	private Shooter shooter;
	
	
	public Bullet_TrackingView(double trackSpeed, MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		
		shooter = shooterWithTrackingBullets;
		trackingSpeed = (trackingSpeed>MAX_TRACKING_SPEED) ? MAX_TRACKING_SPEED * 
				MainActivity.getScreenDens() : trackSpeed*MainActivity.getScreenDens();
		viewTracking=viewToTrack;
		shooter.post(trackingRunnable);
	}
	
	public Bullet_TrackingView(MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		
		shooter = shooterWithTrackingBullets;
		trackingSpeed = DEFAULT_TRACKING_SPEED;
		
		viewTracking=viewToTrack;
		shooter.post(trackingRunnable);
	}

	Runnable trackingRunnable = new Runnable(){
    	@Override
        public void run() {
			if( ! shooter.isDead()){
	    		final float objectTrackingMidPoint = (2* viewTracking.getX()+viewTracking.getWidth() ) /2;
	    		
				ArrayList<BulletView> bullets = shooter.getMyBullets();
				int i=0;
				for(BulletView bullet : bullets){
		    		final float bulletXMidPos = (2 * bullet.getX()+bullet.getWidth() ) / 2; 
					final float diff = bulletXMidPos - objectTrackingMidPoint;
        			
					Log.d("lowrey","diff"+diff);
					//if bullet is approximately at tracking destination, don't move it and set rotation to 0
		    		if( Math.abs(diff) < trackingSpeed*3){
		    			bullet.setSpeedX(Bullet.BULLET_TRAVELS_STRAIGHT);
		    		}else{
		    			if(diff>0){
		        			bullet.setSpeedX(-trackingSpeed);
		        			bullet.moveDirection(MovingView.LEFT);   			
		        		}else{
		        			bullet.setSpeedX(trackingSpeed);
		        			bullet.moveDirection(MovingView.RIGHT);    			
		        		}
		    		}
		    		i++;
	    			bullet.setBulletRotation();
				}
				shooter.postDelayed(this,MovingView.HOW_OFTEN_TO_MOVE*3);
			}
    	}
	};

	public void stopBulletThreads(){
		shooter.removeCallbacks(trackingRunnable);
	}
		
		
}
