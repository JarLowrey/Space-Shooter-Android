package com.jtronlabs.space_shooter;

import backgroundViews.SpecialEffectView;
import backgroundViews.StarView;
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
import parents.MovingView;

/**
 * Game Loop needs to be designed in this fashion : http://gamedev.stackexchange.com/questions/75558/frameskipping-in-android-gameloop-causing-choppy-sprites-open-gl-es-2-0
 * 		Game state contains data, update logic handles phyics and runs faster than rendering, display logic renders as fast as possible (slower than physics)
 * Singleton class responsible for updating game state
 * @author lowre_000
 *
 */
public class GameLoop {

	public static ArrayList<EnemyView> enemyPool = new ArrayList<EnemyView>();

	public static ArrayList<BulletView> friendlyBullets = new ArrayList<BulletView>();
	public static ArrayList<BulletView> enemyBullets = new ArrayList<BulletView>();
	public static ArrayList<FriendlyView> friendlies = new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies = new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses = new ArrayList<BonusView>();
	public static ArrayList<SpecialEffectView> specialEffects = new ArrayList<SpecialEffectView>();
	
	private static final long
			TIME_BETWEEN_SPAWNS = 1000,//check for spawn every second
			MIN_TICK_TIME = 5,
			DEFAULT_FRAME_RATE = 60,
			DEFAULT_TIME_BETWEEN_FRAMES = (long)(1000.0 / DEFAULT_FRAME_RATE);

	public static final double TIME_BTW_PHYSICS_FRAMES = DEFAULT_TIME_BETWEEN_FRAMES * 0.9;
	
	private long targetFrameRate,
			timeAtLastPhysicsUpdate,
			timeAtLastRenderingUpdate,
			timeAtLastSpawn;

	private Handler gameLoopHandler;

	private static GameLoop myGameLoop = null;
		
	private GameLoop(){
		gameLoopHandler = new Handler();
		targetFrameRate = DEFAULT_FRAME_RATE;
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
		Log.d("lowrey", "Game Loop started!");
		//do initial level setup stuff
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

		timeAtLastPhysicsUpdate = SystemClock.uptimeMillis();
		timeAtLastRenderingUpdate = SystemClock.uptimeMillis();
		KillableRunnable physicsRunnable = new KillableRunnable() {
			@Override
			public void doWork() {
				//Log.d("lowrey", "Physics loop posted!");
				final long startTime = SystemClock.uptimeMillis();


				updateAllViewSpeeds(SystemClock.uptimeMillis() - timeAtLastPhysicsUpdate);
				updateAllViewsPhysicalPositions(SystemClock.uptimeMillis() - timeAtLastPhysicsUpdate);
				if(currentActivityIsTheGame) {
					CollisionDetector.detectCollisions();
				}

				//final long howLongThisLoopIterationTook = SystemClock.uptimeMillis() - startTime;
				//Log.d("lowrey", " physics loop required : " + howLongThisLoopIterationTook+"ms");

				timeAtLastPhysicsUpdate = SystemClock.uptimeMillis();
				gameLoopHandler.postDelayed(this, (long) TIME_BTW_PHYSICS_FRAMES);//a little faster than expected frame rate, in-case android decides to slow it down a bit :: Fixed update time to update the physics
			}
		};
		KillableRunnable renderingRunnable = new KillableRunnable() {
			@Override
			public void doWork() {
				//Log.d("lowrey", "Rendering loop posted!");
				final long startTime = SystemClock.uptimeMillis();

				//update timer display
				if(currentActivityIsTheGame){ levelingSystem.updateLevelTimeCountdownDisplay(SystemClock.uptimeMillis() - timeAtLastRenderingUpdate); }

				//update sprite displays
				removeAllViewsReadyToBeRemoved();
				renderAllViewsPositions();

				//check for level end circumstance and if it is time to spawn new enemies. This is in the rendering loop, as spawning a new enemy will result in a new picture being added to screen
				//It would likely make more sense for it to go in the physics/game logic loop, but that would require a lot more refactoring that I don't want to do right now
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
					//stopLevelAndLoop();//must stop loop before ending level!
					levelingSystem.endLevel();
					return;
				}


				//calculate how long until next gameLoop iteration.
				//attempt to make it 60FPS
				final long howLongThisLoopIterationTook = SystemClock.uptimeMillis() - startTime;
				long timeToWait = DEFAULT_TIME_BETWEEN_FRAMES - howLongThisLoopIterationTook;

				//the rendering took too long, a frame was skipped! It might be a bit choppy.
				if(timeToWait < MIN_TICK_TIME){
					Log.d("lowrey", "Rendering time was : " + howLongThisLoopIterationTook+ " which was too long. "+howLongThisLoopIterationTook/DEFAULT_TIME_BETWEEN_FRAMES+" frame(s) skipped");
					timeToWait = MIN_TICK_TIME; //apply a minimum wait time (frame was skipped so it's tempting to wait 0ms to compensate) so system does not starve of resources
				}
				timeAtLastRenderingUpdate = SystemClock.uptimeMillis();
				gameLoopHandler.postDelayed(this, timeToWait);
			}
		};

