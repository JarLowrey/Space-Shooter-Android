package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_UpgradeGunView extends BonusView implements GameObjectInterface{
	
	public Bonus_UpgradeGunView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.upgrade_gun);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.upgradeOrDowngradeGun(true);
	}
}
