package enemies;

import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Duration;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

//TODO cannot get Duration bullets to rotate along with the view properly

public class Shooting_DurationLaserView extends Enemy_ShooterView{

	public static final int DEFAULT_BACKGROUND_ID = R.drawable.ship_enemy_duration_laser,
			DEFAULT_SCORE = 200,
			DEFAULT_HEALTH = ProtagonistView.DEFAULT_BULLET_DAMAGE * 12,
			DEFAULT_BULLET_FREQ = 4000;
	
	public static final float DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .1;
	
	private static final float ROTATION_THRESHOLD = 20,
			DEFAULT_ANGULAR_SPEED = 1;	
	 
//	private int currLevel;
	private double myAngularSpeed = DEFAULT_ANGULAR_SPEED;
	
	public Shooting_DurationLaserView(RelativeLayout layout, int level) {
		super(layout, 
				level, 
				DEFAULT_SCORE, 
				DEFAULT_SPEED_Y, 
				0,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH, 
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_enemy_duration_laser_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_enemy_duration_laser_height), 
				DEFAULT_BACKGROUND_ID);

//		currLevel = level;
		
		setRandomXPos();
		this.setGravityThreshold((int) (MainActivity.getHeightPixels()/3 + Math.random()));

		 //gun that shoots duration bullets
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ * Math.random() + DEFAULT_BULLET_FREQ * .75);
		final int X_POS_ON_SHOOTER = 50;
		Gun defaultGun = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Duration(
				(int)getContext().getResources().getDimension(R.dimen.bullet_xskinny_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red,
				Bullet_Duration.DEFAULT_BULLET_DURATION),
			bulletFreq, 
			Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
			Bullet_Duration.DEFAULT_BULLET_DAMAGE,
			X_POS_ON_SHOOTER);
		this.addGun(defaultGun);
		this.startShooting();
		
	}
	
//	
//	private void constantRotation(){			
//		double nextRotationAngle = myAngularSpeed + getRotation();
//		
//		//change direction if beyond threshold
//		if(nextRotationAngle < -ROTATION_THRESHOLD || nextRotationAngle > ROTATION_THRESHOLD){
//			myAngularSpeed *= -1;
//			nextRotationAngle = myAngularSpeed + getRotation();;
//		}
//		Shooting_DurationLaserView.this.setRotation((float) nextRotationAngle);
//	}
//	
//	//TODO aim this view towards the protagonist
//	private void aimingRotation(){
//		MovingView viewToAimAt = ( (GameActivityInterface)getContext() ).getProtagonist();
//	}
//	
//	@Override
//	public void setRotation(float rotation){
//		super.setRotation(rotation);
//		
//		for(BulletView b : myBullets){
//			if(b instanceof Bullet_HasDurationView){
//				
//				b.setRotation(rotation);
//			}
//		}
//	}
	

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.FIRST_LEVEL_DURATION_BULLETS_APPEAR){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.05);
			}else if(level < AttributesOfLevels.LEVELS_HIGH){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.2);				
			}else{
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * .4);				
			}
		}
		
		return probabilityWeight;
	}
	

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR){
			probabilityWeight = getSpawningProbabilityWeight(level) / 20 ;
		}
		
		return probabilityWeight;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){		
		int numEnemies = 0;
		
		if(lvl < AttributesOfLevels.LEVELS_MED){ //choose how many diagonal enemies spawn
			numEnemies = 3;
		}else if (lvl < AttributesOfLevels.LEVELS_HIGH){
			numEnemies = 4;
		}else {
			numEnemies = 5;
		}
		
		return numEnemies;
	}

	@Override
	public void updateViewSpeed(long millisecondsSinceLastSpeedUpdate) {
		if(hasReachedGravityThreshold()){
			this.setSpeedY(0);
//			if(currLevel > AttributesOfLevels.LEVELS_LOW){
//				if(currLevel < AttributesOfLevels.LEVELS_MED){//if level > MED rotate randomly within a threshold
//					constantRotation();
//				}else{ //level is high so rotate so facing protagonist
//					aimingRotation();
//				}
//			}
		}
	}
}
