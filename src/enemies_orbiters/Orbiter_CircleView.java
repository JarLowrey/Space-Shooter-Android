package enemies_orbiters;

import interfaces.GameObjectInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

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

	public Orbiter_CircleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees,int width,int height,int imageId) {
		super(context,score, speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, width, height, imageId);

		currentDegree=270;
		radius=circleRadius*MainActivity.getScreenDens();
		angularVelocity=angularVelocityInDegrees;
		//ensure radius and angular velocity are within bounds
		if(Math.abs(angularVelocity)>MAX_ANGULAR_VELOCITY){angularVelocity = MAX_ANGULAR_VELOCITY;}
		final int maxRadiusX = (int) (MainActivity.getWidthPixels()/2 - width/2 );
		final int maxRadiusY = (int) (MainActivity.getHeightPixels()/2 - height/2 );
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
