package backgroundViews; 

import android.widget.RelativeLayout;

public class ParticleBackgroundAnimation {
	
	public static final int DEFAULT_NUM_STARS = 27;
	
	private RelativeLayout screen;
	private StarView[] myStars;
	 
	public ParticleBackgroundAnimation(RelativeLayout layout, int numStars){
		screen = layout;
		
		myStars = new StarView[numStars];
	} 
	 
	public void startSpawningStars(){
		for(int i=0; i < myStars.length;i++){
			myStars[i] = new StarView(screen);
		}
	}
	
	public void stopSpawningStars(){
		for(int i=0; i < myStars.length;i++){
			if(myStars[i] != null){myStars[i].removeGameObject();}
			myStars[i] = null;
		}
	}
	
}
