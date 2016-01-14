package levels;

import interfaces.GameActivityInterface;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameActivity;

public abstract class AttributesOfLevels {
	protected RelativeLayout gameScreen;
	private int resourceNo,
		levelNo;
	
	public static final int STANDARD_PROB_WEIGHT = 1000;
	
	public final static int LEVELS_BEGINNER = 2,//0-2
			LEVELS_LOW = 10,//3-10
			LEVELS_MED = 25,//11-25
			LEVELS_HIGH = 50;//26-50 
			//and beyond!
	
	public static final int 
			FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR = 3,
			FIRST_LEVEL_LOTS_OF_TRACKERS_APPEAR = 6,
			FIRST_LEVEL_SPASTICS_APPEAR = 8,
			FIRST_LEVEL_PAUSE_AND_SHOOT_APPEAR = 9,
			FIRST_LEVEL_COORDINATED_CIRCLE_ORBITERS_APPEAR = 11,
			FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR = 12,
			FIRST_LEVEL_TRIANGLE_ORBITERS_APPEAR = 13,
			FIRST_LEVEL_DURATION_BULLETS_APPEAR = 15,
			FIRST_LEVEL_RECTANGLE_ORBITERS_APPEAR = 17,
			
			FIRST_LEVEL_BOSS1_APPEARS = 4,
			FIRST_LEVEL_BOSS2_APPEARS = 10,
			FIRST_LEVEL_BOSS3_APPEARS = 20,
			FIRST_LEVEL_BOSS4_APPEARS = 30,
			FIRST_LEVEL_BOSS5_APPEARS = 40;
	
	protected ArrayList<Integer> levelsWihSpecialEnemies = new ArrayList<Integer>();
	
	protected long timeSpentOnThisLevel,
		timeNeededToEndLevel;//this variable is set at the beginning of every level in the LevelSpawner child class
	
	Handler spawningHandler;

	public AttributesOfLevels(RelativeLayout layout) {
		gameScreen=layout; 
		spawningHandler = new Handler();
	} 
	

	public static final int WAVE_SPAWNER_WAIT = 1000;
		
	public GameActivityInterface getInteractivityInterface(){
		return (GameActivityInterface)ctx();
	}
	protected Context ctx(){
		return gameScreen.getContext();
	}
	
	//Levels
	public  int getLevel(){
		return levelNo;
	}
	protected void incrementLevel(){
		levelNo++;
	}
	
	//Resources
	public void setResources(int scoreValue){
		resourceNo=scoreValue;
	}
	
	public int getResourceCount(){
		return resourceNo;
	}  

	// persistent storage
	public void saveResourceCount(){
		SharedPreferences gameState = ctx().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, resourceNo);
		
		editor.commit();
	} 
	/**
	 * Load saved level and resources from memory. Set wave to 0
	 */
	public void loadScoreAndLevel(){
		SharedPreferences gameState = ctx().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setResources(gameState.getInt(GameActivity.STATE_RESOURCES,0));
		levelNo = gameState.getInt(GameActivity.STATE_LEVEL,0);
//		setWave(gameState.getInt(GameActivity.STATE_WAVE,0));
	}
	

	/**
	 * TODO BADLY NEEDS TO BE OPTIMIZED (DO NOT ACCESS SLOW MEMEORY)
	 * @return
	 */
	public int scoreGainedThisLevel(){
		SharedPreferences gameState = ctx().getSharedPreferences(
				GameActivity.GAME_STATE_PREFS, 0);
		//find how much score was incremented this level to know how much to add to total score
		final int scoreBeforeLevel = gameState.getInt(GameActivity.STATE_RESOURCES, 0);
		return (getResourceCount() - scoreBeforeLevel);		
	}

	public void updateLevelTimeCountdownDisplay(long deltaTime) {
		timeSpentOnThisLevel += deltaTime;
		long millisecondsLeft = timeNeededToEndLevel - timeSpentOnThisLevel;
		millisecondsLeft = Math.max(millisecondsLeft, 0);
		((GameActivityInterface)gameScreen.getContext()).setTimerText(millisecondsLeft);
	}
	public boolean isLevelFinishedSpawning(){
		return timeSpentOnThisLevel >= timeNeededToEndLevel;
	}
}
