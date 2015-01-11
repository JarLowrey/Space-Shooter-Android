package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class GameLevels extends ScriptedLevelFactory{

	private static int myLevel, myScore;
	
	public GameLevels(Context context, RelativeLayout gameScreen) {
		super(context, gameScreen);
	}

	public void nextLevel(){
		myLevel++;
		switch(myLevel){
		case 1:
			startLevelOne();
			break;
		}
	}
	
	public static void incrementScore(int score){
		myScore+=score;
	}
	public static int getScore(){
		return myScore;
	}
}
