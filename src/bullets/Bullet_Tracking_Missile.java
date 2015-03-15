package bullets;

import parents.MovingView;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_Tracking_Missile extends Bullet_Tracking{
	
//	public Bullet_Tracking_Missile(double trackSpeed, MovingView viewToTrack,Shooter shooterWithTrackingBullets){
//		super( trackSpeed,  viewToTrack, shooterWithTrackingBullets);
//	}
//	

	public Bullet_Tracking_Missile(MovingView viewToTrack,Shooter shooterWithTrackingBullets) {
		super(viewToTrack,shooterWithTrackingBullets);
	}
	
	@Override
	public BulletView getTrackingBullet(Context context, Shooter shooter,float bulletSpeedY,int bulletDamage){

		final int width=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		final int height=(int) context.getResources().getDimension(R.dimen.missile_one_height);

		final int backgroundId = R.drawable.bullet_missile;
		 
		BulletView bullet = new BulletView(context,shooter, width, height, bulletSpeedY, bulletDamage,width,height,backgroundId);
		
		return bullet;
	}	
}
