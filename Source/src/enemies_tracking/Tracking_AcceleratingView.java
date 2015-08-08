package enemies_tracking;

import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;

public class Tracking_AcceleratingView extends Shooting_TrackingView{

	public Tracking_AcceleratingView(RelativeLayout layout, int level) {
		super(layout, level);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateViewSpeed(long millisecondsSinceLastSpeedUpdate){
		super.updateViewSpeed(millisecondsSinceLastSpeedUpdate);
		
		Tracking_AcceleratingView.this.setSpeedY(
				(float) (Tracking_AcceleratingView.this.getSpeedY()+MovingView.DEFAULT_SPEED_Y / 30));
	}
}
