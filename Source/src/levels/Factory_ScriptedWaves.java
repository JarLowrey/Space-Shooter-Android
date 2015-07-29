package levels;

import helpers.KillableRunnable;
import helpers.SpawnableWave;
import interfaces.GameActivityInterface;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_non_shooters.Gravity_MeteorView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_Rectangle_Array;
import enemies_tracking.Shooting_TrackingView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every KillableRunnable MUST check for !isLevelPaused() on doWork()
 * 
 * @author JAMES LOWREY
 *  
 */

public abstract class Factory_ScriptedWaves extends AttributesOfLevels{
		
	public Factory_ScriptedWaves(Context context) { 
		super(context);
	}
			
	final  SpawnableWave doNothing(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {}
		};
		return new SpawnableWave(r,0,0);//probability weight of 0, as it should not be randomly used in levelSpawning
	}

	//meteor showers
	final SpawnableWave meteorShowersThatForceUserToMiddle(){
		KillableRunnable r = new KillableRunnable(){//this does not last a whole wave, which is fine.
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors/=2;
				numMeteors-=2;
				
				spawnMeteorShower(numMeteors,400,true);
				spawnMeteorShower(numMeteors,400,false);
				
			}
		}; 
		
		final int probabilityWeight = (int) (10 - 1.5*difficulty());
		
		return new SpawnableWave(r,5000,probabilityWeight);
	}
	final SpawnableWave meteorShowersThatForceUserToRight(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,true);
				
			}
		};
		
		final int probabilityWeight = (int) (10 - 1.5*difficulty());
		
		return new SpawnableWave(r,5000,probabilityWeight );
	}
	final SpawnableWave meteorShowersThatForceUserToLeft(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,false);
				
			}
		};

		final int probabilityWeight = (int) (10 - 1.5*difficulty());
		
		return new SpawnableWave(r,5000,probabilityWeight );
	}	
	//array shooter waves
	final SpawnableWave refreshArrayShooters(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Orbiter_Rectangle_Array.refreshSimpleShooterArray(ctx,
						difficulty() );
			}
		};
		
		int probabilityWeight = 0;
		if(Orbiter_Rectangle_Array.allSimpleShooters.size() < Orbiter_Rectangle_Array.getMaxNumShips()/4){//only refresh if a few left
			if(difficulty() > 0){
				probabilityWeight = 30-difficulty()*3;
				probabilityWeight = Math.max(probabilityWeight, 5);//always have non-zero probability
			}
		}
		
		return new SpawnableWave(r,8000/(difficulty()+1),probabilityWeight );
	}
	
	//tracking waves
	final SpawnableWave trackingEnemy(){
		final int numEnemies = 4;
		final int millisecondsBetweenEachSpawn = 800;
		
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Shooting_TrackingView(ctx,((GameActivityInterface)ctx).getProtagonist(),difficulty());
				numSpawned++;
				
				if(numSpawned<numEnemies){
					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
				}
			}
		};
		
		int probabilityWeight = 0;
		if(difficulty() > 0){
			probabilityWeight = 20+difficulty()*2;
			probabilityWeight = Math.min(probabilityWeight, 40);//limit max weight
		}
		
		return new SpawnableWave(r,600,probabilityWeight);
	}
	
	//circular orbiters
	final SpawnableWave lotsOfCircles(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				spawnCircularOrbiterWave(6,500,3);
				spawnCircularOrbiterWave(6,500,2);
				spawnCircularOrbiterWave(9,500,1);
			} 
		};
		
		int probabilityWeight = 0;
		if(getLevel() == 10){
			probabilityWeight = 10000;
		}
		
		return new SpawnableWave(r,15000,probabilityWeight);
	}
	//spawn enemies over a set period
	private final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		spawningHandler.post(
				new KillableRunnable(){
				
				private int numSpawned=0;
				private boolean meteorsFallLeftToRight = beginOnLeft;
				
				@Override
				public void doWork() {
					//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
					Gravity_MeteorView  met= new Gravity_MeteorView(ctx,difficulty() );
					final int width = met.getLayoutParams().width;//view not added to screen yet, so must use layout params instead of View.getWidth()
					final int numMeteorsPossibleOnScreenAtOnce = (int) (MainActivity.getWidthPixels()/width);
					final int currentMeteor = numSpawned % numMeteorsPossibleOnScreenAtOnce;
					
					
					//reverse direction if full meteor shower has occurred
					if(numSpawned >= numMeteorsPossibleOnScreenAtOnce && numSpawned % numMeteorsPossibleOnScreenAtOnce ==0){
						meteorsFallLeftToRight = !meteorsFallLeftToRight;					
					}
	
					int myXPosition;
					if(meteorsFallLeftToRight){
						myXPosition = width * currentMeteor;
					}else{
						myXPosition = (int) (MainActivity.getWidthPixels()- (width * (currentMeteor+1) ) );
					}
					met.setX(myXPosition);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawningHandler.postDelayed(this,millisecondsBetweenEachMeteor);
	//						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
				}
			}
		);
	}
