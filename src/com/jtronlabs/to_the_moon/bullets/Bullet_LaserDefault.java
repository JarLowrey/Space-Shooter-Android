package com.jtronlabs.to_the_moon.bullets;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
  

public class Bullet_LaserDefault implements Bullet_Interface{
	
	public Projectile_BulletView getBullet(Context context,	Gravity_ShootingView shooter,boolean shootBulletUp,
	double bulletSpeedVertical,double bulletSpeedX, double bulletDamage,
	double positionOnShooterAsAPercentage){

		final int width=(int) context.getResources().getDimension(R.dimen.laser_default_width);
		final int height=(int) context.getResources().getDimension(R.dimen.laser_default_height);
		 
		
		Projectile_BulletView bullet = new Projectile_BulletView(context,shooter, shootBulletUp,
				bulletSpeedVertical, bulletSpeedX, bulletDamage,width,height,positionOnShooterAsAPercentage);

		int backgroundId=R.drawable.laser1_enemy;
		if(shootBulletUp){backgroundId = R.drawable.laser1_friendly;}
		
		bullet.setBackgroundResource(backgroundId);
		
		return bullet;
	}	
}
