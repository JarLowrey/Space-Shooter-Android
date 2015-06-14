package background_objects;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;


public class Meteor extends BackgroundView{

	public static final int CLOUD_1=R.drawable.cloud_1,CLOUD_2=R.drawable.cloud_2;
	
	public Meteor(Context context) {
		super(context, 
				(float) 4, 
				(Math.random()<.5) ? -2 : 2, 
				(int) context.getResources().getDimension(R.dimen.meteor_length), 
				(int) context.getResources().getDimension(R.dimen.meteor_length), 
				R.drawable.meteor);
		

		this.setX((float) (Math.random() * ( MainActivity.getWidthPixels()-context.getResources().getDimension(R.dimen.meteor_length) )) );
		this.setY((float) (- .5 * context.getResources().getDimension(R.dimen.meteor_length)) );
		//Sets height,width,speed, and background in the constructor
	}
}
