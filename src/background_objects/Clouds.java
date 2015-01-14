package background_objects;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;


public class Clouds extends BackgroundObject{

	public static final int CLOUD_1=R.drawable.cloud_img1,CLOUD_2=R.drawable.cloud_img2;
	
	public Clouds(Context context,int imageId) {
		super(context, 
				1, 
				0, 
				(int) context.getResources().getDimension(R.dimen.cloud_height), 
				(int) context.getResources().getDimension(R.dimen.cloud_width), 
				imageId);

		//Set height,width,speed, and background in the constructor
	}
}
