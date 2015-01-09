package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledTriShot;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_SpecialGun_AngledTriShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_SpecialGun_AngledTriShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.tri_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		if(theBenefitter.getGun() instanceof Gun_Special_AngledTriShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.getGun();
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+7);
		}else{
			Gun_Special newGun = new Gun_Special_AngledTriShot(this.getContext(),theBenefitter);

			theBenefitter.giveSpecialGun(newGun, 25);
		}
			
	}
}
