package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserShort;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_AngledDualShot;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_GiveGun_AngledDualShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_GiveGun_AngledDualShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		this.setImageResource(R.drawable.dual_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		Gun gun = new Gun_AngledDualShot(ctx, theBenefitter, new Bullet_LaserShort());
		theBenefitter.giveNewGun(gun);
	}
}
