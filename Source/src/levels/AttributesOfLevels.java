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
	

	public static final int DEFAULT_WAVE_DURATION=5000;
	
	public abstract int getCurrentLevelLengthMilliseconds();
	public abstract int getNumWavesInLevel(int level);
	public abstract boolean areLevelWavesCompleted();
	public abstract int getMaxLevel();
	
	//Waves
	public int getWave(){
		return waveNo;
	}
	public void setWave(int wave){
		waveNo=wave;
	}
	protected void incrementWave(){
		setWave(getWave()+1);		
	}
	
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
	
	//Resources
	public void setResources(int scoreValue){
		resourceNo=scoreValue;
	}
	public void incrementScore(int scoreValue){
		//load resource multiplier 
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		int resourceMultiplier = (int) ( gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0) + 1 );
		
		setResources(getResourceCount()+scoreValue * resourceMultiplier);
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
	public void loadScoreAndWaveAndLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setResources(gameState.getInt(GameActivity.STATE_RESOURCES,0));
		setLevel(gameState.getInt(GameActivity.STATE_LEVEL,0));
//		setWave(gameState.getInt(GameActivity.STATE_WAVE,0));
		setWave(0);
	}
}
