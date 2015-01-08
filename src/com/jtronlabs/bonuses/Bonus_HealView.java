package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_HealView extends BonusView implements GameObjectInterface{
	
	public Bonus_HealView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.heal);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		final double amtToHeal = theBenefitter.getMaxHealth()/5;
		if(theBenefitter.getHealth()+amtToHeal<theBenefitter.getMaxHealth()){
			theBenefitter.heal(amtToHeal);
		}else{
			theBenefitter.heal(theBenefitter.getMaxHealth()-theBenefitter.getHealth());
		}
			
	}
}
