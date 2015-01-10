package levels;

import com.jtronlabs.to_the_moon.R;
 
/**
 * 
 * @author JAMES LOWREY
 *
 */
public class Levels {
	private final int[] levelBackgrounds={R.drawable.btn_gray,
			R.drawable.level2,R.drawable.level3,R.drawable.level4,R.drawable.moon};
	public final int MAX_LEVEL=25, 
			INCREASE_LEVEL_GOAL_INCREMENT=5;
	
	
	private static int score=0,level=0;
	
	/**
	 * Checks if the level needs to be increased, does so if necessary, and returns the new gameScreen background's resource Id
	 * @return-resourceId of new background if levelDifficulty was incremented. Return -1 if levelDifficulty was not incremented
	 */
	public int incrementLevel(){
		int retVal=-1;
		int prevDifficulty=getDifficulty();
		
		if(score>getLevelGoal(level)){level++;} 
		if(prevDifficulty!=getDifficulty()){retVal = getLevelBackground();}
		
		return retVal;
	}
	
	//GET METHODS
	public int getLevel(){
		return level;
	}
	public int getLevelBackground(){
		return levelBackgrounds[getDifficulty()-1];
	}
	/**
	 * 
	 * get score needed to beat level
	 * @param whichLevel-the level whose goal you wish to know
	 * @return-the score needed to beat given level
	 */
	public int getLevelGoal(int whichLevel){
		if(whichLevel==0){
			return 50;
		}else if(level>MAX_LEVEL){
			return Integer.MAX_VALUE;
		}else{
			final int levelDifference = getDifficulty()*50;
			return getLevelGoal(whichLevel-1)+levelDifference;
		}
	}
	public int getScore(){
		return score;
	}
	public int getDifficulty(){
		return level/INCREASE_LEVEL_GOAL_INCREMENT+1;
	}
	public boolean isLastLevelInDifficulty(){
		return level%INCREASE_LEVEL_GOAL_INCREMENT==(INCREASE_LEVEL_GOAL_INCREMENT-1);
	}
	
	//SET METHODS
	public void incrementScore(int howMuch){
		score+=howMuch;
	}
	public void reset(){
		level=0;
		score=0;
	}
}
