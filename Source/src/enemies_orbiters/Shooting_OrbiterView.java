package enemies_orbiters;

import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Enemy_ShooterView;
import enemies.Shooting_PauseAndMove;
import enemies_non_shooters.Gravity_MeteorView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {
	public static final long DEFAULT_ORBIT_TIME = 3000;
	
	public static final float 
			DEFAULT_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y * .6),
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y,			
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	
	public final static int DEFAULT_SCORE=130, 
			DEFAULT_ORBIT_Y = (int) (MainActivity.getHeightPixels()/3),
			DEFAULT_HEALTH = ProtagonistView.DEFAULT_BULLET_DAMAGE * 12;
	
	private final static double TOP_PORTION_OF_SCREEN = .5;
	protected final static int HOW_MANY_TIMES_LESS_LIKELY_TO_SPAWN_MANY_ORBITERS = 12;
	
	protected int orbitY,orbitX;
	protected long currentRevolutionTime = 0,
		orbitRevolutionTime;
	
	public Shooting_OrbiterView(RelativeLayout layout, 
			int level,
			int scoreForKilling, 
			int width,int height,int imageId) {
		super(layout, level,
				scoreForKilling, 
				DEFAULT_SPEED_Y,
				0, 
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				width, height, imageId);

		/*
		 * randomly place ships on screen so they do not run off of screen
		 * Screen is divided as follows
		 * 	X:    	|width |radius|   free space   |radius|width |
		 *  Y:		|height|radius|   free space   |radius|height|  restricted space  |
		*/
		orbitX = (int) (Math.random() * MainActivity.getWidthPixels() * .75 + MainActivity.getWidthPixels() * .125);//whatever, just put them in the middle part of screen
		orbitY = (int) (Math.random() * MainActivity.getHeightPixels() * TOP_PORTION_OF_SCREEN );
//		orbitY=(int) (Math.random() *  ( MainActivity.getHeightPixels()*TOP_PORTION_OF_SCREEN-height*2-orbitLengthY() )/2 ) 
//				+ orbitLengthY()+height/2 ;
		
		this.setGravityThreshold(orbitY);
		
		this.setX(orbitX);

		orbitRevolutionTime = DEFAULT_ORBIT_TIME;
		
		init();
	}
	public Shooting_OrbiterView(RelativeLayout layout,int level,
			int scoreForKilling,
			float speedY,
			int collisionDamage, 
			int health, 
			float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,
			int width,int height,int imageId,
			long orbitTime) {
		super(layout, level,
				scoreForKilling, 
				speedY,
				0, 
				collisionDamage, 
				health,
				probSpawnBeneficialObjecyUponDeath, 
				width, height, imageId);

		//defeault orbit location
		orbitX=orbitPixelX-width/2;
		orbitY=orbitPixelY;

		this.setGravityThreshold(orbitY);
		
		this.setX(orbitX);

		orbitRevolutionTime = orbitTime;
		
		init();
	}
	
	@Override
	public void updateViewSpeed(long deltaTime){
		if(hasReachedGravityThreshold()){
			currentRevolutionTime += deltaTime;
			currentRevolutionTime = currentRevolutionTime % orbitRevolutionTime;
		}
	}
	
	private void init(){

		//set entity's position
		int y = (int) -(getHeight()/2);
		
		//add guns 
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ*1.5 + 3 * DEFAULT_BULLET_FREQ * Math.random());
		this.removeAllGuns();
		Gun g1 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_mid_fat_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red),
				bulletFreq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_mid_fat_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red),
				bulletFreq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
		this.addGun(g1);
		this.addGun(g2);
		this.startShooting();
	}
	
	public void restartThreads(){
		super.restartThreads();
	}
	
	protected abstract int orbitLengthX();
	protected abstract int orbitLengthY();
	

	protected static int orbiterProbWeight(int level) {
		int numOrbitersAvailable = 0;
		
		//maintain a constant spawnProbWeight of all different types of orbiters
		if(level >= AttributesOfLevels.FIRST_LEVEL_TRIANGLE_ORBITERS_APPEAR){ numOrbitersAvailable++; }
		if(level >= AttributesOfLevels.FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR){	numOrbitersAvailable++;	}
		if(level >= AttributesOfLevels.FIRST_LEVEL_RECTANGLE_ORBITERS_APPEAR){	numOrbitersAvailable++;	}
		if(numOrbitersAvailable == 0){numOrbitersAvailable = 1;}//do not divide by 0
		
		return (int) ((Shooting_PauseAndMove.getSpawningProbabilityWeight(level) * 0.75) / (12 * numOrbitersAvailable)) ;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){			
		int numEnemies = 0;
		
		if(lvl < AttributesOfLevels.LEVELS_MED){ //choose how many diagonal enemies spawn
			numEnemies = 6;
		}else if (lvl < AttributesOfLevels.LEVELS_HIGH){
			numEnemies = 8;
		}else {
			numEnemies = 11;
		}
		
		return numEnemies;
	}
}
