package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_LevelWaves{

	public static final int MAX_NUMBER_LEVELS=5,
			GAME_NOT_BEGUN=-1;
	private static int myScore;
	
	CollisionDetector gameDetector;
	
	public LevelSystem(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
		
		gameDetector = new CollisionDetector(this);
	}

	public void newGame(){
		myScore=0;
		currentLevel=GAME_NOT_BEGUN;//= -1. 
		startNextLevel();
	}
	
	/**
	 * 
	 * @return True if user has passed the last level
	 */
	public boolean startNextLevel(){
		currentLevel++;
		if(currentLevel==MAX_NUMBER_LEVELS){
			return true;
		}else{
			levelWavesCompleted=false;
//			levelRunning=true;
			levelPaused=false;
			currentProgressInLevel=0;
			
			/*
			 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
			 * If handler has runnables canceled before level finishes, then current progress will not change.
			 * thus, when restarting a level simply find which runnable to call by using that current progress integer
			 */
			
			
			for(int i=currentProgressInLevel;i<levels[currentLevel].length;i++){
				levelHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION);
			}
			gameDetector.startDetecting();
			return false;
		}
	}
	public void resumeLevel(){
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */

		levelPaused=false;
		
		for(int i=currentProgressInLevel;i<level1.length;i++){
			levelHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION);
		}
		
		gameDetector.startDetecting();
	}
	
	public void pauseLevel(){
		levelPaused=true;
		gameDetector.stopDetecting();
	}
	
	//GET/SET LEVEL STATE
	public void notifyLevelWavesCompleted(){
		levelWavesCompleted=true;
	}
//	
	//SCORE
	public static void incrementScore(int score){
		myScore+=Math.abs(score);
	}
	public static void decrementScore(int score){
		myScore-=Math.abs(score);
	}
	public static int getScore(){
		return myScore;
	}
}
