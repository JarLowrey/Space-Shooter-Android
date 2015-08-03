package levels;

import helpers.KillableRunnable;
import android.content.Context;
import enemies.EnemyView;
import enemies.HorizontalMovement_FinalBoss;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_PauseAndMove;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_tracking.Shooting_TrackingView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	private long timeUntilCanSpawnNextWave = 0,
		timeSinceSpawnedLastWave = 0;
	public SpawnableWave[] allSpawnableWaves;
	 
	public LevelSpawner(Context context) {
		super(context);
	} 

	public void startLevelSpawning(){
		initScoreNeededToEndLevel();
		reinitializeAllSpawnableWaves();
		
		//spawn any special enemies at the beginning of the level
		spawningHandler.post( 
				SpecialSpawnableLevel.specialSpawnableForThisLevel( 
						getLevel()).runnableToSpawn()
				);
		
		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				timeSinceSpawnedLastWave += WAVE_SPAWNER_WAIT;
				
				if ( !isLevelFinishedSpawning() ) {
					spawningHandler.post( backgroundMeteors().runnableToSpawn() );
					
					if( canSpawnMoreEnemies() ){						
						SpawnableWave nextSpawn = SpawnableWave.getRandomWaveUsingWeightedProbabilities();
						
						spawningHandler.post( nextSpawn.runnableToSpawn() );				

						//reset the spawn timers
						timeUntilCanSpawnNextWave = nextSpawn.howLongUntilCanSpawnAgain();
						timeSinceSpawnedLastWave = 0;
					}
					
					spawningHandler.postDelayed(this,WAVE_SPAWNER_WAIT);
				}
			}
			
		});
	}
	
	//GETTER METHODS

	@Override
	public boolean isLevelFinishedSpawning() {
		return scoreGainedThisLevel() > scoreNeededToEndLevel;
	}
	
	
	//HELPER METHODS

	private void initScoreNeededToEndLevel(){

		/*  score needed is in terms of how many diagonal moving views are killed in 1 level
			score  scales as the diagonal moving views (and all other enemies) scale score.
			Each class of levels has a different scaling factor, ie every level will have 2-20x more diagonals in a level.
			Algorithm: Include the previous measure and then increase by the marginal amount. Return relevant amount.
		*/
		final int referenceScore = EnemyView.scaleScore( getLevel() , Shooting_DiagonalMovingView.DEFAULT_SCORE);
		
		final int begLevels = referenceScore*3 + referenceScore * Math.min(LEVELS_BEGINNER, getLevel() ) * 2;
		final int lowLevels = begLevels + referenceScore * ( Math.min(LEVELS_LOW, getLevel() ) - LEVELS_BEGINNER ) * 4;
		final int medLevels = lowLevels + referenceScore * ( Math.min(LEVELS_MED, getLevel() ) - LEVELS_LOW ) * 8;
		final int highLevels = medLevels + referenceScore * ( Math.min(LEVELS_HIGH, getLevel() ) - LEVELS_MED ) * 12;
		final int allOtherLevels = highLevels + referenceScore * ( getLevel() - LEVELS_HIGH ) * 20;
		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreNeededToEndLevel = begLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreNeededToEndLevel = lowLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			scoreNeededToEndLevel = medLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			scoreNeededToEndLevel = highLevels;
		}else{
			scoreNeededToEndLevel = allOtherLevels;
		}
	}
	private boolean canSpawnMoreEnemies(){
		/*  score needed is in terms of how many diagonal moving views can be on screen at one time.
			must scale score as the diagonal moving views (and all other enemies) scale score.
			Each class of levels has a different scaling factor, ie every 2-20 levels the # on screen increases.
			Algorithm: Include the previous measure and then increase by the marginal amount. Return relevant amount.
		*/
		int scoreNeededToSpawnMoreEnemies = 0;
		final int referenceScore = EnemyView.scaleScore( getLevel() , Shooting_DiagonalMovingView.DEFAULT_SCORE);
		
		final int begLevels = referenceScore + referenceScore * Math.min(LEVELS_BEGINNER, getLevel() ) / 2;
		final int lowLevels = begLevels + referenceScore * ( Math.min(LEVELS_LOW, getLevel() ) - LEVELS_BEGINNER ) / 4;
		final int medLevels = lowLevels + referenceScore * ( Math.min(LEVELS_MED, getLevel() ) - LEVELS_LOW ) / 8;
		final int highLevels = medLevels + referenceScore * ( Math.min(LEVELS_HIGH, getLevel() ) - LEVELS_MED ) / 12;
		final int allOtherLevels = highLevels + referenceScore * ( getLevel() - LEVELS_HIGH ) / 20;
		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreNeededToSpawnMoreEnemies = begLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreNeededToSpawnMoreEnemies = lowLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			scoreNeededToSpawnMoreEnemies = medLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			scoreNeededToSpawnMoreEnemies = highLevels;
		}else{
			scoreNeededToSpawnMoreEnemies = allOtherLevels;
		}

		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToSpawnMoreEnemies && 
				timeSinceSpawnedLastWave >= timeUntilCanSpawnNextWave;
	}
	private boolean canSpawnMeteors(){
		/*  
			algorithm is the same as canSpawnMoreEnemies(), but a little more lax. Thus score can be higher than that algorithm.
			In other words, at any point in the level the score required to spawn more meteors is high than it is to spawn more enemies
		*/
		int scoreNeededToSpawnMoreEnemies = 0;
		final int referenceScore = EnemyView.scaleScore( getLevel() , Shooting_DiagonalMovingView.DEFAULT_SCORE);
		
		final int begLevels = referenceScore * 2 + referenceScore * Math.min(LEVELS_BEGINNER, getLevel() ) ;
		final int lowLevels = begLevels + referenceScore * ( Math.min(LEVELS_LOW, getLevel() ) - LEVELS_BEGINNER ) / 2;
		final int medLevels = lowLevels + referenceScore * ( Math.min(LEVELS_MED, getLevel() ) - LEVELS_LOW ) / 4;
		final int highLevels = medLevels + referenceScore * ( Math.min(LEVELS_HIGH, getLevel() ) - LEVELS_MED ) / 10;
		final int allOtherLevels = highLevels + referenceScore * ( getLevel() - LEVELS_HIGH ) / 15;
		
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreNeededToSpawnMoreEnemies = begLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreNeededToSpawnMoreEnemies = lowLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){	
			scoreNeededToSpawnMoreEnemies = medLevels;
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){	
			scoreNeededToSpawnMoreEnemies = highLevels;
		}else{
			scoreNeededToSpawnMoreEnemies = allOtherLevels;
		}
	
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToSpawnMoreEnemies;
	}
	
	/**
	 * SpawnableWave objects need to be reinitialized at beginning of every level.
	 * This is due to the fact their probability weighting is a function of level, and as such changes
	 * as levels increment. Also, KillableRunnables are destroyed and unusable whenever onPause is called in GameActivity
	 */
	private void reinitializeAllSpawnableWaves(){
		//Add spawnable waves
		final SpawnableWave[] ALL_WAVES = {
			meteorShowersThatForceUserToLeft(),
			meteorShowersThatForceUserToRight(),
			meteorShowersThatForceUserToMiddle(),
			spawnGiantMeteor(),
			
			refreshArrayShooters(),
			lotsOfDiagonals(),

			spawnEnemyWithDefaultConstructorArugments(Shooting_TrackingView.class,Shooting_TrackingView.getSpawningProbabilityWeight(getLevel())),
			spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class,Shooting_DiagonalMovingView.getSpawningProbabilityWeight(getLevel())),
			spawnEnemyWithDefaultConstructorArugments(Shooting_PauseAndMove.class,Shooting_PauseAndMove.getSpawningProbabilityWeight(getLevel())),
			spawnEnemyWithDefaultConstructorArugments(Orbiter_CircleView.class,Orbiter_CircleView.getSpawningProbabilityWeight(getLevel())),
			spawnEnemyWithDefaultConstructorArugments(Orbiter_TriangleView.class,Orbiter_TriangleView.getSpawningProbabilityWeight(getLevel())),
			spawnEnemyWithDefaultConstructorArugments(Orbiter_RectangleView.class,Orbiter_RectangleView.getSpawningProbabilityWeight(getLevel())),

			boss1(),
			boss2(),
			boss3(),
			boss4(),
			spawnEnemyWithDefaultConstructorArugments(HorizontalMovement_FinalBoss.class,HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(getLevel()) )
		};		
		SpawnableWave.initializeSpawnableWaves(ALL_WAVES);
		
		
		//add special spawnablewaves
		final SpecialSpawnableLevel[] ALL_SPECIAL_SPAWNABLE_LEVELS = {
			new SpecialSpawnableLevel(spawnEnemyWithDefaultConstructorArugments(
					HorizontalMovement_FinalBoss.class,
					HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(getLevel()) ),
					FIRST_LEVEL_BOSS5_APPEARS),
			new SpecialSpawnableLevel(boss4(), FIRST_LEVEL_BOSS4_APPEARS),
			new SpecialSpawnableLevel(boss3(), FIRST_LEVEL_BOSS3_APPEARS),
			new SpecialSpawnableLevel(boss2(), FIRST_LEVEL_BOSS2_APPEARS),
			new SpecialSpawnableLevel(boss1(), FIRST_LEVEL_BOSS1_APPEARS),
			
			new SpecialSpawnableLevel(lotsOfDiagonals(), FIRST_LEVEL_LOTS_OF_DIAGONALS_APPEAR),
			new SpecialSpawnableLevel(lotsOfCircles(), FIRST_LEVEL_CIRCLE_ORBITERS_APPEAR)
						
		};
		SpecialSpawnableLevel.initializeSpecialSpawnableLevels(ALL_SPECIAL_SPAWNABLE_LEVELS);
	}	
	
	/**
	 * Meteors are an integral part of the game, as they provide a background, constant enemy and movement. This is a special method
	 * that returns a KillableRunnable to spawn Meteors if so desired (which is typically the case).
	 * @return 
	 */
	private SpawnableWave backgroundMeteors(){
		if( canSpawnMeteors() ){
			final Class<? extends Gravity_MeteorView> meteorClass = (Math.random()<.5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class ;
			Class meteor = (Math.random() < .5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class;
			return spawnEnemyWithDefaultConstructorArugments(meteor,0 );//probability weight does not matter here
		}else{
			return doNothing();
		}
	}
	
}
