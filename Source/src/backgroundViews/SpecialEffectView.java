package backgroundViews;

import com.jtronlabs.space_shooter.GameLoop;

import android.widget.RelativeLayout;
import parents.MovingView;

public abstract class SpecialEffectView extends MovingView {

	public SpecialEffectView(float xInitialPosition,float yInitialPosition,RelativeLayout layout, float movingSpeedY,
			float movingSpeedX, int width, int height, int imageId) {
		super(xInitialPosition,yInitialPosition,layout, movingSpeedY, movingSpeedX, width, height, imageId);

		GameLoop.specialEffects.add(this);
	}

	@Override
	public void removeGameObject() {
		super.removeGameObject();
		GameLoop.specialEffects.remove(this);
	}

}
