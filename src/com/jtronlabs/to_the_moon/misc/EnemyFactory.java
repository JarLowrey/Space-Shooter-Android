package com.jtronlabs.to_the_moon.misc;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.specific_view_types.Gravity_MeteorView;
import com.jtronlabs.specific_view_types.Shooting_ArrayMovingView;
import com.jtronlabs.specific_view_types.Shooting_DiagonalMovingView;
import com.jtronlabs.specific_view_types.Shooting_OrbiterView;
import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class EnemyFactory{
	
//	public final static double ENEMY_ONE_SCORE=10,
//			ENEMY_ONE_HEALTH=10,
//			ENEMY_ONE_COLLISION_DAMAGE=10,
//			ENEMY_ONE_SPEED_X_AND_Y=3,
//			ENEMY_ONE_BULLET_FREQ
			
			
			
			
	private Context ctx;
	private Levels levelInfo = new Levels();
	private RelativeLayout gameLayout;
    
    private Handler enemySpawnHandler = new Handler();
    
    private Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
//    		Gravity_MeteorView meteor = spawnMeteorView();
    		Shooting_OrbiterView meteor = spawnOrbitingView();
    		
    		
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorSpawnInterval());
    	}
	};
	
	Runnable spawnSimpleShooterRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = Shooting_ArrayMovingView.allSimpleShooters.size();
    		final int maxNumShooters = Shooting_ArrayMovingView.getMaxNumShips();
//    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.getDifficulty()>0 && numShootersAlive<maxNumShooters){
    			spawnAllSimpleShooters();
//    			//if num shooters< .33 of the max, there is a 33% chance to respawn all shooters
//    			if(numShootersAlive<(maxNumShooters/numShootersAliveCutoff) && Math.random()<0.33 ){
//    				spawnAllSimpleShooters();
//    			}
//    			//otherwise, respawn just 1 simple shooter
//    			else{
//	    			SimpleEnemyShooterArray shooter = new SimpleEnemyShooterArray(ctx);
//	    			gameLayout.addView(shooter,1);
//	        		GameActivity.enemies.add(shooter);
//    			}
			}
    		 
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterSpawnInterval());
    	}
	};
	
	Runnable spawnDiagonalShooterRunnable = new Runnable(){
		@Override
		public void run() {
			
			Shooting_DiagonalMovingView shooter = EnemyFactory.this.spawnFullScreenDiagonalAttacker();
			
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
			
			Shooting_DiagonalMovingView shooter2 = EnemyFactory.this.spawnDiveBomber();
			
			gameLayout.addView(shooter2,1);
    		GameActivity.enemies.add(shooter2);
   
			enemySpawnHandler.postDelayed(this, calculatDiagonalShooterSpawnInterval());
		}
	};
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		gameLayout=gameScreen;
		
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		Shooting_ArrayMovingView.startMovingAllShooters();
	} 
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorSpawningRunnable);
	    enemySpawnHandler.removeCallbacks(spawnSimpleShooterRunnable);
	    enemySpawnHandler.removeCallbacks(spawnDiagonalShooterRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorSpawnInterval());
