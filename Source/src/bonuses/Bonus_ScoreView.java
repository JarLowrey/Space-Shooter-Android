package bonuses;

import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import interfaces.Shooter;
import support.MediaController;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_ScoreView extends BonusView implements MovingViewInterface{
	
	public Bonus_ScoreView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.resources);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_BONUS);
		
		((GameActivityInterface) this.getContext()).incrementScore(1000);
	}
}
