package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class LevelSystem extends Factory_ScriptedLevels{

	public static final int MAX_NUMBER_LEVELS=5;
	private static int myScore;
	
	public LevelSystem(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
		myScore=0;
	}

	public boolean nextLevel(){
		myLevel++;
		if(myLevel==MAX_NUMBER_LEVELS){
			return true;
		}
		switch(myLevel){
		case 1:
			startLevelOne();
			break;
		case 2:
			startLevelTwo();
			break;
		case 3:
			startLevelThree();
			break;
		}
		return false;
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
}
