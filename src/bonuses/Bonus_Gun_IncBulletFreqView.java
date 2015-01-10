package bonuses;

import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_Gun_IncBulletFreqView extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Gun_IncBulletFreqView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_freq);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletFreq(theBenefitter.getBulletFreq() / FREQ_DECREASE_RATIO );
	}
}
