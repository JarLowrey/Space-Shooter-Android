package bonuses;
 
import interfaces.MovingViewInterface;
import interfaces.Shooter;
import support.MediaController;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_HealView extends BonusView implements MovingViewInterface{
	
	public Bonus_HealView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
 
		this.setImageResource(R.drawable.heal);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_BONUS);
		
		final int amtToHeal = theBenefitter.getMaxHealth()/6;
		
		if(theBenefitter.getHealth()+amtToHeal<theBenefitter.getMaxHealth()){
			theBenefitter.heal(amtToHeal);
		}else{
			theBenefitter.heal(theBenefitter.getMaxHealth()-theBenefitter.getHealth());
		}
			
	}
}
