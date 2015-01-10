package levels;

import android.app.Activity;
import android.widget.RelativeLayout;

public class LevelFactory extends ScriptedWavesFactory{

//	Handler 
	
	public LevelFactory(Activity context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	public void startLevelOne(){
		spawnMeteorWaves(20,600,false);
		spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {spawnMeteorWaves(5,500,true);}}
		, 20000);
		
	}

	public void stopLevel(){
		spawnHandler.removeCallbacks(null);
	}
}
