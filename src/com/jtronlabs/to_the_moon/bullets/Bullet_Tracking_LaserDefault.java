//package com.jtronlabs.to_the_moon.bullets;
//
//import android.content.Context;
//
//import com.jtronlabs.to_the_moon.R;
//import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
//import com.jtronlabs.to_the_moon.views.ProjectileView;
//  
//
//public class Bullet_Tracking_LaserDefault extends Bullet_TrackingConstantlyView implements Bullet_Interface{
//	
//	public Bullet_Tracking_LaserDefault(double trackSpeed,
//			ProjectileView viewToTrack) {
//		super(trackSpeed, viewToTrack);
//
//		
//	}
//
//	public Projectile_BulletView getBullet(Context context,	Gravity_ShootingView shooter,boolean shootBulletUp,
//	double bulletSpeedVertical,double bulletSpeedX, double bulletDamage,
//	double positionOnShooterAsAPercentage){
//
//		final int width=(int) context.getResources().getDimension(R.dimen.laser_default_width);
//		final int height=(int) context.getResources().getDimension(R.dimen.laser_default_height);
//		 
//		
//		Projectile_BulletView bullet = new Projectile_BulletView(context,shooter, shootBulletUp,
//				bulletSpeedVertical, bulletSpeedX, bulletDamage,width,height,positionOnShooterAsAPercentage);
//
//		int backgroundId=R.drawable.laser1_enemy;
//		if(shootBulletUp){backgroundId = R.drawable.laser1_friendly;}
//		
//		bullet.setBackgroundResource(backgroundId);
//		
//		this.beginTracking(bullet);
//		
//		
//		return bullet;
//	}	
//}
