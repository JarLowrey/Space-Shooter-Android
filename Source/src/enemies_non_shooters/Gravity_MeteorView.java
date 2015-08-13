package enemies_non_shooters;

import helpers.KillableRunnable;
import levels.AttributesOfLevels;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.R;

import enemies.EnemyView;
import friendlies.ProtagonistView;

public class Gravity_MeteorView extends EnemyView{
	
	public final static int DEFAULT_SCORE=20,
			DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/25, 
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 2),
			DEFAULT_BACKGROUND=R.drawable.meteor,
			DEFAULT_ROTATION_SPEED=7;
	public final static float 
			DEFAULT_SPEED_Y = (float) (MovingView.DEFAULT_SPEED_Y * 1.85),
			DEFAULT_SPEED_X=0,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float) .01;
	
	private int direction = 1;
	private int currentRotation;
	private KillableRunnable rotateRunnable = new KillableRunnable(){
		@Override
		public void doWork() { 
			currentRotation+=DEFAULT_ROTATION_SPEED * direction;
			Gravity_MeteorView.this.setRotation(currentRotation);
			postDelayed(this,100);
		} 
	};
	
	public Gravity_MeteorView(RelativeLayout layout,int level) {
		super(layout,level,
				DEFAULT_SCORE , 
				DEFAULT_SPEED_Y, 
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				(int)layout.getContext().getResources().getDimension(R.dimen.meteor_length),
				(int)layout.getContext().getResources().getDimension(R.dimen.meteor_length), 
				DEFAULT_BACKGROUND);
	
		if(Math.random() < 0.5){direction*=-1;}
		currentRotation=0;
		//ConditionalHandler.postIfAlive(rotateRunnable, this);
		this.setRotation((float) (Math.random() * 360));
		
		//reset scaled stats. Meteors are special
		this.setSpeedY(gravitySpeedMultiplier(level,DEFAULT_SPEED_Y));
		this.setHealth(DEFAULT_HEALTH);
		this.setScoreValue(DEFAULT_SCORE);

		//spawn in middle 9/10 X of screen
		setRandomXPos();
	}
	
	@Override 
	public void restartThreads(){
		post(rotateRunnable);
		super.restartThreads();
	}

	protected static float gravitySpeedMultiplier(int level,float defaultSpeed){
		float speed = defaultSpeed;
		if (level > AttributesOfLevels.LEVELS_LOW && (level/5)%2 == 0){
			speed *= 1.05;
		}
		return speed;
	}

	public static int getSpawningProbabilityWeightOfMeteorShowers(int level) {
		//start at 1/3 giant meteor, decrease a little every 5 levels until equal to 1/5 giant meteor
		//NOTE: Since there are 3 types of meteor waves (left, right, middle) the probability of any wave is 3x any individual weight
		int probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT / 3.0 - 
				(level/5) * AttributesOfLevels.STANDARD_PROB_WEIGHT/10.0);
		
		probabilityWeight = Math.max(probabilityWeight, AttributesOfLevels.STANDARD_PROB_WEIGHT / 5);
		
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightOfGiantMeteors(int level) {
		//ITS THE GIANT METEOR! The standard for spawning probability weights
		//start at 1x giant meteor, decrease a little every 5 levels until equal to 1/3x of original giant meteor weight
		
		int probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT  - 
				(level/5) * AttributesOfLevels.STANDARD_PROB_WEIGHT/5.0);
		
		probabilityWeight = Math.max(probabilityWeight, AttributesOfLevels.STANDARD_PROB_WEIGHT / 3);

		
		return probabilityWeight;
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		//do nothing, constant speed in X and Y after instantiation
	}
}
