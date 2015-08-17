package enemies_tracking;

import android.widget.RelativeLayout;
import enemies_non_shooters.Gravity_MeteorView;

public class Tracking_AcceleratingView extends Shooting_TrackingView{

	public Tracking_AcceleratingView(RelativeLayout layout, int level) {
		super(layout, level);

		this.stopShooting();
	}

	@Override
	public void updateViewSpeed(long deltaTime){
		super.updateViewSpeed(deltaTime);
		
		Tracking_AcceleratingView.this.setSpeedY(
				(float) (Tracking_AcceleratingView.this.getSpeedY()+Gravity_MeteorView.DEFAULT_SPEED_Y * 1.0/deltaTime));
	}

	public static int getSpawningProbabilityWeight(int level) {
		return (int) (Shooting_TrackingView.getSpawningProbabilityWeight(level) / 2); 
	}
}
