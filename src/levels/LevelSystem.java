package levels;

import java.util.ArrayList;

import support.KillableRunnable;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import background_objects.BackgroundView;
import background_objects.Bird;
import background_objects.Clouds;
import background_objects.Sun;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class LevelSystem extends Levels{


	public static ArrayList<BackgroundView> backgroundViews = new ArrayList<BackgroundView>();
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
	public void resumeLevel() {
		// conditionalHandler = new ConditionalHandler(this);//must be reset
		// every time level is resumed for previous wave spawnings to stop
		createBackgroundEffects();

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

	/**
	 * flag level as paused, stop collision detector and level spawner. Remove
	 * every Game Object from the activity
	 */
	public void pauseLevel() {
		gameDetector.stopDetecting();
		// clean up - kill Views & associated threads, stop all spawning &
		// background threads
		for (int i = backgroundViews.size() - 1; i >= 0; i--) {
			backgroundViews.get(i).removeGameObject();
		}
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
		gameDetector.stopDetecting();
		// conditionalHandler.stopSpawningWaves();

		// set new level
		incrementLevel();

		// Save variables
		SharedPreferences gameState = ctx.getSharedPreferences(
				GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();

		editor.putInt(GameActivity.STATE_RESOURCES, this.getResourceCount());
		editor.putInt(GameActivity.STATE_LEVEL, this.getLevel());
		editor.putInt(GameActivity.STATE_HEALTH, this
				.getInteractivityInterface().getProtagonist().getHealth());

		editor.commit();

		/* kill Views. Friendly still needed and enemies already removed. Thus
		 remove friendly bullets and background objects to
		 clean up the screen. enemies, enemy bullets, and bonuses must all already be removed for
		 level to end (see collision detector)
		 */
		for (int i = backgroundViews.size() - 1; i >= 0; i--) {
			backgroundViews.get(i).removeGameObject();
		}
		for (int i = friendlyBullets.size() - 1; i >= 0; i--) {
			friendlyBullets.get(i).removeGameObject();
		}

		//check if user has lost game, beaten game, or beaten the level. Must be done after level has incremented
		if(getInteractivityInterface().getProtagonist().getHealth() <= 0 ){
			getInteractivityInterface().lostGame();
		}else if( getLevel() > getMaxLevel() ){
			getInteractivityInterface().beatGame();
		}else{
			getInteractivityInterface().openStore();
		}
		
		//reset waves for the next level
		setWave(0);
	}

	//
	// public ConditionalHandler getConditionalHandler(){
	// return this.conditionalHandler;
	// }

	// Background Animations and effects
	private final int[] backgroundColors = { R.color.blue, R.color.dark_blue,
			R.color.very_dark_blue };

	private void createBackgroundEffects() {
		if (getLevel() < 3) {
			new Sun(ctx);

			for (int i = 0; i < 12 / (getLevel() + 1); i++) {
				Clouds a = new Clouds(ctx);
				Clouds b = new Clouds(ctx);
				a.setY((float) (MainActivity.getHeightPixels() * Math.random()));
				b.setY((float) (MainActivity.getHeightPixels() * Math.random()));
			}

			spawningHandler.post(clouds);
			// this.conditionalHandler.postIfLevelResumed(clouds);
		}
		if (getLevel() >= backgroundColors.length) {
			this.getInteractivityInterface()
					.changeGameBackground(R.color.black);
		} else {
			this.getInteractivityInterface().changeGameBackground(
					backgroundColors[getLevel()]);
		}
	}

	KillableRunnable clouds = new KillableRunnable() {
		@Override
		public void doWork() {
			if (Math.random() < .5) {
				new Bird(ctx);
			}
			if (Math.random() < .5) {
				new Clouds(ctx);
			}
			new Bird(ctx);
			new Clouds(ctx);

			spawningHandler.postDelayed(this, 4000 * (getLevel() + 1));
			// conditionalHandler.postIfLevelResumed(this, 4000*(getLevel()+1));
		}
	};

}
