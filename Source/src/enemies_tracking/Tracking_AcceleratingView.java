package enemies_tracking;

import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.games.Game;
import com.jtronlabs.space_shooter.GameLoop;

import enemies_non_shooters.Gravity_MeteorView;

public class Tracking_AcceleratingView extends Shooting_TrackingView{


	public Tracking_AcceleratingView(RelativeLayout layout, int level) {
		super(layout, level);

		this.stopShooting();
	}

	@Override
	public void updateViewSpeed(long deltaTime){
		super.updateViewSpeed(deltaTime);

		//keep in mind that this speed update will be occurring every FixedUpdate of the Physics Frame.
		//The math show that over 1s, the speed will be 2x its original
		//	#physics frames/second = 1000ms/GameLoop.timeBetweenPhysicsFramesInMilliseconds()
		//	X + #physics frames/second * Y = 1.75X where X= original speed, Y = increment required each iteration of updateViewSpeed
		//	Y = 0.75X * GameLoop.timeBetweenPhysicsFramesInMilliseconds() / 1000

		Tracking_AcceleratingView.this.setSpeedY(
				(float) (Tracking_AcceleratingView.this.getSpeedY()+ ( 0.75 * DEFAULT_SPEED_Y * GameLoop.TIME_BTW_PHYSICS_FRAMES)/1000.0 ));
	}

	public static int getSpawningProbabilityWeight(int level) {
		return (int) (Shooting_TrackingView.getSpawningProbabilityWeight(level) / 2);
	}
}
