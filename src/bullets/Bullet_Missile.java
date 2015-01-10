package bullets;

import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;
  

public class Bullet_Missile extends Bullet_NotFancy{
	
	public BulletView getBullet(Context context,Shooter shooter){
		

		final int width=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		final int height=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		
		BulletView bullet = new BulletView(context,shooter,
				width,height);

		int backgroundId=R.drawable.missile;
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}
	
}
