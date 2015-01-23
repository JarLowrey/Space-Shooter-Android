package enemies;

import friendlies.ProtagonistView;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

public class Shooting_TrackingView extends Enemy_ShooterView{

	public static final float DEFAULT_BULLET_SPEED_Y=7,
			DEFAULT_BULLET_SPEED_X=2,
			DEFAULT_BULLET_FREQ=5000;
	
	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/10;
	
	private Moving_ProjectileView viewToTrack;
	
	private Runnable track = new Runnable(){
		@Override
		public void run() {
			final int trackPoint =(int) ( viewToTrack.getX()*2 + viewToTrack.getWidth() )/2;
			final int myPos = (int)( Shooting_TrackingView.this.getX()*2 + Shooting_TrackingView.this.getWidth() )/2;
			final int diff = trackPoint - myPos;
			
			final float speedX = diff/Math.abs(diff) * Math.abs( Shooting_TrackingView.this.getSpeedX() );
			Shooting_TrackingView.this.setSpeedX(speedX);
			Shooting_TrackingView.this.moveDirection(SIDEWAYS);
			
			ConditionalHandler.postIfAlive(this, Shooting_TrackingView.this);
		}
	};
	
	public Shooting_TrackingView(Context context,Moving_ProjectileView trackMe, int scoreForKilling,
			float projectileSpeedY, float projectileSpeedX,
			int projectileDamage, int projectileHealth,
			float probSpawnBeneficialObject, int width, int height, int imageId) {
		super(context, scoreForKilling, projectileSpeedY, projectileSpeedX,
				projectileDamage, projectileHealth, probSpawnBeneficialObject, width,
				height, imageId);
		
		viewToTrack=trackMe;
		ConditionalHandler.postIfAlive(track, this);
	}

	@Override
	public float getShootingFreq(){
		return DEFAULT_BULLET_FREQ;
	}

}
