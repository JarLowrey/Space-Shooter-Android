package levels;

import android.widget.RelativeLayout;
import enemies.EnemyView;
import enemies.HorizontalMovement_FinalBoss;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_DurationLaserView;
import enemies.Shooting_PauseAndMove;
import enemies.Shooting_SpasticView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_tracking.Shooting_TrackingView;
import enemies_tracking.Tracking_AcceleratingView;

public class LevelSpawner extends Factory_Bosses{
	
	private int
		scoreThresholdForSpawningMoreMeteors,
		scoreThresholdForSpawningMoreEnemies;
	private long timeUntilCanSpawnNextWave = 0,
		timeSinceSpawnedLastWave = 0;
	public SpawnableWave[] allSpawnableWaves;
	 
	public LevelSpawner(RelativeLayout layout) {
		super(layout);
	}
	
	public void initializeLevelEndCriteriaAndSpawnFirstEnemy(){
		timeSpentOnThisLevel = 0;
		
		initLevelEndCriteriaAndSpawningThresholds();
		reinitializeAllSpawnableWaves();

		//spawn any special enemies at the beginning of the level
		for(SpawnableWave wave : SpecialSpawnableLevel.specialSpawnableWavesForThisLevel(getLevel()) ){
			wave.spawn();
		}
	}
	
	public void spawnEnemiesIfPossible(){		
		timeSinceSpawnedLastWave += WAVE_SPAWNER_WAIT;
		
		if ( !isLevelFinishedSpawning() ) {
			if(canSpawnMoreMeteors()){
				backgroundMeteor().spawn();	
			}
			if( canSpawnMoreEnemies() ){						
				SpawnableWave nextSpawn = SpawnableWave.getRandomWaveUsingWeightedProbabilities();
				
				nextSpawn.spawn();				

				//reset the spawn timers
				timeUntilCanSpawnNextWave = nextSpawn.howLongUntilCanSpawnAgain();
				timeSinceSpawnedLastWave = 0;
			}
		}
	}
	
