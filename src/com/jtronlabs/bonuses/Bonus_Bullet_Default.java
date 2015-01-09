package com.jtronlabs.bonuses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserDefault;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public class Bonus_Bullet_Default extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_Default(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		//I gave up on giving an xml drawable a width and height		
		this.setImageResource(R.drawable.laser_default_img);
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		theBenefitter.myBulletType = new Bullet_LaserDefault();
	}
}
