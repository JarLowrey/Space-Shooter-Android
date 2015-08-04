package enemies;
 
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import android.util.Log;
import android.widget.RelativeLayout;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Shooting_SpasticView extends Enemy_ShooterView{
	
	public static int DEFAULT_BACKGROUND = R.drawable.ship_enemy_spastic,
			DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/25,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 9),
			DEFAULT_SCORE=150;
	public static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float).1;
		 
	public Shooting_SpasticView (RelativeLayout layout,int level) {
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
		//spawn in random position of the screen
		this.setX( (float) ((MainActivity.getWidthPixels()-width) *Math.random() + width/2) );
		this.setSpeedY(DEFAULT_SPEED_Y);
		this.setThreshold((int) (MainActivity.getHeightPixels()/4));

		float freq = (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());

		//override default gun
		this.removeAllGuns();
		Gun g1 = new Gun_SingleShotStraight(getMyLayout(), 
				this, 
				new Bullet_Tracking(
					( (GameActivityInterface)getContext()).getProtagonist(),
					this,
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					R.drawable.bullet_laser_round_red),
				freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,20);
		Gun g2 = new Gun_SingleShotStraight(getMyLayout(), 
				this, 
				new Bullet_Tracking(
					( (GameActivityInterface)getContext()).getProtagonist(),
					this,
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
					R.drawable.bullet_laser_round_red),
				freq, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,80);
		this.addGun(g1);
		this.addGun(g2);
		this.startShooting();
	}

	@Override
	public void reachedGravityPosition() {		
		this.reassignMoveRunnable(new KillableRunnable(){
			@Override
			public void doWork() {
				setRandomSpeed();
				
				move();
				
				ConditionalHandler.postIfAlive (this, HOW_OFTEN_TO_MOVE,Shooting_SpasticView.this);
			}
		});
	}
	
	private void setRandomSpeed(){
		//set a random speed, thus the person is "spastic"
		float ySpeed = (float) (Math.random() * DEFAULT_SPEED_Y * .8 + DEFAULT_SPEED_Y * .2);
		float xSpeed = (float) (Math.random() * DEFAULT_SPEED_X * .8 + DEFAULT_SPEED_X * .2);
		if(Math.random()  < .5){
			ySpeed *= -1;
		}
		if(Math.random() < .5){
			xSpeed*= -1;
		}
		
		//ensure object does not move off sides or top of screen
		float x = this.getX();
		float y = this.getY();
		
		y += ySpeed;
		x += xSpeed;
		
		if(y < 0){
			ySpeed *= -1;
		}
		if(x < 0 || ( x+this.getWidth() ) > MainActivity.getWidthPixels()){
			xSpeed *= -1;		
		}
		

		setSpeedY(ySpeed);
		setSpeedX(xSpeed);
	}
	
	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
//		if(level > AttributesOfLevels.LEVELS_LOW){
			probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT *10 + 
					(level/10) * AttributesOfLevels.STANDARD_PROB_WEIGHT/2);
			
//			probabilityWeight = Math.min(probabilityWeight, 2 * AttributesOfLevels.STANDARD_PROB_WEIGHT);
//		}
		return probabilityWeight;
	}

}
