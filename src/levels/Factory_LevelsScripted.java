package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class Factory_LevelsScripted extends Factory_Waves{
  
	public static int DEFAULT_WAVE_DURATION=5000;
	
	protected static boolean levelCompleted,levelStarted;
	protected int currentProgressInLevel;
	
	private final Runnable levelOverRunnable = new Runnable(){
		@Override
		public void run() {	levelOver(); }
	};
	
	public Factory_LevelsScripted(Context context,RelativeLayout gameScreen){
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
	/*
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
										spawnMeteorShower(4,500,true);
										spawnMeteorShower(4,500,false);
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
	*/
	protected void startLevelOne(){
		
		final int numWaves=9;
		
		Runnable wave1 = new Runnable(){
			@Override
			public void run() {
				if(!levelStopped){
					spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
					spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
					spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * numWaves )/800 ,800);//spawn for entire level
					spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * numWaves )/1500 ,1500);//spawn for entire level
				}
			}
		};
		Runnable wave2 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(10,DEFAULT_WAVE_DURATION/10,true);//spawn for entire wave
				spawnMeteorShower(5,DEFAULT_WAVE_DURATION/5,false);//spawn for entire wave
			}
		};
		Runnable wave3 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave				
			}
		};
		Runnable wave4 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};
		Runnable wave5 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};

		Runnable wave6 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(DEFAULT_WAVE_DURATION/500,500,true);//spawn for entire wave
			}
		};
		Runnable wave7 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnSidewaysMeteors(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			}
		};
		Runnable wave8 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
			}
		};

		Runnable wave9 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(4,DEFAULT_WAVE_DURATION/4);//spawn for entire wave
			}
		};
		final Runnable[] waves = {wave1,wave2,wave3,wave4,wave5,wave6,wave7,wave8,wave9,levelOverRunnable};
		
		for(int i=0;i<waves.length;i++){
			spawnHandler.postDelayed(waves[i], i * DEFAULT_WAVE_DURATION);
		}
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
