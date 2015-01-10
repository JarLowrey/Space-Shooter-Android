package com.jtronlabs.to_the_moon.levels;

import android.app.Activity;
import android.widget.RelativeLayout;

public class LevelFactory extends ScriptedWavesFactory{

	public LevelFactory(Activity context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	public void startLevelOne(){
			try {
				spawnMeteorWaves(10,500,true,false);
				spawnMeteorWaves(5,500,false,false);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
