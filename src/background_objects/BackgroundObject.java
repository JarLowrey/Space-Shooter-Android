package background_objects;

import com.jtronlabs.to_the_moon.MainActivity;

import android.content.Context;
import parents.Moving_GravityView;

public class BackgroundObject extends Moving_GravityView{

	public BackgroundObject(Context context, double movingSpeedY,
			double movingSpeedX, int width, int height, int imageId) {
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);

		this.setX( (float) (( MainActivity.getWidthPixels()-width ) *Math.random()));//random X position
		this.addToBackground();
	}

}
