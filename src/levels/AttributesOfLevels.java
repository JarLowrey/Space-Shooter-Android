package levels;

import interfaces.GameActivityInterface;
import android.content.Context;
import android.content.SharedPreferences;

import com.jtronlabs.to_the_moon.GameActivity;

public abstract class AttributesOfLevels {
	protected Context ctx;
	protected boolean levelPaused;
	private int waveNo,resourceNo,levelNo, myId;	
	private static int levelingId;

	public AttributesOfLevels(Context context) {
		ctx=context;
		myId=levelingId;
	}
	

	public static final int DEFAULT_WAVE_DURATION=5000;
	
	//get methods
	public abstract int getCurrentLevelLengthMilliseconds();
	public abstract int getNumWavesInLevel(int level);
	public abstract boolean areLevelWavesCompleted();
	public abstract int getMaxLevel();
	
	/**
	 * 
	 * @return
	 */
	protected boolean canSpawn(){
		return !levelPaused && !areLevelWavesCompleted() && myId==levelingId;
	}
	
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
	
	//paused?
	public boolean isLevelPaused(){
		return levelPaused;
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
