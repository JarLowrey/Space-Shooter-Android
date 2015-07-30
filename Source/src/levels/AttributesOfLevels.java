package levels;

import interfaces.GameActivityInterface;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.jtronlabs.to_the_moon.GameActivity;

public abstract class AttributesOfLevels {
	protected Context ctx;
	private int resourceNo,levelNo;
	private final static int NUM_LVLS_TILL_DIFFICULTY_CHANGE = 5;
	
	protected ArrayList<Integer> levelsWihSpecialEnemies = new ArrayList<Integer>();
	
	Handler spawningHandler;

	public AttributesOfLevels(Context context) {
		ctx=context; 
		spawningHandler = new Handler();
	} 
	

	public static final int WAVE_SPAWNER_WAIT = 1000;
	
	public abstract boolean isLevelFinishedSpawning();
	public abstract int getMaxLevel();
	
	
	public GameActivityInterface getInteractivityInterface(){
		return (GameActivityInterface)ctx;
	}
	
	//Levels
	public  int getLevel(){
		return levelNo;
	}
	protected void setLevel(int level){
		levelNo=level;
	}
	protected void incrementLevel(){
		setLevel(getLevel()+1);
	}
	/**
	 * Find current state of game difficulty
	 * @return
	 */
	protected int difficulty(){
		return getLevel() / NUM_LVLS_TILL_DIFFICULTY_CHANGE;
	}
	protected int secondaryDifficulty(){
		return getLevel() % NUM_LVLS_TILL_DIFFICULTY_CHANGE;
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
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, resourceNo);
		
		editor.commit();
	} 
	/**
	 * Load saved level and resources from memory. Set wave to 0
	 */
	public void loadScoreAndLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setResources(gameState.getInt(GameActivity.STATE_RESOURCES,0));
		setLevel(gameState.getInt(GameActivity.STATE_LEVEL,0));
//		setWave(gameState.getInt(GameActivity.STATE_WAVE,0));
	}
	

	/**
	 * TODO BADLY NEEDS TO BE OPTIMIZED (DO NOT ACCESS SLOW MEMEORY)
	 * @return
	 */
	public int scoreGainedThisLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(
				GameActivity.GAME_STATE_PREFS, 0);
		//find how much score was incremented this level to know how much to add to total score
		final int scoreBeforeLevel = gameState.getInt(GameActivity.STATE_RESOURCES, 0);
		return (getResourceCount() - scoreBeforeLevel);		
	}    
}
