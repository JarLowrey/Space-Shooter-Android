package levels;

import support.CollisionDetector;
import support.ConditionalHandler;
import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_LevelsScripted{

	public static final int MAX_NUMBER_LEVELS=5;
	private static int myScore;
	protected static boolean levelWavesCompleted,levelStarted;
	
	final Runnable[] level1 = {meteorSidewaysOnePerSecondForWholeLevel,
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
			levelOverRunnable};
	
	final Runnable[] level2 =level1;
	
	final Runnable[] level3 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			levelOverRunnable};
	
	final Runnable[] level4 = {meteorSidewaysOnePerSecondForWholeLevel,
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
			levelOverRunnable};
	
	final Runnable[] level5 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			doNothing,
			diveBomberOnePerSecond,
			doNothing,
			diveBomberOnePerSecond,
			boss1,
			levelOverRunnable};
	
	final Runnable levels[][] ={level1,level2,level3,level4,level5};
	
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
			
			
			for(int i=currentProgressInLevel;i<levels[myLevel-1].length;i++){
				ConditionalHandler.postIfLevelResumed(levels[myLevel-1][i], i * DEFAULT_WAVE_DURATION);
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
			ConditionalHandler.postIfLevelResumed(levels[myLevel][i], i * DEFAULT_WAVE_DURATION);
		}
		
		CollisionDetector.startDetecting();
	}
	
	public void pauseLevel(){
		levelPaused=true;
		ConditionalHandler.removeLevelHandlerCallbacks();
	}
	
	//GET/set LEVEL STATE
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
	
	//GET LEVEL
	public static int getLevel(){
		return myLevel;
	}
}
