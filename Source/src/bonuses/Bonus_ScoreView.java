package bonuses;

import levels.AttributesOfLevels;
import helpers.MediaController;
import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import android.content.SharedPreferences;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.R;

public class Bonus_ScoreView extends BonusView implements MovingViewInterface{
	
	public Bonus_ScoreView(RelativeLayout layout,float positionX,float positionY) {
		super(layout,positionX,positionY);	
		
		this.setImageResource(R.drawable.resources);
	}
	
	public void applyBenefit(){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_BONUS);
		
		SharedPreferences gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		final int level = gameState.getInt(GameActivity.STATE_LEVEL, 0);
		
		int amtToIncrementScore = 0;		
		if(level < AttributesOfLevels.LEVELS_BEGINNER) {
			amtToIncrementScore = 1000;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			amtToIncrementScore = 1500;
		}else if(level < AttributesOfLevels.LEVELS_MED){
			amtToIncrementScore = 2000;			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			amtToIncrementScore = 4000;			
		}else{
			amtToIncrementScore = 6000;			
		}
		
		((GameActivityInterface) this.getContext()).incrementScore(amtToIncrementScore);
	}
}
