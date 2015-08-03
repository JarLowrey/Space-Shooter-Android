package bonuses;
 
import friendlies.ProtagonistView;
import helpers.MediaController;
import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;

public class Bonus_HealView extends BonusView implements MovingViewInterface{
	
	public Bonus_HealView(RelativeLayout layout,float positionX,float positionY) {
		super(layout,positionX,positionY);	
 
		this.setImageResource(R.drawable.heal);
	}
	
	public void applyBenefit(){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_BONUS);
		
		ProtagonistView protag = ((GameActivityInterface) this.getContext()).getProtagonist();
		
		final int amtToHeal = protag.getMaxHealth()/6;
		
		if(protag.getHealth()+amtToHeal<protag.getMaxHealth()){
			protag.heal(amtToHeal);
		}else{
			protag.heal(protag.getMaxHealth()-protag.getHealth());
		}
			
	}
}
