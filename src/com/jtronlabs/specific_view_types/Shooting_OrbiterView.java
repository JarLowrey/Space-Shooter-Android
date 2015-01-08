package com.jtronlabs.specific_view_types;

import android.content.Context;

import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Shooting_OrbiterView extends Gravity_ShootingView implements GameObjectInterface {

	public static final int SQUARE=0,CIRCLE=1,TRIANGLE=2;
	float orbitY;
	int orbitType=0;
	
	// Constantly move all instances of this class in a square shape
	private final static int TOP_LEFT = 0, BOTTOM_LEFT = 1, BOTTOM_RIGHT = 2,
			TOP_RIGHT = 3;
	private final static int TOP=0,LEFT=1,RIGHT=2;
	
	private boolean hasBegunOrbiting=false;
	private int angularVelocity=5;
	private int orbitDist;
	
	private static int currentPos = TOP_LEFT, howManyTimesMoved = 0;
	private Runnable moveInASquareRunnable = new Runnable() {
		@Override
		public void run() {
			switch (currentPos) {
			case TOP_LEFT:
				Shooting_OrbiterView.this.move(ProjectileView.DOWN);
				break;
			case BOTTOM_RIGHT:
				Shooting_OrbiterView.this.move(ProjectileView.UP);
				break;
			case BOTTOM_LEFT:
				Shooting_OrbiterView.this.move(ProjectileView.RIGHT);
				break;
			case TOP_RIGHT:
				Shooting_OrbiterView.this.move(ProjectileView.LEFT);
				break;
			}
			if (howManyTimesMoved % orbitDist == 0) {
				currentPos = (currentPos + 1) % 4;
			}
			howManyTimesMoved++;
			
			Shooting_OrbiterView.this.postDelayed(this,
					ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};
	private Runnable moveInACircleRunnable = new Runnable(){
		@Override
		public void run() {
			//	imagine a circle around this triangle. the right point is the next position 
			//		|\-----top triangle
			//		| \----third side
			//		|  \___________________angleNext
			//		| /
			//		|/------bottom triangle
			//		^center point of circle
			//using law of cosines, the length between the current and next postition can be found given the radius of the circle
			final double thirdSide = Math.sqrt(
					orbitDist*orbitDist + orbitDist*orbitDist-2*orbitDist*orbitDist*Math.cos(angularVelocity) );
			final double angleNext = Math.acos(
					thirdSide*thirdSide/(2*orbitDist*thirdSide));
			final double topTriangleAngle = angleNext-(90-angularVelocity);//bottom triangle is right, thus 180-90-angular velocity is third angle. angleNext = third angle + an Angle in the top triangle
			final float newX=(float) (Math.cos(topTriangleAngle)*thirdSide);
			final float newY=(float) (Math.sin(topTriangleAngle)*thirdSide);
			
			Shooting_OrbiterView.this.setX(howManyTimesMoved*newX);
			Shooting_OrbiterView.this.setX(howManyTimesMoved*newY);
			
			howManyTimesMoved++;
			
			Shooting_OrbiterView.this.postDelayed(this,
					ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};
	private Runnable moveInATriangleRunnable = new Runnable(){
		@Override
		public void run() {
			//triangle is equilateral
				switch (currentPos) {
				case TOP:
					Shooting_OrbiterView.this.move(ProjectileView.LEFT);
					break;
				case LEFT:
					Shooting_OrbiterView.this.move(ProjectileView.RIGHT);
					Shooting_OrbiterView.this.move(ProjectileView.DOWN);
					break;
				case RIGHT:
					Shooting_OrbiterView.this.move(ProjectileView.RIGHT);
					Shooting_OrbiterView.this.move(ProjectileView.UP);
					break;
				}
				if (howManyTimesMoved % orbitDist == 0) {
					currentPos = (currentPos + 1) % 3;
				}
				howManyTimesMoved++;
				
				Shooting_OrbiterView.this.postDelayed(this,
						ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};
	
	@Override
	public boolean move(int direction){
		boolean atThreshold = super.move(direction);
		
		if(!hasBegunOrbiting){
			stopGravity();
			hasBegunOrbiting=true;
			this.beginOrbit();
		};
		
		return atThreshold;		
	}
	

	public Shooting_OrbiterView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,double bulletDamage,double bulletVerticalSpeed,
			int orbitingType, int orbitDistance) {
		super(context, false,score, speedY, speedY,
				speedX, collisionDamage, health,probSpawnBeneficialObjectOnDeath);

		this.myGun=new Gun_Upgradeable_StraightSingleShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
		
		//default to begin orbit at this point
		this.lowestPositionThreshold=(int) (heightPixels/4);
		orbitType= orbitingType;
		orbitDist=orbitDistance;
		
		cleanUpThreads();
		restartThreads();
	}
	
	public void setAngularVelocity(int newVelocity){
		this.angularVelocity=newVelocity;
	}
	public int getAngularVelocity(){
		return angularVelocity;
	}
	
	public int removeView(boolean showExplosion) {
		cleanUpThreads();

		return super.removeView(showExplosion);
	}
	public void beginOrbit(){
		switch(orbitType){
		case SQUARE:
			this.post(moveInASquareRunnable);
			break;
		case CIRCLE:
			this.post(moveInACircleRunnable);
			break;
		case TRIANGLE:
			this.post(moveInATriangleRunnable);
			break;
		}
	}
	
	public void cleanUpThreads(){
		this.removeCallbacks(moveInASquareRunnable);
		this.removeCallbacks(moveInACircleRunnable);
		this.removeCallbacks(moveInATriangleRunnable);
		super.cleanUpThreads();
	}
	public void restartThreads(){
		this.beginOrbit();
		super.restartThreads();
	}

}
