package enemies_orbiters;

import interfaces.MovingViewInterface;
import levels.AttributesOfLevels;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.R;


public class Orbiter_TriangleView extends Shooting_OrbiterView implements MovingViewInterface {

	public final static int 
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_triangle;

	public static final float 
			DEFAULT_SPEED_Y = (float) (Shooting_OrbiterView.DEFAULT_SPEED_Y * .4),
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y;
	
	public Orbiter_TriangleView(RelativeLayout layout,int level) {
		super(layout, level, 
				DEFAULT_SCORE, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_triangular_width), 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_triangular_height), 
				DEFAULT_BACKGROUND);

		orbitRevolutionTime = DEFAULT_ORBIT_TIME;
		
		init();
	}
	

	public Orbiter_TriangleView(RelativeLayout layout,int level,int score,float speedY,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitRevolutionTimeLength, int orbitPixelX, int orbitPixelY,int width,int height,int imageId) {
		super(layout, level,score,speedY,
				collisionDamage, health, probSpawnBeneficialObjecyUponDeath, 
				orbitPixelX, orbitPixelY, width, height, imageId,orbitRevolutionTimeLength);

		init();
	}
	
	private void init(){

	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		if(hasReachedGravityThreshold()){
			
			//triangle is equilateral
			long orbitDivisor = orbitRevolutionTime / 3;//break triangle to 3 portions and switch on which side the triangle is currently on
			switch ( (int)(currentRevolutionTime / orbitDivisor) ) {
			case 0:
				Orbiter_TriangleView.this.setSpeedY(0);
				Orbiter_TriangleView.this.setSpeedX( - DEFAULT_SPEED_X * 2);
				break;
			case 1:
				Orbiter_TriangleView.this.setSpeedY(DEFAULT_SPEED_Y);
				Orbiter_TriangleView.this.setSpeedX(DEFAULT_SPEED_X);
				break;
			case 2:
				Orbiter_TriangleView.this.setSpeedY( - DEFAULT_SPEED_Y);
				Orbiter_TriangleView.this.setSpeedX(DEFAULT_SPEED_X);
				break;
			}
		}
		super.updateViewSpeed(deltaTime);
	}
	

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.FIRST_LEVEL_TRIANGLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level);
		}
		
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_TRIANGLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level) / HOW_MANY_TIMES_LESS_LIKELY_TO_SPAWN_MANY_ORBITERS;
		}
		
		return probabilityWeight;
	}
	
	
	public int orbitLengthX(){
		return (int) ( orbitRevolutionTime * (2.0/6) * getSpeedX() );
	}
	public int orbitLengthY(){
		return (int) (orbitRevolutionTime/6 * getSpeedY() );
	}
}
