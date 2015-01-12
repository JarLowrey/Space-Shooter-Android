package enemies_orbiters;

import interfaces.GameObjectInterface;
import abstract_parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Orbiter_CircleView extends Shooting_OrbiterView implements GameObjectInterface {
	
	public static final int DEFAULT_ANGULAR_VELOCITY=2, 
			MAX_ANGULAR_VELOCITY = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_circle;
	public static final int DEFAULT_CIRCLE_RADIUS=(int)(MainActivity.getWidthPixels());
	
	private int currentDegree;
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
	
				//dear christ. Why am i so stupid. Figured it out eventually!
				currentDegree = ( angularVelocity+currentDegree )%360;
				float y = (float) (radius * Math.sin(Math.toRadians(currentDegree)));
				float x = (float) (radius * Math.cos(Math.toRadians(currentDegree)));
				
				Orbiter_CircleView.this.setX( orbitX+x );
				Orbiter_CircleView.this.setY( orbitY+y );
			
				howManyTimesMoved++;
				
				Orbiter_CircleView.this.postDelayed(this,
						Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	

	public Orbiter_CircleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees) {
		super(context,score, speedY, speedX,
				collisionDamage, health,heightView,widthView,
				 probSpawnBeneficialObjecyUponDeath);

		//set image background, width, and height, and orbit location
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_circular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_circular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setImageResource(DEFAULT_BACKGROUND);
		
		currentDegree=270;
		radius=circleRadius*MainActivity.getScreenDens();
		angularVelocity=angularVelocityInDegrees;
		//ensure radius and angular velocity are within bounds
		if(Math.abs(angularVelocity)>MAX_ANGULAR_VELOCITY){angularVelocity = MAX_ANGULAR_VELOCITY;}
		final int maxRadiusX = (int) (MainActivity.getWidthPixels()/2 - width_int/2 );
		final int maxRadiusY = (int) (MainActivity.getHeightPixels()/2 - height_int/2 );
		final int maxRadius = (maxRadiusX>maxRadiusY) ? maxRadiusY : maxRadiusX;//choose the smaller of the x and y
		if(Math.abs(radius)>maxRadius){radius = maxRadius;}
		howManyTimesMoved=0;
		
		this.setThreshold((int) (orbitY - radius));//begin orbit at top of circle
		this.setX(orbitX-width_int/2);
	}
	
	

	public Orbiter_CircleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double bulletDamage,double bulletVerticalSpeed,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees, int orbitPixelX,int orbitPixelY) {
		super(context,score, speedY, speedX,
				collisionDamage, health,heightView,widthView,
			 probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY);

		//set image background, width, and height, and orbit location
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_circular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_circular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setImageResource(DEFAULT_BACKGROUND);
		
		currentDegree=270;
		radius=circleRadius*MainActivity.getScreenDens();
		angularVelocity=angularVelocityInDegrees;
		//ensure radius and angular velocity are within bounds
		if(Math.abs(angularVelocity)>MAX_ANGULAR_VELOCITY){angularVelocity = MAX_ANGULAR_VELOCITY;}
		final int maxRadiusX = (int) (MainActivity.getWidthPixels()/2 - width_int/2 );
		final int maxRadiusY = (int) (MainActivity.getHeightPixels()/2 - height_int/2 );
		final int maxRadius = (maxRadiusX>maxRadiusY) ? maxRadiusY : maxRadiusX;//choose the smaller of the x and y
		if(Math.abs(radius)>maxRadius){radius = maxRadius;}
		howManyTimesMoved=0;
		
		this.setThreshold((int) (orbitY - radius));//begin orbit at top of circle
		this.setX(orbitX-width_int/2);
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
	
	public void setWidthAndHeight(int width, int height){
		
	}

}
