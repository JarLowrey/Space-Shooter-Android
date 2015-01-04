package com.jtronlabs.new_proj;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jtronlabs.views.MeteorView;
import com.jtronlabs.views.SideToSideMovingShooter;

public class EnemyFactory{
	
    private final int[] spaceJunkDrawableIds = {R.drawable.ufo,R.drawable.satellite,R.drawable.meteor};
    private float screenDens,widthPixels,heightPixels;    
	private Context ctx;
	private Levels levelInfo = new Levels();
	
    //
    public int meteorInterval=5000;
    public int movingSideToSideShooterInterval=5000;
    
    Handler enemySpawnHandler = new Handler();
    Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
//    		double rand = Math.random()*100;
//    		switch(levelInfo.getLevel()){
//    		case 1:
//    			if(rand<)
//    			break;
//    		case 2:
//    			
//    			break;
//    		case 3:
//    			
//    			break;
//    		case 4:
//    			
//    			break;
//    		case 5:
//    			
//    			break;
//    		}
    		MeteorView meteor = new MeteorView(ctx);
    		GameActivity.gameScreen.addView(meteor,1);
    		GameActivity.dangerousObjects.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, getMeteorInterval());
    	}
	};
	
	Runnable spawnMovingSideToSideShootingRunnable = new Runnable(){
    	@Override
        public void run() {
    		if(levelInfo.getLevel()>1 /*&& SideToSideMovingShooter.allSideToSideShooters.size()<SideToSideMovingShooter.NUM_SHOOTERS_IN_A_ROW*4*/){
    			SideToSideMovingShooter shooter = new SideToSideMovingShooter(ctx);
        		GameActivity.gameScreen.addView(shooter,1);
        		GameActivity.dangerousObjects.add(shooter);
        		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		}
    		enemySpawnHandler.postDelayed(this, getMovingSideToSideShooterInterval());
    	}
	};
	
	public EnemyFactory(Context context){
		ctx=context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
	}
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnMovingSideToSideShootingRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,getMeteorInterval());
	    enemySpawnHandler.postDelayed(spawnMovingSideToSideShootingRunnable,getMovingSideToSideShooterInterval());
	}
	
	private long getMeteorInterval(){
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*1000);
	}
	private long getMovingSideToSideShooterInterval(){
		return (long) (movingSideToSideShooterInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*3000);
	}
}
