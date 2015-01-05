package com.jtronlabs.to_the_moon;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.views.MeteorView;
import com.jtronlabs.to_the_moon.views.SimpleEnemyShooterArray;

public class EnemyFactory{
	
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
	
    //
    public int meteorInterval=4000;
    public int movingSideToSideShooterInterval=5000;
    
    private Handler enemySpawnHandler = new Handler();
    
    private Runnable meteorSpawningRunnable = new Runnable(){
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
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorInterval());
    	}
	};
	
	Runnable spawnSimpleShooterRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = SimpleEnemyShooterArray.allSimpleShooters.size();
    		final int maxNumShooters = SimpleEnemyShooterArray.getMaxNumShips();
    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.levelDifficulty()>1 && numShootersAlive<maxNumShooters){

    			Log.d("lowrey","level="+levelInfo.getLevel());
    			Log.d("lowrey","goal="+levelInfo.getLevelGoal(levelInfo.getLevel()));
    			
    			//if num shooters< .33 of the max, there is a 33% chance to respawn all shooters
    			if(numShootersAlive<(maxNumShooters/numShootersAliveCutoff) && Math.random()<0.33 ){
    				spawnAllSimpleShooters();
    			}
    			//otherwise, respawn just 1 simple shooter
    			else{
	    			SimpleEnemyShooterArray shooter = new SimpleEnemyShooterArray(ctx);
	    			gameLayout.addView(shooter,1);
	        		GameActivity.enemies.add(shooter);
    			}
			}
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterInterval());
    	}
	};
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		gameLayout=gameScreen;
		
		SimpleEnemyShooterArray.resetSimpleShooterPositions();
	}
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnSimpleShooterRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorInterval());
	    enemySpawnHandler.postDelayed(spawnSimpleShooterRunnable,calculateMovingSideToSideShooterInterval());
	}
	
	private long calculateMeteorInterval(){
		return (long) (meteorInterval/(Math.sqrt(levelInfo.levelDifficulty()))+Math.random()*1000);
	}
	private long calculateMovingSideToSideShooterInterval(){
		return (long) (movingSideToSideShooterInterval/(Math.sqrt(levelInfo.levelDifficulty()))+Math.random()*3000);
	}
	public void spawnAllSimpleShooters(){
		int temp=SimpleEnemyShooterArray.allSimpleShooters.size();//needed due to the intricacies of a for loop
		
		for(int i=temp;i<SimpleEnemyShooterArray.getMaxNumShips();i++){
			SimpleEnemyShooterArray shooter = new SimpleEnemyShooterArray(ctx);
			shooter.changeSpeedYDown(1.2);
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
		}
	}
}
