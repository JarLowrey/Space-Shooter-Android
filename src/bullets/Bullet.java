package bullets;

import interfaces.Shooter;
import android.content.Context;

  

public abstract class Bullet{
	
	public final static int BULLET_LEFT=0,
			BULLET_MIDDLE=50,
			BULLET_RIGHT=100;
	public static final double BULLET_TRAVELS_STRAIGHT=0;
	
	public abstract void stopBulletThreads();
	public abstract BulletView getBullet(Context context,Shooter shooter);
	
}
