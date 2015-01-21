package levels;

import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_Diagonal_DiveBomberView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_HorizontalLineView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every runnable MUST check for levelPaused on run()
 * 
 * @author JAMES LOWREY
 *  
 */

public class Factory_Waves extends Factory_Bosses{
    
	ConditionalHandler conditionalHandler;
	protected boolean levelWavesCompleted=false;
	protected boolean levelPaused;
	
	public Factory_Waves(Context context) {
		super(context);
		
		conditionalHandler = new ConditionalHandler(this);
	}

	public boolean isLevelPaused(){
		return levelPaused;
	}
	public boolean areLevelWavesCompleted(){
		return levelWavesCompleted;
	}

	public final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		conditionalHandler.postIfLevelResumed(new Runnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void run() {
					//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
					Gravity_MeteorView  met= new Gravity_MeteorView(ctx);
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
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Gravity_MeteorView(ctx);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final  void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Meteor_SidewaysView(ctx);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
	
	public final void spawnGiantMeteorWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				conditionalHandler.postIfLevelResumed(spawnGiantMeteor);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				} 
			}
		});
	}
		 
	public final void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_Diagonal_DiveBomberView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_DiagonalMovingView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}

	
	//orbiters
	public final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCirclesOnScreen){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				final int currentShip = numSpawned % numCirclesOnScreen ;
				final int width  = (int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCirclesOnScreen-width ) / 2;
				final int orbitX= ( width/2 ) * (2*currentShip)+radius * (2*currentShip +1);
//				if(currentShip!=0){orbitX= ( (orbitX+width) * (currentShip+2) );}
				final int orbitY=Orbiter_CircleView.DEFAULT_ORBIT_Y;
				
				new Orbiter_CircleView(ctx,Orbiter_CircleView.DEFAULT_SCORE,Orbiter_CircleView.DEFAULT_SPEED_Y,
						Orbiter_CircleView.DEFAULT_SPEED_X,Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE,
						Orbiter_CircleView.DEFAULT_HEALTH,Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
						orbitX,orbitY,
						width, 
						(int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height),
						Orbiter_CircleView.DEFAULT_BACKGROUND,radius,10);
				
//				new Orbiter_CircleView(ctx);
				
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_RectangleView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_TriangleView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}
			}
		});
	}
	
	public final void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_HorizontalLineView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
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
//
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
						e.printStackTrace();
					}
//					waiting.countDown();
				}
					
//				if(waitForExecutionToFinish){
//					try {
//						waiting.await();
//					} catch (InterruptedException e) {///////////////////WHAT TO DO?
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
