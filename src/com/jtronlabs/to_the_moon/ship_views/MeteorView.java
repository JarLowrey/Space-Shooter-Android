package com.jtronlabs.to_the_moon.ship_views;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObject;

public class MeteorView extends GravityView implements GameObject{
	
	public final static int DEFAULT_SCORE=5;
	public final static double DEFAULT_SPEED_UP=1,DEFAULT_SPEED_DOWN=6.4,DEFAULT_SPEEDX=1,DEFAULT_COLLISION_DAMAGE=12, 
			DEFAULT_HEALTH=5;
	
	public MeteorView(Context context) {
		super(context,DEFAULT_SCORE,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH);

		this.lowestPositionThreshold=heightPixels;
		
		//set image background
		this.setImageResource(R.drawable.meteor);
		
		//set image width,length
		int len=(int)ctx.getResources().getDimension(R.dimen.meteor_length);
		this.setLayoutParams(new LayoutParams(len,len));
		
		//set initial position of View
		float xRand = (float) ((widthPixels-len)*Math.random());
		this.setX(xRand);
		this.setY(0);
		
		cleanUpThreads();
		restartThreads();
	}
}
