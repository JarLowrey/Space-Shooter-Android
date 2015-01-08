package com.jtronlabs.orbiters;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;

public abstract class Shooting_OrbiterView extends Gravity_ShootingView implements GameObjectInterface {

	public final static int DEFAULT_SCORE=10,DEFAULT_BACKGROUND=R.drawable.ufo,DEFAULT_BULLET_FREQ_INTERVAL=3000;
	public final static double DEFAULT_SPEED_Y=5,DEFAULT_SPEED_X=5,
			DEFAULT_COLLISION_DAMAGE=20, DEFAULT_HEALTH=10,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,DEFAULT_BULLET_DAMAGE=10;
	
	private boolean hasBegunOrbiting=false;
	protected int howManyTimesMoved;
	
	@Override
	public boolean move(int direction){
		boolean atThreshold = super.move(direction);
		
		if(atThreshold && !hasBegunOrbiting){
			stopGravity();
			hasBegunOrbiting=true;
			this.beginOrbit();
		};
		
		return atThreshold;		
	}
	

	public Shooting_OrbiterView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,double bulletDamage,double bulletVerticalSpeed) {
		super(context, false,score, speedY, speedY,
				speedX, collisionDamage, health,probSpawnBeneficialObjectOnDeath);

		this.myGun=new Gun_Upgradeable_StraightSingleShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
				
		//set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setY(0);
		this.setX(widthPixels/2);
		
	}
	
	public void restartThreads(){
		if(hasBegunOrbiting){
			beginOrbit();
		}
		super.restartThreads();
	}
	public abstract void beginOrbit();
	public abstract void endOrbit();

}
