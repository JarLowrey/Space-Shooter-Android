package com.jtronlabs.to_the_moon.bullets;

import android.util.Log;

import com.jtronlabs.to_the_moon.views.ProjectileView;
  

public abstract class Bullet_TrackingConstantlyView implements Bullet_Interface{

	public static final double MAX_TRACKING_SPEED=3;
	public static final double DEFAULT_TRACKING_SPEED=2;

	public static int num=0;
	
	private double trackingSpeed;
	private ProjectileView viewTracking;
	private Projectile_BulletView bulletTracking;
	
	
	public Bullet_TrackingConstantlyView(double trackSpeed, ProjectileView viewToTrack) {
		trackingSpeed = (trackingSpeed>MAX_TRACKING_SPEED) ? MAX_TRACKING_SPEED * 
				viewToTrack.screenDens: trackSpeed*viewToTrack.screenDens;
		viewTracking=viewToTrack;
	}
	
	public void beginTracking(Projectile_BulletView bulletDoingTheTracking){

		Runnable trackingRunnable = new Runnable(){
	    	@Override
	        public void run() {
				if(!bulletTracking.isRemoved()){
		    		
		    		final float objectTrackingMidPoint = (viewTracking.getX()+viewTracking.getX()+viewTracking.getWidth())/2;
		    		final float myXPos = bulletTracking.getX(); 
					final float diff = myXPos - objectTrackingMidPoint;
		    		
					//if bullet is approximately at tracking destination, don't move it and set rotation to 0
		    		if( Math.abs(bulletTracking.getX()-objectTrackingMidPoint) < Math.abs(trackingSpeed)+bulletTracking.getWidth() ){
		    			bulletTracking.setSpeedX(Bullet_Interface.BULLET_TRAVELS_STRAIGHT);
		    			final float rotation = (bulletTracking.shootingUp) ? 0 : 180;
		    			bulletTracking.setRotation(rotation);
		    		}else{
		    			if(diff>0){
		        			bulletTracking.setSpeedX(-trackingSpeed);
		        			bulletTracking.move(ProjectileView.LEFT);   			
		        		}else{
		        			bulletTracking.setSpeedX(trackingSpeed);
		        			bulletTracking.move(ProjectileView.RIGHT);    			
		        		}
		    			bulletTracking.setBulletRotation();
		    		}
		    		 
		    		bulletTracking.setX(objectTrackingMidPoint);
		    		num++;
		    		Log.d("lowrey","num"+num);
		    		bulletTracking.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
				}
	    	}
		};
		
		bulletTracking = bulletDoingTheTracking;
		bulletDoingTheTracking.post(trackingRunnable);
	}
}
