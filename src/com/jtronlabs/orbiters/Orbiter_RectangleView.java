package com.jtronlabs.orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Orbiter_RectangleView extends Shooting_OrbiterView implements GameObjectInterface {

	public final static int DEFAULT_ORBIT_LENGTH=15;
	
	private int currentSideOfRectangle =0;
	private int orbitDist;

	private Runnable moveInARectangleRunnable = new Runnable() {
		@Override
		public void run() {
    		//ensure view is not removed before running
			if( ! Orbiter_RectangleView.this.isRemoved()){
				switch (currentSideOfRectangle) {
				case 0:
					Orbiter_RectangleView.this.move(ProjectileView.RIGHT);
					break;
				case 1:
					Orbiter_RectangleView.this.move(ProjectileView.UP);
					break;
				case 2:
					Orbiter_RectangleView.this.move(ProjectileView.LEFT);
					break;
				case 3:
					Orbiter_RectangleView.this.move(ProjectileView.DOWN);
					break;
				}
				
				//change side
				if (howManyTimesMoved % orbitDist == 0) {
					currentSideOfRectangle = (currentSideOfRectangle + 1) % 4;
				}
				howManyTimesMoved++;
				
				Orbiter_RectangleView.this.postDelayed(this,
						ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	
	public Orbiter_RectangleView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,double bulletDamage,double bulletVerticalSpeed,
			int orbitLength) {
		super(context, score, speedY, speedX,
				collisionDamage, health,bulletFreq,heightView,widthView,
				probSpawnBeneficialObjectOnDeath,bulletDamage,bulletVerticalSpeed);

		this.myGun=new Gun_Upgradeable_StraightSingleShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
		orbitDist=orbitLength;
		
		//default to begin orbit at this point
		this.lowestPositionThreshold=(int) (MainActivity.getHeightPixels()/3);
		howManyTimesMoved=0;
		
		this.setY(0);

	}
	
	public void beginOrbit(){
		this.post(moveInARectangleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInARectangleRunnable);
	}

}
