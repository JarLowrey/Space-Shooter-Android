package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_Missile;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_Bullet_Missile extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_Missile(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.missile_bonus);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.myBulletType = new Bullet_Missile();
	}
}
