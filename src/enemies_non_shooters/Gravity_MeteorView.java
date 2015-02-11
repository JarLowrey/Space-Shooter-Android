package enemies_non_shooters;

import parents.MovingView;
import support.ConditionalHandler;
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
	public final static float DEFAULT_SPEED_Y=7,
			DEFAULT_SPEED_X=0,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .01;
	
	private int direction = 1;
	private int currentRotation;
	private Runnable rotateRunnable = new Runnable(){
		@Override
		public void run() { 
			currentRotation+=DEFAULT_ROTATION_SPEED * direction;
			Gravity_MeteorView.this.setRotation(currentRotation);
			ConditionalHandler.postIfAlive(this,MovingView.HOW_OFTEN_TO_MOVE,Gravity_MeteorView.this);
		} 
	};
	
	public Gravity_MeteorView(Context context) {
		super(context,DEFAULT_SCORE, DEFAULT_SPEED_Y, DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				(int)context.getResources().getDimension(R.dimen.meteor_length),
				(int)context.getResources().getDimension(R.dimen.meteor_length), 
				DEFAULT_BACKGROUND);
				
		if(Math.random() < 0.5){direction*=-1;}
		currentRotation=0;
		ConditionalHandler.postIfAlive(rotateRunnable, this);
		
		//spawn anywhere in X on screen
		float xRand = (float) ((MainActivity.getWidthPixels()-context.getResources().getDimension(R.dimen.meteor_length))*Math.random());
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
