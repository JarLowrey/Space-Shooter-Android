package enemies;

import levels.AttributesOfLevels;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;

public class Shooting_HorizontalMovementView extends Enemy_ShooterView{

	public final static int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/4),
			DEFAULT_ANGLE = 30;

	public final static float 
			DEFAULT_SPEED_Y = MovingView.DEFAULT_SPEED_Y * 2,
			DEFAULT_SPEED_X = (float) (MovingView.DEFAULT_SPEED_X * 1.5),
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	
	private boolean hasBegunHorizontalMovement = false;
	
	
	public Shooting_HorizontalMovementView(RelativeLayout layout, 
			int level,
			int scoreForKilling,
			float projectileSpeedY,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, 
			int width, int height, int imageId) {
		super(layout, level,
				scoreForKilling, projectileSpeedY,0,
				projectileDamage, projectileHealth, probSpawnBeneficialObject, width,
				height, imageId);

		init(width);
	}
	
	private void init(int width){
		this.setX((float) (MainActivity.getWidthPixels()/2 - width/2.0) );
		this.setGravityThreshold((int) MainActivity.getHeightPixels()/3);
	}
	
	public static int getSpawningProbabilityWeightForBoss1(int level) {
		int probabilityWeight = 0;
		
		if( level > AttributesOfLevels.FIRST_LEVEL_BOSS1_APPEARS){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.1);
			}else if(level < AttributesOfLevels.LEVELS_HIGH){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.13);				
			}else{
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.16);				
			}
		}
		
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightForBoss2(int level) {	
		if( level > AttributesOfLevels.FIRST_LEVEL_BOSS2_APPEARS){
			return getSpawningProbabilityWeightForBoss1(level) / 5;
		}
		return 0;
	}

	public static int getSpawningProbabilityWeightForBoss3(int level) {		
		if( level > AttributesOfLevels.FIRST_LEVEL_BOSS3_APPEARS){
			return getSpawningProbabilityWeightForBoss1(level) / 10;
		}
		return 0;
	}

	public static int getSpawningProbabilityWeightForBoss4(int level) {	
		if( level > AttributesOfLevels.FIRST_LEVEL_BOSS4_APPEARS){
			return getSpawningProbabilityWeightForBoss1(level) / 15;
		}
		return 0;
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		if(hasReachedGravityThreshold()){
			if(!hasBegunHorizontalMovement){
				hasBegunHorizontalMovement = true;
				this.setSpeedY(0);
				this.setSpeedX(DEFAULT_SPEED_X);
			}
			
			final float nextLeftPos = (float) (Shooting_HorizontalMovementView.this.getX() - this.getSpeedX());
			final float nextRightPos = (float) (nextLeftPos + Shooting_HorizontalMovementView.this.getWidth() + this.getSpeedX());
			if(nextLeftPos < 0 || nextRightPos > MainActivity.getWidthPixels()){
				Shooting_HorizontalMovementView.this.
					setSpeedX(Shooting_HorizontalMovementView.this.getSpeedX() * -1);//reverse horizontal direction
			}
		}
	}
}
