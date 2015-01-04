package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;

import com.jtronlabs.new_proj.R;

public class SideToSideMovingShooter extends ShootingView{
	
	private final static double DEFAULT_SPEEDY=20,DEFAULT_SPEEDX=10,DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=10,DEFAULT_BULLET_SPEED=30,DEFAULT_BULLET_DAMAGE=5,DEFAULT_BULLET_SPAWNING_FREQ=(1000-+Math.random()*1000);
	public static ArrayList<SideToSideMovingShooter> allMovingShooters= new ArrayList<SideToSideMovingShooter>();
	
	public SideToSideMovingShooter(Context context) {
		super(context,DEFAULT_SPEEDY,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_SPEED,DEFAULT_BULLET_DAMAGE,R.drawable.laser2);
		spawnBulletsAutomatically(DEFAULT_BULLET_SPAWNING_FREQ);
		
		float height = context.getResources().getDimension(R.dimen.side_to_side_moving_shooter);
		//have these ships move down to half of the screen, but only have 5 in a row at once
		//if there are more than 5, do not pass the first 5
		this.threshold=heightPixels/2-(allMovingShooters.size()/5)*height;
	}
	
	
	
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
	}
	public void restartThreads(){
		super.restartThreads();
	}
	
}
