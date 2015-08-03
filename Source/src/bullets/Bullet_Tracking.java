package bullets;

import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.Shooter;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
  

// 					NOT WORKING			
public class Bullet_Tracking extends Bullet_Interface{

	public static final float MAX_TRACKING_SPEED=5*MainActivity.getScreenDens();
	public static final float DEFAULT_TRACKING_SPEED=4*MainActivity.getScreenDens();
	
	private float trackingSpeed;
	private MovingView viewTracking;
	
	public Bullet_Tracking(float trackSpeed, MovingView viewToTrack,Shooter shooterWithTrackingBullets,
			int bulletWidth, int bulletHeight, int bulletBackgroundId){
		super(bulletWidth,bulletHeight,bulletBackgroundId);

		viewTracking=viewToTrack;
		
		final float trackSpeedDPI  = trackSpeed*MainActivity.getScreenDens();
		trackingSpeed = (trackSpeedDPI>MAX_TRACKING_SPEED) ? MAX_TRACKING_SPEED : trackSpeedDPI;
	}
	
	public Bullet_Tracking(MovingView viewToTrack,Shooter shooterWithTrackingBullets,
			int bulletWidth, int bulletHeight, int bulletBackgroundId){
		super(bulletWidth,bulletHeight,bulletBackgroundId);

		viewTracking=viewToTrack;
	}

	public BulletView getBullet(RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage){
		BulletView b = new BulletView(layout,shooter, bulletSpeedY, bulletDamage,width,height,backgroundId);
		setToTrackingBullet(b);
		return b;
	}
	
	private void setToTrackingBullet(final BulletView b){
		b.reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				final float objectTrackingMidPoint = (2* viewTracking.getX()+viewTracking.getWidth() ) /2;
	    		final float bulletXMidPos = (2 * b.getX()+b.getWidth() ) / 2; 
				final float diff = objectTrackingMidPoint - bulletXMidPos ;
				
				//if bullet is approximately at tracking destination, don't move it and set rotation to 0
	    		if( Math.abs(diff) < viewTracking.getWidth()/2 ){
	    			b.setSpeedX(0);
	    		}else{
	    			trackingSpeed = diff/Math.abs(diff) * Math.abs(trackingSpeed);//track in direction of difference
	    			 
	    			//set speed and move sideways
	    			b.setSpeedX(trackingSpeed); 
	    		}
	    		
				b.setBulletRotation();
				b.move();
				
				ConditionalHandler.postIfAlive(this, MovingView.HOW_OFTEN_TO_MOVE,b);
			}
		});
	}
	
}
