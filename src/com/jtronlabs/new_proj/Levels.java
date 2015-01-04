package com.jtronlabs.new_proj;

public class Levels {
	//GAME STATUS DATA
	public boolean rightBtnWasTappedPreviously=true;//indicates which button the user should tap. 
	public final int[] levelBackgrounds={R.drawable.level1,R.drawable.level2,R.drawable.level3,R.drawable.level4,R.drawable.moon};
	private static int numBtnTaps=0,level=1;
	
	//VARIABLE GET/SET METHODS
	public void incrementNumBtnTaps(){ 
		numBtnTaps++;  
	}
	public int numBtnTaps(){
		return numBtnTaps;
	}
	/**
	 * Checks if the level needs to be increased, does so if necessary, and returns the new gameScreen background's resource Id
	 * @return-resourceId of new background if level was incremented. Return -1 if level was not incremented
	 */
	public int incrementLevel(){
		if(numBtnTaps>getLevelGoal(level)){
			level++;
			return getLevelBackground();
		}else{
			return -1;
		}
	}
	public int getLevel(){
		return level;
	}
	public int getLevelBackground(){
		return levelBackgrounds[(level-1)];
	}
	/**
	 * get number of taps needed for current level. (There must be a better way?)
	 * @return-the number of taps needed for current level
	 */
	public int getLevelGoal(int whichLevel){
		switch(whichLevel){
		case 1:
			return 50;
		case 2:
			return getLevelGoal(whichLevel-1)+200;
		case 3:
			return getLevelGoal(whichLevel-1)+400;
		case 4:
			return getLevelGoal(whichLevel-1)+800;
		default:
			return Integer.MAX_VALUE;
		}
	}
	public void reset(){
		numBtnTaps=0;
		level=1;
	}
}
