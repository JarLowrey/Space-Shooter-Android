package com.jtronlabs.to_the_moon.bullets;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon_interfaces.Shooter;
  

public class Bullet_Missile extends Bullet{
	
	public BulletView getBullet(Context context,Shooter shooter,double bulletSpeedX,
	double positionOnShooterAsAPercentage){
		

		final int width=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		final int height=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		
		BulletView bullet = new BulletView(context,shooter, shooter.isFriendly(),
				shooter.getBulletSpeedY(), bulletSpeedX, shooter.getBulletDamage(),
				width,height,positionOnShooterAsAPercentage);

		int backgroundId=R.drawable.missile;
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}
	
}
