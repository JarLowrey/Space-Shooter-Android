package enemies;

import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_PauseAndMove extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND=0,
			DEFAULT_HEALTH=ProtagonistView.UPGRADE_BULLET_DAMAGE*5,
			DEFAULT_SCORE=100;
	public static float DEFAULT_SPEED_Y=2,
			DEFAULT_SPEED_X=0,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private long amtOfTimeToPause;
	
	public Shooting_PauseAndMove (Context context) {
		super(context,DEFAULT_SCORE,
				DEFAULT_SPEED_Y,DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_height), 
				DEFAULT_BACKGROUND);

		this.setThreshold((int) MainActivity.getHeightPixels()/3);
		amtOfTimeToPause = 3000;
	}
	
	public Shooting_PauseAndMove(Context context, int scoreForKilling,
			float projectileSpeedY, float projectileSpeedX,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, int width, int height, int imageId, long howLongToPause) {
		super(context, scoreForKilling, projectileSpeedY, projectileSpeedX,
				projectileDamage, projectileHealth, probSpawnBeneficialObject, width,
				height, imageId);

		this.setThreshold((int) MainActivity.getHeightPixels()/3);
		amtOfTimeToPause = howLongToPause;
	}

	@Override
	public float getShootingFreq() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reachedGravityPosition() {
		this.postDelayed(new KillableRunnable(){
			@Override
			public void doWork() {
				startGravity();
			}
		},this.amtOfTimeToPause);
	}

}
