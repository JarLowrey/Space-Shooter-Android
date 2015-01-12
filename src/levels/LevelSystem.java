package levels;

import com.jtronlabs.to_the_moon.CollisionDetector;

import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_LevelsScripted{

	public static final int MAX_NUMBER_LEVELS=5;
	private static int myScore;
	
	public LevelSystem(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
		myScore=0; 
	}

	public void newGame(){
		myScore=0;
		myLevel=0;
		startNextLevel();
	}
	
	public boolean startNextLevel(){
		myLevel++;
		if(myLevel==MAX_NUMBER_LEVELS){
			return true;
		}
		switch(myLevel){
		case 1:
			startLevelFour(true);
			break;
		case 2:
			startLevelTwo(true);
			break;
		case 3:
			startLevelThree(true);
			break;
		case 4:
			startLevelFour(true);
			break;
		}
		CollisionDetector.startDetecting();
		return false;
	}
	public void resumeLevel(){
		super.levelPaused=false;
		switch(myLevel){
		case 1:
			startLevelOne(false);
			break;
		case 2:
			startLevelTwo(false);
			break;
		case 3:
			startLevelThree(false);
			break;
		case 4:
			startLevelFour(false);
			break;
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
}
