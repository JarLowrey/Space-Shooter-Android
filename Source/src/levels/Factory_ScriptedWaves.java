package levels;

import helpers.KillableRunnable;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_Rectangle_Array;
import enemies_orbiters.Shooting_OrbiterView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every KillableRunnable MUST check for !isLevelPaused() on doWork()
 * 
 * @author JAMES LOWREY
 *  
 */

public abstract class Factory_ScriptedWaves extends AttributesOfLevels{
		
	private final static int WAIT_TIME_AFTER_METEOR_WAVE = 3000;
	
	public Factory_ScriptedWaves(RelativeLayout layout) { 
		super(layout);
	}
			
	public final static SpawnableWave doNothing(){
		return new SpawnableWave(0,0){//probability weight of 0, as it should not be randomly used in levelSpawning
				@Override 
				public void spawn(){}
		};
	}

	//meteor showers
	protected SpawnableWave backgroundMeteor(){
		final Class<? extends Gravity_MeteorView> meteorClass = (Math.random()<.5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class ;
		return spawnEnemyWithDefaultConstructorArguments(meteorClass,0 );//probability weight does not matter here
	}
	
	final SpawnableWave meteorShowersThatForceUserToMiddle(){
		return new SpawnableWave(WAIT_TIME_AFTER_METEOR_WAVE / 2,Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) ){
			@Override 
			public void spawn(){
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors/=2;
				numMeteors-=2;
				
				spawnMeteorShower(numMeteors,400,true);
				spawnMeteorShower(numMeteors,400,false);
			}
		};
	}
	final SpawnableWave meteorShowersThatForceUserToRight(){
		return new SpawnableWave(WAIT_TIME_AFTER_METEOR_WAVE,Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) ){
			@Override 
			public void spawn(){
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,true);
			}
		};
	}
	final SpawnableWave meteorShowersThatForceUserToLeft(){
		return new SpawnableWave(WAIT_TIME_AFTER_METEOR_WAVE,
				Gravity_MeteorView.getSpawningProbabilityWeightOfMeteorShowers(getLevel()) ){
			@Override 
			public void spawn(){
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx().getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,false);
			}
		};
	}	
	//shooter waves
	final SpawnableWave refreshArrayShooters(){
		return new SpawnableWave(8000/((getLevel()/5)+1),Orbiter_Rectangle_Array.getSpawningProbabilityWeight(getLevel()) ){
			@Override 
			public void spawn(){
				Orbiter_Rectangle_Array.refreshSimpleShooterArray(gameScreen,
						getLevel() );
			}
		};
	}
	
	//generic waves
	public final SpawnableWave spawnLotsOfEnemiesWithDefaultConstructorArguments(final Class c,
			int probabilityWeight,
			final int howManyEnemies,
			final int delayBtwEachEnemy, 
			int delayAfterSpawningAllEnemies){
		
		return new SpawnableWave(delayAfterSpawningAllEnemies + delayBtwEachEnemy * howManyEnemies,probabilityWeight ){
			@Override 
			public void spawn(){
				for(int i = 0; i < howManyEnemies; i++){
					spawningHandler.postDelayed(new KillableRunnable(){//spawn a diagonal moving  enemy every DELAY interval
						@Override
						public void doWork() {
							spawnDefaultEnemy(c);
						}
					}, delayBtwEachEnemy * i);
				}
			}
		};
	}
	public final SpawnableWave spawnEnemyWithDefaultConstructorArguments(final Class c,int probabilityWeight){
		return new SpawnableWave(1050,probabilityWeight ){
			@Override 
			public void spawn(){
				spawnDefaultEnemy(c);
			}
		};
	}
	
	//SPECIAL SPAWNING WAVES
	final SpawnableWave coordinatedCircularAttack(int level){
		final int CIRCLE_SPAWN_DELAY = 14000;
		
		int probabilityWeight = 0;
		if(level > FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR){
			probabilityWeight = Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) / 70;
		}
		
		return new SpawnableWave((long) (CIRCLE_SPAWN_DELAY * 3.5),probabilityWeight){
			@Override 
			public void spawn(){
				spawningHandler.post(spawnCircularOrbiterWave(4,500,3));
				spawningHandler.postDelayed(spawnCircularOrbiterWave(5,500,2),CIRCLE_SPAWN_DELAY);
				spawningHandler.postDelayed(spawnCircularOrbiterWave(7,500,1),CIRCLE_SPAWN_DELAY * 2);
			}
		};
	}
	
	
	//HELPER WAVES
	private void spawnDefaultEnemy(Class c){
		try {
			Class [] constructorArgs = new Class[] {RelativeLayout.class,int.class}; //get constructor with list of arguments
			c.getDeclaredConstructor(constructorArgs).newInstance(gameScreen,getLevel()); //instantiate the passed class with parameters
		} catch (Exception e){
			Log.d("lowrey","default enemy not spawned with correct constructor arguments!");
			e.printStackTrace();
		}	
	}
	
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
						(int)radius,Shooting_OrbiterView.DEFAULT_ORBIT_TIME);
								
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawningHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		};
	}
	
}
