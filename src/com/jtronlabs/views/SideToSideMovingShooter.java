package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.new_proj.R;

public class SideToSideMovingShooter extends ShootingView implements GameObject{
	
	private final static double DEFAULT_SPEED_UP=5,DEFAULT_SPEED_DOWN=5,DEFAULT_SPEEDX=10,DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=10,DEFAULT_BULLET_SPEED=8,DEFAULT_BULLET_DAMAGE=5,DEFAULT_BULLET_SPAWNING_FREQ=(3000+Math.random()*4000);
	
	public final static int NUM_SHOOTERS_IN_A_ROW=8,NUM_ROWS=4,MAX_NUM_SIDE_TO_SIDE_SHOOTERS=NUM_SHOOTERS_IN_A_ROW*NUM_ROWS;
	
	public static ArrayList<SideToSideMovingShooter> allSideToSideShooters= new ArrayList<SideToSideMovingShooter>();
	
	public SideToSideMovingShooter(Context context) {
		super(context,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_SPEED,DEFAULT_BULLET_DAMAGE,R.drawable.laser2);
		spawnBulletsAutomatically(DEFAULT_BULLET_SPAWNING_FREQ);
		
		int numAlive = allSideToSideShooters.size();
		float height = context.getResources().getDimension(R.dimen.side_to_side_moving_shooter_height);
		//ships move to a certain position on screen. There a set number of ships in each row, if the number is exceeded move to the next row
		this.lowestPositionOnScreen=heightPixels/NUM_ROWS-(numAlive/NUM_SHOOTERS_IN_A_ROW)*height;
		
		changeSpeedYDown(2);
		
		//set image background, width, and height
		this.setImageResource(R.drawable.ufo);
		int height_int=(int)height;
		int width_int = (int)context.getResources().getDimension(R.dimen.side_to_side_moving_shooter_height);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		//set initial position
		float xRand = (float) ((widthPixels-height)*Math.random());
		float marginOnSides = screenDens*10;
		float xPrecise  =(float)(((widthPixels-marginOnSides)/NUM_SHOOTERS_IN_A_ROW)*(numAlive%NUM_SHOOTERS_IN_A_ROW));
		xPrecise+=marginOnSides/2;
		this.setX(xPrecise);
		this.setY(0);
		
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
		allSideToSideShooters.remove(this);
		cleanUpThreads();
	}
	
}
