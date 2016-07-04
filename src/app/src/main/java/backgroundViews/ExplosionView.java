package backgroundViews;

import helpers.KillableRunnable;
import helpers.MediaController;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;

public class ExplosionView extends SpecialEffectView {

	public ExplosionView(final RelativeLayout layout,MovingView parent, int imageId,long[] vibrationPattern) {
		super(parent.getX(),parent.getY(),layout, 0,0, parent.getWidth(), parent.getHeight(), imageId);
		
		initExplosion(vibrationPattern);
	}

	public void unremoveExplosion(final RelativeLayout layout,MovingView parent, int imageId,long[] vibrationPattern){
		super.unRemoveSpecialEffectView(parent.getX(),parent.getY(),layout, 0,0, parent.getWidth(), parent.getHeight(), imageId);

		initExplosion(vibrationPattern);
	}

	private void initExplosion(long[] vibrationPattern){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_EXPLOSION1);
		if(vibrationPattern == null){
			vibrationPattern = new long[0];
		}
		MediaController.vibrate(getContext(), vibrationPattern);

		GameLoop.specialEffects.add(this);

		postDelayed(new KillableRunnable(){
			@Override
			public void doWork() {
				ExplosionView.this.setViewToBeRemovedOnNextRendering();
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

}