		gameLoopHandler.post(physicsRunnable);
		gameLoopHandler.post(renderingRunnable);
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
			friendlyBullets.get(i).setViewToBeRemovedOnNextRendering();
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).setViewToBeRemovedOnNextRendering();
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).setViewToBeRemovedOnNextRendering();
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).setViewToBeRemovedOnNextRendering();
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			bonuses.get(i).setViewToBeRemovedOnNextRendering();
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			SpecialEffectView s = specialEffects.get(i);
			if(! (s instanceof StarView) ) {//leave the stars alone!
				s.setViewToBeRemovedOnNextRendering();
			}
		}
		renderAllViewsPositions();
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

	private void updateAllViewsPhysicalPositions(long deltaTime) {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).movePhysicalPosition(deltaTime);
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			enemyBullets.get(i).movePhysicalPosition(deltaTime);
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).movePhysicalPosition(deltaTime);
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			enemies.get(i).movePhysicalPosition(deltaTime);
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			bonuses.get(i).movePhysicalPosition(deltaTime);
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			specialEffects.get(i).movePhysicalPosition(deltaTime);
		}
	}

	private void removeAllViewsReadyToBeRemoved(){
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			BulletView b = friendlyBullets.get(i);
			if(b.isRemoved()) {
				friendlyBullets.remove(i);
				b.removeGameObject();
			}
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			BulletView b = enemyBullets.get(i);
			if(b.isRemoved()){
				enemyBullets.remove(i);
				b.removeGameObject();
			}
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			FriendlyView f = friendlies.get(i);
			if(f.isRemoved()){
				friendlies.remove(i);
				f.removeGameObject();
			}
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			EnemyView e = enemies.get(i);
			if(e.isRemoved()){
				enemies.remove(i);
				e.removeGameObject();
			}
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			BonusView b = bonuses.get(i);
			if(b.isRemoved()) {
				bonuses.remove(i);
				b.removeGameObject();
			}
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			SpecialEffectView s = specialEffects.get(i);
			if(s.isRemoved()){
				specialEffects.remove(i);
				s.removeGameObject();
			}
		}
	}

	private void renderAllViewsPositions() {
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			BulletView b = friendlyBullets.get(i);
			b.renderPosition();
		}
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			BulletView b = enemyBullets.get(i);
			b.renderPosition();
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			FriendlyView f = friendlies.get(i);
			f.renderPosition();
		}
		for (int i = enemies.size() - 1; i >= 0; i--) {
			EnemyView e = enemies.get(i);
			e.renderPosition();
		}
		for (int i = bonuses.size() - 1; i >= 0; i--) {
			BonusView b = bonuses.get(i);
			b.renderPosition();
		}
		for (int i = specialEffects.size() - 1; i >= 0; i--) {
			SpecialEffectView s = specialEffects.get(i);
			s.renderPosition();
		}
	}

}
