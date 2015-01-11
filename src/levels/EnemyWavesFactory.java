package levels;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;

import enemies.Shooting_ArrayMovingView;
import enemies_non_shooters.Gravity_MeteorView;

public class EnemyWavesFactory extends BossFactory{

    Handler spawnHandler;
    
	public EnemyWavesFactory(Context context,RelativeLayout gameScreen) {
		super(context,gameScreen);
		
		spawnHandler = new Handler();
	}

	public void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		spawnHandler.post(new Runnable(){
			
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
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}

	public void spawnMeteorsAtRandomXPositions(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnMeteor();
				
				numSpawned++;
				if(numSpawned<numMeteors){
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}

	public void spawnSidewaysMeteors(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnSidewaysMeteor();
				
				numSpawned++;
				if(numSpawned<numMeteors){
					spawnHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
		
	public void spawnDiveBomberWaves(final int totalNumShips, final int millisecondsBetweenEachSpawn, final int numShipsPerSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				for(int i=0;i<numShipsPerSpawn;i++){
					spawnDiveBomber();
				}
				numSpawned+=numShipsPerSpawn;
				
				if(numSpawned<totalNumShips){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnFullScreenDiagonalAttacker();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}

	public void spawnMovingArrayShooters(final int numShooters){
//		enemyProducer
	}
	
	public void spawnMovingArrayShooters(){
		int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
		
		for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
			spawnOneShooting_MovingArrayView();
		}
	}
	
	public void spawnCircularOrbiters(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnCircularOrbitingView();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public void spawnRectangularOrbiters(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnCircularOrbitingView();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public void spawnTriangularOrbiters(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		spawnHandler.post(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				spawnCircularOrbitingView();
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawnHandler.postDelayed(this,millisecondsBetweenEachSpawn);
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
