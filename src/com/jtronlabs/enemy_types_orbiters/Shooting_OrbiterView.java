package com.jtronlabs.enemy_types_orbiters;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.enemy_types.Enemy_ShooterView;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {


	public static final float DEFAULT_ORBIT_Y=MainActivity.getHeightPixels()/3,
			DEFAULT_ORBIT_X=MainActivity.getWidthPixels()/2;
	
	public final static int DEFAULT_SCORE=10,
			DEFAULT_BACKGROUND=R.drawable.ufo,
			DEFAULT_BULLET_FREQ_INTERVAL=1500;
	public final static double DEFAULT_SPEED_Y=5,
			DEFAULT_SPEED_X=5,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=50,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10;
	
	private boolean hasBegunOrbiting=false;
	protected int howManyTimesMoved;
	protected float orbitY,orbitX;
	
	@Override
	public boolean moveDirection(int direction){
		boolean atThreshold = super.moveDirection(direction);
		
		if(atThreshold && !hasBegunOrbiting){
			stopGravity();
			hasBegunOrbiting=true;
			this.beginOrbit();
		};
		
		return atThreshold;		
	}
	

	public Shooting_OrbiterView(Context context,int scoreForKilling,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double bulletDamage,double bulletVerticalSpeed,
			double probSpawnBeneficialObjecyUponDeath) {
		super(context, scoreForKilling, speedY,
				speedX, collisionDamage, health,probSpawnBeneficialObjecyUponDeath, 
				bulletFreq, bulletDamage, bulletVerticalSpeed);

		this.setGun(new Gun_Upgradeable_StraightSingleShot(context, this));
				
		//set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));

		orbitX=DEFAULT_ORBIT_X;
		orbitY=DEFAULT_ORBIT_Y;
		this.setY(0);
		this.setX(orbitX-this.getWidth()/2);
	}
	
	public void restartThreads(){
		if(hasBegunOrbiting){
			beginOrbit();
		}
		super.restartThreads();
	}

	public void setOrbitLocation(float pixelX,float pixelY){
		orbitX=pixelX;
		orbitY=pixelY;
	}
	public abstract void beginOrbit();
	public abstract void endOrbit();

}
