package enemies;
 
import support.KillableRunnable;
import android.content.Context;
import bullets.Bullet_Basic_LaserLong;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_PauseAndMove extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_pause_and_shoot,
			DEFAULT_HEALTH=ProtagonistView.UPGRADE_BULLET_DAMAGE*5,
			DEFAULT_SCORE=100;
	public static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private long amtOfTimeToPause;
	
	public Shooting_PauseAndMove (Context context) {
		super(context,DEFAULT_SCORE,
				DEFAULT_SPEED_Y,0,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_width),
				(int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_height), 
				DEFAULT_BACKGROUND);

		init( (int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_width) );
	}
	
	public Shooting_PauseAndMove(Context context, int scoreForKilling,
			float projectileSpeedY,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, int width, int height, int imageId) {
		super(context, scoreForKilling, projectileSpeedY, 0,
				projectileDamage, projectileHealth, probSpawnBeneficialObject, width,
				height, imageId);
		
		init(width);
	}
	
	private void init(int width){
		//spawn anywhere in X on screen
		float xRand = (float) ((MainActivity.getWidthPixels()-width)*Math.random());
		this.setX(xRand);
		
		float freq = getShootingFreq();
		amtOfTimeToPause= (long) (freq*3.2);
		this.setThreshold((int) (MainActivity.getHeightPixels()/6 + Math.random() * MainActivity.getHeightPixels()/2.7));

		//override default gun
		this.removeAllGuns();
		Gun g1 = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),
				freq, DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),
				freq, DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
		this.addGun(g1);
		this.addGun(g2);
		this.startShooting();
	}
	
	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
	}

	@Override
	public void reachedGravityPosition() {
		killMoveRunnable();
		
		this.postDelayed(new KillableRunnable(){
			@Override
			public void doWork() {
				setThreshold((int) MainActivity.getHeightPixels()); 
				reviveMoveRunnable();
			}
		},this.amtOfTimeToPause);
	}

}
