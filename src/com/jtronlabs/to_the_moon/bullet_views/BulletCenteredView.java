package com.jtronlabs.to_the_moon.bullet_views;
  

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;

public class BulletCenteredView extends BulletView{
		
	public BulletCenteredView(Context context,Gravity_ShootingView shooter,boolean shootBulletUp,float bulletHeight,float bulletWidth,float startingPixelPositionY,
			float leftPositionShooter,float rightPositionShooter,double projectileSpeedVertical,
			double projectileSpeedX, double projectileDamage){
		super(context,shooter,shootBulletUp,false,bulletHeight,startingPixelPositionY,projectileSpeedVertical,
				projectileSpeedX,projectileDamage);
		
		float x= (leftPositionShooter+rightPositionShooter)/2;
		x-=bulletWidth/2;
		this.setX(x);
	}
	
	public BulletCenteredView(Context context,AttributeSet at,Gravity_ShootingView shooter,boolean shootBulletUp,float bulletHeight,float bulletWidth,
			float startingPixelPositionY,float leftPositionShooter,float rightPositionShooter,
			double projectileSpeedVertical,double projectileSpeedX, 
			double projectileDamage) {
		super(context,at,shooter,shootBulletUp,false,bulletHeight,startingPixelPositionY,projectileSpeedVertical,
				projectileSpeedX,projectileDamage);

		float x= (leftPositionShooter+rightPositionShooter)/2;
		x-=bulletWidth/2;
		this.setX(x);
	}
}
