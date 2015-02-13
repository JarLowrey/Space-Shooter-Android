package levels;

import interfaces.GameActivityInterface;
import android.content.Context;
import android.content.SharedPreferences;

import com.jtronlabs.to_the_moon.GameActivity;

public class LevelAttributes {
	protected Context ctx;
	private int waveNo,resourceNo,levelNo;

	public LevelAttributes(Context context){
		ctx=context;
	}
	
	public GameActivityInterface getInteractivityInterface(){
		return (GameActivityInterface)ctx;
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
		//resources only DECREASE when buying things. Thus they should be saved to persistent memory
		if(scoreValue < resourceNo){
			resourceNo=scoreValue;
    		saveResourceCount();
		}else{
			resourceNo=scoreValue;
		}
	}
	public int getResourceCount(){
		return resourceNo;
	}

	// persistent storage
	protected void saveResourceCount(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, resourceNo);
		
		editor.commit();
	}
	/**
	 * Load saved level and resources from memory. Set wave to 0
	 */
	protected void loadScoreAndWaveAndLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setResources(gameState.getInt(GameActivity.STATE_RESOURCES,0));
		setLevel(gameState.getInt(GameActivity.STATE_LEVEL,0));
//		setWave(gameState.getInt(GameActivity.STATE_WAVE,0));
		setWave(0);
	}
}
