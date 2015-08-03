package enemies;
 
import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;
import helpers.KillableRunnable;

public class Shooting_PauseAndMove extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_pause_and_shoot,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 8.5),
			DEFAULT_SCORE=130;
	public static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private long amtOfTimeToPause; 
	 
	public Shooting_PauseAndMove (RelativeLayout layout,int level) {
		super(layout,level,
				DEFAULT_SCORE,
				DEFAULT_SPEED_Y,
				0,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_height), 
				DEFAULT_BACKGROUND);

		init( (int)getContext().getResources().getDimension(R.dimen.ship_pause_and_shoot_width) );
	}
	
	private void init(int width){
		//spawn anywhere in X on screen
		float xRand = (float) ((MainActivity.getWidthPixels()-width)*Math.random());
		this.setX(xRand);
		
		float freq = (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
		amtOfTimeToPause= (long) (freq*3.2);
		this.setThreshold((int) (MainActivity.getHeightPixels()/6 + Math.random() * MainActivity.getHeightPixels()/2.7));

		//override default gun
		this.removeAllGuns();
		Gun g1 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red),
				freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
				R.drawable.bullet_laser_round_red),
				freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
		this.addGun(g1);
		this.addGun(g2);
		this.startShooting();
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
	
	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		if(level > AttributesOfLevels.LEVELS_LOW){
			probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT / 3 + 
					(level/10) * AttributesOfLevels.STANDARD_PROB_WEIGHT/2);
			
			probabilityWeight = Math.min(probabilityWeight, 2 * AttributesOfLevels.STANDARD_PROB_WEIGHT);
		}
		return probabilityWeight;
	}

}
