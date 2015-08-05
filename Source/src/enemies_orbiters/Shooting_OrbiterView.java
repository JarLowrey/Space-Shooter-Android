package enemies_orbiters;

import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Enemy_ShooterView;
import enemies.Shooting_PauseAndMove;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {
	
	public static final float DEFAULT_SPEED_Y = 10,
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	
	public final static int DEFAULT_SCORE=130, 
			DEFAULT_ORBIT_Y = (int) (MainActivity.getHeightPixels()/3),
			DEFAULT_HEALTH = ProtagonistView.DEFAULT_BULLET_DAMAGE * 12;
	
	private final static double TOP_PORTION_OF_SCREEN = .6;
	
	protected int howManyTimesMoved;
	protected int orbitY,orbitX;
	
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
		orbitX=(int) (Math.random() *  ( MainActivity.getWidthPixels()-width*2-orbitLengthX()*2 )/2 ) 
				+ orbitLengthX()+width ;
		orbitY=(int) (Math.random() *  ( MainActivity.getHeightPixels()*TOP_PORTION_OF_SCREEN-height*2-orbitLengthY()*2 )/2 ) 
				+ orbitLengthY()+height ;
		
		this.setX(orbitX);
		
		init();
	}
	public Shooting_OrbiterView(RelativeLayout layout,int level,
			int scoreForKilling,
			float speedY,
			int collisionDamage, 
			int health, 
			float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,
			int width,int height,int imageId) {
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
		
		this.setX(orbitX);
		
		init();
	}
	
	private void init(){

		//set entity's position
		int y = (int) -(getHeight()/2);
		setGravityThreshold(y);
		
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
	

	public static int getSpawningProbabilityWeight(int level) {
		/*
		 * little less likely to spawn than a Shooting_PauseAndMove. Since there are currently 3 types of 
		 * orbiter shooters (not including array orbiter shooters) in levelSpawner, divide weight by 3
		 * so that screen is not inundated with orbiters
		 */
		return (int) (Shooting_PauseAndMove.getSpawningProbabilityWeight(level) * 0.75) / 3;
	}
	

	public static int getSpawningProbabilityWeightForLotsOfEnemiesWave(int level){
		int probabilityWeight = 0;
	
		if(level >= AttributesOfLevels.FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR){
			probabilityWeight = getSpawningProbabilityWeight(level) / (10 * 3) ;
		}
		
		return probabilityWeight;
	}
	
	public static int getNumEnemiesInLotsOfEnemiesWave(int lvl){		
		return Shooting_PauseAndMove.getNumEnemiesInLotsOfEnemiesWave(lvl);
	}
}
