package com.jtronlabs.specific_view_types;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Projectile_GravityView;

public class Gravity_MeteorView extends Projectile_GravityView implements GameObjectInterface{
	
	public final static int DEFAULT_SCORE=5,DEFAULT_BACKGROUND=R.drawable.meteor;
	public final static double DEFAULT_SPEED_Y=6.4,DEFAULT_SPEED_X=1,
			DEFAULT_COLLISION_DAMAGE=12, 
			DEFAULT_HEALTH=5,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.02;
	
	public Gravity_MeteorView(Context context,int score,double speedY,double speedX,
			double collisionDamage,double health,double probSpawnBeneficialObjectOnDeath) {
		super(context,score,speedY,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);

		this.lowestPositionThreshold=(int) heightPixels;
		
		//set image background
		this.setImageResource(R.drawable.meteor);
		
		//set image width,length
		int len=(int)ctx.getResources().getDimension(R.dimen.meteor_length);
		this.setLayoutParams(new LayoutParams(len,len));
		
		//set initial position of View
		float xRand = (float) ((widthPixels-len)*Math.random());
		this.setX(xRand);
		this.setY(0);
		
	}
}
