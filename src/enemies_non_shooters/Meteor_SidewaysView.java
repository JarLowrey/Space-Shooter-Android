package enemies_non_shooters;

import com.jtronlabs.to_the_moon.MainActivity;

import parents.MovingView;
import support.ConditionalHandler;
import android.content.Context;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static float DEFAULT_SPEED_Y=7*MainActivity.getScreenDens(),
			DEFAULT_SPEED_X=(float) (1.5*MainActivity.getScreenDens());
	
	private Runnable moveSideways = new Runnable(){

		@Override
		public void run() {
			Meteor_SidewaysView.this.moveDirection(MovingView.SIDEWAYS);
			ConditionalHandler.postIfAlive(this, MovingView.HOW_OFTEN_TO_MOVE,Meteor_SidewaysView.this);
		}
		
	};
	
	public Meteor_SidewaysView(Context context) {
		super(context);
				
		float speedX=DEFAULT_SPEED_X;
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);
		this.setSpeedY(DEFAULT_SPEED_Y);

		//must spawn in middle 3/4 X of screen
		float xRand = (float) ( MainActivity.getWidthPixels()/2*Math.random() + MainActivity.getWidthPixels()/4);
		this.setX(xRand);
		
		ConditionalHandler.postIfAlive(moveSideways, this);
	}
	
	@Override 
	public void restartThreads(){
		ConditionalHandler.postIfAlive(moveSideways,HOW_OFTEN_TO_MOVE, this);
		super.restartThreads();
	}
}
