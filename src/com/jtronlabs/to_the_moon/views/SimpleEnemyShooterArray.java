package com.jtronlabs.to_the_moon.views;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;

public class SimpleEnemyShooterArray extends ShootingView implements GameObject{
	
	public final static double DEFAULT_SPEED_UP=5,DEFAULT_SPEED_DOWN=5,DEFAULT_SPEEDX=10,DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=10,DEFAULT_BULLET_SPEED=7,DEFAULT_BULLET_DAMAGE=5;
	
	public final static int NUM_SHOOTERS_IN_A_ROW=8,NUM_ROWS=4;
	
	private static ArrayList<Integer> freePositions = new ArrayList<Integer>();
	private int myPosition;
	
	public static ArrayList<SimpleEnemyShooterArray> allSimpleShooters= new ArrayList<SimpleEnemyShooterArray>();
	
	public SimpleEnemyShooterArray(Context context) {
		super(context,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_SPEED,DEFAULT_BULLET_DAMAGE,R.drawable.laser2);
		
		final double bulletFreq = (5000+Math.random()*4000);
		spawnBulletsAutomatically(bulletFreq);
		
		final int randPos = (int) (freePositions.size()*Math.random());
		myPosition = freePositions.remove(randPos);
		
		//ships move to a certain position on screen. There a set number of ships in each row, if the number is exceeded move to the next row
		final float height = context.getResources().getDimension(R.dimen.simple_enemy_shooter_height);
		final float lowestPointOnScreen = heightPixels/NUM_ROWS;
		final float myRowNum = (myPosition/NUM_SHOOTERS_IN_A_ROW)*height;
		this.lowestPositionThreshold=lowestPointOnScreen-myRowNum;
		
		changeSpeedYDown(2);
		
		//set image background, width, and height
		this.setImageResource(R.drawable.ufo);
		final int height_int=(int)height;
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_height);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		//set initial position
		final float marginOnSides = screenDens*10;
		final float shipXInterval = (widthPixels-marginOnSides)/NUM_SHOOTERS_IN_A_ROW;
		final float myColPos = myPosition%NUM_SHOOTERS_IN_A_ROW;
		final float xPos = shipXInterval*myColPos + marginOnSides/2;
		this.setX(xPos);
		this.setY(0);
		
		allSimpleShooters.add(this);
		
		cleanUpThreads();
		restartThreads();
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
	}
	public void restartThreads(){
		super.restartThreads();
	}
	
	public static int getMaxNumShips(){
		return NUM_SHOOTERS_IN_A_ROW*NUM_ROWS;
	}

	public void removeView(boolean showExplosion){
		super.removeView(showExplosion);
		allSimpleShooters.remove(this);
		freePositions.add(myPosition);
		cleanUpThreads();
	}
	public static void resetSimpleShooterPositions(){
		for(int i=0;i<getMaxNumShips();i++){
			freePositions.add(i);
		}
	}
	
}
