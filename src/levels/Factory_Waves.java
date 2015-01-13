package levels;

import support.ConditionalHandler;
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies_non_shooters.Gravity_MeteorView;

/**
 * spawn a number of a given enemy over a duration of time
 * 
 * Every runnable MUST check for levelPaused on run()
 * 
 * @author JAMES LOWREY
 *
 */

public class Factory_Waves extends Factory_Bosses{
    
	public Factory_Waves(Context context,RelativeLayout gameScreen) {
		super(context,gameScreen);
	}

	public final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void run() {
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
						ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
					spawnMeteor();
					
					numSpawned++;
					if(numSpawned<numMeteors){
						ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final  void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnSidewaysMeteor();
				
				numSpawned++;
				if(numSpawned<numMeteors){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
	
	public final void spawnGiantMeteorWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				ConditionalHandler.postIfLevelResumed(spawnGiantMeteor);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
		
	public final void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnDiveBomber();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnFullScreenDiagonalAttacker();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}

	
	public final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnCircularOrbitingView();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnRectanglularOrbitingView();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
					spawnTriangularOrbitingView();
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}
			}
		});
	}
	
	public final void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		ConditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
					spawnHorizontalLineOrbiter();
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						ConditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	
//	//USING JAVA FUNCTION POINTERS--all these functions pretty much do the same thing, but vary by which function they call. 
//	Is there a way to simplify this utilizing function pointers?
//	
//	//http://stackoverflow.com/questions/24148175/pass-function-with-parameter-defined-behavior-in-java
//		
//	public SpawnOverTime spawnSetNumbetOfEnemiesOverTime(final int numEnemies, final int delayBetweenEnemies){
//		return new SpawnOverTime(){//this is an interface
//			@Override
//			public void spawn(int numEnemies, int delayBetweenEach) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//	}
//	public void spawnFnPointer(){
//		
//	}
//	
//	
//	
//	
//		
	
	
	
	
	
	
	
	
	/*	Trying to get wave spawning behavior using ASyncTask. Tried to wait for end of wave before spawning a new one, but issue was calling Thread.sleep prevented function
	 * from reaching the CountDownLatch's await call. so the caller would be free to continue onwards. decided to give up and use much easier Handler/Runnable system
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