//	
//	/**
//	 * Spawn entities that take a Context and difficulty (int) parameter in their constructor.
//	 * Supported classes are
//	 *  Orbiter_CircleView Orbiter_Circle_SwitchDirectionOnHitView Orbiter_Circle_IncSpeedOnHitView
//	 *  Orbiter_RectangleView Orbiter_TriangleView 
//	 *  Gravity_MeteorView Meteor_SidewaysView
//	 *  Shooting_DiagonalMovingView
//	 *  Shooting_PauseAndMove
//	 * @param numEnemies
//	 * @param millisecondsBetweenEachSpawn
//	 * @param c
//	 * @return
//	 */
//	public final SpawnableWave spawnEnemiesWithDefaultConstructorArguments(final int numEnemies, final int millisecondsBetweenEachSpawn,final Class c){
//		KillableRunnable r = new KillableRunnable(){
//			private int numSpawned=0;
//			
//			@Override
//			public void doWork() {
//				spawnDefaultEnemy(c);
//				numSpawned++;
//				
//				if(numSpawned<numEnemies){
//					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
//				}
//			}
//		};
//		return new SpawnableWave(r,0,probabilityWeight );
//	}
//	public final SpawnableWave spawnEnemiesWithDefaultConstructorArguments(final int numEnemies,final Class c){
//		return spawnEnemiesWithDefaultConstructorArguments(numEnemies, WAVE_SPAWNER_WAIT/numEnemies, c);
//	}
	public final SpawnableWave spawnEnemyWithDefaultConstructorArugments(final Class c,int probabilityWeight){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork(){
				spawnDefaultEnemy(c);
			}
		};
		return new SpawnableWave(r,0,probabilityWeight );
	}
	private void spawnDefaultEnemy(Class c){
		try {
			Class [] constructorArgs = new Class[] {Context.class,int.class}; //get constructor with list of arguments
			c.getDeclaredConstructor(constructorArgs).newInstance(ctx,difficulty()); //instantiate the passed class with parameters
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	//orbiters
	public final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCols){
		
		spawningHandler.post(
		new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				final int currentShip = numSpawned % numCols ;
				final int width  = (int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCols - width ) / 2;
				final double height = ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height);	
				final int orbitX= ( width/2 ) * (2*currentShip+1) + radius * (2*currentShip +1);
				final int orbitY=Orbiter_CircleView.DEFAULT_ORBIT_Y;
				new Orbiter_CircleView(ctx,Orbiter_CircleView.DEFAULT_SCORE,Orbiter_CircleView.DEFAULT_SPEED_Y,
						Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE,
						Orbiter_CircleView.DEFAULT_HEALTH,Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
						(int)orbitX,(int)orbitY,
						(int)width, (int)height,
						Orbiter_CircleView.DEFAULT_BACKGROUND,
						(int)radius,10);
								
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawningHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
}
