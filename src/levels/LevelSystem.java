package levels;

import support.CollisionDetector;
import support.ConditionalHandler;
import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_LevelsScripted{

	public static final int MAX_NUMBER_LEVELS=5;
	private static int myScore;
	
	public LevelSystem(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
	}

	public void newGame(){
		myScore=0;
		myLevel=0;
		startNextLevel();
	}
	
	/**
	 * 
	 * @return True if user has passed the last level
	 */
	public boolean startNextLevel(){
		myLevel++;
		if(myLevel==MAX_NUMBER_LEVELS){
			return true;
		}
		initLevelVarsAtBeginningOfEveryLevel();//this will reset current progress in level
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */
		
		
		for(int i=currentProgressInLevel;i<levels[myLevel-1].length;i++){
			ConditionalHandler.postIfLevelResumed(levels[myLevel-1][i], i * DEFAULT_WAVE_DURATION);
		}
		CollisionDetector.startDetecting();
		return false;
	}
	public void resumeLevel(){
//		startNextLevel();//this is for testing purposes only
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */
		
		
		for(int i=currentProgressInLevel;i<level1.length;i++){
			ConditionalHandler.postIfLevelResumed(levels[myLevel][i], i * DEFAULT_WAVE_DURATION);
		}
		
		CollisionDetector.startDetecting();
	}
	
	public static void incrementScore(int score){
		myScore+=Math.abs(score);
	}
	public static void decrementScore(int score){
		myScore-=Math.abs(score);
	}
	public static int getScore(){
		return myScore;
	}
	public static int getLevel(){
		return myLevel;
	}
	public static boolean isLevelPaused(){
		return levelPaused;
	}
}
