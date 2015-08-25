package enemies;
 
import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import android.util.Log;
import android.widget.RelativeLayout;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies_non_shooters.Gravity_MeteorView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_SpasticView extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_spastic,
			DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/25,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 9),
			DEFAULT_SCORE=150;
	public static float 
			DEFAULT_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y * 0.6),
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private static final long INTERVAL_WITH_ONE_SET_SPEED = 150;
	private long timeSinceLastRandomSpeedSet =0;
		 
	public Shooting_SpasticView (RelativeLayout layout,int level) {
		super(layout,level,
				DEFAULT_SCORE,
				DEFAULT_SPEED_Y,
				0,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_height), 
				DEFAULT_BACKGROUND);

		init( (int)getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width),level );
	}
	
	private void init(int width,int level){
		//spawn in random position of the screen
		setRandomXPos();
		this.setSpeedY(DEFAULT_SPEED_Y);
		this.setGravityThreshold((int) (MainActivity.getHeightPixels()/4));

		float bulletFreq = (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());

		//assign guns
		this.removeAllGuns();
		Gun g1 = new Gun_SingleShotStraight(getMyLayout(), 
				this, 
				new Bullet_Tracking(
					( (GameActivityInterface)getContext()).getProtagonist(),
					this,
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					R.drawable.bullet_laser_round_red),
					bulletFreq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getMyLayout(), 
				this, 
				new Bullet_Tracking(
					( (GameActivityInterface)getContext()).getProtagonist(),
					this,
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					R.drawable.bullet_laser_round_red),
					bulletFreq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
		this.addGun(g1);
		this.addGun(g2);
		
		if(level > AttributesOfLevels.LEVELS_MED){
			Gun g0 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Tracking(
					( (GameActivityInterface)getContext()).getProtagonist(),
					this,
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_red),
					(float) (bulletFreq * 0.5), 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Bullet_Interface.WEAK_BULLET_DMG,20);
			this.addGun(g0);
		}
		this.startShooting();
	}
	
	private void setRandomSpeed(long deltaTime){
		timeSinceLastRandomSpeedSet = 0;
		
		//set a random speed, thus the person is "spastic"
		float ySpeed = (float) (Math.random() * DEFAULT_SPEED_Y * .8 + DEFAULT_SPEED_Y * .2);
		float xSpeed = (float) (Math.random() * DEFAULT_SPEED_X * .8 + DEFAULT_SPEED_X * .2);
		if(Math.random()  < .5){
			ySpeed *= -1;
		}
		if(Math.random() < .5){
			xSpeed*= -1;
		}
		
		//ensure object does not move off sides or top of screen
		float x = this.getX();
		float y = this.getY();
		
		y += ySpeed * deltaTime;
		x += xSpeed * deltaTime;
		
		if(y <= 0) {
			ySpeed *= -1;
		}
		if(x <= 0 || ( x+this.getWidth() ) >= MainActivity.getWidthPixels()){
			xSpeed *= -1;		
		}
		

		setSpeedY(ySpeed);
		setSpeedX(xSpeed);
	}

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.FIRST_LEVEL_SPASTICS_APPEAR){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.3);
			}else if(level < AttributesOfLevels.LEVELS_HIGH){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.5);				
			}else{
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * .75);				
			}
		}
		
		return probabilityWeight;
	}
	

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_SPASTICS_APPEAR){
			probabilityWeight = getSpawningProbabilityWeight(level) / 15 ;
		}
		
		return probabilityWeight;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){		
		int numEnemies = 0;
		
		if(lvl > AttributesOfLevels.FIRST_LEVEL_SPASTICS_APPEAR){
			if(lvl < AttributesOfLevels.LEVELS_LOW){ //choose how many diagonal enemies spawn
				numEnemies = 4;
			}else if (lvl < AttributesOfLevels.LEVELS_MED){
				numEnemies = 5;
			}else {
				numEnemies = 6;
			}
		}
		
		return numEnemies;
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		if(hasReachedGravityThreshold()){
			timeSinceLastRandomSpeedSet += deltaTime;
			
			float x = (float) (this.getX() +this.getSpeedX()*deltaTime);
			float y = (float) (this.getY() +this.getSpeedY()*deltaTime);

			if(timeSinceLastRandomSpeedSet >= INTERVAL_WITH_ONE_SET_SPEED){
				Log.d("lowrey","updated spastic view speed");
				setRandomSpeed(deltaTime);
			}
			else if( y <=0 ||
					x <= 0 || ( x+this.getWidth() ) >= MainActivity.getWidthPixels() ){//do not let go offscreen it it happens in between speed updates
				setRandomSpeed(deltaTime);
			}
		}
	}

}
