package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_Gun_IncBulletDamageView extends BonusView implements GameObjectInterface{
	
	public static double DAMAGE_INCREASE_RATIO=1.1;
	
	public Bonus_Gun_IncBulletDamageView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_damage);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.myGun.setBulletSpeedY(theBenefitter.myGun.getBulletDamage() * DAMAGE_INCREASE_RATIO );
	}
}