//	    enemySpawnHandler.postDelayed(spawnSimpleShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
//	    enemySpawnHandler.postDelayed(spawnDiagonalShooterRunnable,calculateMovingSideToSideShooterSpawnInterval());
	}  
	
	private long calculateMeteorSpawnInterval(){
		final int meteorInterval = 3000;
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	private long calculateMovingSideToSideShooterSpawnInterval(){
		final int simpleArrayShooterInterval = 6000;
		return (long) (simpleArrayShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	private long calculatDiagonalShooterSpawnInterval(){
		final int diagonalShooterInterval=2000; 
		return (long) (diagonalShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*4000);
	}
	public void spawnAllSimpleShooters(){
		int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
		
		for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
			Shooting_ArrayMovingView shooter = spawnOneShooting_MovingArrayView();
			
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
		}
	}
	private Shooting_DiagonalMovingView spawnFullScreenDiagonalAttacker(){
		final int diff = levelInfo.getDifficulty();
		
		final int score=Shooting_DiagonalMovingView.DEFAULT_SCORE*diff;
		final double speedY=Shooting_DiagonalMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_DiagonalMovingView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_DiagonalMovingView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Shooting_DiagonalMovingView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Shooting_DiagonalMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Shooting_DiagonalMovingView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_DiagonalMovingView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * 1.5 * Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		return new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,spawnBeneficialObject,bulletSpdY,bulletDmg);
		
	}

	private Shooting_DiagonalMovingView spawnDiveBomber(){
		final int diff = levelInfo.getDifficulty();
		
		final int score=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_SCORE*diff;
		final double speedY=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_SPEED_Y,
				speedX=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_SPEED_X, 
				collisionDamage=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_COLLISION_DAMAGE*diff,
				health=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_HEALTH*diff,
				spawnBeneficialObject= Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * 1.5 * Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_DiagonalMovingView diveBomber =  new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,spawnBeneficialObject,bulletSpdY,bulletDmg);
		diveBomber.setDiveBomber();
		
		return diveBomber;
	}
	
	private Shooting_OrbiterView spawnOrbitingView(){
		final int diff = levelInfo.getDifficulty();

		final int orbitingType=Shooting_OrbiterView.CIRCLE,
				orbitDist=Shooting_OrbiterView.DEFAULT_ORBIT_DIST;

		final int score=Shooting_OrbiterView.DEFAULT_SCORE*diff;
		final double speedY=Shooting_OrbiterView.DEFAULT_SPEED_Y,
				speedX=Shooting_OrbiterView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_OrbiterView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Shooting_OrbiterView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Shooting_OrbiterView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Shooting_OrbiterView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_OrbiterView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_OrbiterView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * 1.5 * Shooting_OrbiterView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView orbiter =  new Shooting_OrbiterView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,
				spawnBeneficialObject,bulletSpdY,bulletDmg,orbitingType,orbitDist);
		
		return orbiter;
	}
	private Shooting_ArrayMovingView spawnOneShooting_MovingArrayView(){
		final int diff = levelInfo.getDifficulty();

		final int score=Shooting_ArrayMovingView.DEFAULT_SCORE*diff;
		final double speedY=Shooting_ArrayMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_ArrayMovingView.DEFAULT_SPEEDX, 
				collisionDamage=Shooting_ArrayMovingView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Shooting_ArrayMovingView.DEFAULT_HEALTH*diff,
				bulletDmg=Shooting_ArrayMovingView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_ArrayMovingView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * 1.5* Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff),
				spawnBeneficialFreq=Shooting_ArrayMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff;
		
		final float height = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		return new Shooting_ArrayMovingView(ctx,score,speedY,speedX,collisionDamage,health,bulletFreq,
				height,width,spawnBeneficialFreq,bulletDmg,bulletSpdY);
		
	}
	private Gravity_MeteorView spawnMeteorView(){
		final int diff = levelInfo.getDifficulty();

		final int score=Gravity_MeteorView.DEFAULT_SCORE*diff;
				
		final double speedY=Gravity_MeteorView.DEFAULT_SPEED_Y,
				speedX=Gravity_MeteorView.DEFAULT_SPEED_X, 
				collisionDamage=Gravity_MeteorView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Gravity_MeteorView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Gravity_MeteorView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff;
		
		return new Gravity_MeteorView(ctx,score,speedY,speedX,collisionDamage,health,spawnBeneficialObject);
	}
	public void spawnGiantMeteor(){
		Gravity_MeteorView giant = spawnMeteorView();
		
		//change width and height. set X and Y positions
		final int width = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_width);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_height);
		giant.setLayoutParams(new LayoutParams(width,height));
		giant.setY(-height);
		giant.setX((float) ((ProjectileView.widthPixels-width)*Math.random()));
		
		//set damage and health to 200, score to 20
		giant.setDamage(150);
		giant.heal(150-Gravity_MeteorView.DEFAULT_HEALTH);
		giant.setScoreValue(20);
		giant.changeSpeedYDown(.45);
		
		//add to layout
		gameLayout.addView(giant,1);
		GameActivity.enemies.add(giant);		
	}
}
