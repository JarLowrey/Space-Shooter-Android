package levels;

import helpers.CollisionDetector;
import helpers.MediaController;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class LevelSystem extends LevelSpawner{

	public static ArrayList<BulletView> friendlyBullets = new ArrayList<BulletView>();
	public static ArrayList<BulletView> enemyBullets = new ArrayList<BulletView>();
	public static ArrayList<FriendlyView> friendlies = new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies = new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses = new ArrayList<BonusView>();

	private static CollisionDetector gameDetector;
      
	/**     
	 * 
	 * @param context
	 *            MUST IMPLEMENT InteractiveGameInterface.java
	 */
	public LevelSystem(Context context) {
		super(context);
		gameDetector = new CollisionDetector(this);
	}

	/**
	 * Set levelPaused flag to false. Begin generating background effects. Load
	 * resources and level Number, set Wave to 0. Set waves to spawn every
	 * DEFAULT_WAVE_DURATION seconds. Start Collision detection
	 */
	public void resumeLevel(Context c) {
		Log.d("lowrey","level resumed!!!");

		MediaController.stopLoopingSound();
		MediaController.playSoundClip(ctx, R.raw.background_playing_game,true);
		
		// conditionalHandler = new ConditionalHandler(this);//must be reset
		// every time level is resumed for previous wave spawnings to stop
//		createBackgroundEffects();

		/*
		 * Waves are a series of runnables. each runnable increments progress in
		 * level, and each new level resets that progress. If handler has
		 * runnables canceled before level finishes, then current wave will not
		 * change. thus, when restarting a level simply find which runnable to
		 * call by using that current progress integer, and subtract the delay
		 * from the currrentWave for the next wave's post
		 */

		startLevelSpawning();

		gameDetector.startDetecting();
	}
	
	public static int totalSumOfLivingEnemiesScore(){
		int sum = 0;
		for(EnemyView enemy : enemies){
			sum += enemy.getScoreForKilling();
		}
		return sum;
	}

	/**
	 * flag level as paused, stop collision detector and level spawner. Remove
	 * every Game Object from the activity
	 */
	public void pauseLevel() {
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
	}

	/**
	 * flag level as paused, stop collision detection and spawning. Increment
	 * level and set wave to 0, then save level, resources, and health. Remove
	 * all needed game objects from screen (background objects and friendly
	 * bullets)
	 */
	public void endLevel() {
		// set new level
		incrementLevel();

		// Save variables
		SharedPreferences gameState = ctx.getSharedPreferences(
				GameActivity.GAME_STATE_PREFS, 0);
		
		SharedPreferences.Editor editor = gameState.edit();

		editor.putInt(GameActivity.STATE_TOTAL_RESOURCES, scoreGainedThisLevel() + gameState.getInt(GameActivity.STATE_TOTAL_RESOURCES, 0));
		editor.putInt(GameActivity.STATE_RESOURCES, this.getResourceCount());//current running score is kept trak thru getResourceCount
		editor.putInt(GameActivity.STATE_LEVEL, this.getLevel());
		editor.putInt(GameActivity.STATE_HEALTH, this
				.getInteractivityInterface().getProtagonist().getHealth());

		editor.commit();

		boolean protagonistDied = getInteractivityInterface().getProtagonist().getHealth() <= 0;//check this before protagonist is removed
		
		/* kill Views. Level won't end until enemies, enemyBullets, and bonuses are already removed (see collision detector). 
		 * Thus just remove friendlies and friendly bullets and whatever else to
		 * clean up the screen.
		 */
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).removeGameObject();
		}
		for (int i = friendlies.size() - 1; i >= 0; i--) {
			friendlies.get(i).removeGameObject();
		}

		//check if user has lost game, beaten game, or beaten the level. 
		if( protagonistDied ){
			getInteractivityInterface().gameOver();
		}else{
			getInteractivityInterface().openStore();
		} 
	}

}
