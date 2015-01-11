package levels;

import android.app.Activity;
import android.widget.RelativeLayout;

public class LevelFactory extends ScriptedWavesFactory{

	boolean levelOver = false;
	int currentSpotInLevel;
	
	public LevelFactory(Activity context,RelativeLayout gameScreen){
		super( context, gameScreen);
		
		currentSpotInLevel=0;
	}
	
	public void startLevelOne(){
		spawnMeteorShower(20,600,false);
		currentSpotInLevel++;
		
		spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {spawnMeteorShower(5,500,true);currentSpotInLevel++;}}
		, 20000);
		
		spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {spawnSidewaysMeteors(20,500);currentSpotInLevel++;}}
		, 20000);

		spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {spawnDiveBomberWaves(20,5000,3);currentSpotInLevel++;}}
		, 30000);

		spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {spawnMovingArrayShooters();currentSpotInLevel++;}}
		, 60000);
	}

	public void stopLevel(){
		spawnHandler.removeCallbacks(null);
	}
}
