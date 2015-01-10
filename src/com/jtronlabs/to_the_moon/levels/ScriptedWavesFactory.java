package com.jtronlabs.to_the_moon.levels;

import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.enemy_types.EnemyView;
import com.jtronlabs.enemy_types_non_shooters.Gravity_MeteorView;
import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;

public class ScriptedWavesFactory {

	EnemyFactory enemyProducer;
	Activity myActivity;
	
	RelativeLayout gameLayout;
    Handler spawnHandler;
    
    
    
    
	public ScriptedWavesFactory(Activity a,RelativeLayout gameScreen) {
		myActivity=a;
		enemyProducer = new EnemyFactory((Context)a);
		gameLayout = gameScreen;

		Looper looper = Looper.getMainLooper();
		spawnHandler = new Handler(looper);
	}
	

	private void addEnemyToGame(EnemyView enemy){
		gameLayout.addView(enemy,1);
		GameActivity.enemies.add(enemy);
	}
	
//	private boolean meteorsFallFromLeftToRight;
	
	
	public void spawnMeteorWaves(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean firstMeteorShowerRunsLeftToRight,
			final boolean waitForExecutionToFinish) throws InterruptedException {
		
		final AsyncTask<String, Integer, String> myTask = new AsyncTask<String, Integer, String>(){
			
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

}
