package bullets;

import parents.MovingView;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_Tracking_LaserShort extends Bullet_Tracking{
	
	public Bullet_Tracking_LaserShort(double trackSpeed, MovingView viewToTrack,Shooter shooterWithTrackingBullets){
		super( trackSpeed,  viewToTrack, shooterWithTrackingBullets);
	}
	

	public Bullet_Tracking_LaserShort(MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		super(viewToTrack,shooterWithTrackingBullets);
	}
	
	public BulletView getBullet(Context context, Shooter shooter,double bulletSpeedY,double bulletDamage){

		final int width=(int) context.getResources().getDimension(R.dimen.laser_short_width);
		final int height=(int) context.getResources().getDimension(R.dimen.laser_short_height);
		 
		BulletView bullet = new BulletView(context,shooter, width, height, bulletSpeedY, bulletDamage);

		int backgroundId=R.drawable.laser1_enemy;
		if(shooter.isFriendly()){backgroundId = R.drawable.laser1_friendly;}
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}	
}
