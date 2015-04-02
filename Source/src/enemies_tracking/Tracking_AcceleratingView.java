package enemies_tracking;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Tracking_AcceleratingView extends Shooting_TrackingView{
	
	public static final int 
			DEFAULT_SCORE=150,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker;
	
	public Tracking_AcceleratingView(Context context,Moving_ProjectileView trackMe) {
		super(context, trackMe );
		init();
	}

	public Tracking_AcceleratingView(Context context,Moving_ProjectileView trackMe, int scoreForKilling,
			float projectileSpeedY, float projectileSpeedX,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, int width, int height, int imageId) {
		super(context, trackMe,
				scoreForKilling, 
				projectileSpeedY, projectileSpeedX,
				projectileDamage,
				projectileHealth, 
				probSpawnBeneficialObject, 
				width,height, 
				imageId);
		init();
	}
	
	
	private void init(){
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				Tracking_AcceleratingView.this.setSpeedX(getTrackingSpeedX());
				Tracking_AcceleratingView.this.setSpeedY(
						(float) (Tracking_AcceleratingView.this.getSpeedY()+0.2*MainActivity.getScreenDens()));
				
				move();				
				ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,Tracking_AcceleratingView.this);
			}
		});
	}
}
