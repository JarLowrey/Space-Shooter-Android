package enemies_tracking;

import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;

import enemies_non_shooters.Gravity_MeteorView;

public class Tracking_AcceleratingView extends Shooting_TrackingView{

	public Tracking_AcceleratingView(RelativeLayout layout, int level) {
		super(layout, level);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateViewSpeed(long deltaTime){
		super.updateViewSpeed(deltaTime);
		
		Tracking_AcceleratingView.this.setSpeedY(
				(float) (Tracking_AcceleratingView.this.getSpeedY()+Gravity_MeteorView.DEFAULT_SPEED_Y *0.01 * 1.0/deltaTime));
	}
}
