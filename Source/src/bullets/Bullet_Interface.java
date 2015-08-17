package bullets;

import enemies_non_shooters.Gravity_MeteorView;
import parents.MovingView;
import interfaces.Shooter;
import android.widget.RelativeLayout;

  

public abstract class Bullet_Interface{
	
	public final static float 
		DEFAULT_BULLET_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y * 1.45);//Density Pixels per millisecond;
	
	public final static int BULLET_LEFT=20,
			BULLET_MIDDLE=50,
			BULLET_RIGHT=80; 

	int width, height, backgroundId;
	
	public Bullet_Interface(int bulletWidth, int bulletHeight, int bulletBackgroundId){

		width = bulletWidth;
		height = bulletHeight;
		backgroundId = bulletBackgroundId;
	}
	
	public abstract BulletView getBullet(RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage);
	
}
