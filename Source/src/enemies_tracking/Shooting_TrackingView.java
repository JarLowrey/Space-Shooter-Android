package enemies_tracking;

import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Enemy_ShooterView;
import enemies.Shooting_DiagonalMovingView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_TrackingView extends Enemy_ShooterView{

	public static final float
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .02,
			DEFAULT_BULLET_FREQ=850,
			DEFAULT_SPEED_X = (float) (MovingView.DEFAULT_SPEED_X *.9);
	
	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/50,
			DEFAULT_SCORE=100,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE  * 5),
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker,
			DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE = 1600,
			DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE = 3500;
	
	private Moving_ProjectileView viewToTrack;
	
	public Shooting_TrackingView(RelativeLayout layout,int level) {
		super(layout, level,
				DEFAULT_SCORE,
				getDefaultSpeedY(level), 
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, 
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_tracker_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_tracker_height), 
				DEFAULT_BACKGROUND);

		//set up the enemy to track the given MovingView
		viewToTrack = ((GameActivityInterface)getContext()).getProtagonist();
		setRandomXPos();
	
		//add guns
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ + 2 * DEFAULT_BULLET_FREQ * Math.random());
		Gun defaultGun = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
				R.drawable.bullet_laser_round_red),
				bulletFreq, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				DEFAULT_BULLET_DAMAGE,50);
		this.addGun(defaultGun);
		this.startShooting();
	}
	
	protected float getTrackingSpeedX(){
		final int trackPoint =(int) ( viewToTrack.getX()*2 + viewToTrack.getWidth() )/2;
		final int myPos = (int)( Shooting_TrackingView.this.getX()*2 + Shooting_TrackingView.this.getWidth() )/2;
		final int diff = trackPoint - myPos;
		float speedX = DEFAULT_SPEED_X;
		
		//set X direction
		final float pixelDistanceDelta = 5 * MainActivity.getScreenDens();
		if(Math.abs(diff) > pixelDistanceDelta){
			speedX  *= diff/Math.abs(diff);//multiply by sign of diff 
		}else{
			speedX = 0;
		}
		return speedX;	
	}
	
	@Override 
	public void restartThreads(){
		super.restartThreads();
	}

	private static float getDefaultSpeedY(int level){
		if(level< AttributesOfLevels.LEVELS_MED){
			return DEFAULT_SPEED_Y;
		}else{
			return (float) (DEFAULT_SPEED_Y * 1.1);
		}
	}

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		if( level > AttributesOfLevels.LEVELS_BEGINNER ){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * .78);
			}else{
				probabilityWeight = Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level);				
			}
		}
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR){
			probabilityWeight = getSpawningProbabilityWeight(level) / 14 ;
		}
		
		return probabilityWeight;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){		
		int numEnemies = 0;
		
		if(lvl > AttributesOfLevels.FIRST_LEVEL_LOTS_OF_TRACKERS_APPEAR){
			if(lvl < AttributesOfLevels.LEVELS_LOW){ //choose how many diagonal enemies spawn
				numEnemies = 5;
			}else if (lvl < AttributesOfLevels.LEVELS_MED){
				numEnemies = 8;
			}else {
				numEnemies = 10;
			}
		}
		
		return numEnemies;
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		Shooting_TrackingView.this.setSpeedX(getTrackingSpeedX());
		//constant speed Y
	}
}
