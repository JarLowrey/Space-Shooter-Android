package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_UpgradeGunView extends BonusView implements GameObjectInterface{
	
	public Bonus_UpgradeGunView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.upgrade_gun);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.upgradeOrDowngradeGun(true);
	}
}
