package backgroundViews;

import helpers.KillableRunnable;
import helpers.MediaController;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;

public class ExplosionView extends SpecialEffectView {

	public ExplosionView(final RelativeLayout layout,MovingView parent, int imageId,long[] vibrationPattern) {
		super(layout, 0,0, parent.getWidth(), parent.getHeight(), imageId);
		
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_EXPLOSION1);
		if(vibrationPattern == null){
			vibrationPattern = new long[0];
		}
		MediaController.vibrate(getContext(), vibrationPattern);

		this.setX(parent.getX());
		this.setY(parent.getY());

		GameLoop.specialEffects.add(this);
		
		postDelayed(new KillableRunnable(){
			@Override
			public void doWork() {
				ExplosionView.this.removeGameObject();
			}
		},500);	
	}

	@Override
	public void restartThreads() {
		// do nothing
		
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		// do nothing
		
	}

	@Override
	public void removeGameObject() {
		super.defaultCleanupOnRemoval();		
	}

}
