package background_objects;

import interfaces.GameView;

import com.jtronlabs.to_the_moon.MainActivity;

import android.content.Context;
import parents.Moving_GravityView;

public class BackgroundObject extends Moving_GravityView{

	public BackgroundObject(Context context, double movingSpeedY,
			double movingSpeedX, int width, int height, int imageId) {
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);


		((GameView)context).addToBackground(this);
		
		this.setY(-height/2);
		this.setX( (float) (( MainActivity.getWidthPixels()-width ) *Math.random()));//random X position
	}

}
