package enemies_non_shooters;

import support.ConditionalHandler;
import abstract_parents.MovingView;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;

public class Gravity_MeteorView extends EnemyView{
	
	public final static int DEFAULT_SCORE=20,
			DEFAULT_BACKGROUND=R.drawable.meteor,
			DEFAULT_ROTATION_SPEED=7;
	public final static double DEFAULT_SPEED_Y=7,
			DEFAULT_SPEED_X=0,
			DEFAULT_COLLISION_DAMAGE=12, 
			DEFAULT_HEALTH=40,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.01;
	
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
	
	public Gravity_MeteorView(Context context,int score,double speedY,
			double collisionDamage,double health,double probSpawnBeneficialObjectOnDeath) {
		super(context,score,speedY, DEFAULT_SPEED_X ,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);
				
		if(Math.random() < 0.5){direction*=-1;}
		currentRotation=0;
		ConditionalHandler.postIfAlive(rotateRunnable, this);
		
		//set image background
		this.setImageResource(R.drawable.meteor);
		
		//set image width,length
		int len=(int)this.getResources().getDimension(R.dimen.meteor_length);
		this.setLayoutParams(new LayoutParams(len,len));
		
		//set initial position of View
		float xRand = (float) ((MainActivity.getWidthPixels()-len)*Math.random());
		this.setX(xRand);
		this.setY(0);
		
	}
	
	@Override 
	public void restartThreads(){
		ConditionalHandler.postIfAlive(rotateRunnable, this);
		super.restartThreads();
	}
}
