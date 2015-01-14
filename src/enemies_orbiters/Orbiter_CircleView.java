package enemies_orbiters;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import interfaces.GameObjectInterface;
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
