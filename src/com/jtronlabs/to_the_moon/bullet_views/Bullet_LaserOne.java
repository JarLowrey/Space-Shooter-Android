package com.jtronlabs.to_the_moon.bullet_views;
  

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;

public class Bullet_LaserOne extends BulletView{
		
	private static final int ENEMY_BACKGROUND=R.drawable.laser1_enemy;
//	private static final double ENEMY_DAMAGE=5,ENEMY_SPEED_Y=8;
	

	private static final int FRIENDLY_BACKGROUND=R.drawable.laser1_hero;
//	private static final double FRIENDLY_DAMAGE=10,FRIENDLY_SPEED_Y=10;
//	
//	private static final double SPEED_X=-10;
	
	public Bullet_LaserOne(Context context,Gravity_ShootingView shooter, boolean shootBulletUp,int whichSideIsBulletOn,double projectileSpeedVertical,double projectileSpeedX, 
			double projectileDamage){
		super( context, shooter, shootBulletUp,context.getResources().getDimension(R.dimen.bullet_height),
				context.getResources().getDimension(R.dimen.bullet_width),whichSideIsBulletOn,
				 projectileSpeedVertical, projectileSpeedX,  projectileDamage);
		

		//give bullet a width and height
		int width=(int)context.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)context.getResources().getDimension(R.dimen.bullet_height);
		this.setLayoutParams(new LayoutParams(width,height));
		
		this.setBackgroundResource(getBackgroundId(shootBulletUp));
	}
	
	public Bullet_LaserOne(Context context,AttributeSet at,Gravity_ShootingView shooter, boolean shootBulletUp,int whichSideIsBulletOn,double projectileSpeedVertical,double projectileSpeedX, 
			double projectileDamage){
		super( context, at, shooter, shootBulletUp, context.getResources().getDimension(R.dimen.bullet_height),
				context.getResources().getDimension(R.dimen.bullet_width),whichSideIsBulletOn,
				 projectileSpeedVertical, projectileSpeedX,  projectileDamage);

		//give bullet a width and height
		int width=(int)context.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)context.getResources().getDimension(R.dimen.bullet_height);
		this.setLayoutParams(new LayoutParams(width,height));
		
		this.setBackgroundResource(getBackgroundId(shootBulletUp));
	}
	
	private static int getBackgroundId(boolean friendly){
		if(friendly){return FRIENDLY_BACKGROUND;}
		else{return ENEMY_BACKGROUND;}
	}
}
