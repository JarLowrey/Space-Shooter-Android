package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

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
