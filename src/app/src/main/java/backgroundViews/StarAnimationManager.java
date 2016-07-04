package backgroundViews; 

import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class StarAnimationManager {
	private static StarAnimationManager starManager;
	private static StarView[] myStars;

	 
	private StarAnimationManager(RelativeLayout layout){
		final float numPixelsOnScreen = MainActivity.getHeightPixels() * MainActivity.getWidthPixels();
		final double areaOnScreen = numPixelsOnScreen / MainActivity.getScreenDens();
		//Log.d("lowrey",areaOnScreen+" AREA");
		// For a Samsung S4, Area = 691200.0 (found via testing), and about 35 stars look good on the screen.
		// Thus 691200 / 35 = 19748.57142857143 as the #star to area coeffient
		final int numStars = (int)(areaOnScreen / 19748.57142857143);

		myStars = new StarView[numStars];
	}

	public static StarAnimationManager createStars(RelativeLayout layout){
		if(starManager == null){
			starManager = new StarAnimationManager(layout);
		}

		for(int i=0; i < myStars.length;i++){
			myStars[i]= (StarView)SpecialEffectView.getEffect(layout, StarView.DEFAULT_BACKGROUND_ID,StarView.class,null,null);
		}

		return starManager;
	}
}
