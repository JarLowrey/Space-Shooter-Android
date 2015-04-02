package background_objects;

import interfaces.GameActivityInterface;
import levels.LevelSystem;
import parents.MovingView;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;

public class BackgroundView extends MovingView{

	public BackgroundView(Context context, float movingSpeedY,
			float movingSpeedX, int width, int height, int imageId) {
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);


		((GameActivityInterface)context).addToBackground(this);
		LevelSystem.backgroundViews.add(this);
		this.setY(-height/2);
		this.setX( (float) (( MainActivity.getWidthPixels()-width ) *Math.random()));//random X position
	}

	@Override
	public void removeGameObject(){
		LevelSystem.backgroundViews.remove(this);
		super.deaultCleanupOnRemoval();
	}
}
 