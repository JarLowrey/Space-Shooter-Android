package levels;

import android.app.Activity;
import android.widget.RelativeLayout;

public class ScriptedLevelFactory extends EnemyWavesFactory{

	boolean levelOver = false;
	int currentProgressInLevel;
	
	public ScriptedLevelFactory(Activity context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	//each new postdelayed is another wave in the level
	public void startLevelOne(){
		levelOver=false;
		currentProgressInLevel=0;
		
		spawnMeteorShower(10,600,false);
		spawnSidewaysMeteors(20,500);
		spawnSidewaysMeteors(20,3000);
		currentProgressInLevel++;
		
		spawnHandler.postDelayed(new Runnable(){
				@Override
				public void run() {
					spawnMeteorShower(5,500,true);
					spawnSidewaysMeteors(20,500);
					currentProgressInLevel++;
					
					spawnHandler.postDelayed(new Runnable(){
						@Override
						public void run() {
							spawnDiveBomberWaves(10,5000,2);
							spawnFullScreenDiagonalAttackersWave(6,5000);
							currentProgressInLevel++;
							
							spawnHandler.postDelayed(new Runnable(){
								@Override
								public void run() {
									spawnMeteorShower(10,600,false);
									spawnMovingArrayShooters();
									currentProgressInLevel++;
									
									spawnHandler.postDelayed(new Runnable(){
										@Override
										public void run() {
											spawnCircularOrbiters(5,4000);
											currentProgressInLevel++;
											
											spawnHandler.postDelayed(new Runnable(){
												@Override
												public void run() {
													spawnRectangularOrbiters(5,4000);
													currentProgressInLevel++;
													
													spawnHandler.postDelayed(new Runnable(){
														@Override
														public void run() {
															spawnTriangularOrbiters(5,4000);
															currentProgressInLevel++;
															
															levelOver=true;
															}
														}
													, 10000);
													}
												}
											, 5000);
											}
										}
									, 7000);
									}
								}
							, 20000);
						}
						}
					, 10000);
				}
			}
		, 20000);
		
	}
	
	public void startLevelTwo(){
		levelOver=false;
		currentProgressInLevel=0;
		
	}
	
	public void stopLevel(){
		spawnHandler.removeCallbacks(null);
	}
	
	//to be called in GameActivity's collision detection when all enemies have been killed
	public void enemiesEmpty(){
		
	}
}
