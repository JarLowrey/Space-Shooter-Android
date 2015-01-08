package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_IncBulletFreqView extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_IncBulletFreqView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_freq);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.myGun.setBulletFreq(theBenefitter.myGun.getBulletFreq() / FREQ_DECREASE_RATIO );
	}
}
