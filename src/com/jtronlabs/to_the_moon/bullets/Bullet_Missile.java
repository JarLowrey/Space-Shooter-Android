package com.jtronlabs.to_the_moon.bullets;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
  

public class Bullet_Missile implements Bullet_Interface{
	
	public Projectile_BulletView getBullet(Context context,	Gravity_ShootingView shooter,boolean shootBulletUp,
	double bulletSpeedVertical,double bulletSpeedX, double bulletDamage,
	double positionOnShooterAsAPercentage){

		final int width=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		final int height=(int) context.getResources().getDimension(R.dimen.missile_one_width);
		
		Projectile_BulletView bullet = new Projectile_BulletView(context,shooter, shootBulletUp,
				bulletSpeedVertical, bulletSpeedX, bulletDamage,
				width,height,positionOnShooterAsAPercentage);

		int backgroundId=R.drawable.missile;
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}
	
}
