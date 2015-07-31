package enemies;
 
import levels.AttributesOfLevels;
import android.content.Context;
import bullets.Bullet_Basic;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;
import helpers.KillableRunnable;

public class Shooting_PauseAndMove extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_pause_and_shoot,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE*4.5),
			DEFAULT_SCORE=100;
	public static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
	
	private long amtOfTimeToPause; 
	 
	public Shooting_PauseAndMove (Context context,int level) {
		super(context,level,
				DEFAULT_SCORE,
				DEFAULT_SPEED_Y,
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_width),
				(int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_height), 
				DEFAULT_BACKGROUND);

		init( (int)context.getResources().getDimension(R.dimen.ship_pause_and_shoot_width) );
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
		Gun g1 = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.laser_long_width), 
				(int)getContext().getResources().getDimension(R.dimen.laser_long_height), 
				R.drawable.laser_rectangular_enemy),
				freq, DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.laser_long_width), 
				(int)getContext().getResources().getDimension(R.dimen.laser_long_height), 
				R.drawable.laser_rectangular_enemy),
				freq, DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
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
		//start at 1/2 giant meteor, increase a little every 5 levels until equal to 2x giant meteor
		int probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 3 + 
				(level/5) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/2);
		
		probabilityWeight = Math.min(probabilityWeight, 2 * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR);
		
		return probabilityWeight;
	}

}
