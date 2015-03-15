package bullets;

import interfaces.Shooter;
import android.content.Context;

  

public abstract class Bullet{
	
	public final static int BULLET_LEFT=20,
			BULLET_MIDDLE=50,
			BULLET_RIGHT=80;
	
	public abstract BulletView getBullet(Context context,Shooter shooter,float bulletSpeedY,int bulletDamage);
	
}
