package enemies_orbiters;

import interfaces.MovingViewInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Orbiter_CircleView extends Shooting_OrbiterView implements MovingViewInterface {
	
	public static final int DEFAULT_ANGULAR_VELOCITY=2, 
			MAX_ANGULAR_VELOCITY = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_circle;
	public static final int DEFAULT_CIRCLE_RADIUS=(int)(MainActivity.getWidthPixels());
	
	private int currentDegree;
	private int angularVelocity;
	private double radius;

	public Orbiter_CircleView(Context context) {
		super(context,DEFAULT_SCORE, 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_circular_width), 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_circular_height), 
				DEFAULT_BACKGROUND);

		final int width=(int)context.getResources().getDimension(R.dimen.ship_orbit_circular_width);
		final int height=(int)context.getResources().getDimension(R.dimen.ship_orbit_circular_width);
		radius=(MainActivity.getWidthPixels()-width)/2;
		angularVelocity=DEFAULT_ANGULAR_VELOCITY;
		
		init(width,height);
	}
	
	public Orbiter_CircleView(Context context,int score,double speedY, 
			double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,int width,int height,int imageId,
			int circularRadius,int angVelocity) {
		super(context, score,speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY, width, height, imageId);
		
		radius=circularRadius;
		angularVelocity=angVelocity;
		
		init(width,height);
	}
	
	private void init(int width,int height){
		currentDegree=270;
		
		//ensure radius and angular velocity are within bounds
		if(Math.abs(angularVelocity)>MAX_ANGULAR_VELOCITY){angularVelocity = MAX_ANGULAR_VELOCITY;}
		final int maxRadiusX = (int) (MainActivity.getWidthPixels()/2 - width/2 );
		final int maxRadiusY = (int) (MainActivity.getHeightPixels()/2 - height/2 );//this could still hit protagonist, needs to be further limited or not worried about (as X will always be smaller than Y)
		final int maxRadius = (maxRadiusX>maxRadiusY) ? maxRadiusY : maxRadiusX;//choose the smaller of the x and y
		if(Math.abs(radius)>maxRadius){radius = maxRadius;}
		howManyTimesMoved=0; 
		
		this.setThreshold((int) (orbitY - radius));//begin orbit at top of circle
		this.setX(orbitX-width/2);
		
		
		orbitingRunnable = new Runnable(){
			@Override
			public void run() {
				currentDegree = ( angularVelocity+currentDegree )%360;
				float y = (float) (radius * Math.sin(Math.toRadians(currentDegree)));
				float x = (float) (radius * Math.cos(Math.toRadians(currentDegree)));
				
				Orbiter_CircleView.this.setX( orbitX+x );
				Orbiter_CircleView.this.setY( orbitY+y );
			
				howManyTimesMoved++;
				
				ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,Orbiter_CircleView.this);
			}
		};
	}
	
	public void setAngularVelocity(int newVelocity){
		this.angularVelocity=newVelocity;
	}
	public int getAngularVelocity(){
		return angularVelocity;
	}

}
