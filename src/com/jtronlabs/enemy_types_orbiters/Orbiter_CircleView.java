package com.jtronlabs.enemy_types_orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;

public class Orbiter_CircleView extends Shooting_OrbiterView implements GameObjectInterface {
	
	public static final int DEFAULT_ANGULAR_VELOCITY=5;
	public static final double DEFAULT_CIRCLE_RADIUS=100*MainActivity.getScreenDens();
	
	
	private int angularVelocity;
	private double radius;
	
	private Runnable moveInACircleRunnable = new Runnable(){
		@Override
		public void run() {

    		//ensure view is not removed before running
			if( ! Orbiter_CircleView.this.isRemoved()){
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
	
				//dear christ. Why am i so stupid.
				final int currentDegree = ( angularVelocity*howManyTimesMoved )%360;
				float y = (float) (radius* Math.sin(Math.toRadians(currentDegree)));
				float x = (float) (radius* Math.cos(Math.toRadians(currentDegree)));
				
				Orbiter_CircleView.this.setX(MainActivity.getWidthPixels()/2+x);
				Orbiter_CircleView.this.setY(Orbiter_CircleView.this.getThreshold()+y);
				
				howManyTimesMoved++;
				
				Orbiter_CircleView.this.postDelayed(this,
						Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	

	public Orbiter_CircleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double bulletDamage,double bulletVerticalSpeed,double probSpawnBeneficialObjecyUponDeath,
			double circleRadius,int angularVelocityInDegrees) {
		super(context,score, speedY, speedX,
				collisionDamage, health,bulletFreq,heightView,widthView,
				bulletDamage,bulletVerticalSpeed, probSpawnBeneficialObjecyUponDeath);

		//default to begin orbit at this point
		this.setThreshold((int) (MainActivity.getHeightPixels()/3));
		radius=circleRadius;
		angularVelocity=angularVelocityInDegrees;
		howManyTimesMoved=0;
		
		this.setY(0);

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
