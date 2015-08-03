package backgroundViews;

import android.util.Log;
import android.widget.RelativeLayout;

public class ParticleBackgroundAnimation {
	
	private RelativeLayout screen;
	private StarView[] myStars;
	
	public ParticleBackgroundAnimation(RelativeLayout layout){
		screen = layout;
		
		myStars = new StarView[27];
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
