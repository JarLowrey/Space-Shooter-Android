package enemies_orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;

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
	
	public Shooting_OrbiterView(Context context, int difficulty,
			int scoreForKilling, 
			int width,int height,int imageId) {
		super(context, 
				scoreForKilling, 
				DEFAULT_SPEED_Y,
				0, 
				(int) scaledValue(DEFAULT_COLLISION_DAMAGE,difficulty,SMALL_SCALING), 
				(int) scaledValue(DEFAULT_HEALTH,difficulty,SMALL_SCALING),
				(int) scaledValue(DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,difficulty,SMALL_SCALING), 
				width, height, imageId);

		//attempt to randomly place ships on screen so they do not run off of screen (currently they do run off screen a bit)
		orbitX=(int) (Math.random() *  (MainActivity.getWidthPixels()-width- 2 * orbitLengthX() ) + orbitLengthX() );
		orbitY=(int) (Math.random() *  (MainActivity.getHeightPixels()/2-height- 2 * orbitLengthY() ) + orbitLengthY() );
		
		this.setX(orbitX);
		
	}
	public Shooting_OrbiterView(Context context,int scoreForKilling,float speedY,int collisionDamage, 
			int health, 
			float probSpawnBeneficialObjecyUponDeath,int orbitPixelX,int orbitPixelY,int width,int height,int imageId) {
		super(context, 
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
		
	}
	
	private void init(){

		//set entity's position
		int x = (int) (Math.random() * (MainActivity.getWidthPixels() - orbitLengthX() ));
		int y = (int) (Math.random() * (MainActivity.getHeightPixels()/2 - orbitLengthY()));
		setX(x);
		setThreshold(y);
	}
	public void restartThreads(){
		super.restartThreads();
	}

	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
	}
	
	protected abstract int orbitLengthX();
	protected abstract int orbitLengthY();
}
