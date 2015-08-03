package enemies_non_shooters;

import android.content.Context;
import android.widget.RelativeLayout;

public class Meteor_SidewaysView extends Gravity_MeteorView{
	
	public final static float
			DEFAULT_SPEED_X=2;
	
	public Meteor_SidewaysView(RelativeLayout layout,int level) {
		super(layout,level);
				
		float speedX = gravitySpeedMultiplier(level,DEFAULT_SPEED_X);
		if(Math.random()<0.5){speedX *= -1;}
		this.setSpeedX(speedX);
	}
	
}
