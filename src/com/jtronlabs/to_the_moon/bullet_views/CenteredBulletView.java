package com.jtronlabs.to_the_moon.bullet_views;
  

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.ship_views.ShootingView;

public class CenteredBulletView extends BulletView{
		
	public CenteredBulletView(Context context,ShootingView shooter,boolean shootBulletUp,float bulletHeight,float bulletWidth,float startingPixelPositionY,
			float leftPositionShooter,float rightPositionShooter,double projectileSpeedVertical,
			double projectileSpeedX, double projectileDamage){
		super(context,shooter,shootBulletUp,false,bulletHeight,startingPixelPositionY,projectileSpeedVertical,
				projectileSpeedX,projectileDamage);
		
		float x= (leftPositionShooter+rightPositionShooter)/2;
		x-=bulletWidth/2;
		this.setX(x);
	}
	
	public CenteredBulletView(Context context,AttributeSet at,ShootingView shooter,boolean shootBulletUp,float bulletHeight,float bulletWidth,
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
