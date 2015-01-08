package com.jtronlabs.orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Orbiter_CircleView extends Shooting_OrbiterView implements GameObjectInterface {
	public static final int DEFAULT_ANGULAR_VELOCITY=5;
	public static final double DEFAULT_CIRCLE_RADIUS=100*screenDens;
	
	
	private int angularVelocity;
	private double radius;
	
	private Runnable moveInACircleRunnable = new Runnable(){
		@Override
		public void run() {
//				imagine a circle around this triangle. the right point is the next position 
//					|\-----top triangle
//					| \----third side
//					|  \___________________angleNext
//					| /
//					|/------bottom triangle
//					^center point of circle
//			using law of cosines, the length between the current and next postition can be found given the radius of the circle
//			final double radius = orbitDist*screenDens;
//			
//			final double thirdSide = Math.sqrt(
//					2*Math.pow(radius, 2)-2*Math.pow(radius, 2)*Math.cos( Math.toRadians(angularVelocity) ) );
//			final double angleNext = Math.acos(	Math.pow(thirdSide, 2)/(2*radius*thirdSide) );
//			final double topTriangleAngle = Math.toDegrees(angleNext)-(90-angularVelocity);//bottom triangle is right, thus 180-90-angular velocity is third angle. angleNext = third angle + an Angle in the top triangle
//			final float xCircleIncrement=(float) (Math.cos(Math.toRadians(topTriangleAngle))*thirdSide);
//			final float yCircleIncrement=(float) (Math.sin(Math.toRadians(topTriangleAngle))*thirdSide);
//			
//			//adjust for quadrants
//			final int currentDegree = ( angularVelocity*howManyTimesMoved )%360;
//			final int num_increments = howManyTimesMoved % (360/angularVelocity);
//			final int quadrant = currentDegree/90;
//			
//			float yDiff,xDiff;
//			switch(quadrant){
//			case 0:
//				yDiff= yCircleIncrement*num_increments;
//				xDiff=xCircleIncrement*num_increments;
//				break;
//			case 1:
//				yDiff= yCircleIncrement*num_increments;
//				xDiff=xCircleIncrement*num_increments;				
//				break;
//			case 2:
//				
//				break;
//			case 3:
//				
//				break;
//			}
//			
			//dear christ. Why am i so stupid.
			final int currentDegree = ( angularVelocity*howManyTimesMoved )%360;
			float y = (float) (radius* Math.sin(Math.toRadians(currentDegree)));
			float x = (float) (radius* Math.cos(Math.toRadians(currentDegree)));
			
			Orbiter_CircleView.this.setX(widthPixels/2+x);
			Orbiter_CircleView.this.setY(Orbiter_CircleView.this.lowestPositionThreshold+y);
			
			howManyTimesMoved++;
			
			Orbiter_CircleView.this.postDelayed(this,
					ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};
	

	public Orbiter_CircleView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,double bulletDamage,double bulletVerticalSpeed,
			double circleRadius,int angularVelocityInDegrees) {
		super(context, score, speedY, speedX,
				collisionDamage, health,bulletFreq,heightView,widthView,
				probSpawnBeneficialObjectOnDeath,bulletDamage,bulletVerticalSpeed);

		this.myGun=new Gun_Upgradeable_StraightSingleShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
		
		//default to begin orbit at this point
		this.lowestPositionThreshold=(int) (heightPixels/3);
		radius=circleRadius;
		angularVelocity=angularVelocityInDegrees;
		howManyTimesMoved=0;
		
		this.setY(0);
		this.setX(widthPixels/2);
		
		cleanUpThreads();
		restartThreads();
	}
	public void setAngularVelocity(int newVelocity){
		this.angularVelocity=newVelocity;
	}
	public int getAngularVelocity(){
		return angularVelocity;
	}
	public void beginOrbit(){
		this.post(moveInACircleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInACircleRunnable);
	}

}
