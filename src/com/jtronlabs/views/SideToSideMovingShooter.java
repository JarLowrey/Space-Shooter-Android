package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.new_proj.R;

public class SideToSideMovingShooter extends ShootingView{
	
	private final static double DEFAULT_SPEED_UP=15,DEFAULT_SPEED_DOWN=5,DEFAULT_SPEEDX=10,DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=10,DEFAULT_BULLET_SPEED=8,DEFAULT_BULLET_DAMAGE=5,DEFAULT_BULLET_SPAWNING_FREQ=(3000+Math.random()*4000);
	
	public final static int NUM_SHOOTERS_IN_A_ROW=5;
	
	public static ArrayList<SideToSideMovingShooter> allSideToSideShooters= new ArrayList<SideToSideMovingShooter>();
	
	public SideToSideMovingShooter(Context context) {
		super(context,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_SPEED,DEFAULT_BULLET_DAMAGE,R.drawable.laser2);
		spawnBulletsAutomatically(DEFAULT_BULLET_SPAWNING_FREQ);
		
		float height = context.getResources().getDimension(R.dimen.side_to_side_moving_shooter_height);
		//have these ships move down to half of the screen, but only have 5 in a row at once
		//if there are more than 5, do not pass the first 5
		this.threshold=heightPixels/NUM_SHOOTERS_IN_A_ROW-(allSideToSideShooters.size()/NUM_SHOOTERS_IN_A_ROW)*height;
		
		changeSpeedYDown(1.5);
		
		//set image background, width, and height
		this.setImageResource(R.drawable.ufo);
		int height_int=(int)height;
		int width_int = (int)context.getResources().getDimension(R.dimen.side_to_side_moving_shooter_height);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		//set initial position
		float xRand = (float) ((widthPixels-height)*Math.random());
		this.setX(xRand);
		this.setY(-height/2);//slightly off top of screen
		
		allSideToSideShooters.add(this);
		
		cleanUpThreads();
		restartThreads();
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
	}
	public void restartThreads(){
		super.restartThreads();
	}

	public void removeView(boolean showExplosion){
		super.removeView(showExplosion);
		cleanUpThreads();
	}
	
}
