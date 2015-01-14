package enemies_non_shooters;

import parents.MovingView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static int DEFAULT_SCORE=30,
			DEFAULT_BACKGROUND=R.drawable.meteor,
			DEFAULT_ROTATION_SPEED=7;
	public final static double DEFAULT_SPEED_Y=6,
			DEFAULT_SPEED_X=6,
			DEFAULT_COLLISION_DAMAGE=12, 
			DEFAULT_HEALTH=40,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.02;
	
	private Runnable moveSideways = new Runnable(){

		@Override
		public void run() {
			Meteor_SidewaysView.this.moveDirection(MovingView.SIDEWAYS);
			ConditionalHandler.postIfAlive(this, MovingView.HOW_OFTEN_TO_MOVE,Meteor_SidewaysView.this);
		}
		
	};
	
	public Meteor_SidewaysView(Context context,int score,double speedY,double speedX,
			double collisionDamage,double health,double probSpawnBeneficialObjectOnDeath) {
		super(context,score,speedY,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);
				
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);

		ConditionalHandler.postIfAlive(moveSideways, this);
	}
	
	@Override 
	public void restartThreads(){
		ConditionalHandler.postIfAlive(moveSideways,HOW_OFTEN_TO_MOVE, this);
		super.restartThreads();
	}
}
