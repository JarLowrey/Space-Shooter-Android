package bullets;

import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_Basic_Missile extends Bullet_Basic{

	public BulletView getBullet(Context context, Shooter shooter,double bulletSpeedY,double bulletDamage){
		
		final int width=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		final int height=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		
		BulletView bullet = new BulletView(context,shooter,
				width,height, bulletSpeedY, bulletDamage,width,height,R.drawable.bullet_missile);

		return bullet;
	}
	
}
