package levels;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public class Factory_LevelsScripted extends Factory_Waves{

	public static int DEFAULT_WAVE_DURATION=5000, NEW_LEVEL=-1;
	
	protected static boolean levelCompleted,levelStarted;
	protected static int currentProgressInLevel;
	
	private final Runnable levelOverRunnable = new Runnable(){
		@Override
		public void run() {	levelOver(); }
	};
	
	public Factory_LevelsScripted(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
		levelCompleted = false;
		levelPaused=false;
	}

	private void initLevelVarsAtBeginningOfEveryLevel(){
		levelCompleted=false;
		levelStarted=true;
		levelPaused=false;
		currentProgressInLevel=0;
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
	
	
	protected void startLevelOne(boolean levelJustNowBeginning){
		if(levelJustNowBeginning){initLevelVarsAtBeginningOfEveryLevel();}//this will reset current progress in level

		final int levelOneNumWaves=9;

		Runnable levelOneWaveOne = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
				spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * levelOneNumWaves )/800 ,800);//spawn for entire level
				spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * levelOneNumWaves )/1500 ,1500);//spawn for entire level
			}
		};
		Runnable levelOneWave2 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(10,DEFAULT_WAVE_DURATION/10,true);//spawn for entire wave
				spawnMeteorShower(5,DEFAULT_WAVE_DURATION/5,false);//spawn for entire wave
			}
		};
		Runnable levelOneWave3 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave				
			}
		};
		Runnable levelOneWave4 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};
		Runnable levelOneWave5 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};

		Runnable levelOneWave6 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(DEFAULT_WAVE_DURATION/500,500,true);//spawn for entire wave
			}
		};
		Runnable levelOneWave7 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnSidewaysMeteors(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			}
		};
		Runnable levelOneWave8 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
			}
		};

		Runnable levelOneWave9 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(4,DEFAULT_WAVE_DURATION/4);//spawn for entire wave
			}
		};
		final Runnable[] waves = {levelOneWaveOne,levelOneWave2,levelOneWave3,levelOneWave4,levelOneWave5,levelOneWave6,levelOneWave7,levelOneWave8,levelOneWave9,levelOverRunnable};



		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */
		
		
		for(int i=currentProgressInLevel;i<waves.length;i++){
			spawnHandler.postDelayed(waves[i], i * DEFAULT_WAVE_DURATION);
		}
	}
	
	protected void startLevelTwo(boolean levelJustNowBeginning){
		startLevelOne( levelJustNowBeginning);//allow user to buy a gun, and then run the same level
	}
	
	protected void startLevelThree(boolean levelJustNowBeginning){
		if(levelJustNowBeginning){initLevelVarsAtBeginningOfEveryLevel();}//this will reset current progress in level
		

		final int levelThreeNumWaves=9;

		Runnable levelOneWaveOne = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(( DEFAULT_WAVE_DURATION * levelThreeNumWaves )/1500 ,2000);//spawn for entire level
				spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * levelThreeNumWaves )/800 ,800);//spawn for entire level
				spawnStraightFallingMeteorsAtRandomXPositions( ( DEFAULT_WAVE_DURATION * levelThreeNumWaves )/1500 ,1500);//spawn for entire level
			}
		};
		Runnable levelThreeWave2 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(10,DEFAULT_WAVE_DURATION/10,true);//spawn for entire wave
				spawnMeteorShower(5,DEFAULT_WAVE_DURATION/5,false);//spawn for entire wave
			}
		};
		Runnable levelThreeWave3 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave				
			}
		};
		Runnable levelThreeWave4 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};
		Runnable levelThreeWave5 = new Runnable(){
			@Override
			public void run() {
				spawnSidewaysMeteors(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			}
		};

		Runnable levelThreeWave6 = new Runnable(){
			@Override
			public void run() {
				spawnMeteorShower(DEFAULT_WAVE_DURATION/500,500,true);//spawn for entire wave
			}
		};
		Runnable levelThreeWave7 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnSidewaysMeteors(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			}
		};
		Runnable levelThreeWave8 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
				spawnMeteorShower(4,600,true);//spawn at beginning of wave
				spawnMeteorShower(4,600,false);//spawn at beginning of wave
			}
		};

		Runnable levelThreeWave9 = new Runnable(){
			@Override
			public void run() {
				spawnGiantSidewaysMeteors(4,DEFAULT_WAVE_DURATION/4);//spawn for entire wave
			}
		};
		final Runnable[] waves = {levelOneWaveOne,levelThreeWave2,levelThreeWave3,levelThreeWave4,levelThreeWave5,levelThreeWave6,levelThreeWave7,levelThreeWave8,levelThreeWave9,levelOverRunnable};



		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */
		
		
		for(int i=currentProgressInLevel;i<waves.length;i++){
			spawnHandler.postDelayed(waves[i], i * DEFAULT_WAVE_DURATION);
		}
		
	}
	
	public void pauseLevel(){
		levelPaused=true;
		spawnHandler.removeCallbacks(null);
	}
	
	public static boolean isLevelCompleted(){
		return levelCompleted;
	}
	
	public static boolean hasLevelStarted(){
		return levelStarted;
	}
	
	private void levelOver(){
		levelCompleted=true;
		levelStarted=false;
		levelPaused=true;
		pauseLevel();
	}
/*
	private void changeGameBackgroundImage(final int idToChangeTo){
		Animation fade_out=AnimationUtils.loadAnimation(this,R.anim.fade_out);
		final Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
		fade_out.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				gameWindowOverlay.setBackgroundResource(idToChangeTo);
				gameWindowOverlay.startAnimation(fade_in);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {}

			@Override
			public void onAnimationStart(Animation arg0) {}
			
		});
		gameWindowOverlay.startAnimation(fade_out);
	} 
	*/
}
