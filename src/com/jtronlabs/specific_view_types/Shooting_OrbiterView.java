package com.jtronlabs.specific_view_types;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightSingleShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Shooting_OrbiterView extends Gravity_ShootingView implements GameObjectInterface {

	public final static int DEFAULT_SCORE=10,DEFAULT_BACKGROUND=R.drawable.ufo,DEFAULT_BULLET_FREQ_INTERVAL=3000,
			DEFAULT_ORBIT_DIST_CIRCLE=100,
			DEFAULT_ORBIT_DIST_NON_CIRCLE=10,
			DEFAULT_ANGULAR_VELOCITY=5;
	public final static double DEFAULT_SPEED_Y=5,DEFAULT_SPEED_X=5,
			DEFAULT_COLLISION_DAMAGE=20, DEFAULT_HEALTH=10,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,DEFAULT_BULLET_DAMAGE=10;
	
	public static final int SQUARE=0,CIRCLE=1,TRIANGLE=2;
	
	
	float orbitY;
	int orbitType=0;
	
	// Constantly move all instances of this class in a square shape
	private final static int TOP_LEFT = 0, BOTTOM_LEFT = 1, BOTTOM_RIGHT = 2,
			TOP_RIGHT = 3;
	private final static int TOP=0,LEFT=1,RIGHT=2;
	
	private boolean hasBegunOrbiting=false;
	private int angularVelocity=DEFAULT_ANGULAR_VELOCITY;
	private int orbitDist;
	
	private int currentPos = TOP_LEFT, howManyTimesMoved;
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
			final double radius = orbitDist*screenDens;
			final int currentDegree = ( angularVelocity*howManyTimesMoved )%360;
			float y = (float) (radius* Math.sin(Math.toRadians(currentDegree)));
			float x = (float) (radius* Math.cos(Math.toRadians(currentDegree)));
			
			Shooting_OrbiterView.this.setX(widthPixels/2+x);
			Shooting_OrbiterView.this.setY(Shooting_OrbiterView.this.lowestPositionThreshold+y);
			
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
		
		if(atThreshold && !hasBegunOrbiting){
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
		this.lowestPositionThreshold=(int) (heightPixels/3);
		orbitType= orbitingType;
		orbitDist=orbitDistance;
		howManyTimesMoved=0;
		
		//set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
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
//		this.beginOrbit();
		super.restartThreads();
	}

}
