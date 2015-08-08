package com.jtronlabs.space_shooter;

import helpers.CollisionDetector;
import helpers.KillableRunnable;
import helpers.MediaController;
import interfaces.GameActivityInterface;

import java.util.ArrayList;

import levels.LevelSystem;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import bonuses.BonusView;
import bullets.BulletView;
import enemies.EnemyView;
import friendlies.FriendlyView;

/**
 * Singleton class responsible for updating game state
 * @author lowre_000
 *
 */
public class GameLoop {

	public static ArrayList<BulletView> friendlyBullets = new ArrayList<BulletView>();
	public static ArrayList<BulletView> enemyBullets = new ArrayList<BulletView>();
	public static ArrayList<FriendlyView> friendlies = new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies = new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses = new ArrayList<BonusView>();
	
	private static final long 
			TIME_BETWEEN_SPAWNS = 1000,//check for spawn every second
			MIN_TICK_TIME = 5;
	
	private long targetFrameRate,
		howOftenToRerunLoop;
	
	private Handler gameLoopHandler;
	private KillableRunnable loopingRunnable;

	private static GameLoop myGameLoop = null;
		
	private GameLoop(){
		gameLoopHandler = new Handler();
		targetFrameRate = 40;
		howOftenToRerunLoop = 1000 / targetFrameRate;//default to ~30 FPS = 30 Frames Per 1000 Milliseconds
	}
	
	public long targetFrameRate(){
		return targetFrameRate;
	}
	
	public static GameLoop instance(){
		if(myGameLoop == null){
			myGameLoop = new GameLoop();
		}
		return myGameLoop;
	}
	
	public void startLevelAndLoop(final Context ctx, final LevelSystem levelingSystem){
		//do initial level setup stuff
		MediaController.stopLoopingSound();
		MediaController.playSoundClip(ctx, R.raw.background_playing_game,true);
		
		//do game logic setup
		levelingSystem.initializeLevelAndSpawnFirstEnemy();
		
		
		loopingRunnable = new KillableRunnable(){
			private long timeAtLastSpawn = SystemClock.uptimeMillis(),
					prevTimeToWait = 0;
			
			@Override
			public void doWork() {
//		Log.d("lowrey","enemiesNo="+enemies.size() +" enemy bulletsNo=" +
//				enemyBullets.size()+" waveNo="+levelingSystem.getWave() +" level="+levelingSystem.getLevel() );
				
				final long startTime = SystemClock.uptimeMillis();
				
				if(((GameActivityInterface)ctx).getProtagonist().getHealth() > 0 &&					//continue with loop only if level has not finished
		    			(! levelingSystem.isLevelFinishedSpawning()
		    	    			 ||  enemies.size() !=0 || enemyBullets.size() != 0  || bonuses.size() != 0)){
					
					updateAllViewSpeeds(SystemClock.uptimeMillis() - startTime + prevTimeToWait);
					moveAllViews(SystemClock.uptimeMillis() - startTime + prevTimeToWait);
					CollisionDetector.detectCollisions();
					checkAllViewsForRemovalFromStaticLists();
										
					if(startTime - timeAtLastSpawn >= TIME_BETWEEN_SPAWNS){//spawn new enemies if possible
						levelingSystem.spawnEnemiesIfPossible();
						timeAtLastSpawn = SystemClock.uptimeMillis();
					}
					
					//calculate how long until next gameLoop iteration
					final long stopTime = SystemClock.uptimeMillis();
					final long howLongThisLoopIterationTook = stopTime - startTime;
					long timeToWait = howOftenToRerunLoop - howLongThisLoopIterationTook;
					if(timeToWait < MIN_TICK_TIME){ //apply a minimum wait time so system does not starve
						timeToWait = MIN_TICK_TIME;
					}
					prevTimeToWait = timeToWait;
					gameLoopHandler.postDelayed(this,timeToWait);
				}
				
				else{//level over
					levelingSystem.endLevel();					
				}
			}
		};
		
		gameLoopHandler.post(loopingRunnable);
	}

	private void checkAllViewsForRemovalFromStaticLists() {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			if(friendlyBullets.get(i).isRemoved()){friendlyBullets.remove(i);}
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			if(enemyBullets.get(i).isRemoved()){enemyBullets.remove(i);}
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			if(friendlies.get(i).isRemoved()){friendlies.remove(i);}
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			if(enemies.get(i).isRemoved()){enemies.remove(i);}
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			if(bonuses.get(i).isRemoved()){bonuses.remove(i);}
		}
	}
	/**
	 * flag level as paused, stop collision detector and level spawner. Remove
	 * every Game Object from the activity
	 */
	public void stopLevel() {
        KillableRunnable.killAll();
		
		// clean up - kill Views & associated threads, stop all spawning &
		// background threads
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).removeGameObject();
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).removeGameObject();
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).removeGameObject();
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).removeGameObject();
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			bonuses.get(i).removeGameObject();
		}
		
		checkAllViewsForRemovalFromStaticLists();
	}
	

	private void updateAllViewSpeeds(long millisecondsSinceLastSpeedUpdate) {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).updateViewSpeed(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).updateViewSpeed(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).updateViewSpeed(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).updateViewSpeed(millisecondsSinceLastSpeedUpdate);
		}
		//bonuses have constant speed
	}

	private void moveAllViews(long millisecondsSinceLastSpeedUpdate) {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).move(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).move(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).move(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).move(millisecondsSinceLastSpeedUpdate);
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			bonuses.get(i).move(millisecondsSinceLastSpeedUpdate);
		}
	}
}
