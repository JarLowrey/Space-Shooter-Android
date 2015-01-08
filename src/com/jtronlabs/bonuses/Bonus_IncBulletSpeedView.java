package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_IncBulletSpeedView extends BonusView implements GameObjectInterface{
	
	public static double SPEED_INCREASE_RATIO=1.1;
	
	public Bonus_IncBulletSpeedView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_speed);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.myGun.setBulletSpeedY(theBenefitter.myGun.getBulletSpeedY() * SPEED_INCREASE_RATIO );
	}
}
