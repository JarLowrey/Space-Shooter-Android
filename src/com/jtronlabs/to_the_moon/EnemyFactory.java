package com.jtronlabs.to_the_moon;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.views.MeteorView;
import com.jtronlabs.to_the_moon.views.SimpleEnemyShooter;

public class EnemyFactory{
	
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
	
    //
    public int meteorInterval=4000;
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
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorInterval());
    	}
	};
	
	Runnable spawnSimpleShooterRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = SimpleEnemyShooter.allSimpleShooters.size();
    		final int maxNumShooters = SimpleEnemyShooter.getMaxNumShips();
    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.getLevel()>1 && numShootersAlive<maxNumShooters){
    			//if num shooters< .33 of the max, there is a 33% chance to respawn all shooters
    			if(numShootersAlive<(maxNumShooters/numShootersAliveCutoff) && Math.random()<0.33 ){
    				spawnAllSimpleShooters();
    			}
    			//otherwise, respawn just 1 simple shooter
    			else{
	    			SimpleEnemyShooter shooter = new SimpleEnemyShooter(ctx);
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
		
		SimpleEnemyShooter.resetSimpleShooterPositions();
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
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*1000);
	}
	private long calculateMovingSideToSideShooterInterval(){
		return (long) (movingSideToSideShooterInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*3000);
	}
	public void spawnAllSimpleShooters(){
		int temp=SimpleEnemyShooter.allSimpleShooters.size();//needed due to the intricacies of a for loop
		Log.d("lowrey","size="+temp);
		Log.d("lowrey","max="+SimpleEnemyShooter.getMaxNumShips());
		
		for(int i=temp;i<SimpleEnemyShooter.getMaxNumShips();i++){
			SimpleEnemyShooter shooter = new SimpleEnemyShooter(ctx);
			shooter.changeSpeedYDown(1.2);
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
		}
	}
}
