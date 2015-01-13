package levels;

import support.CollisionDetector;
import support.ConditionalHandler;
import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_LevelWaves{

	public static final int MAX_NUMBER_LEVELS=5;
	private static int myScore;
	protected static boolean levelWavesCompleted,levelStarted, levelPaused;
	
	final static Runnable[] level1 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToRight,
			doNothing,
			meteorShowersThatForceUserToLeft,
			meteorsGiantAndSideways,
			meteorsGiantAndSideways,
			meteorShowerLong,
			meteorsOnlyGiants,
			meteorsOnlyGiants,
			levelWavesOver};
	
	final static Runnable[] level2 =level1;
	
	final static Runnable[] level3 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			levelWavesOver};
	
	final static Runnable[] level4 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorSidewaysThisWave,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			doNothing,
			diveBomberOnePerSecond,
			diveBomberOnePerSecond,
			levelWavesOver};
	
	final static Runnable[] level5 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			doNothing,
			diveBomberOnePerSecond,
			doNothing,
			diveBomberOnePerSecond,
			boss1,
			levelWavesOver};
	
	final static Runnable levels[][] ={level1,level2,level3,level4,level5};
	
	public LevelSystem(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
	}

	public void newGame(){
		myScore=0;
		currentLevel=-1;
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
			levelStarted=true;
			levelPaused=false;
			currentProgressInLevel=0;
			
			/*
			 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
			 * If handler has runnables canceled before level finishes, then current progress will not change.
			 * thus, when restarting a level simply find which runnable to call by using that current progress integer
			 */
			
			
			for(int i=currentProgressInLevel;i<levels[currentLevel].length;i++){
				ConditionalHandler.postIfLevelResumed(levels[currentLevel-1][i], i * DEFAULT_WAVE_DURATION);
			}
			CollisionDetector.startDetecting();
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
			ConditionalHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION);
		}
		
		CollisionDetector.startDetecting();
	}
	
	public void pauseLevel(){
		levelPaused=true;
		ConditionalHandler.removeLevelHandlerCallbacks();
	}
	
	//GET/SET LEVEL STATE
	public static boolean isLevelPaused(){
		return levelPaused;
	}
	public static boolean areLevelWavesCompleted(){
		return levelWavesCompleted;
	}
	public static boolean hasLevelStarted(){
		return levelStarted;
	}
	public static void notifyLevelWavesCompleted(){
		levelWavesCompleted=true;
	}
	public static void notifyLevelFinishedAndAllEnemiesAreDead(){
		levelStarted=false;
	}
	
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

	//other GET methods
	public static int getCurrentLevelLength(){
		return levels[currentLevel].length*DEFAULT_WAVE_DURATION;
	}
}
