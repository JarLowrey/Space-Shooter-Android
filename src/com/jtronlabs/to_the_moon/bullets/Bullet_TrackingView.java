package com.jtronlabs.to_the_moon.bullets;
  
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Bullet_TrackingView extends BulletView{

	private float trackingSpeed;
	private ProjectileView objectTracking;
	
	Runnable trackingRunnable = new Runnable(){
    	@Override
        public void run() {
    		final float objectMidPoint = (objectTracking.getX()+objectTracking.getX()+objectTracking.getWidth())/2;
    		final float myXPos = Bullet_TrackingView.this.getX(); 
			final float diff = myXPos - objectMidPoint;
    		
    		if(diff>0){
        		Bullet_TrackingView.this.setX(myXPos-trackingSpeed);    			
    		}else{
        		Bullet_TrackingView.this.setX(myXPos+trackingSpeed);    			
    		}
    		
    		Bullet_TrackingView.this.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
    	}
	};
	
	public Bullet_TrackingView(Context context,ProjectileView objectToTrack,float trackSpeed,
			Gravity_ShootingView shooter,boolean shootBulletUp,int whichSideIsBulletOn,
			double projectileSpeedVertical,double projectileSpeedX, double projectileDamage) {
		
		super(context,shooter, shootBulletUp, whichSideIsBulletOn,
				 projectileSpeedVertical, projectileSpeedX, projectileDamage);
		
		trackingSpeed=trackSpeed*screenDens;
		objectTracking=objectToTrack;
		this.post(trackingRunnable);
	}
	
	public void cleanUpThreads(){
		this.removeCallbacks(trackingRunnable);
		super.cleanUpThreads();
	}
	public void restartThreads(){
		this.post(trackingRunnable);
		super.restartThreads();
	}
	public void setTrackingSpeed(float newSpeed){
		trackingSpeed=newSpeed;
	}
//	
//	public Bullet_TrackingView(Context context,AttributeSet at,Gravity_ShootingView shooter,boolean shootBulletUp,
//			int whichSideIsBulletOn,double projectileSpeedVertical,double projectileSpeedX, 
//			double projectileDamage) {
//		super(context,at,DEFAULT_SCORE,projectileSpeedVertical,projectileSpeedVertical,
//				projectileSpeedX,projectileDamage,DEFAULT_HEALTH,0);
//
//	}
}
