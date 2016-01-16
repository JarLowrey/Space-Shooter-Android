package enemies;
 
import guns.Gun_TrackingSingle;
import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies_non_shooters.Gravity_MeteorView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_PauseAndMove extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_pause_and_shoot,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 8.5),
			DEFAULT_SCORE=130;
	public static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private long amtOfTimeToPause,currentTimePaused = 0;
	 
	public Shooting_PauseAndMove (RelativeLayout layout,int level) {
		super(
				getRandomXPosInMiddle(layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width)),
				layout,level,
				DEFAULT_SCORE,
				(float) (Gravity_MeteorView.DEFAULT_SPEED_Y ),
				0,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_height), 
				DEFAULT_BACKGROUND);

		init( level );
	}
	
	private void init(int level){
		float freq = (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
		amtOfTimeToPause= (long) (freq*3.2);
		this.setGravityThreshold((int) (MainActivity.getHeightPixels()/6 + Math.random() * MainActivity.getHeightPixels()/2.7));

		//give this man some flipping guns
		this.removeAllGuns();
		if(level < AttributesOfLevels.LEVELS_MED){
			Gun g1 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
					(int)getContext().getResources().getDimension(R.dimen.bullet_mid_fat_width), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
					R.drawable.bullet_laser_round_red),
					freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
			Gun g2 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
					(int)getContext().getResources().getDimension(R.dimen.bullet_mid_fat_width), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
					R.drawable.bullet_laser_round_red),
					freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
			this.addGun(g1);
			this.addGun(g2);
		}else if(Math.random() < 0.5){
			/*
			Gun g1 = new Gun_SingleShotStraight(getMyLayout(), 
					this, 
					new Bullet_Tracking(
						( (GameActivityInterface)getContext()).getProtagonist(),
						this,
						(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
						(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
						R.drawable.bullet_laser_round_red),
						freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,50);
			this.addGun(g1);
			*/
			Gun g0 = new Gun_TrackingSingle(getMyLayout(),
					((GameActivityInterface)getContext()).getProtagonist(),
					this,
					new Bullet_Basic(
						(int) getContext().getResources().getDimension(R.dimen.bullet_round_med_length),
						(int) getContext().getResources().getDimension(R.dimen.bullet_round_med_length),
						R.drawable.bullet_laser_round_red),
						freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y,
						DEFAULT_BULLET_DAMAGE,
						50);
			this.addGun(g0);
		}
		this.startShooting();
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		currentTimePaused += deltaTime;
		if(hasReachedGravityThreshold() && currentTimePaused >= amtOfTimeToPause){
			this.setSpeedY(Gravity_MeteorView.DEFAULT_SPEED_Y);
		}else if(hasReachedGravityThreshold()){
			this.setSpeedY( 0 );
		}
	}
	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.LEVELS_LOW){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.5);
			}else if(level < AttributesOfLevels.LEVELS_HIGH){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.65);				
			}else{
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.9);				
			}
		}
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR){
			probabilityWeight = getSpawningProbabilityWeight(level) / 10 ;
		}
		
		return probabilityWeight;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){			
		int numEnemies = 0;
		
		if(lvl < AttributesOfLevels.LEVELS_MED){ //choose how many diagonal enemies spawn
			numEnemies = 8;
		}else if (lvl < AttributesOfLevels.LEVELS_HIGH){
			numEnemies = 10;
		}else {
			numEnemies = 11;
		}
		
		return numEnemies;
	}
}
