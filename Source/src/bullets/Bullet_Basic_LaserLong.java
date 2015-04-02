package bullets;

import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_Basic_LaserLong extends Bullet_Basic{
	
	public BulletView getBullet(Context context,Shooter shooter,float bulletSpeedY,int bulletDamage){

		final int width=(int) context.getResources().getDimension(R.dimen.laser_long_width);
		final int height=(int) context.getResources().getDimension(R.dimen.laser_long_height);

		int backgroundId=R.drawable.laser1_enemy;
		if(shooter.isFriendly()){backgroundId = R.drawable.laser1_friendly;}
		
		BulletView bullet = new BulletView(context,shooter, 
				width,height, bulletSpeedY, bulletDamage,width,height,backgroundId);
		
		return bullet;
	}
}
