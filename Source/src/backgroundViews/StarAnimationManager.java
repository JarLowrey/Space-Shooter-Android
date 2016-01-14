package backgroundViews; 

import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;

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
		for(int i=0; i < myStars.length;i++){
			myStars[i] = new StarView(layout);

		}
	}

	public static StarAnimationManager createStars(RelativeLayout layout){
		if(starManager == null){
			starManager = new StarAnimationManager(layout);
		}else{
			starManager.reassignStarViewsLayout(layout);
		}
		return starManager;
	}

	private void reassignStarViewsLayout(RelativeLayout newLayout){
		for(int i=0; i < myStars.length;i++){
			RelativeLayout originalLayout = (RelativeLayout)myStars[i].getParent();
			originalLayout.removeView(myStars[i]);


			newLayout.addView(myStars[i],0);//add star to background
		}
	}

	/*
	public static void cleanUpAndRemove(){
		for(int i=0; i < myStars.length;i++){
			if(myStars[i] != null){
				myStars[i].setViewToBeRemovedOnNextRendering();
				myStars[i].removeGameObject();
			}
			myStars[i] = null;
		}
		starManager = null;
	}
	*/
}