	//HELPER METHODS
	private void initLevelEndCriteriaAndSpawningThresholds(){
		/*  
			Algorithm: Include the previous measure and then increase by the marginal amount. Return relevant amount.
		*/
		timeNeededToEndLevel = 1000;//milliseconds
		
		//find how many seconds level will last
		final int begLevels = 15 + Math.min(LEVELS_BEGINNER, getLevel() ) * 8; 						
		final int lowLevels = (int) (begLevels +  ( Math.min(LEVELS_LOW, getLevel() ) - LEVELS_BEGINNER ) * 5);
		final int medLevels = (int) (lowLevels +  ( Math.min(LEVELS_MED, getLevel() ) - LEVELS_LOW ) * 3);
		final int highLevels = (int) (medLevels + ( Math.min(LEVELS_HIGH, getLevel() ) - LEVELS_MED ) * 2 );
		final int allOtherLevels = (int) (highLevels + ( getLevel() - LEVELS_HIGH ) );
		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			timeNeededToEndLevel *= begLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			timeNeededToEndLevel *= lowLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			timeNeededToEndLevel *= medLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			timeNeededToEndLevel *= highLevels;
		}else{
			timeNeededToEndLevel *= allOtherLevels;
		}		
		timeNeededToEndLevel = (long) Math.min(timeNeededToEndLevel, 60 * 1.5 * 1000);// max = 1.5 minutes
		
		
		initThresholdForSpawningMoreEnemiesAndMeteors();
	}
	
	public int getPercentageLeftInLevel(){  
		double levelProgress = ( ( (double)scoreGainedThisLevel() ) / timeNeededToEndLevel ) * 100;
		return Math.max(0, 100 - (int) levelProgress);
	}
	private boolean canSpawnMoreEnemies(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreThresholdForSpawningMoreEnemies && 
						timeSinceSpawnedLastWave >= timeUntilCanSpawnNextWave;
	}
	private boolean canSpawnMoreMeteors(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreThresholdForSpawningMoreMeteors;		
	}
	private void initThresholdForSpawningMoreEnemiesAndMeteors(){
		/*  score needed is in terms of how many diagonal moving views can be on screen at one time.
			must scale score as the diagonal moving views (and all other enemies) scale score.
			Each class of levels has a different scaling factor, ie every 2-20 levels the # on screen increases.
			Algorithm: Include the previous measure and then increase by the marginal amount. Return relevant amount.
		*/
		final int diagScore = EnemyView.scaleScore( getLevel() , Shooting_DiagonalMovingView.DEFAULT_SCORE);
		final int meteorScore = EnemyView.scaleScore( getLevel() , Shooting_DiagonalMovingView.DEFAULT_SCORE);
		
		final int minimumNumEnemies = meteorScore * 5 + diagScore * 2; 														//bare minimum, 5 meteors and 2 diagonal shooters can be on screen.
		final int begLevels = minimumNumEnemies + diagScore * Math.min(LEVELS_BEGINNER, getLevel() ) ; 						//by end of beg levels, 4 diagShooters + 5 meteors should be on screen
		final int lowLevels = (int) (begLevels + diagScore * ( Math.min(LEVELS_LOW, getLevel() ) - LEVELS_BEGINNER ) / 4);	//by end of low levels, 6 diagShooters + 5 meteors should be on screen
		final int medLevels = (int) (lowLevels + diagScore * ( Math.min(LEVELS_MED, getLevel() ) - LEVELS_LOW ) / 5);		//by end of med levels, 9 diagShooters + 5 meteors should be on screen
		final int highLevels = (int) (medLevels + diagScore * ( Math.min(LEVELS_HIGH, getLevel() ) - LEVELS_MED ) / 6.5);	//by end of high levels, 12 diagShooters + 5 meteors should be on screen
		int allOtherLevels = highLevels + diagScore * ( getLevel() - LEVELS_HIGH ) / 8;										//increase takes longer than other levels. 
		allOtherLevels = Math.min(allOtherLevels, meteorScore * 5 + diagScore * 15);										//MAX: 15 diagShooters + 5 meteors should be on screen
		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreThresholdForSpawningMoreEnemies = begLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreThresholdForSpawningMoreEnemies = lowLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			scoreThresholdForSpawningMoreEnemies = medLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			scoreThresholdForSpawningMoreEnemies = highLevels;
		}else{
			scoreThresholdForSpawningMoreEnemies = allOtherLevels;
		}
		
		initThresholdForSpawningMoreMeteors();
	}
	private void initThresholdForSpawningMoreMeteors(){		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreThresholdForSpawningMoreMeteors = scoreThresholdForSpawningMoreEnemies * 5;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreThresholdForSpawningMoreMeteors = scoreThresholdForSpawningMoreEnemies * 2;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			scoreThresholdForSpawningMoreMeteors = (int) (scoreThresholdForSpawningMoreEnemies * 1.5);
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			scoreThresholdForSpawningMoreMeteors = (int) (scoreThresholdForSpawningMoreEnemies * 1.3);
		}else{
			scoreThresholdForSpawningMoreMeteors = (int) (scoreThresholdForSpawningMoreEnemies * 1.1);
		}
	}
	
	/**
	 * SpawnableWave objects need to be reinitialized at beginning of every level.
	 * This is due to the fact their probability weighting is a function of level, and as such changes
	 * as levels increment. Also, KillableRunnables are destroyed and unusable whenever onPause is called in GameActivity
	 */
	private void reinitializeAllSpawnableWaves(){
		final int lvl = getLevel();
		
		//Add spawnable waves. Needs to be done at every level for probability weights to be correct
		final SpawnableWave[] ALL_WAVES = {
			//spawnables with some sort of special logic
			meteorShowersThatForceUserToLeft(),
			meteorShowersThatForceUserToRight(),
			meteorShowersThatForceUserToMiddle(),
			spawnGiantMeteor(),
			refreshArrayShooters(),
			coordinatedCircularAttack(lvl),
			
			//use default spawnable for an enemy >1 times
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_DiagonalMovingView.class,
					Shooting_DiagonalMovingView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Shooting_DiagonalMovingView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Shooting_DiagonalMovingView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Shooting_DiagonalMovingView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_TrackingView.class,
					Shooting_TrackingView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Shooting_TrackingView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Shooting_TrackingView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Shooting_TrackingView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_SpasticView.class,
					Shooting_SpasticView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Shooting_SpasticView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Shooting_SpasticView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Shooting_SpasticView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_PauseAndMove.class,
					Shooting_PauseAndMove.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Shooting_PauseAndMove.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Shooting_PauseAndMove.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Shooting_PauseAndMove.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_DurationLaserView.class,
					Shooting_DurationLaserView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Shooting_DurationLaserView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Shooting_DurationLaserView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Shooting_DurationLaserView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_RectangleView.class,
					Orbiter_RectangleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Orbiter_RectangleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Orbiter_RectangleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Orbiter_RectangleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_CircleView.class,
					Orbiter_CircleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Orbiter_CircleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Orbiter_CircleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Orbiter_CircleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),
			spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_TriangleView.class,
					Orbiter_TriangleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
					Orbiter_TriangleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
					Orbiter_TriangleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
					Orbiter_TriangleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
					),   
					
			//spawn 1 default enemy
			spawnEnemyWithDefaultConstructorArguments(Shooting_DurationLaserView.class,
					Shooting_DurationLaserView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Shooting_SpasticView.class,
					Shooting_SpasticView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Shooting_TrackingView.class,
					Shooting_TrackingView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Tracking_AcceleratingView.class,
					Tracking_AcceleratingView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Shooting_DiagonalMovingView.class,
					Shooting_DiagonalMovingView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Shooting_PauseAndMove.class,
					Shooting_PauseAndMove.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Orbiter_CircleView.class,
					Orbiter_CircleView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Orbiter_TriangleView.class,
					Orbiter_TriangleView.getSpawningProbabilityWeight(lvl)),
			spawnEnemyWithDefaultConstructorArguments(Orbiter_RectangleView.class,
					Orbiter_RectangleView.getSpawningProbabilityWeight(lvl)),

			//bosses
			boss1(),
			boss2(),
			boss3(),
			boss4(),
			spawnEnemyWithDefaultConstructorArguments(HorizontalMovement_FinalBoss.class,HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(lvl) )
		};		
		SpawnableWave.initializeSpawnableWaves(ALL_WAVES);
		
		
		//add special spawnablewaves. Needs to be done after KillableRunnable.killAll() is called
		final SpecialSpawnableLevel[] ALL_SPECIAL_SPAWNABLE_LEVELS = {
			//bosses 1-5
			new SpecialSpawnableLevel(spawnEnemyWithDefaultConstructorArguments(
					HorizontalMovement_FinalBoss.class,
					HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(lvl) ),
					FIRST_LEVEL_BOSS5_APPEARS),
			new SpecialSpawnableLevel(boss4(), FIRST_LEVEL_BOSS4_APPEARS),
			new SpecialSpawnableLevel(boss3(), FIRST_LEVEL_BOSS3_APPEARS),
			new SpecialSpawnableLevel(boss2(), FIRST_LEVEL_BOSS2_APPEARS),
			new SpecialSpawnableLevel(boss1(), FIRST_LEVEL_BOSS1_APPEARS),
			
			//new enemy has been introduced, spawn lots of them!
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_DiagonalMovingView.class,
						Shooting_DiagonalMovingView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						Shooting_DiagonalMovingView.getNumEnemiesInLotsOfEnemiesWave(lvl),
						Shooting_DiagonalMovingView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Shooting_DiagonalMovingView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						), 
					FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_TrackingView.class,
						Shooting_TrackingView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						15,
						Shooting_TrackingView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Shooting_TrackingView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						), 
					FIRST_LEVEL_LOTS_OF_TRACKERS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_SpasticView.class,
						Shooting_SpasticView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						5,
						Shooting_SpasticView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Shooting_SpasticView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						), 
					FIRST_LEVEL_SPASTICS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_DurationLaserView.class,
						Shooting_DurationLaserView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						4,
						Shooting_DurationLaserView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Shooting_DurationLaserView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						), 
					FIRST_LEVEL_DURATION_BULLETS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Shooting_PauseAndMove.class,
						Shooting_PauseAndMove.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						10,
						Shooting_PauseAndMove.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Shooting_PauseAndMove.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						), 
						FIRST_LEVEL_PAUSE_AND_SHOOT_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_RectangleView.class,
						Orbiter_RectangleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						Orbiter_RectangleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
						Orbiter_RectangleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Orbiter_RectangleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						),
						FIRST_LEVEL_RECTANGLE_ORBITERS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_CircleView.class,
						Orbiter_CircleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						Orbiter_CircleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
						Orbiter_CircleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Orbiter_CircleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						),
						FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR),
			new SpecialSpawnableLevel(
					spawnLotsOfEnemiesWithDefaultConstructorArguments(Orbiter_TriangleView.class,
						Orbiter_TriangleView.getSpawningProbabilityWeightForLotsOfEnemiesWave(lvl),
						Orbiter_TriangleView.getNumEnemiesInLotsOfEnemiesWave(lvl),
						Orbiter_TriangleView.DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE,
						Orbiter_TriangleView.DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE
						),
						FIRST_LEVEL_TRIANGLE_ORBITERS_APPEAR),
			new SpecialSpawnableLevel(coordinatedCircularAttack(lvl), FIRST_LEVEL_COORDINATED_CIRCLE_ORBITERS_APPEAR)
						
		};
		SpecialSpawnableLevel.initializeSpecialSpawnableLevels(ALL_SPECIAL_SPAWNABLE_LEVELS);
	}
	
}
