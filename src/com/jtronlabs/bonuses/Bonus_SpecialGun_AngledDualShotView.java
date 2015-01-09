package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledDualShot;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_SpecialGun_AngledDualShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_SpecialGun_AngledDualShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		this.setImageResource(R.drawable.dual_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		if(theBenefitter.getGun() instanceof Gun_Special_AngledDualShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.getGun();
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+12);
		}else{
			Gun_Special newGun = new Gun_Special_AngledDualShot(this.getContext(),theBenefitter);
			
			theBenefitter.giveSpecialGun(newGun, 30);
		}	
	}
}
