package background_objects;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;


public class Sun extends BackgroundView{

	public static final int CLOUD_1=R.drawable.cloud_img1,CLOUD_2=R.drawable.cloud_img2;
	
	public Sun(Context context) {
		super(context, 
				.1, 
				0, 
				(int) context.getResources().getDimension(R.dimen.sun_length), 
				(int) context.getResources().getDimension(R.dimen.sun_length), 
				R.drawable.sun);

		this.setX(0+context.getResources().getDimension(R.dimen.sun_length)/3);
		this.setY(0+context.getResources().getDimension(R.dimen.sun_length)/3);
		//Sets height,width,speed, and background in the constructor
	}
}
