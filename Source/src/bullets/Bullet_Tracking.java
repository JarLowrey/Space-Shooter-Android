package bullets;

import interfaces.Shooter;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;

public class Bullet_Tracking extends Bullet_Interface{

	public Bullet_Tracking(MovingView viewToTrack,
			Shooter shooterWithTrackingBullets,
			int bulletWidth, int bulletHeight, int bulletBackgroundId){
		super(bulletWidth,bulletHeight,bulletBackgroundId);

	}
	
	public BulletView getBullet(RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage){
		Bullet_TrackingView b = new Bullet_TrackingView(layout,shooter,
				bulletSpeedY, bulletDamage,width,height,backgroundId);
		return b;
	}
	
}
