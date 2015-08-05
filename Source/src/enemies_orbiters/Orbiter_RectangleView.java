package enemies_orbiters;

import helpers.KillableRunnable;
import interfaces.MovingViewInterface;
import levels.AttributesOfLevels;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;


public class Orbiter_RectangleView extends Shooting_OrbiterView implements MovingViewInterface {
	
	public static final int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/3),
			DEFAULT_ORBIT_X=(int) (MainActivity.getWidthPixels()/2),
			DEFAULT_ORBIT_LENGTH = 6,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_rectangle;
	
	private int currentSideOfRectangle,orbitDist;

	public Orbiter_RectangleView(RelativeLayout layout,int level) {
		super(layout,level, 
				DEFAULT_SCORE, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_rectangular_width), 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_orbit_rectangular_height), 
				DEFAULT_BACKGROUND);

		orbitDist=DEFAULT_ORBIT_LENGTH;
		
		init();
	}
	

	public Orbiter_RectangleView(RelativeLayout layout,int level,int score,float speedY,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitLength,int orbitPixelX,int orbitPixelY,int width,int height,int imageId) {
		super(layout, level,score,speedY,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY, width, height, imageId);
		
		orbitDist=orbitLength;
		
		init();
	}
	
	private void init(){
		currentSideOfRectangle=0;
		
		//default to begin orbit at top of rectangle, 3/4 of way through (thus top middle, moving right)
		this.setGravityThreshold((int) (orbitY-(orbitDist*Math.abs(this.getSpeedY()) ) / 2 ));
		howManyTimesMoved=0;//(int) (.75*orbitDist);
	}


	@Override
	protected void reachedGravityPosition() {
		this.assignRectangularMoveRunnable(DEFAULT_SPEED_X,DEFAULT_SPEED_Y);
	}
	
	protected void assignRectangularMoveRunnable(final float spdX,final float spdY){
		
		this.setSpeedX(spdX);//start moving right
		this.setSpeedY(0);
		
		reassignMoveRunnable( new KillableRunnable() {
			@Override
			public void doWork() {
				howManyTimesMoved = (howManyTimesMoved+1) % orbitDist;
			
				//change side
				if (howManyTimesMoved == 0) {
					currentSideOfRectangle = (currentSideOfRectangle + 1) % 4;
					
					switch (currentSideOfRectangle) {
						case 0://right  
							Orbiter_RectangleView.this.setSpeedY(0);
							Orbiter_RectangleView.this.setSpeedX(spdX);
							break;
						case 1://down
							Orbiter_RectangleView.this.setSpeedX(0);
							Orbiter_RectangleView.this.setSpeedY(spdY);
							break;
						case 2://left
							Orbiter_RectangleView.this.setSpeedY(0);
							Orbiter_RectangleView.this.setSpeedX( - spdX);
							break;
						case 3://up
							Orbiter_RectangleView.this.setSpeedX(0);
							Orbiter_RectangleView.this.setSpeedY( - spdY);
							break;
					}
				}
				
				move();
				postDelayed(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		});
	}


	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.FIRST_LEVEL_RECTANGLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level);
		}
		
		return probabilityWeight;
	}
	
	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_RECTANGLE_ORBITERS_APPEAR){
			probabilityWeight = orbiterProbWeight(level) / HOW_MANY_TIMES_LESS_LIKELY_TO_SPAWN_MANY_ORBITERS;
		}
		
		return probabilityWeight;
	}
	
	public int orbitLengthX(){
		return (int) ( orbitDist*DEFAULT_SPEED_X * MainActivity.getScreenDens() );
	}
	public int orbitLengthY(){
		return (int) (orbitDist*DEFAULT_SPEED_Y  * MainActivity.getScreenDens() );
	}
}
