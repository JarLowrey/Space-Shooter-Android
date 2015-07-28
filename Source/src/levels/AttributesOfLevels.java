package levels;

import interfaces.GameActivityInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.jtronlabs.to_the_moon.GameActivity;

public abstract class AttributesOfLevels {
	protected Context ctx;
	private int waveNo,resourceNo,levelNo;
	
	Handler spawningHandler;

	public AttributesOfLevels(Context context) {
		ctx=context; 
		spawningHandler = new Handler();
	} 
	

	public static final int DEFAULT_WAVE_DURATION=2000;
	
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
		return getLevel() / 5;
	}
	
	//Resources
	public void setResources(int scoreValue){
		resourceNo=scoreValue;
	}
//	public void incrementScore(int scoreValue){
//		//load resource multiplier 
//		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
//		int resourceMultiplier = (int) ( gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0) + 1 );
//		
//		setResources(getResourceCount()+scoreValue * resourceMultiplier);
//	}
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
