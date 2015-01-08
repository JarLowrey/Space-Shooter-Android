package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledTriShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_AngledTriShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_AngledTriShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.tri_bullet);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		if(theBenefitter.myGun instanceof Gun_Special_AngledTriShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.myGun;
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+7);
		}else{
			Gun_Special newGun = new Gun_Special_AngledTriShot(ctx,theBenefitter);

			theBenefitter.giveSpecialGun(newGun, 25);
		}
			
	}
}
