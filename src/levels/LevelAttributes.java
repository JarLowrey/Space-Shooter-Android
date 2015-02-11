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
//		//get saved Wave number.
//		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
//		int savedWave = gameState.getInt(GameActivity.STATE_WAVE, 0);
//		if(savedWave>0){
//			waveNo=savedWave;
//
//			SharedPreferences.Editor editor = gameState.edit();
//			editor.putInt(GameActivity.STATE_WAVE, 0);
//			editor.commit();
//		}
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
//		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
//		return gameState.getInt(GameActivity.STATE_LEVEL, 0);
	}
	protected void setLevel(int level){
		levelNo=level;
//		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
//		SharedPreferences.Editor editor = gameState.edit();
//		editor.putInt(GameActivity.STATE_LEVEL, level);
//		editor.commit();
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
/*	public void saveScoreAndWaveAndLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, resourceNo);
		editor.putInt(GameActivity.STATE_LEVEL, levelNo);
		editor.putInt(GameActivity.STATE_WAVE, waveNo);
		
		editor.commit();
	}
*/
	public void saveResourceCount(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, resourceNo);
		
		editor.commit();
	}
	public void loadScoreAndWaveAndLevel(){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setResources(gameState.getInt(GameActivity.STATE_RESOURCES,0));
		setLevel(gameState.getInt(GameActivity.STATE_LEVEL,0));
		setWave(gameState.getInt(GameActivity.STATE_WAVE,0));
	}
}
