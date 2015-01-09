package com.jtronlabs.enemy_types_orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;

public class Orbiter_RectangleView extends Shooting_OrbiterView implements GameObjectInterface {

	public final static int DEFAULT_ORBIT_LENGTH=15;
	
	private int currentSideOfRectangle,orbitDist;

	private Runnable moveInARectangleRunnable = new Runnable() {
		@Override
		public void run() {
    		//ensure view is not removed before running
			if( ! Orbiter_RectangleView.this.isRemoved()){
				switch (currentSideOfRectangle) {
				case 0:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.RIGHT);
					break;
				case 1:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.DOWN);
					break;
				case 2:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.LEFT);
					break;
				case 3:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.UP);
					break;
				}
				
				//change side
				if (howManyTimesMoved % orbitDist == 0) {
					currentSideOfRectangle = (currentSideOfRectangle + 1) % 4;
				}
				howManyTimesMoved++;
				
				Orbiter_RectangleView.this.postDelayed(this,
						Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	
	public Orbiter_RectangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double bulletDamage,double bulletVerticalSpeed,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength) {
		super(context, score,speedY, speedX,
				collisionDamage, health,bulletFreq,heightView,widthView,
				bulletDamage,bulletVerticalSpeed, probSpawnBeneficialObjecyUponDeath);

		currentSideOfRectangle=0;
		orbitDist=orbitLength;
	
		//default to begin orbit at top of rectangle, 3/4 of way through (thus top middle, moving right)
		this.setThreshold((int) (orbitY+(orbitDist*this.getSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist *3.0/4);
		
		this.setY(0);

	}
	
	public void beginOrbit(){
		this.post(moveInARectangleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInARectangleRunnable);
	}

}
