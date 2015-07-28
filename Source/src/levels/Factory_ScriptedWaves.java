package levels;

import interfaces.GameActivityInterface;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_Rectangle_Array;
import enemies_orbiters.Orbiter_TriangleView;
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
	
//	ConditionalHandler conditionalHandler;
	
	public Factory_ScriptedWaves(Context context) { 
		super(context);
	}
			
	final  KillableRunnable doNothing(){
		return new KillableRunnable(){
			@Override
			public void doWork() {}
		};
	}

	//meteor showers
	final KillableRunnable meteorShowersThatForceUserToMiddle(){
		return new KillableRunnable(){//this does not last a whole wave, which is fine.
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors/=2;
				numMeteors-=2;
		//			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
				
				spawnMeteorShower(numMeteors,400,true);
				spawnMeteorShower(numMeteors,400,false);
				
			}
		}; 
	}
	final KillableRunnable meteorShowersThatForceUserToRight(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,true);
				
			}
		};
	}
	final KillableRunnable meteorShowersThatForceUserToLeft(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
				numMeteors-=4;
				spawnMeteorShower(numMeteors,400,false);
				
			}
		};
	}	
	//array shooter waves
	final KillableRunnable refreshArrayShooters(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				Orbiter_Rectangle_Array.refreshSimpleShooterArray(ctx,
						difficulty() );
			}
		};
	}
	//tracking waves
	final KillableRunnable trackingEnemy(){
		final int numEnemies = 4;
		final int millisecondsBetweenEachSpawn = LEVEL_SPAWNER_WAIT/4;
		
		return new KillableRunnable(){
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
	}
	
	//circular orbiters
	final KillableRunnable circlesThreeOrbiters(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				spawnCircularOrbiterWave(6,500,3);
				
			} 
		};
	}
	final KillableRunnable circlesTwoOrbiters(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				spawnCircularOrbiterWave(6,500,2);
				
			} 
		};
	}
	final KillableRunnable circlesOneOrbiters(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				spawnCircularOrbiterWave(9,500,1);
				
			} 
		};
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
	
	/**
	 * Spawn entities that take a Context and difficulty (int) parameter in their constructor.
	 * Supported classes are
	 *  Orbiter_CircleView Orbiter_Circle_SwitchDirectionOnHitView Orbiter_Circle_IncSpeedOnHitView
	 *  Orbiter_RectangleView Orbiter_TriangleView 
	 *  Gravity_MeteorView Meteor_SidewaysView
	 *  Shooting_DiagonalMovingView
	 *  Shooting_PauseAndMove
	 * @param numEnemies
	 * @param millisecondsBetweenEachSpawn
	 * @param c
	 * @return
	 */
	public final KillableRunnable spawnEnemiesWithDefaultConstructorArguments(final int numEnemies, final int millisecondsBetweenEachSpawn,final Class c){
		return new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				spawnDefaultEnemy(c);
				numSpawned++;
				
				if(numSpawned<numEnemies){
					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
				}
			}
		};
	}
	public final KillableRunnable spawnEnemiesWithDefaultConstructorArguments(final int numEnemies,final Class c){
		return spawnEnemiesWithDefaultConstructorArguments(numEnemies, LEVEL_SPAWNER_WAIT/numEnemies, c);
	}
	public final KillableRunnable spawnEnemyWithDefaultConstructorArugments(final Class c){
		return new KillableRunnable(){
			@Override
			public void doWork(){
				spawnDefaultEnemy(c);
			}
		};
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
		
//		KillableRunnable r = new KillableRunnable(){
//			private int numSpawned=0;
//			final double width  = ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
//			final double height = ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height);	
//			final double rTemp= (int)( MainActivity.getWidthPixels()/numCols-width ) / 2;
//			final double radius = (rTemp > Orbiter_CircleView.MAX_RADIUS) ? Orbiter_CircleView.MAX_RADIUS : rTemp;	
//			final int numColsPossible = (int) (MainActivity.getWidthPixels() / (width*2 + radius*2));
//			final int numRowsPossible = (int) ((MainActivity.getHeightPixels() - Orbiter_CircleView.DEFAULT_ORBIT_Y) / (height*2 + radius*2));
//
//			
//			@Override
//			public void doWork() {				
//				if(numSpawned<numRowsPossible*numColsPossible){
//					final int myRow = numSpawned / numColsPossible;
//					final int myCol = numSpawned % numColsPossible ;
//					final double orbitX= ( width/2 ) * (2*myCol) + radius * (2*myCol +1);
//					final double orbitY = (height/2)* (2*myRow) + radius *(2*myRow+1) + Orbiter_CircleView.DEFAULT_ORBIT_Y;
//					
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
