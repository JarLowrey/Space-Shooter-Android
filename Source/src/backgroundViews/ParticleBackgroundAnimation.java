package backgroundViews;

import android.content.Context;
import android.util.Log;

public class ParticleBackgroundAnimation {
	
	private Context ctx;
	private StarView[] myStars;
	
	public ParticleBackgroundAnimation(Context context){
		ctx = context;
		
		myStars = new StarView[25];
	}
	
	public void startSpawningStars(){
		Log.d("lowrey","stars created and began moving");
		for(int i=0; i < myStars.length;i++){
			myStars[i] = new StarView(ctx);
		}
	}
	
	public void stopSpawningStars(){
		Log.d("lowrey","stars stopped moving and removed");
		for(int i=0; i < myStars.length;i++){
			myStars[i].removeGameObject();
			myStars[i] = null;
		}
	}
	
}
