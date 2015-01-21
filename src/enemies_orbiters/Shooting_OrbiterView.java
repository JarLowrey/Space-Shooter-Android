package enemies_orbiters;

import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {

	//NEEDS TO BE INSTANTIATED IN CHILD CLASS
	public Runnable orbitingRunnable;
	
	public final static int DEFAULT_SCORE=100, 
			DEFAULT_ORBIT_Y = (int) (MainActivity.getHeightPixels()/3);
	
	public final static float DEFAULT_SPEED_Y=5,
			DEFAULT_SPEED_X=5,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	public final static int	DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*8;
		
	private boolean hasBegunOrbiting=false;
	protected int howManyTimesMoved;
	protected int orbitY,orbitX;
	
	@Override
	public boolean moveDirection(int direction){
		boolean atThreshold = super.moveDirection(direction);
		
		if(atThreshold && !hasBegunOrbiting){
			stopGravity();
			hasBegunOrbiting=true;
			this.beginOrbit();
		};
		
		return atThreshold;		
	}
	

	public Shooting_OrbiterView(Context context,int scoreForKilling, 
			int width,int height,int imageId) {
		super(context, scoreForKilling, DEFAULT_SPEED_Y,
				DEFAULT_SPEED_X, DEFAULT_COLLISION_DAMAGE, DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, width, height, imageId);

		//defeault orbit location
		orbitX=(int) (MainActivity.getWidthPixels()/2-width/2);
		orbitY=DEFAULT_ORBIT_Y;
		
		this.setX(orbitX);
		
	}
	public Shooting_OrbiterView(Context context,int scoreForKilling,float speedY, float speedX,int collisionDamage, 
			int health, 
			float probSpawnBeneficialObjecyUponDeath,int orbitPixelX,int orbitPixelY,int width,int height,int imageId) {
		super(context, scoreForKilling, speedY,
				speedX, collisionDamage, health,probSpawnBeneficialObjecyUponDeath, width, height, imageId);

		//defeault orbit location
		orbitX=orbitPixelX;
		orbitY=orbitPixelY;
		
		this.setX(orbitX);
		
	}
	public void restartThreads(){
		if(hasBegunOrbiting){
			beginOrbit();
		}
		super.restartThreads();
	}
	
	public void beginOrbit(){
		ConditionalHandler.postIfAlive(orbitingRunnable, this);
	}
	public void endOrbit(){
		this.removeCallbacks(orbitingRunnable);
	}

}
