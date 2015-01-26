package levels;

import interfaces.GameActivityInterface;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_TrackingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_HorizontalLineView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;

/** 
 * spawn a number of a given enemy over a duration of time
 * 
 * Every runnable MUST check for levelPaused on run()
 * 
 * @author JAMES LOWREY
 *  
 */

public class Factory_Waves extends Factory_Bosses{

	public static final int DEFAULT_WAVE_DURATION=5000;
	
	ConditionalHandler conditionalHandler;
	protected boolean levelPaused;
	
	public Factory_Waves(Context context) {
		super(context);
		
		conditionalHandler = new ConditionalHandler(this);
	}

	//get methods
	private int getCurrentLevelLengthMilliseconds(){
		return levels[currentLevel].length*DEFAULT_WAVE_DURATION;
	}
	public int getNumWavesInLevel(int level){
		if(level>=0 && level<levels.length){
			return levels[level].length;			
		}else{
			return 0;		
		}
	}
	public boolean areLevelWavesCompleted(){
		return currentWave==levels[currentLevel].length;
	}
	public boolean isLevelPaused(){
		return levelPaused;
	}
		
	protected final  Runnable doNothing = new Runnable(){
		@Override
		public void run() {currentWave++;}};
	
	//regular meteors
	
		//meteor waves
	final Runnable meteorSidewaysForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			currentWave++;
		}
	};
	final Runnable meteorsStraightForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( getCurrentLevelLengthMilliseconds() /2000 ,2000);
			currentWave++;
		}
	};
	final Runnable meteorSidewaysThisWave = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentWave++;
		}
	};	
	
	//meteor showers
	final Runnable meteorShowerLong = new Runnable(){//lasts 2 waves
		@Override
		public void run() {
			spawnMeteorShower( (DEFAULT_WAVE_DURATION * 2 )/1000,1000,true);
			currentWave++;
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
			currentWave++;
		}
	};
	final Runnable meteorShowersThatForceUserToRight = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			currentWave++;
		}
	};
	final Runnable meteorShowersThatForceUserToLeft = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,false);
			currentWave++;
		}
	};

	//giant meteors
	final Runnable meteorsGiantAndSideways = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentWave++;
		}
	};
	final Runnable meteorsOnlyGiants = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(4,DEFAULT_WAVE_DURATION/4);
			currentWave++;
		}
	};
	
	//array shooters
	
	//array shooter waves
	final Runnable refreshArrayShooters = new Runnable(){
		@Override
		public void run() {
			int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
			
			for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
				new Shooting_ArrayMovingView(ctx);
			}
			currentWave++;
		}
	};

	//diagonal Waves
	
	//dive bombers	
	
	//diagonal shooter waves
	final Runnable diagonalColumns = new Runnable(){
		@Override
		public void run() {
			spawnDiveBomberWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			currentWave++;
		}
	};
	final Runnable diagonalFullScreen = new Runnable(){
		@Override
		public void run() {
			spawnFullScreenDiagonalAttackersWave(3,DEFAULT_WAVE_DURATION/3);//spawn for entire wave
			currentWave++;
		}
	};
	
	//tracking waves
	
	//tracking waves
	final Runnable trackingEnemy = new Runnable(){
		@Override
		public void run() {
			spawnTrackingAttackerWave(4,DEFAULT_WAVE_DURATION/4);
			currentWave++;
		}
	};
	
	//circular orbiters
	final Runnable circlesThreeOrbiters = new Runnable(){
		@Override
		public void run() {
			spawnCircularOrbiterWave(6,500,3);
		} 
	};
	
	//levels defined in terms of 5second  waves
	final Runnable[] level1 = {diagonalFullScreen,diagonalFullScreen,meteorSidewaysForWholeLevel,
			meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToLeft,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft
		}; 
	
	final  Runnable[] level2 ={meteorSidewaysForWholeLevel,
			meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToRight,
			doNothing,
			meteorShowersThatForceUserToLeft,
			meteorsGiantAndSideways,
			meteorsGiantAndSideways,
			meteorShowerLong,
			meteorsOnlyGiants,
			meteorsOnlyGiants
		};
	
	final  Runnable[] level3 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			diagonalColumns,
			diagonalColumns,
			diagonalColumns
		};
	
	final  Runnable[] level4 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			diagonalColumns,
			diagonalColumns,
			doNothing,
			doNothing,
			doNothing
		};
	
	final  Runnable[] level5 = {meteorSidewaysForWholeLevel,
			meteorSidewaysThisWave,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			doNothing,
			diagonalColumns,
			diagonalColumns
		};
	
	final  Runnable[] level6 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			doNothing,
			diagonalColumns,
			doNothing,
			diagonalColumns,
			boss1
		};
	
	final Runnable[] level7 = {
			circlesThreeOrbiters,
			boss1_1,
			boss2,
			boss3
		};
	
	final Runnable levels[][] ={level1,level7};
	
	
	
	
	
	
	public final void spawnMeteorShower(final int numMeteors,final int millisecondsBetweenEachMeteor,final boolean beginOnLeft) {
		conditionalHandler.postIfLevelResumed(new Runnable(){
			
			private int numSpawned=0;
			private boolean meteorsFallLeftToRight = beginOnLeft;
			
			@Override
			public void run() {
					//create a meteor, find how many meteors can possibly be on screen at once, and then find which meteor out of the maxNum is the current one
					Gravity_MeteorView  met= new Gravity_MeteorView(ctx);
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
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final void spawnStraightFallingMeteorsAtRandomXPositionsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Gravity_MeteorView(ctx);
					
					numSpawned++;
					if(numSpawned<numMeteors){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
					}
			}
		});
	}

	public final  void spawnSidewaysMeteorsWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Meteor_SidewaysView(ctx);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				}
			}
		});
	}
	
	public final void spawnGiantMeteorWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				conditionalHandler.postIfLevelResumed(spawnGiantMeteor);
				
				numSpawned++;
				if(numSpawned<numMeteors){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachMeteor);
				} 
			}
		});
	}
		 
	public final void spawnDiveBomberWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_DiagonalMovingView(ctx,Shooting_DiagonalMovingView.DEFAULT_DIVE_BOMBER_COLUMNS);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnFullScreenDiagonalAttackersWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_DiagonalMovingView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}

	public final void spawnTrackingAttackerWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Shooting_TrackingView(ctx,((GameActivityInterface)ctx).getProtagonist());
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	//orbiters
	public final void spawnCircularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn,final int numCirclesOnScreen){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				final int currentShip = numSpawned % numCirclesOnScreen ;
				final int width  = (int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width);
				final int radius= (int)( MainActivity.getWidthPixels()/numCirclesOnScreen-width ) / 2;
				final int orbitX= ( width/2 ) * (2*currentShip) + radius * (2*currentShip +1);
				final int orbitY=Orbiter_CircleView.DEFAULT_ORBIT_Y;
				
				new Orbiter_CircleView(ctx,Orbiter_CircleView.DEFAULT_SCORE,Orbiter_CircleView.DEFAULT_SPEED_Y,
						Orbiter_CircleView.DEFAULT_SPEED_X,Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE,
						Orbiter_CircleView.DEFAULT_HEALTH,Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
						orbitX,orbitY,
						width, 
						(int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height),
						Orbiter_CircleView.DEFAULT_BACKGROUND,radius,10);
								
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnRectangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_RectangleView(ctx);
				numSpawned++;
				
				if(numSpawned<totalNumShips){
					conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
				}
			}
		});
	}
	
	public final void spawnTriangularOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_TriangleView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}
			}
		});
	}
	
	public final void spawnHorizontalOrbiterWave(final int totalNumShips, final int millisecondsBetweenEachSpawn){
		conditionalHandler.postIfLevelResumed(new Runnable(){
			private int numSpawned=0;
			
			@Override
			public void run() {
				new Orbiter_HorizontalLineView(ctx);
					numSpawned++;
					
					if(numSpawned<totalNumShips ){
						conditionalHandler.postIfLevelResumed(this,millisecondsBetweenEachSpawn);
					}
			}
		});
	}

}
