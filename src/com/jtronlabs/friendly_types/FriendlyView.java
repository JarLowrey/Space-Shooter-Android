package com.jtronlabs.friendly_types;

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;

public class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(Context context,double projectileSpeedY
			,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super( context, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		GameActivity.friendlies.add(this);
	}
	
	public FriendlyView(Context context,AttributeSet at,double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super( context,at, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		GameActivity.friendlies.add(this);
	}
}
