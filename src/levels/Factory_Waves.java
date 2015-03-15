package levels;

import interfaces.GameActivityInterface;
import parents.Moving_ProjectileView;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_HorizontalMovement;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_tracking.Shooting_TrackingView;
import enemies_tracking.Tracking_AcceleratingView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every KillableRunnable MUST check for !isLevelPaused() on doWork()
 * 
 * @author JAMES LOWREY
 *  
 */

public abstract class Factory_Waves extends AttributesOfLevels{
	
//	ConditionalHandler conditionalHandler;
	
	public Factory_Waves(Context context) { 
		super(context);
	}
			
	protected final  KillableRunnable doNothing = new KillableRunnable(){
		@Override
		public void doWork() {}
	};
	
	//regular meteors
	
	//meteor waves
	final KillableRunnable meteorSidewaysForWholeLevel = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnSidewaysMeteorsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			
		}
	};
	final KillableRunnable meteorsStraightForWholeLevel = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			
		}
	};
	final KillableRunnable meteorSidewaysThisWave = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			
		}
	};	
	
	//meteor showers
	final KillableRunnable meteorShowerLong = new KillableRunnable(){//lasts 2 waves
		@Override
		public void doWork() {
			spawnMeteorShower( (DEFAULT_WAVE_DURATION * 2 )/1000,1000,true);
			
		}
	};
	final KillableRunnable meteorShowersThatForceUserToMiddle = new KillableRunnable(){//this does not last a whole wave, which is fine.
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
	final KillableRunnable meteorShowersThatForceUserToRight = new KillableRunnable(){
		@Override
		public void doWork() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			
		}
	};
	final KillableRunnable meteorShowersThatForceUserToLeft = new KillableRunnable(){
		@Override
		public void doWork() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,false);
			
		}
	};
	
	//array shooters
	
	//array shooter waves
	final KillableRunnable refreshArrayShooters = new KillableRunnable(){
		@Override
		public void doWork() {
			Shooting_ArrayMovingView.refreshSimpleShooterArray(ctx);
			
		}
	};

	//diagonal Waves
	
	//dive bombers	
	
	//diagonal shooter waves
	final KillableRunnable diagonalColumns = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnDiveBomberWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			
		}
	};
	final KillableRunnable diagonalFullScreen = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnFullScreenDiagonalAttackersWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			
		}
	};
	
	//tracking waves
	final KillableRunnable trackingEnemy = new KillableRunnable(){
		@Override
		public void doWork() {
//			spawnTrackingAttackerWave(4,DEFAULT_WAVE_DURATION/4);
			spawnTrackingEnemyOverTime(4,DEFAULT_WAVE_DURATION/4,Shooting_TrackingView.class);
		}
	};
	final KillableRunnable trackingAcceleratingEnemy = new KillableRunnable(){
		@Override
		public void doWork() {
//			spawnTrackingAttackerWave(4,DEFAULT_WAVE_DURATION/4);
			spawnTrackingEnemyOverTime(4,DEFAULT_WAVE_DURATION/4,Tracking_AcceleratingView.class);
			
		}
	};
	
	//circular orbiters
	final KillableRunnable circlesThreeOrbiters = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnCircularOrbiterWave(6,500,3);
			
		} 
	};

	//spawn enemies over a set period
	public final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {

		KillableRunnable r = new KillableRunnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void doWork() {
				//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
				Gravity_MeteorView  met= new Gravity_MeteorView(ctx);
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
		};
		
		spawningHandler.post(r);
	}

	public final void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		
		KillableRunnable r =new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Gravity_MeteorView(ctx);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawningHandler.postDelayed(this, millisecondsBetweenEachMeteor);
					}
			}
		};

		spawningHandler.post(r);
	}

	public final  void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Meteor_SidewaysView(ctx);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					spawningHandler.postDelayed(this,millisecondsBetweenEachMeteor);
				}
			}
		};

		spawningHandler.post(r);
	}
		 
	public final void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Shooting_DiagonalMovingView(ctx,Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_COLUMNS);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawningHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		};

		spawningHandler.post(r);
	}
	
	public final void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Shooting_DiagonalMovingView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawningHandler.postDelayed(this,millisecondsBetweenEachSpawn);
				}
			}
		};

		spawningHandler.post(r);
	}
	
	/**
	 * 
	 * @param numEnemies
	 * @param millisecondsBetweenEachSpawn
	 * @param c-must be of type Shooting_TrackingView, or a child with an equal constructor
	 */
	public final void spawnTrackingEnemyOverTime(final int numEnemies, final int millisecondsBetweenEachSpawn,final Class c){
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				try {
					Class [] constructorArgs = new Class[] {Context.class,Moving_ProjectileView.class};
					c.getDeclaredConstructor(constructorArgs).newInstance(ctx,((GameActivityInterface)ctx).getProtagonist());
				} catch (Exception e){
					e.printStackTrace();
				}
				numSpawned++;
				
				if(numSpawned<numEnemies){
					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
				}
			}
		};
		
		spawningHandler.post(r);
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
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				final int currentShip = numSpawned % numCols ;
				final int width  = (int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCols-width ) / 2;
				final double height = ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height);	
				final int orbitX= ( width/2 ) * (2*currentShip) + radius * (2*currentShip +1);
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
		};

		spawningHandler.post(r);
	}
	
	
	
	
	public final KillableRunnable rect = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnRectangularOrbiterWave(5,1000);
		}
	};

	public final KillableRunnable tri = new KillableRunnable(){
		@Override
		public void doWork() {
			spawnTriangularOrbiterWave(5,1000);
		}
	};
	
	
	
	public final void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		
		KillableRunnable r  = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Orbiter_RectangleView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
				}
			}
		};

		spawningHandler.post(r);
	}
	
	public final void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Orbiter_TriangleView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						spawningHandler.postDelayed(this, millisecondsBetweenEachSpawn);
					}
			}
		};

		spawningHandler.post(r);
	}
	
	public final void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		KillableRunnable r = new KillableRunnable(){
			private int numSpawned=0;
			
			@Override
			public void doWork() {
				new Shooting_HorizontalMovement(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						spawningHandler.postDelayed(this,millisecondsBetweenEachSpawn);
					}
			}
		};

		spawningHandler.post(r);
	}

}
