package enemies_non_shooters;

import android.content.Context;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static float
			DEFAULT_SPEED_X=2;
	
	public Meteor_SidewaysView(Context context,int level) {
		super(context,level);
				
		float speedX = gravitySpeedMultiplier(level,DEFAULT_SPEED_X);
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);
	}
	
}
