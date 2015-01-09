package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_Gun_IncBulletDamageView extends BonusView implements GameObjectInterface{
	
	public static double DAMAGE_INCREASE_RATIO=1.1;
	
	public Bonus_Gun_IncBulletDamageView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_damage);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletDamage(theBenefitter.getBulletDamage() * DAMAGE_INCREASE_RATIO );
	}
}
