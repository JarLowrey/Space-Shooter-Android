package bonuses;

import helpers.MediaController;
import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;

public class Bonus_ScoreView extends BonusView implements MovingViewInterface{
	
	public Bonus_ScoreView(RelativeLayout layout,float positionX,float positionY) {
		super(layout,positionX,positionY);	
		
		this.setImageResource(R.drawable.resources);
	}
	
	public void applyBenefit(){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_BONUS);
		
		((GameActivityInterface) this.getContext()).incrementScore(1000);
	}
}
