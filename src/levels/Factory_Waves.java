package levels;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies_non_shooters.Gravity_MeteorView;

/**
 * spawn a number of a given enemy over a duration of time
 * @author JAMES LOWREY
 *
 */

public class Factory_Waves extends Factory_Bosses{

    protected Handler spawnHandler;
    protected boolean levelPaused;
    
	public Factory_Waves(Context context,RelativeLayout gameScreen) {
		super(context,gameScreen);
		
		spawnHandler = new Handler();
		levelPaused=false;
	}

	public void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		spawnHandler.post(new Runnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void run() {
				if(!levelPaused){
					//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
					Gravity_MeteorView  met= spawnMeteor();
					final int width = +met.getLayoutParams().width;//view not added to screen yet, so must use layout params instead of View.getWidth()
					final int numMeteorsPossibleOnScreenAtOnce= (int) (MainActivity.getWidthPixels()/width);
					final int currentMeteor = numSpawned % numMeteorsPossibleOnScreenAtOnce;
					
					
					int myXPosition;
					//reverse direction if full meteor shower has occurred
					if(numSpawned >= numMeteorsPossibleOnScreenAtOnce && numSpawned % numMeteorsPossibleOnScreenAtOnce ==0){
						meteorsFallLeftToRight = !meteorsFallLeftToRight;					
					}
					
					if(meteorsFallLeftToRight){
						myXPosition = width * currentMeteor;
					}else{
						myXPosition = (int) (MainActivity.getWidthPixels()- (width * (currentMeteor+1) ) );
					}
					met.setX(myXPosition);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
					}
				}
			}
		});
	}

	public void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnMeteor();
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
					}
				}
			}
		});
	}

	public void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnSidewaysMeteor();
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
					}
				}
			}
		});
	}
	
	public void spawnGiantMeteorWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnHandler.post(spawnGiantMeteor);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
					}
				}
			}
		});
	}
		
	public void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnDiveBomber();
					numSpawned++;
					
					if(numSpawned<totalNumShips){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}
	
	public void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnFullScreenDiagonalAttacker();
					numSpawned++;
					
					if(numSpawned<totalNumShips){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}

	
	public void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnCircularOrbitingView();
					numSpawned++;
					
					if(numSpawned<totalNumShips && !levelPaused){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}
	
	public void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnRectanglularOrbitingView();
					numSpawned++;
					
					if(numSpawned<totalNumShips && !levelPaused){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}
	
	public void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnTriangularOrbitingView();
					numSpawned++;
					
					if(numSpawned<totalNumShips && !levelPaused){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}
	
	public void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				if(!levelPaused){
					spawnHorizontalLineOrbiter();
					numSpawned++;
					
					if(numSpawned<totalNumShips && !levelPaused){
						spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
				}
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*	Trying to get wave spawning behavior using ASyncTask. Tried to wait for end of wave before spawning a new one, but issue was calling Thread.sleep prevented function
	 * from reaching the CountDownLatch's await call. so the caller would be free to continue onwards. decided to give up and use much easier Handler/Runnable system for now
	 * 	
//	private boolean meteorsFallFromLeftToRight;
	public void spawnMeteorWaves(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean firstMeteorShowerRunsLeftToRight,
			final boolean waitForExecutionToFinish) throws InterruptedException {
		InterruptedException e = null;
		
		 AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>(){
			
			@Override
			protected String doInBackground(String... params) {
//				final CountDownLatch waiting = new CountDownLatch(numMeteors);
				
				for(int i=0;i<numMeteors;i++){
					
					final int numMeteorSpawned=i;
					
					Runnable spawnOneMeteor = new Runnable(){
						@Override
						public void run() {
							//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
							Gravity_MeteorView  met= enemyProducer.spawnMeteor();
							final int width = +met.getLayoutParams().width;//view not added yet, so must use layout params instead of View.getWidth()
							final int numMeteorsPossibleOnScreenAtOnce= (int) (MainActivity.getWidthPixels()/width);
							final int currentMeteor = numMeteorSpawned % numMeteorsPossibleOnScreenAtOnce;
							int myXPosition;
							
							boolean meteorsFallFromLeftToRight = firstMeteorShowerRunsLeftToRight;
							//reverse direction if full meteor shower has occurred
							if(numMeteorSpawned >= numMeteorsPossibleOnScreenAtOnce && numMeteorSpawned % numMeteorsPossibleOnScreenAtOnce ==0){
								meteorsFallFromLeftToRight = !meteorsFallFromLeftToRight;					
							}
							
							if(meteorsFallFromLeftToRight){
								myXPosition = width * currentMeteor;
							}else{
								myXPosition = (int) (MainActivity.getWidthPixels()- (width * (currentMeteor+1) ) );
							}
							met.setX(myXPosition);
							
							GameActivity.enemies.add(met);
							gameLayout.addView(met,1);
						}
					};
					 
					myActivity.runOnUiThread(spawnOneMeteor);
					
					
					try {
						Thread.sleep(millisecondsBetweenEachMeteor);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					waiting.countDown();
				}
					
//				if(waitForExecutionToFinish){
//					try {
//						waiting.await();
//					} catch (InterruptedException e) {///////////////////WHAT TO DO?
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				return null;
			}
		};
		
		myTask.execute();
		
	}
*/
}
