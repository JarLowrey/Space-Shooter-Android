package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserDefault;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_Bullet_Default extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_Default(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		//I gave up on giving an xml drawable a width and height		
		this.setImageResource(R.drawable.laser_default_img);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletType(new Bullet_LaserDefault());
	}
}
