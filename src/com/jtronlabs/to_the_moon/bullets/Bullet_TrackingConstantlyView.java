package com.jtronlabs.to_the_moon.bullets;

import com.jtronlabs.to_the_moon.views.ProjectileView;
  

public abstract class Bullet_TrackingConstantlyView extends Bullet{

	public static double DEFAULT_TRACKING_SPEED=2;
	
	private float widthPix;
	private double trackingSpeed;
	private ProjectileView objectTracking;
	private Projectile_BulletView bulletTracking;
	
	Runnable trackingRunnable = new Runnable(){
    	@Override
        public void run() {
    		final float objectTrackingMidPoint = (objectTracking.getX()+objectTracking.getX()+objectTracking.getWidth())/2;
    		final float myXPos = bulletTracking.getX(); 
			final float diff = myXPos - objectTrackingMidPoint;
			final double weight = trackingSpeed*(diff/widthPix);//farther away from object tracking means an increase in trackingSpeed
			
			bulletTracking.setSpeedX(bulletTracking.getSpeedX()+weight);
			bulletTracking.setBulletRotation();
			
    		if(diff>0){
    			bulletTracking.move(ProjectileView.LEFT);   			
    		}else{
    			bulletTracking.move(ProjectileView.RIGHT);    			
    		}
    		
    		bulletTracking.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
    	}
	};
	
	public Bullet_TrackingConstantlyView(double trackSpeed, ProjectileView viewToTrack,
			Projectile_BulletView bulletDoingTheTracking, double screenDens,float widthPixels) {
		
		trackingSpeed=trackSpeed*screenDens;
		objectTracking=viewToTrack;
		bulletTracking = bulletDoingTheTracking;
		bulletTracking.post(trackingRunnable);
		widthPix=widthPixels;
		
		bulletDoingTheTracking.post(trackingRunnable);
	}
}
