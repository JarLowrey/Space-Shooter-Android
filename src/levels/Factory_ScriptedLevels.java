package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class Factory_ScriptedLevels extends Factory_Waves{
  
	protected static boolean levelCompleted,levelStarted;
	protected int currentProgressInLevel;
	
	public Factory_ScriptedLevels(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
		levelCompleted = false;
		levelStopped=false;
	}

	private void initLevelVarsAtBeginningOfEveryLevel(){
		levelCompleted=false;
		levelStarted=true;
		levelStopped=false;
		currentProgressInLevel=1;
		
	}
	
	protected void startLevelOne(){
		initLevelVarsAtBeginningOfEveryLevel();
		spawnSidewaysMeteors(20,1000);
		spawnSidewaysMeteors(10,2000);
		spawnStraightFallingMeteorsAtRandomXPositions(200,1000);
		final long wave1Duration = 20*1000;

		if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(4,500,true);
				spawnMeteorShower(4,500,false);
				final long wave2Duration = 4*500;


				if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
					@Override
					public void run() {
						spawnMeteorShower(20,1000,true);
						spawnSidewaysMeteors(20,1000);
						final long wave3Duration = 20*1000;
						currentProgressInLevel++;

						if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
							@Override
							public void run() {
								spawnMeteorShower(4,500,true);
								spawnMeteorShower(4,500,false);
								final long wave4Duration = 4*500;
								currentProgressInLevel++;

								if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
									@Override
									public void run() {
										spawnGiantSidewaysMeteors(20, 2000);
										spawnSidewaysMeteors(40,1000);
										final long wave5Duration = 20*2000;
										currentProgressInLevel++;

										if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
											@Override
											public void run() {
												spawnMeteorShower(20,1000,true);
												spawnGiantSidewaysMeteors(20, 2000);
												spawnGiantSidewaysMeteors(20, 2000);
												final long wave6Duration = 20*2000;
												currentProgressInLevel++;


												if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
													@Override
													public void run() { levelOver(); }
												},wave6Duration);}
											}
										}, wave5Duration);}
									}
								}, wave4Duration);}
							}
						}, wave3Duration);}
					}
				}, wave2Duration);}
			}
		}, wave1Duration);}
		
	}
	
	protected void startLevelTwo(){
		startLevelOne();//allow user to buy a gun, and then run the same level
	}
	
	protected void startLevelThree(){
		initLevelVarsAtBeginningOfEveryLevel();
//		spawnMovingArrayShooters();
//		spawnMeteorShower(10,600,false);
//		spawnSidewaysMeteors(20,500);
//		spawnSidewaysMeteors(20,3000);
//		
//		if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//				@Override
//				public void run() {
//					spawnMeteorShower(5,500,true);
//					spawnSidewaysMeteors(20,500);
//					currentProgressInLevel++;
//
//					if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//						@Override
//						public void run() {
//							spawnDiveBomberWaves(10,5000,2);
//							spawnFullScreenDiagonalAttackersWave(6,5000);
//							currentProgressInLevel++;
//							
//
//							if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//								@Override
//								public void run() {
//									spawnMeteorShower(10,600,false);
//									spawnMovingArrayShooters();
//									currentProgressInLevel++;
//
//									if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//										@Override
//										public void run() {
//											spawnCircularOrbiters(5,4000);
//											currentProgressInLevel++;
//											
//
//											if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//												@Override
//												public void run() {
//													spawnRectangularOrbiters(5,4000);
//													currentProgressInLevel++;
//
//													if(!levelStopped){spawnHandler.postDelayed(new Runnable(){
//														@Override
//														public void run() {
//															spawnTriangularOrbiters(5,4000);
//															currentProgressInLevel++;
//															
//															levelOver();
//															}
//														}
//													, 10000);
//													}
//											, 5000);
//											}
//											}
//										}
//									, 7000);
//									}
//									}
//								}
//							, 20000);
//							}
//						}
//						}
//					, 10000);
//				}
//			}
//		}
//		, 20000);
//		}
		
	}
	
	public void stopLevelSpawning(){
		levelStopped=true;
		spawnHandler.removeCallbacks(null);
	}
	
	public static boolean isLevelCompleted(){
		return levelCompleted;
	}
	
	public static boolean isLevelStarted(){
		return levelStarted;
	}
	
	private void levelOver(){
		levelCompleted=true;
		levelStopped=true;
		stopLevelSpawning();
	}
	
}
