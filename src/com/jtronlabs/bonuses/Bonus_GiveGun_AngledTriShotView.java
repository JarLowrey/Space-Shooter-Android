package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserShort;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_AngledTriShot;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_GiveGun_AngledTriShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_GiveGun_AngledTriShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.tri_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		Gun gun = new Gun_AngledTriShot(ctx, theBenefitter, new Bullet_LaserShort());
		theBenefitter.giveNewGun(gun);
	}
}
