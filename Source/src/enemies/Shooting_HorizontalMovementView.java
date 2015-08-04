package enemies;

import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import levels.AttributesOfLevels;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;

import enemies_orbiters.Orbiter_Rectangle_Array;

public class Shooting_HorizontalMovementView extends Enemy_ShooterView{

	public final static int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/4),
			DEFAULT_ANGLE = 30;

	public final static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	
	
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

	@Override
	protected void reachedGravityPosition() {
		this.setSpeedY(0);
		this.setSpeedX(DEFAULT_SPEED_X);
		
		reassignMoveRunnable( new KillableRunnable(){ 
			@Override
			public void doWork() {
				final float leftPos = Shooting_HorizontalMovementView.this.getX();
				final float rightPos = leftPos + Shooting_HorizontalMovementView.this.getWidth();
				if(leftPos < 0 || rightPos > MainActivity.getWidthPixels()){
					Shooting_HorizontalMovementView.this.setSpeedX(Shooting_HorizontalMovementView.this.getSpeedX() * -1);//reverse horizontal direction
				}
				
				move();
				ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE, Shooting_HorizontalMovementView.this);
			}
		}); 
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
}
