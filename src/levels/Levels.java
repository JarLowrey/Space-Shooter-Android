package levels;

import support.ConditionalHandler;
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
	public boolean areLevelWavesCompleted(){
		return getWave()==levels[getLevel()].length /*&& ! currentlySpawningSomeWave*/;
	}
	
	KillableRunnable startLevel;
	
	/**
	 * DOES NOT WORK. When exit activity and re-enter quickly
	 */
	public void startLevelSpawning(){
		if(startLevel!=null){startLevel.kill();}//prevent previous previous Runnables from spawning
		
		startLevel = new KillableRunnable() {
			@Override
			public void doWork() {
				if (!isLevelPaused() && !areLevelWavesCompleted()) {//check isKilled() to ensure Runnable cannot progress
					ConditionalHandler.postIfCondition(levels[getLevel()][getWave()], !isLevelPaused() && !areLevelWavesCompleted());
					incrementWave();
					ConditionalHandler.postIfCondition(this,DEFAULT_WAVE_DURATION, !isLevelPaused() && !areLevelWavesCompleted());
				}
			}
			
		};
		ConditionalHandler.postIfCondition(startLevel, !isLevelPaused() && !areLevelWavesCompleted());
	}
	
	//levels defined in terms of 5second  waves
	final Runnable[] level_0 = {meteorSidewaysForWholeLevel,
			meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToLeft,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft
		}; 
	
	final  Runnable[] level_1 ={meteorSidewaysForWholeLevel,
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
	
	final  Runnable[] level_2 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			diagonalFullScreen,
			diagonalColumns,
			diagonalColumns,
			diagonalColumns
		};
	
	final  Runnable[] level_3 = {meteorSidewaysForWholeLevel,
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
	
	final  Runnable[] level_4 = {meteorSidewaysForWholeLevel,
			meteorSidewaysThisWave,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			trackingEnemy,
			trackingEnemy,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			doNothing,
			trackingEnemy
		};
	
	final  Runnable[] level_5 = {meteorSidewaysForWholeLevel,
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
			trackingEnemy
		};
	
	final Runnable[] level_6 = {
			circlesThreeOrbiters,
			boss2,
			boss3
		};
	
	
	/*
	final Runnable toastyTest = new Runnable(){
		@Override
		public void run() {
			Toast.makeText(ctx, "wave = "+getWave()+" paused = "+!isLevelPaused(), Toast.LENGTH_LONG).show();			
		}
		
	};
	final Runnable[] test_level = {
			toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest,toastyTest
	};
	*/
	
	final Runnable levels[][] ={		/*test_level*/
			level_0,level_1,level_2,level_3,level_4,level_5,level_6};
	
	
}
