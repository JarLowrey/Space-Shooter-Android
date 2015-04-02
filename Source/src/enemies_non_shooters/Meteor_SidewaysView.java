package enemies_non_shooters;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static float
			DEFAULT_SPEED_X=2;
	
	public Meteor_SidewaysView(Context context) {
		super(context);
				
		float speedX=DEFAULT_SPEED_X;
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);
		this.setSpeedY(DEFAULT_SPEED_Y);

		//must spawn in middle 3/4 X of screen
		float xRand = (float) ( MainActivity.getWidthPixels()/2*Math.random() + MainActivity.getWidthPixels()/4);
		this.setX(xRand);
	}
	
	@Override 
	public void restartThreads(){
		super.restartThreads();
	}
}
