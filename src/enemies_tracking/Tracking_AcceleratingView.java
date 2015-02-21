package enemies_tracking;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

import parents.Moving_ProjectileView;
import android.content.Context;

public class Tracking_AcceleratingView extends Shooting_TrackingView{

	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/15,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*2,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker;
	
	public Tracking_AcceleratingView(Context context,Moving_ProjectileView trackMe) {
		super(context, trackMe );
		
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
	
	}
	
	@Override
	public boolean moveDirection(int direction){
		//increase the speed a little bit on every gravity movement (every 200ms)
		if(direction==DOWN){
			this.setSpeedY((float) (this.getSpeedY()+0.2*MainActivity.getScreenDens()));
		}
		return super.moveDirection(direction);
	}
}
