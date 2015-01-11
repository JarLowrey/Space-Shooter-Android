package levels;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;

import enemy_types.Shooting_ArrayMovingView;
import enemy_types_non_shooters.Gravity_MeteorView;

public class ScriptedWavesFactory extends EnemyFactory{

    Handler spawnHandler;
    
	public ScriptedWavesFactory(Context context,RelativeLayout gameScreen) {
		super(context,gameScreen);
		
		spawnHandler = new Handler();
	}

	public void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean firstMeteorShowerRunsLeftToRight) {
		spawnHandler.post(new Runnable(){
			
			private int numMeteorsSpawned=0;
			private boolean meteorsFallLeftToRight = firstMeteorShowerRunsLeftToRight;
			
			@Override
			public void run() {
				//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
				Gravity_MeteorView  met= spawnMeteor();
				final int width = +met.getLayoutParams().width;//view not added to screen yet, so must use layout params instead of View.getWidth()
				final int numMeteorsPossibleOnScreenAtOnce= (int) (MainActivity.getWidthPixels()/width);
				final int currentMeteor = numMeteorsSpawned % numMeteorsPossibleOnScreenAtOnce;
				
				
				int myXPosition;
				//reverse direction if full meteor shower has occurred
				if(numMeteorsSpawned >= numMeteorsPossibleOnScreenAtOnce && numMeteorsSpawned % numMeteorsPossibleOnScreenAtOnce ==0){
					meteorsFallLeftToRight = !meteorsFallLeftToRight;					
				}
				
				if(meteorsFallLeftToRight){
					myXPosition = width * currentMeteor;
				}else{
					myXPosition = (int) (MainActivity.getWidthPixels()- (width * (currentMeteor+1) ) );
				}
				met.setX(myXPosition);
				
				numMeteorsSpawned++;
				if(numMeteorsSpawned<numMeteors){
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}

	public void spawnMeteorsAtRandomXPositions(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numMeteorsSpawned=0;
			
			@Override
			public void run() {
				spawnMeteor();
				
				numMeteorsSpawned++;
				if(numMeteorsSpawned<numMeteors){
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}

	public void spawnSidewaysMeteors(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numMeteorsSpawned=0;
			
			@Override
			public void run() {
				spawnSidewaysMeteor();
				
				numMeteorsSpawned++;
				if(numMeteorsSpawned<numMeteors){
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
	
	public void spawnDiveBombers(final int numDiveBombers){
		for(int i=0;i<numDiveBombers;i++){
			spawnDiveBomber();
		}
	}
	
	public void spawnDiveBombers(final int totalNumDiveBomber, final int millisecondsBetweenEachSpawn, final int numDiveBombersPerSpawn){
		spawnHandler.post(new Runnable(){
			private int numDiveBombersSpawned=0;
			
			@Override
			public void run() {
				spawnDiveBombers(numDiveBombersPerSpawn);
				numDiveBombersSpawned+=numDiveBombersPerSpawn;
				
				if(numDiveBombersSpawned<totalNumDiveBomber){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public void spawnMovingArrayShooters(final int numShooters){
//		enemyProducer
	}
	
	public void spawnAllSimpleShooters(){
		int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
		
		for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
			spawnOneShooting_MovingArrayView();
		}
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
