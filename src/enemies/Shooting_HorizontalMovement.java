package enemies;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Shooting_HorizontalMovement extends Enemy_ShooterView{

	public final static int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/4),
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_horizontal_line;

	public final static int DEFAULT_SCORE=100,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=300,
			DEFAULT_BULLET_FREQ_INTERVAL=1000;
	public final static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	
	public Shooting_HorizontalMovement (Context context) {
		super(context,DEFAULT_SCORE,
				DEFAULT_SPEED_Y,0,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_height), 
				DEFAULT_BACKGROUND);
		
		init( (int)context.getResources().getDimension(R.dimen.ship_diagonal_width));
	}
	
	public Shooting_HorizontalMovement(Context context, int scoreForKilling,
			float projectileSpeedY,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, 
			int width, int height, int imageId) {
		super(context, scoreForKilling, projectileSpeedY,0,
				projectileDamage, projectileHealth, probSpawnBeneficialObject, width,
				height, imageId);

		init(width);
	}
	
	private void init(int width){
		this.setX((float) (MainActivity.getWidthPixels()/2-width/2.0) );
		this.setThreshold((int) MainActivity.getHeightPixels()/3);
	}
	
	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
	}

	@Override
	public void reachedGravityPosition() {
		this.setSpeedY(0);
		this.setSpeedX(DEFAULT_SPEED_X);
		
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				final float leftPos = Shooting_HorizontalMovement.this.getX();
				final float rightPos = Shooting_HorizontalMovement.this.getX()+Shooting_HorizontalMovement.this.getHeight();
				if(leftPos<0 || rightPos > MainActivity.getWidthPixels()){
					Shooting_HorizontalMovement.this.setSpeedX(Shooting_HorizontalMovement.this.getSpeedX() * -1);//reverse horizontal direction
				}
				
				move();
				ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE, Shooting_HorizontalMovement.this);
			}
		});
	}

}
