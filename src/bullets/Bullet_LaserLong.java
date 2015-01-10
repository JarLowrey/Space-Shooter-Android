package bullets;

import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_LaserLong extends Bullet_NotFancy{
	
	public BulletView getBullet(Context context,Shooter shooter){

		final int width=(int) context.getResources().getDimension(R.dimen.laser_long_width);
		final int height=(int) context.getResources().getDimension(R.dimen.laser_long_height);

		BulletView bullet = new BulletView(context,shooter, 
				width,height);
		
		int backgroundId=R.drawable.laser1_enemy;
		if(shooter.isFriendly()){backgroundId = R.drawable.laser1_friendly;}
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}
}
