package enemies_orbiters;

import helpers.KillableRunnable;
import interfaces.MovingViewInterface;
import levels.AttributesOfLevels;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class Orbiter_CircleView extends Shooting_OrbiterView implements MovingViewInterface {
	
	public static final float DEFAULT_ANGULAR_VELOCITY=MovingView.DEFAULT_SPEED_Y / 3, 
			MAX_ANGULAR_VELOCITY = 30, 
			MAX_RADIUS = (int) (140  * MainActivity.getScreenDens()),
			MIN_RADIUS = (int) (30  * MainActivity.getScreenDens());
	public static final int
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_circle;
	public static final int DEFAULT_CIRCLE_RADIUS=(int)(MainActivity.getWidthPixels());
	
	private float angularVelocity,currentDegree;
	private double radius;

	public Orbiter_CircleView(RelativeLayout layout,int level) {
		super(layout,level,
				DEFAULT_SCORE, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_circular_width), 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_circular_height), 
				DEFAULT_BACKGROUND);

		final int width=(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_circular_width);
		final int height=(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_circular_width);
		radius = Math.random() * ( MainActivity.getWidthPixels()-width) / 2 ;
		radius = Math.max(radius,MIN_RADIUS);
		radius = Math.min(radius,MAX_RADIUS);
		angularVelocity=DEFAULT_ANGULAR_VELOCITY;
		
		init(width,height);
	}
	
	public Orbiter_CircleView(RelativeLayout layout,int level,int score,float speedY, 
			int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,int width,int height,int imageId,
			int circularRadius,int angVelocity) {
		super(layout, level,score,speedY,
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
		radius = Math.max(radius,MIN_RADIUS);
		radius = Math.min(radius,MAX_RADIUS);		
		howManyTimesMoved=0; 
		
		//begin orbit at top of circle
		this.setGravityThreshold((int) (orbitY - radius));
		this.setX(orbitX-width/2);
	}
	
	public void setAngularVelocity(float f){
		this.angularVelocity=f;
	}
	public float getAngularVelocity(){
		return angularVelocity;
	}
	
	@Override
	public void updateViewSpeed(long deltaTime) {
		if(hasReachedGravityThreshold()){
			currentDegree = ( angularVelocity+currentDegree )%360;
			float y = (float) (radius * Math.sin(Math.toRadians(currentDegree)));
			float x = (float) (radius * Math.cos(Math.toRadians(currentDegree)));
			
			Orbiter_CircleView.this.setX( orbitX+x );
			Orbiter_CircleView.this.setY( orbitY+y );
		
			howManyTimesMoved++;
		}else{
//			super.updateViewSpeed(deltaTime
		}
	}

	@Override
	protected int orbitLengthX() {
		return (int) radius;
	}

	@Override
	protected int orbitLengthY() {
		return (int) radius;
	}

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level);
		}
		
		return probabilityWeight;
	}
	

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level) / HOW_MANY_TIMES_LESS_LIKELY_TO_SPAWN_MANY_ORBITERS;
		}
		
		return probabilityWeight;
	}
}
