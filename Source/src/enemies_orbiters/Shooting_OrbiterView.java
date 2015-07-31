package enemies_orbiters;

import android.content.Context;
import bullets.Bullet_Basic_LaserShort;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {
	
	public static final float DEFAULT_SPEED_Y = 10,
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y;
	
	public final static int DEFAULT_SCORE=100, 
			DEFAULT_ORBIT_Y = (int) (MainActivity.getHeightPixels()/3);
	
	public final static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	public final static int	DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*8;
	
	protected int howManyTimesMoved;
	protected int orbitY,orbitX;
	
	public Shooting_OrbiterView(Context context, 
			int level,
			int scoreForKilling, 
			int width,int height,int imageId) {
		super(context, level,
				scoreForKilling, 
				DEFAULT_SPEED_Y,
				0, 
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				width, height, imageId);

		//attempt to randomly place ships on screen so they do not run off of screen (currently they do run off screen a bit)
		orbitX=(int) (Math.random() *  (MainActivity.getWidthPixels()-width- 2 * orbitLengthX() ) + orbitLengthX() );
		orbitY=(int) (Math.random() *  (MainActivity.getHeightPixels()/2-height- 2 * orbitLengthY() ) + orbitLengthY() );
		
		this.setX(orbitX);
		
		init();
	}
	public Shooting_OrbiterView(Context context,int level,
			int scoreForKilling,
			float speedY,
			int collisionDamage, 
			int health, 
			float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,
			int width,int height,int imageId) {
		super(context, level,
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
		setThreshold(y);
		
		//add guns
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ + 3 * DEFAULT_BULLET_FREQ * Math.random());
		Gun defaultGun = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserShort(),
				bulletFreq, 
				DEFAULT_BULLET_SPEED_Y, 
				DEFAULT_BULLET_DAMAGE,50);
		this.addGun(defaultGun);
		this.startShooting();
	}
	public void restartThreads(){
		super.restartThreads();
	}
	
	protected abstract int orbitLengthX();
	protected abstract int orbitLengthY();
}
