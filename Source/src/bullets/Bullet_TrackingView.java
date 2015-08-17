package bullets;

import com.jtronlabs.space_shooter.MainActivity;

import enemies_non_shooters.Gravity_MeteorView;

import interfaces.GameActivityInterface;
import interfaces.Shooter;
import parents.MovingView;
import android.widget.RelativeLayout;

public class Bullet_TrackingView extends BulletView
{
	public static final float 
			MAX_TRACKING_SPEED = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y*.75),
			DEFAULT_TRACKING_SPEED = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y*.3);
	
	private float trackingSpeed;
	
	public Bullet_TrackingView(RelativeLayout layout, Shooter shooter,
			float bulletSpeedY, int bulletDamage, int width, int height,
			int imageId) {
		super(layout, shooter, bulletSpeedY, bulletDamage, width, height, imageId);

		final float trackSpeedDPI  = DEFAULT_TRACKING_SPEED * MainActivity.getScreenDens();
		trackingSpeed = (trackSpeedDPI>MAX_TRACKING_SPEED) ? MAX_TRACKING_SPEED : trackSpeedDPI;
	}
	

	@Override
	public void updateViewSpeed(long deltaTime){
		MovingView viewTracking = ( (GameActivityInterface)getContext() ).getProtagonist();
		final float objectTrackingMidPoint = (2* viewTracking.getX()+viewTracking.getWidth() ) /2;
		final float bulletXMidPos = (2 * getX()+getWidth() ) / 2; 
		final float diff = objectTrackingMidPoint - bulletXMidPos ;
		
		//if bullet is approximately at tracking destination, don't move it and set rotation to 0
		if( Math.abs(diff) < viewTracking.getWidth()/2 ){
			setSpeedX(0);
		}else{				
			trackingSpeed = diff/Math.abs(diff) * Math.abs(trackingSpeed);//track in direction of difference
			
			//set speed and move sideways
			setSpeedX(trackingSpeed); 
		}
	}
	
	@Override
	public void move(long deltaTime){
		setBulletRotation();//use new speed and set rotation
		
		super.move(deltaTime);
	}
}
