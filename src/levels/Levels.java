package levels;

import support.KillableRunnable;
import android.content.Context;

public class Levels extends Factory_Bosses{
		
	public Levels(Context context) {
		super(context);
	}

	public int getMaxLevel() {
		return levels.length - 1;
	}
	public int getCurrentLevelLengthMilliseconds(){
		return levels[getLevel()].length*DEFAULT_WAVE_DURATION;
	}
	public int getNumWavesInLevel(int level){
		if(level>=0 && level<levels.length){
			return levels[level].length;			
		}else{
			return 0;		
		}
	}
	
	@Override
	public boolean areLevelWavesCompleted(){
		return getWave()==levels[getLevel()].length;
	}
	
	/**
	 * DOES NOT WORK. When exit activity and re-enter quickly
	 */
	public void startLevelSpawning(){
		
		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				if (!areLevelWavesCompleted()) {//check isKilled() to ensure Runnable cannot progress
					spawningHandler.post(levels[getLevel()][getWave()]);
					incrementWave();
					spawningHandler.postDelayed(this,DEFAULT_WAVE_DURATION);
				}
			}
			
		});
	}
	 
	
	//levels defined in terms of 5second  waves
	private KillableRunnable levels[][] ={

			//TEST LEVEL
			{
				boss1,
				doNothing,
				doNothing
			},
			
			
			{meteorSidewaysForWholeLevel,			
				meteorSidewaysForWholeLevel,
				meteorShowersThatForceUserToMiddle,
				meteorShowersThatForceUserToLeft,
				meteorShowersThatForceUserToRight,
				meteorShowersThatForceUserToLeft
				},
					
			{meteorSidewaysForWholeLevel,			
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
				},
					
			{meteorSidewaysForWholeLevel,			
				meteorShowersThatForceUserToMiddle,
				meteorShowersThatForceUserToMiddle,
				meteorShowersThatForceUserToMiddle,
				diagonalFullScreen,
				diagonalFullScreen,
				diagonalColumns,
				diagonalColumns
				},
					
			{meteorSidewaysForWholeLevel,			
				meteorShowersThatForceUserToMiddle,
				refreshArrayShooters,
				doNothing,
				doNothing,
				doNothing,
				},
				
			{meteorSidewaysForWholeLevel,			
				meteorShowersThatForceUserToMiddle,
				refreshArrayShooters,
				meteorShowersThatForceUserToMiddle,
				doNothing,
				doNothing,
				doNothing,
				diagonalFullScreen,
				diagonalColumns
				},
					
			{meteorSidewaysForWholeLevel,			
				meteorSidewaysThisWave,
				meteorShowersThatForceUserToMiddle,
				refreshArrayShooters,
				doNothing,
				doNothing,
				trackingEnemy,
				trackingEnemy,
				doNothing,
				doNothing,
				trackingEnemy,
				doNothing,
				doNothing,
				trackingEnemy
				},
				
			{meteorSidewaysForWholeLevel,			
				meteorShowersThatForceUserToRight,	
				meteorShowersThatForceUserToLeft,
				refreshArrayShooters,
				doNothing,
				doNothing,
				doNothing,
				doNothing,
				diagonalColumns,
				boss1,
				doNothing,
				doNothing,
				trackingEnemy,
				doNothing,
				trackingEnemy
				},
					
			{meteorSidewaysForWholeLevel,					
				boss1,
				meteorsGiantAndSideways,
				meteorsGiantAndSideways,
				meteorShowersThatForceUserToMiddle,
				trackingAcceleratingEnemy,
				trackingAcceleratingEnemy,
				trackingAcceleratingEnemy,
				boss1,
				trackingAcceleratingEnemy,
				trackingAcceleratingEnemy
				},
				
			{meteorSidewaysForWholeLevel,					
				boss1,
				doNothing,
				doNothing,
				doNothing,
				meteorShowersThatForceUserToMiddle,
				trackingEnemy,
				trackingEnemy,
				doNothing,
				doNothing,
				meteorShowersThatForceUserToMiddle,
				boss2
				}
			};
	
	
}
