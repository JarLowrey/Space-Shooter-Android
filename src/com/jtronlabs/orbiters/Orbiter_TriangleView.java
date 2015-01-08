package com.jtronlabs.orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Orbiter_TriangleView extends Shooting_OrbiterView implements GameObjectInterface {

	public final static int DEFAULT_ORBIT_LENGTH=10;

	public static final int DEFAULT_ANGLE = 60;
	
	private int currentSideOfTriangle =0;
	private int orbitDist;

	private Runnable moveInATriangleRunnable = new Runnable(){
		@Override
		public void run() {
			//triangle is equilateral
				switch (currentSideOfTriangle) {
				case 0:
					Orbiter_TriangleView.this.move(ProjectileView.LEFT);
					Orbiter_TriangleView.this.move(ProjectileView.LEFT);
					break;
				case 1:
					Orbiter_TriangleView.this.move(ProjectileView.RIGHT);
					Orbiter_TriangleView.this.move(ProjectileView.DOWN);
					break;
				case 2:
					Orbiter_TriangleView.this.move(ProjectileView.RIGHT);
					Orbiter_TriangleView.this.move(ProjectileView.UP);
					break;
				}
				//change side
				if (howManyTimesMoved % orbitDist == 0) {
					currentSideOfTriangle = (currentSideOfTriangle + 1) % 3;
				}
				howManyTimesMoved++;
				
				Orbiter_TriangleView.this.postDelayed(this,
						ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};
	
	public Orbiter_TriangleView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,double bulletDamage,double bulletVerticalSpeed,
			int orbitLength) {
		super(context, score, speedY, speedX,
				collisionDamage, health,bulletFreq,heightView,widthView,
				probSpawnBeneficialObjectOnDeath,bulletDamage,bulletVerticalSpeed);

		this.myGun=new Gun_Upgradeable_StraightSingleShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
		orbitDist=orbitLength;
		
		//default to begin orbit at this point
		this.lowestPositionThreshold=(int) (heightPixels/3);
		howManyTimesMoved=0;
		
		this.setY(0);
		this.setX(widthPixels/2);
		
	}
	
	public void beginOrbit(){
		this.post(moveInATriangleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInATriangleRunnable);
	}

}
