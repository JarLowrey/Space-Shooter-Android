package com.jtronlabs.to_the_moon;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.views.DiagonalShooterView;
import com.jtronlabs.to_the_moon.views.MeteorView;
import com.jtronlabs.to_the_moon.views.SimpleEnemyShooterArray;

public class EnemyFactory{
	
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
	
    //
    public int meteorInterval=4000;
    public int simpleArrayShooterInterval=5000;
    public int diagonalShooterInterval=5500;
    
    private Handler enemySpawnHandler = new Handler();
    
    private Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
    		spawnGiantMeteor();
    		MeteorView meteor = new MeteorView(ctx);
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorSpawnInterval());
    	}
	};
	
	Runnable spawnSimpleShooterRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = SimpleEnemyShooterArray.allSimpleShooters.size();
    		final int maxNumShooters = SimpleEnemyShooterArray.getMaxNumShips();
    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.levelDifficulty()>1 && numShootersAlive<maxNumShooters){
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
    		
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterSpawnInterval());
    	}
	};
	
	Runnable spawnDiagonalShooterRunnable = new Runnable(){
		@Override
		public void run() {
			DiagonalShooterView shooter = new DiagonalShooterView(ctx);
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
			
			enemySpawnHandler.postDelayed(this, calculatDiagonalShooterSpawnInterval());
		}
	};
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		gameLayout=gameScreen;
		
		SimpleEnemyShooterArray.resetSimpleShooterArray();
	} 
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnSimpleShooterRunnable);
	    enemySpawnHandler.removeCallbacks(spawnDiagonalShooterRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnSimpleShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnDiagonalShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
	}
	
	private long calculateMeteorSpawnInterval(){
		return (long) (meteorInterval/(Math.sqrt(levelInfo.levelDifficulty()))+Math.random()*3000);
	}
	private long calculateMovingSideToSideShooterSpawnInterval(){
		return (long) (simpleArrayShooterInterval/(Math.sqrt(levelInfo.levelDifficulty()))+Math.random()*3000);
	}
	private long calculatDiagonalShooterSpawnInterval(){
		return (long) (diagonalShooterInterval/(Math.sqrt(levelInfo.levelDifficulty()))+Math.random()*4000);
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
	public void spawnGiantMeteor(){
		MeteorView giant = new MeteorView(ctx);
		
		//change width and height
		final int wid = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_width);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_height);
		giant.setLayoutParams(new LayoutParams(wid,height));
		
		//set damage and health to 200, score to 20
		giant.setDamage(200);
		giant.heal(200-MeteorView.DEFAULT_HEALTH);
		giant.setScoreValue(20);
		
		//add to layout
		gameLayout.addView(giant,1);
		GameActivity.enemies.add(giant);		
	}
}
