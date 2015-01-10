package bullets;

import interfaces.Shooter;
import android.content.Context;

  

public abstract class Bullet_NotFancy extends Bullet{
	
	//no threads needed for a default bullet
	public void stopBulletThreads(){}
	
	public abstract BulletView getBullet(Context context,Shooter shooter);
	
}
