package com.jtronlabs.to_the_moon.bullet_views;
  

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.ship_views.ShootingView;

public class BulletViewOne extends CenteredBulletView{
		
	private static final int ENEMY_BACKGROUND=R.drawable.laser1_enemy;
	private static final double ENEMY_DAMAGE=5,ENEMY_SPEED_Y=8;
	

	private static final int FRIENDLY_BACKGROUND=R.drawable.laser1_hero;
	private static final double FRIENDLY_DAMAGE=10,FRIENDLY_SPEED_Y=10;
	
	private static final double SPEED_X=-10;
	
	public BulletViewOne(Context context,ShootingView shooter, boolean shootBulletUp,float startingPixelPositionY,
			float leftPositionShooter,float rightPositionShooter){
		super(context,shooter,shootBulletUp,getBulletHeight(context),getBulletWidth(context),startingPixelPositionY, 
				leftPositionShooter, rightPositionShooter,
				getSpeedY(shootBulletUp),SPEED_X,getDamage(shootBulletUp));
		

		//give bullet a width and height
		int width=(int)context.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)context.getResources().getDimension(R.dimen.bullet_height);
		this.setLayoutParams(new LayoutParams(width,height));
		
		this.setBackgroundResource(getBackgroundId(shootBulletUp));
	}
	
	public BulletViewOne(Context context,AttributeSet at,ShootingView shooter,boolean shootBulletUp,
			float startingPixelPositionY,float leftPositionShooter,float rightPositionShooter) {
		super(context,at,shooter,shootBulletUp,getBulletHeight(context),getBulletWidth(context),
				startingPixelPositionY, leftPositionShooter,rightPositionShooter,
				getSpeedY(shootBulletUp),SPEED_X,getDamage(shootBulletUp));

		//give bullet a width and height
		int width=(int)context.getResources().getDimension(R.dimen.bullet_width);
		int height=(int)context.getResources().getDimension(R.dimen.bullet_height);
		this.setLayoutParams(new LayoutParams(width,height));
		
		this.setBackgroundResource(getBackgroundId(shootBulletUp));
	}
	
	private static double getDamage(boolean friendly){
		if(friendly){return FRIENDLY_DAMAGE;}
		else{return ENEMY_DAMAGE;}
	}
	private static double getSpeedY(boolean friendly){
		if(friendly){return FRIENDLY_SPEED_Y;}
		else{return ENEMY_SPEED_Y;}
	}
	private static int getBackgroundId(boolean friendly){
		if(friendly){return FRIENDLY_BACKGROUND;}
		else{return ENEMY_BACKGROUND;}
	}
	private static float getBulletWidth(Context context){
		return context.getResources().getDimension(R.dimen.bullet_width);
	}

	private static float getBulletHeight(Context context){
		return context.getResources().getDimension(R.dimen.bullet_height);
	}
}
