package levels;

import helpers.KillableRunnable;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_Rectangle_Array;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every KillableRunnable MUST check for !isLevelPaused() on doWork()
 * 
 * @author JAMES LOWREY
 *  
 */

public abstract class Factory_ScriptedWaves extends AttributesOfLevels{
		
	private final static int WAIT_TIME_AFTER_METEOR_WAVE = 4500;
	
	public Factory_ScriptedWaves(RelativeLayout layout) { 
		super(layout);
	}
			
	public final static SpawnableWave doNothing(){
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
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors/=2;
				numMeteors-=2;
				
				spawnMeteorShower(numMeteors,400,true);
				spawnMeteorShower(numMeteors,400,false);
				
			}
		}; 

		return new SpawnableWave(r,WAIT_TIME_AFTER_METEOR_WAVE / 2,// half as tall as the other 2 meteor waves
				Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
	}
	final SpawnableWave meteorShowersThatForceUserToRight(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,true);
				
			}
		};

		return new SpawnableWave(r,WAIT_TIME_AFTER_METEOR_WAVE,
				Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
	}
	final SpawnableWave meteorShowersThatForceUserToLeft(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,false);
				
			}
		};

		return new SpawnableWave(r,WAIT_TIME_AFTER_METEOR_WAVE,
				Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
	}	
	//shooter waves
	final SpawnableWave refreshArrayShooters(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Orbiter_Rectangle_Array.refreshSimpleShooterArray(gameScreen,
						getLevel() );
			}
		};
		
		
		return new SpawnableWave(r,8000/((getLevel()/5)+1),Orbiter_Rectangle_Array.getSpawningProbabilityWeight(getLevel()) );
	}
	
	final SpawnableWave lotsOfDiagonals(){
		final int DELAY_BTW_DIAGONALS = 1000;
		final int lvl = getLevel();
		
		int numDiagonalEnemies = 0;
		if(lvl < LEVELS_MED){ //choose how many diagonal enemies spawn
			numDiagonalEnemies = 5;
		}else if (lvl < LEVELS_HIGH){
			numDiagonalEnemies = 8;
		}else {
			numDiagonalEnemies = 13;
		}
		
		final int numDiagonalEnemiesCopy = numDiagonalEnemies;
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {				
				for(int i = 0; i < numDiagonalEnemiesCopy; i++){
					spawningHandler.postDelayed(new KillableRunnable(){//spawn a diagonal moving  enemy every DELAY interval
						@Override
						public void doWork() {
							spawnDefaultEnemy(Shooting_DiagonalMovingView.class);
						}
					}, DELAY_BTW_DIAGONALS * i);
				}
			}
		};
		
		return new SpawnableWave(r, DELAY_BTW_DIAGONALS*numDiagonalEnemies + 4000, 
				Shooting_DiagonalMovingView.getSpawningProbabilityWeightForLotsOfDiagonals(lvl) );		
	}	

	//generic waves
	public final SpawnableWave spawnEnemyWithDefaultConstructorArugments(final Class c,int probabilityWeight){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork(){
				spawnDefaultEnemy(c);
			}
		};
		return new SpawnableWave(r,1050,probabilityWeight );
	}
	private void spawnDefaultEnemy(Class c){
		try {
			Class [] constructorArgs = new Class[] {RelativeLayout.class,int.class}; //get constructor with list of arguments
			c.getDeclaredConstructor(constructorArgs).newInstance(gameScreen,getLevel()); //instantiate the passed class with parameters
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	//SPECIAL SPAWNING WAVES
	final SpawnableWave lotsOfCircles(){
		final int CIRCLE_SPAWN_DELAY = 3500;
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				spawningHandler.postDelayed(spawnCircularOrbiterWave(6,500,3),CIRCLE_SPAWN_DELAY);
				spawningHandler.postDelayed(spawnCircularOrbiterWave(7,500,2),CIRCLE_SPAWN_DELAY);
				spawningHandler.postDelayed(spawnCircularOrbiterWave(9,500,1),CIRCLE_SPAWN_DELAY);
			} 
		};
		
		return new SpawnableWave(r,(long) (CIRCLE_SPAWN_DELAY * 3.7),0);
	}
	
	
	//HELPER WAVES
	private final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		spawningHandler.post(
				new KillableRunnable(){
				
				private int numSpawned=0;
				private boolean meteorsFallLeftToRight = beginOnLeft;
				
				@Override
				public void doWork() {
					//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
					Gravity_MeteorView  met= new Gravity_MeteorView(gameScreen,getLevel() );
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
					}
				}
			}
		);
	}
	
	private final KillableRunnable spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCols){
		return new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				final int currentShip = numSpawned % numCols ;
				final int width  = (int)ctx().getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCols - width ) / 2;
				final double height = ctx().getResources().getDimension(R.dimen.ship_orbit_circular_height);	
				final int orbitX= ( width/2 ) * (2*currentShip+1) + radius * (2*currentShip +1);
				final int orbitY=Orbiter_CircleView.DEFAULT_ORBIT_Y;
				new Orbiter_CircleView(gameScreen,getLevel(),
						Orbiter_CircleView.DEFAULT_SCORE,Orbiter_CircleView.DEFAULT_SPEED_Y,
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
		};
	}
	
}
