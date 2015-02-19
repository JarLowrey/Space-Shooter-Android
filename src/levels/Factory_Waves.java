package levels;

import interfaces.GameActivityInterface;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;
import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_HorizontalLineView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_tracking.Shooting_TrackingView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every runnable MUST check for !isLevelPaused() on run()
 * 
 * @author JAMES LOWREY
 *  
 */

public abstract class Factory_Waves extends AttributesOfLevels{
	
//	ConditionalHandler conditionalHandler;
	boolean currentlySpawningSomeWave;
	
	public Factory_Waves(Context context) { 
		super(context);
	}
			
	protected final  Runnable doNothing = new Runnable(){
		@Override
		public void run() {}
	};
	
	//regular meteors
	
	//meteor waves
	final Runnable meteorSidewaysForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			
		}
	};
	final Runnable meteorsStraightForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			
		}
	};
	final Runnable meteorSidewaysThisWave = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			
		}
	};	
	
	//meteor showers
	final Runnable meteorShowerLong = new Runnable(){//lasts 2 waves
		@Override
		public void run() {
			spawnMeteorShower( (DEFAULT_WAVE_DURATION * 2 )/1000,1000,true);
			
		}
	};
	final Runnable meteorShowersThatForceUserToMiddle = new Runnable(){//this does not last a whole wave, which is fine.
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors/=2;
			numMeteors-=2;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			
			spawnMeteorShower(numMeteors,400,true);
			spawnMeteorShower(numMeteors,400,false);
			
		}
	};
	final Runnable meteorShowersThatForceUserToRight = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			
		}
	};
	final Runnable meteorShowersThatForceUserToLeft = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,false);
			
		}
	};
	
	//array shooters
	
	//array shooter waves
	final Runnable refreshArrayShooters = new Runnable(){
		@Override
		public void run() {
			Shooting_ArrayMovingView.refreshSimpleShooterArray(ctx);
			
		}
	};

	//diagonal Waves
	
	//dive bombers	
	
	//diagonal shooter waves
	final Runnable diagonalColumns = new Runnable(){
		@Override
		public void run() {
			spawnDiveBomberWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			
		}
	};
	final Runnable diagonalFullScreen = new Runnable(){
		@Override
		public void run() {
			spawnFullScreenDiagonalAttackersWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			
		}
	};
	
	//tracking waves
	final Runnable trackingEnemy = new Runnable(){
		@Override
		public void run() {
			spawnTrackingAttackerWave(4,DEFAULT_WAVE_DURATION/4);
			
		}
	};
	
	//circular orbiters
	final Runnable circlesThreeOrbiters = new Runnable(){
		@Override
		public void run() {
			spawnCircularOrbiterWave(6,500,3);
			
		} 
	};

	//spawn enemies over a set period
	
	public final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {

		Runnable r = new Runnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void run() {
				if(!isLevelPaused()){
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
						currentlySpawningSomeWave=true;
						ConditionalHandler.postIfCondition(this,millisecondsBetweenEachMeteor, !isLevelPaused());
//						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}else{
						currentlySpawningSomeWave=false;
					}
				}
			}
		};
		
		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}

	public final void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		
		Runnable r =new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Gravity_MeteorView(ctx);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						currentlySpawningSomeWave=true;
						ConditionalHandler.postIfCondition(this, millisecondsBetweenEachMeteor,!isLevelPaused());
//						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}else{
						currentlySpawningSomeWave=false;
					}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}

	public final  void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Meteor_SidewaysView(ctx);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this,millisecondsBetweenEachMeteor, !isLevelPaused());
//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
		 
	public final void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_DiagonalMovingView(ctx,Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_COLUMNS);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this,millisecondsBetweenEachSpawn, !isLevelPaused());
//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
	
	public final void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_DiagonalMovingView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this,millisecondsBetweenEachSpawn, !isLevelPaused());
//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}

	public final void spawnTrackingAttackerWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_TrackingView(ctx,((GameActivityInterface)ctx).getProtagonist());
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this, millisecondsBetweenEachSpawn,!isLevelPaused());
//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};
		
		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
	
	//orbiters
	public final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCols){
		
//		Runnable r = new Runnable(){
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
//			public void run() {				
//				if(numSpawned<numRowsPossible*numColsPossible){
//					final int myRow = numSpawned / numColsPossible;
//					final int myCol = numSpawned % numColsPossible ;
//					final double orbitX= ( width/2 ) * (2*myCol) + radius * (2*myCol +1);
//					final double orbitY = (height/2)* (2*myRow) + radius *(2*myRow+1) + Orbiter_CircleView.DEFAULT_ORBIT_Y;
//					
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				final int currentShip = numSpawned % numCols ;
				final int width  = (int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCols-width ) / 2;
				final double height = ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height);	
				final int orbitX= ( width/2 ) * (2*currentShip) + radius * (2*currentShip +1);
				final int orbitY=Orbiter_CircleView.DEFAULT_ORBIT_Y;
				new Orbiter_CircleView(ctx,Orbiter_CircleView.DEFAULT_SCORE,Orbiter_CircleView.DEFAULT_SPEED_Y,
						Orbiter_CircleView.DEFAULT_SPEED_X,Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE,
						Orbiter_CircleView.DEFAULT_HEALTH,Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
						(int)orbitX,(int)orbitY,
						(int)width, (int)height,
						Orbiter_CircleView.DEFAULT_BACKGROUND,
						(int)radius,10);
								
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this,millisecondsBetweenEachSpawn, !isLevelPaused());
	//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
	
	public final void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		
		Runnable r  = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_RectangleView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					currentlySpawningSomeWave=true;
					ConditionalHandler.postIfCondition(this, millisecondsBetweenEachSpawn,!isLevelPaused());
//					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}else{
					currentlySpawningSomeWave=false;
				}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
	
	public final void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_TriangleView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						currentlySpawningSomeWave=true;
						ConditionalHandler.postIfCondition(this, millisecondsBetweenEachSpawn,!isLevelPaused());
//						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}else{
						currentlySpawningSomeWave=false;
					}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}
	
	public final void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		Runnable r = new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_HorizontalLineView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						currentlySpawningSomeWave=true;
						ConditionalHandler.postIfCondition(this,millisecondsBetweenEachSpawn, !isLevelPaused());
//						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}else{
						currentlySpawningSomeWave=false;
					}
			}
		};

		ConditionalHandler.postIfCondition(r, !isLevelPaused());
	}

}
