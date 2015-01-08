package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledDualShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_AngledDualShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_AngledDualShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		this.setImageResource(R.drawable.dual_bullet);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		if(theBenefitter.myGun instanceof Gun_Special_AngledDualShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.myGun;
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+12);
		}else{
			Gun_Special newGun = new Gun_Special_AngledDualShot(ctx,theBenefitter);
			
			theBenefitter.giveSpecialGun(newGun, 30);
		}	
	}
}
