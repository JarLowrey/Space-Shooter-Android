package enemies_non_shooters;

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
	
	public Gravity_MeteorView(Context context,int difficulty) {
		super(context,
				(int) scaledValue(DEFAULT_SCORE,difficulty,SMALL_SCALING) , 
				(float) scaledValue(DEFAULT_SPEED_Y,difficulty,XXSMALL_SCALING), 
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				(int)context.getResources().getDimension(R.dimen.meteor_length),
				(int)context.getResources().getDimension(R.dimen.meteor_length), 
				DEFAULT_BACKGROUND);
				
		if(Math.random() < 0.5){direction*=-1;}
		currentRotation=0;
		ConditionalHandler.postIfAlive(rotateRunnable, this);
		//this.setRotation((float) (Math.random() * 360));

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
}
