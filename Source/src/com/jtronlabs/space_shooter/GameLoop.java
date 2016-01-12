package com.jtronlabs.space_shooter;

import helpers.CollisionDetector;
import helpers.KillableRunnable;
import helpers.MediaController;
import interfaces.GameActivityInterface;

import java.util.ArrayList;

import levels.LevelSystem;
import parents.MovingView;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import bonuses.BonusView;
import bullets.BulletView;
import enemies.EnemyView;
import friendlies.FriendlyView;

/**
 * Game Loop needs to be designed in this fashion : http://gamedev.stackexchange.com/questions/75558/frameskipping-in-android-gameloop-causing-choppy-sprites-open-gl-es-2-0
 * 		Game state contains data, update logic handles phyics and runs faster than rendering, display logic renders as fast as possible (slower than physics)
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
	public static ArrayList<MovingView> specialEffects = new ArrayList<MovingView>();
	
	private static final long 
			TIME_BETWEEN_SPAWNS = 1000,//check for spawn every second
			MIN_TICK_TIME = 5,
			DEFAULT_FRAME_RATE = 40;
	
	private long targetFrameRate,
		howOftenToRerunLoop,
		timeAtLastFrame,
		timeAtLastSpawn;
	
	private Handler gameLoopHandler;
	private KillableRunnable loopingRunnable;

	private static GameLoop myGameLoop = null;
		
	private GameLoop(){
		gameLoopHandler = new Handler();
		targetFrameRate = DEFAULT_FRAME_RATE;
		howOftenToRerunLoop = 1000 / targetFrameRate;
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
		GameActivityInterface theGame;
		try{
			theGame = (GameActivityInterface)ctx;
		}catch(ClassCastException e){
			theGame = null;		//the current context is not the game, but rather some other activity that needs this loop, likely for special effects
		}
		final GameActivityInterface theGameCopyPointer = theGame;
		
		final boolean currentActivityIsTheGame = theGameCopyPointer != null && levelingSystem!= null;

		//do game logic setup
		if(currentActivityIsTheGame){
			levelingSystem.initializeLevelEndCriteriaAndSpawnFirstEnemy();
			timeAtLastSpawn = 0; //force GameLoop to spawn right away
		}
		
		loopingRunnable = new KillableRunnable(){			
			@Override
			public void doWork() {
				final long startTime = SystemClock.uptimeMillis();

				if(currentActivityIsTheGame){ levelingSystem.updateLevelTimeAndSpawnIfNeeded(SystemClock.uptimeMillis() - timeAtLastFrame); }
				
				updateAllViewSpeeds(SystemClock.uptimeMillis() - timeAtLastFrame );
				moveAllViews(SystemClock.uptimeMillis() - timeAtLastFrame );
				CollisionDetector.detectCollisions();
				
				final boolean levelEntitiesStillAlive = enemies.size() !=0 || enemyBullets.size() != 0  || bonuses.size() != 0;
				final boolean continueLevel = currentActivityIsTheGame && ( !levelingSystem.isLevelFinishedSpawning() || levelEntitiesStillAlive );
				final boolean protagAlive = currentActivityIsTheGame && theGameCopyPointer.getProtagonist().getHealth() > 0;
						 				
				if( continueLevel && protagAlive){
					//check to spawn new enemies 
					if(levelingSystem!= null && startTime - timeAtLastSpawn >= TIME_BETWEEN_SPAWNS){
						levelingSystem.spawnEnemiesIfPossible();
						timeAtLastSpawn = SystemClock.uptimeMillis();
					}
				}else if (currentActivityIsTheGame){//the current activity is the level, and the level has now ended. End the loop
					levelingSystem.endLevel();	
					return;
				}
				
				//calculate how long until next gameLoop iteration
				final long howLongThisLoopIterationTook = SystemClock.uptimeMillis() - startTime;
				long timeToWait = howOftenToRerunLoop - howLongThisLoopIterationTook;

				if(howLongThisLoopIterationTook > timeToWait)
					Log.d("lowrey", "wait: " + timeToWait + " duration: " + howLongThisLoopIterationTook+" max-time: "+howOftenToRerunLoop);
				/*
				if(timeToWait < MIN_TICK_TIME){ //apply a minimum wait time so system does not starve
					timeToWait = MIN_TICK_TIME;
				}*/
				timeAtLastFrame = SystemClock.uptimeMillis();
				gameLoopHandler.postDelayed(this, timeToWait);
			}
		};
		
		gameLoopHandler.postDelayed(loopingRunnable,100);
	}
	/**
	 * flag level as paused, stop collision detector and level spawner. Remove
	 * every Game Object from the activity
	 */
	public void stopLevelAndLoop() {
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
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			specialEffects.get(i).removeGameObject();
		}
	}
	

	private void updateAllViewSpeeds(long deltaTime) {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).updateViewSpeed(deltaTime);
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).updateViewSpeed(deltaTime);
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).updateViewSpeed(deltaTime);
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).updateViewSpeed(deltaTime);
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			specialEffects.get(i).updateViewSpeed(deltaTime);
		}
		//bonuses have constant speed
	}

	private void moveAllViews(long deltaTime) {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).move(deltaTime);
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).move(deltaTime);
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).move(deltaTime);
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).move(deltaTime);
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			bonuses.get(i).move(deltaTime);
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			specialEffects.get(i).move(deltaTime);
		}
	}
}
