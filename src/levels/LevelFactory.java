package levels;

import android.app.Activity;
import android.widget.RelativeLayout;

public class LevelFactory extends ScriptedWavesFactory{

//	Handler 
	
	public LevelFactory(Activity context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	public void startLevelOne(){
		spawnMeteorWaves(5,300,false);
		spawnMeteorWaves(5,300,true);
		
	}

}
