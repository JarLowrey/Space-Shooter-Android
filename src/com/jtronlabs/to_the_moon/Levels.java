package com.jtronlabs.to_the_moon;

import android.util.Log;


public class Levels {
	//GAME STATUS DATA
	public boolean rightBtnWasTappedPreviously;//indicates which button the user should tap. 
	public final int[] levelBackgrounds={R.drawable.level1,R.drawable.level2,R.drawable.level3,R.drawable.level4,R.drawable.moon};
	private final int MAX_LEVEL=25, INCREASE_LEVEL_GOAL_INCREMENT=5,BTN_TAP_SCORE_WEIGHT=1;
	private static int score,numBtnTaps,level;
	
	public Levels(){
		score=0;
		numBtnTaps=0;
		level=0;
		rightBtnWasTappedPreviously=true;
	}
	//VARIABLE GET/SET METHODS
	public void incrementNumBtnTaps(){ 
		numBtnTaps++; 
		score+=BTN_TAP_SCORE_WEIGHT;
	}
	public int numBtnTaps(){
		return numBtnTaps;
	}
	/**
	 * Checks if the level needs to be increased, does so if necessary, and returns the new gameScreen background's resource Id
	 * @return-resourceId of new background if levelDifficulty was incremented. Return -1 if levelDifficulty was not incremented
	 */
	public int incrementLevel(){
		int retVal=-1;
		int prevDifficulty=levelDifficulty();
		
		if(numBtnTaps>getLevelGoal(level)){level++;}
		if(prevDifficulty==levelDifficulty()){retVal = getLevelBackground();}
		
		return retVal;
	}
	public int getLevel(){
		return level;
	}
	public int getLevelBackground(){
		return levelBackgrounds[(level/MAX_LEVEL)];
	}
	/**
	 * 
	 * get number of taps needed to beat level
	 * @param whichLevel-the level whose goal you wish to know
	 * @return-the number of taps needed to beat given level
	 */
	public int getLevelGoal(int whichLevel){
		if(whichLevel==0){
			return 20;
		}else if(level>MAX_LEVEL){
			return Integer.MAX_VALUE;
		}else{
			final int levelDifference = levelDifficulty()*20;
			return getLevelGoal(whichLevel-1)+levelDifference;
		}
	}
	public void incrementScore(int howMuch){
		score+=howMuch;
	}
	public int getScore(){
		return score;
	}
	public int levelDifficulty(){
		return level/INCREASE_LEVEL_GOAL_INCREMENT+1;
	}
	public void reset(){
		numBtnTaps=0;
		level=1; 
	}
}
