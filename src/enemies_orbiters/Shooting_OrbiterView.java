package enemies_orbiters;

import android.content.Context;
import enemies.Enemy_ShooterView;

public abstract class Shooting_OrbiterView extends Enemy_ShooterView {
	
	public final static int DEFAULT_SCORE=10,
			DEFAULT_BULLET_FREQ_INTERVAL=1000;
	public final static double DEFAULT_SPEED_Y=5,
			DEFAULT_SPEED_X=5,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=100,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10;
	
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
	

	public Shooting_OrbiterView(Context context,int scoreForKilling,double speedY, double speedX,double collisionDamage, 
			double health, 
			float heightView,float widthView,
			double probSpawnBeneficialObjecyUponDeath) {
		super(context, scoreForKilling, speedY,
				speedX, collisionDamage, health,probSpawnBeneficialObjecyUponDeath);

	}
	
	public void restartThreads(){
		if(hasBegunOrbiting){
			beginOrbit();
		}
		super.restartThreads();
	}
	
	public abstract void beginOrbit();
	public abstract void endOrbit();

}
