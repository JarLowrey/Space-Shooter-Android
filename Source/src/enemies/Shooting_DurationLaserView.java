package enemies;

import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.BulletView;
import bullets.Bullet_Duration;
import bullets.Bullet_HasDurationView;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_DurationLaserView extends Enemy_ShooterView{

	public static final int DEFAULT_BACKGROUND_ID = R.drawable.ship_enemy_duration_laser,
			DEFAULT_SCORE = 200,
			DEFAULT_HEALTH = ProtagonistView.DEFAULT_BULLET_DAMAGE * 12,
			DEFAULT_BULLET_DAMAGE = ProtagonistView.DEFAULT_HEALTH / 70,
			DEFAULT_BULLET_FREQ = 3000;
	
	public static final float DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .1;
	
	public Shooting_DurationLaserView(RelativeLayout layout, int level) {
		super(layout, 
				level, 
				DEFAULT_SCORE, 
				DEFAULT_SPEED_Y, 
				0,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH, 
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_enemy_duration_laser_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_enemy_duration_laser_height), 
				DEFAULT_BACKGROUND_ID);

		setRandomXPos();
		this.setGravityThreshold((int) (MainActivity.getHeightPixels()/3 + Math.random()));

		 //gun that shoots duration bullets
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ * Math.random() + DEFAULT_BULLET_FREQ * .75);
		final int X_POS_ON_SHOOTER = 50;
		Gun defaultGun = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Duration(
				(int)getContext().getResources().getDimension(R.dimen.bullet_xskinny_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red,
				(long) (bulletFreq / 3),
				X_POS_ON_SHOOTER),
			bulletFreq, 
			Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
			DEFAULT_BULLET_DAMAGE,
			X_POS_ON_SHOOTER);
		this.addGun(defaultGun);
		this.startShooting();
		
	}

	@Override
	protected void reachedGravityPosition() {
		reassignMoveRunnable(null);
		
		//if level > MED rotate randomly within a threshold
		
		//if level > high rotate so facing protagonist
	}
	
	@Override
	public void setRotation(float rotation){
		super.setRotation(rotation);
		
		for(BulletView b : myBullets){
			if(b instanceof Bullet_HasDurationView){
				b.setRotation(rotation);
			}
		}
	}
	

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		
		if(level > AttributesOfLevels.LEVELS_LOW){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.2);
			}else if(level < AttributesOfLevels.LEVELS_HIGH){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * 0.4);				
			}else{
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * .7);				
			}
		}
		return probabilityWeight;
	}
}
