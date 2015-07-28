package enemies_non_shooters;

import android.content.Context;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static float
			DEFAULT_SPEED_X=2;
	
	public Meteor_SidewaysView(Context context,int difficulty) {
		super(context,difficulty);
				
		float speedX = (float) scaledValue(DEFAULT_SPEED_X,difficulty,XXSMALL_SCALING);
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);
	}
	
}
