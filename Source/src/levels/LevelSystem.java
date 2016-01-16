package levels;

import android.content.SharedPreferences;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.GameLoop;

import enemies.EnemyView;

public class LevelSystem extends LevelSpawner{
      
	/**     
	 * 
	 * @param context
	 *            MUST IMPLEMENT InteractiveGameInterface.java
	 */
	public LevelSystem(RelativeLayout layout) {
		super(layout);
	}
	
	public static int totalSumOfLivingEnemiesScore(){
		int sum = 0;
		for(EnemyView enemy : GameLoop.enemies){
			sum += enemy.getScoreForKilling();
		}
		return sum;
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
		SharedPreferences gameState = ctx().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		
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
		for (int i = GameLoop.friendlyBullets.size() - 1; i >= 0; i--) {
			GameLoop.friendlyBullets.get(i).setViewToBeRemovedOnNextRendering();
			GameLoop.friendlyBullets.get(i).removeGameObject();
		}
		for (int i = GameLoop.friendlies.size() - 1; i >= 0; i--) {
			GameLoop.friendlies.get(i).setViewToBeRemovedOnNextRendering();
			GameLoop.friendlies.get(i).removeGameObject();
		}

		//check if user has lost game, beaten game, or beaten the level. 
		if( protagonistDied ){
			getInteractivityInterface().gameOver();
		}else{
			getInteractivityInterface().openStore();
		} 
	}

}
