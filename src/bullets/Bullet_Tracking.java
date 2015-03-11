package bullets;

import interfaces.Shooter;

import java.util.ArrayList;

import parents.MovingView;
import support.ConditionalHandler;
import support.KillableRunnable;

import com.jtronlabs.to_the_moon.MainActivity;
  

// 					NOT WORKING			
public abstract class Bullet_Tracking extends Bullet{

	public static final float MAX_TRACKING_SPEED=5*MainActivity.getScreenDens();
	public static final float DEFAULT_TRACKING_SPEED=4*MainActivity.getScreenDens();
	
	private float trackingSpeed;
	private MovingView viewTracking;
	private Shooter shooter;
	protected ArrayList<BulletView> myTrackingBullets;
	
	
	public Bullet_Tracking(float trackSpeed, MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		
		myTrackingBullets= new ArrayList<BulletView>();
		shooter = shooterWithTrackingBullets;
		final float trackSpeedDPI  = trackSpeed*MainActivity.getScreenDens();
		trackingSpeed = (trackSpeedDPI>MAX_TRACKING_SPEED) ? MAX_TRACKING_SPEED * 
				MainActivity.getScreenDens() : trackSpeedDPI;
		viewTracking=viewToTrack;
		shooter.post(trackingRunnable);
	}
	
	public Bullet_Tracking(MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		
		myTrackingBullets= new ArrayList<BulletView>();
		shooter = shooterWithTrackingBullets;
		trackingSpeed = DEFAULT_TRACKING_SPEED;
		
		viewTracking=viewToTrack;
		shooter.post(trackingRunnable);
	}

	KillableRunnable trackingRunnable = new KillableRunnable(){
    	@Override
        public void doWork() {
    		final float objectTrackingMidPoint = (2* viewTracking.getX()+viewTracking.getWidth() ) /2;
    		
			for(BulletView bullet : myTrackingBullets){
	    		final float bulletXMidPos = (2 * bullet.getX()+bullet.getWidth() ) / 2; 
				final float diff = objectTrackingMidPoint - bulletXMidPos ;
    			
				//if bullet is approximately at tracking destination, don't move it and set rotation to 0
	    		if( Math.abs(diff) < viewTracking.getWidth()/2 ){
	    			bullet.setSpeedX(0);
	    		}else{
	    			trackingSpeed = diff/Math.abs(diff) * Math.abs(trackingSpeed);//track in direction of difference
	    			 
	    			//set speed and move sideways
        			bullet.setSpeedX(trackingSpeed);
        			bullet.moveDirection(MovingView.SIDEWAYS);    
	    		}
	    		
    			bullet.setBulletRotation();
			}
			ConditionalHandler.postIfAlive(this,MovingView.HOW_OFTEN_TO_MOVE*3,shooter);//I could use the default time and lower the speed, but having a higher speed and a less often movement time is less computationally intense
    	}
	};

	@Override
	public void removeBulletType(){
		shooter.removeCallbacks(trackingRunnable);
		for(int i=myTrackingBullets.size()-1;i>=0;i--){
			myTrackingBullets.remove(i);
		}
	}
	
}
