package enemies_non_shooters;

import levels.AttributesOfLevels;
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import parents.MovingView;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;
import friendlies.ProtagonistView;

public class Gravity_MeteorView extends EnemyView{
	
	public final static int DEFAULT_SCORE=20,
			DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/25, 
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE),
			DEFAULT_BACKGROUND=R.drawable.meteor,
			DEFAULT_ROTATION_SPEED=7;
	public final static float 
			DEFAULT_SPEED_X=0,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH = (float) .01;
	
	private int direction = 1;
	private int currentRotation;
	private KillableRunnable rotateRunnable = new KillableRunnable(){
		@Override
		public void doWork() { 
			currentRotation+=DEFAULT_ROTATION_SPEED * direction;
			Gravity_MeteorView.this.setRotation(currentRotation);
			ConditionalHandler.postIfAlive(this,2*MovingView.HOW_OFTEN_TO_MOVE,Gravity_MeteorView.this);
		} 
	};
	
	public Gravity_MeteorView(Context context,int level) {
		super(context,level,
				DEFAULT_SCORE , 
				DEFAULT_SPEED_Y, 
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				(int)context.getResources().getDimension(R.dimen.meteor_length),
				(int)context.getResources().getDimension(R.dimen.meteor_length), 
				DEFAULT_BACKGROUND);
				
		if(Math.random() < 0.5){direction*=-1;}
		currentRotation=0;
		//ConditionalHandler.postIfAlive(rotateRunnable, this);
		this.setRotation((float) (Math.random() * 360));
		
		this.setSpeedY(gravitySpeedMultiplier(level,DEFAULT_SPEED_Y));

		//spawn in middle 9/10 X of screen
		final float xRand = (float) ( MainActivity.getWidthPixels()* .8 *Math.random() + MainActivity.getWidthPixels()*.1);
		this.setX(xRand);
	}
	
	@Override 
	public void restartThreads(){
		ConditionalHandler.postIfAlive(rotateRunnable, this);
		super.restartThreads();
	}

	@Override
	public void reachedGravityPosition() {
		removeGameObject();
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
		int probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 3.0 - 
				(level/5) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/10.0);
		
		probabilityWeight = Math.max(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 5);
		
		return probabilityWeight;
	}

	public static int getSpawningProbabilityWeightOfGiantMeteors(int level) {
		//ITS THE GIANT METEOR! The standard for spawning probability weights
		int probabilityWeight = AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR ; 
		return probabilityWeight;
	}
}
