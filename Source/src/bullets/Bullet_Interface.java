package bullets;

import interfaces.Shooter;
import android.widget.RelativeLayout;

  

public abstract class Bullet_Interface{
	
	public final static int DEFAULT_BULLET_SPEED_Y = 20;
	
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
