package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_Gun_IncBulletSpeedView extends BonusView implements GameObjectInterface{
	
	public static double SPEED_INCREASE_RATIO=1.1;
	
	public Bonus_Gun_IncBulletSpeedView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_speed);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletSpeedY(theBenefitter.getBulletSpeedY() * SPEED_INCREASE_RATIO );
	}
}
