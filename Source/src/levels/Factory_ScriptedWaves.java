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
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors/=2;
				numMeteors-=2;
				
				spawnMeteorShower(numMeteors,400,true);
				spawnMeteorShower(numMeteors,400,false);
				
			}
		}; 

		return new SpawnableWave(r,5000,Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
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

		return new SpawnableWave(r,5000,Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
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

		return new SpawnableWave(r,5000,Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) );
	}	
	//array shooter waves
	final SpawnableWave refreshArrayShooters(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Orbiter_Rectangle_Array.refreshSimpleShooterArray(ctx,
						getLevel() );
			}
		};
		
		
		return new SpawnableWave(r,8000/((getLevel()/5)+1),Orbiter_Rectangle_Array.getSpawningProbabilityWeight(getLevel()) );
	}
	
	//tracking waves
	final SpawnableWave trackingEnemy(){
		final int numEnemies = 3;
		final int millisecondsBetweenEachSpawn = 800;
		
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Shooting_TrackingView(ctx,((GameActivityInterface)ctx).getProtagonist(),getLevel() );
				numSpawned++;
				
				if(numSpawned<numEnemies){
					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
				}
			}
		};
		
		return new SpawnableWave(r,600,Shooting_TrackingView.getSpawningProbabilityWeight(getLevel()));
	}
	

	//generic waves
	public final SpawnableWave spawnEnemyWithDefaultConstructorArugments(final Class c,int probabilityWeight){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork(){
				spawnDefaultEnemy(c);
			}
		};
		return new SpawnableWave(r,1000,probabilityWeight );
	}
	private void spawnDefaultEnemy(Class c){
		try {
			Class [] constructorArgs = new Class[] {Context.class,int.class}; //get constructor with list of arguments
			c.getDeclaredConstructor(constructorArgs).newInstance(ctx,getLevel()); //instantiate the passed class with parameters
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	//SPECIAL SPAWNING WAVES
	final SpawnableWave lotsOfCircles(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				spawnCircularOrbiterWave(6,500,3);
				spawnCircularOrbiterWave(6,500,2);
				spawnCircularOrbiterWave(9,500,1);
			} 
		};
		
		return new SpawnableWave(r,15000,0);
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
					Gravity_MeteorView  met= new Gravity_MeteorView(ctx,getLevel() );
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
	
	private final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCols){
		
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
				new Orbiter_CircleView(ctx,getLevel(),
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
		});
	}
	
}
