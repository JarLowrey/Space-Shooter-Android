package levels;

import helpers.KillableRunnable;
import helpers.SpawnableWave;
import helpers.SpecialSpawnableLevel;
import android.content.Context;
import enemies.HorizontalMovement_FinalBoss;
import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	private long timeUntilCanSpawnNextWave = 0,
		timeSinceSpawnedLastWave = 0;
	public SpawnableWave[] allSpawnableWaves;
	 
	public LevelSpawner(Context context) {
		super(context);
		
		SpecialSpawnableLevel.addSpecialSpawnableLevel(
				spawnEnemyWithDefaultConstructorArugments(
						HorizontalMovement_FinalBoss.class,
						HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(getLevel()) )
				, FIRST_LEVEL_BOSS5_APPEARS);
		SpecialSpawnableLevel.addSpecialSpawnableLevel(boss4(), FIRST_LEVEL_BOSS4_APPEARS);
		SpecialSpawnableLevel.addSpecialSpawnableLevel(boss3(), FIRST_LEVEL_BOSS3_APPEARS);
		SpecialSpawnableLevel.addSpecialSpawnableLevel(boss2(), FIRST_LEVEL_BOSS2_APPEARS);
		SpecialSpawnableLevel.addSpecialSpawnableLevel(boss1(), FIRST_LEVEL_BOSS1_APPEARS);

		SpecialSpawnableLevel.addSpecialSpawnableLevel(lotsOfCircles(), 19);
	}

	public int getMaxLevel() {
		return 75;
	}
	
	private void initScoreNeededToEndLevel(){
		if(getLevel() < AttributesOfLevels.LEVELS_BEGINNER){
			scoreNeededToEndLevel = (int) (500 + 500 * Math.random() * getLevel() );
		}else if(getLevel() < AttributesOfLevels.LEVELS_LOW) {
			scoreNeededToEndLevel = LEVELS_BEGINNER * 500 + (int) (600 * Math.random() * (getLevel()-LEVELS_BEGINNER) );
		}else if(getLevel() < AttributesOfLevels.LEVELS_MED){
			scoreNeededToEndLevel = LEVELS_BEGINNER * 500 + (LEVELS_LOW-LEVELS_BEGINNER) * 600 
					+ (int) (800 * Math.random() * (getLevel()-LEVELS_LOW) );	
		}else if(getLevel() < AttributesOfLevels.LEVELS_HIGH){
			scoreNeededToEndLevel = LEVELS_BEGINNER * 500 + (LEVELS_LOW-LEVELS_BEGINNER) * 600 
					+(LEVELS_MED - LEVELS_LOW) * 800 +
					+ (int) (900 * Math.random() * (getLevel()-LEVELS_MED) );	
		}else{
			scoreNeededToEndLevel = LEVELS_BEGINNER * 500 + (LEVELS_LOW-LEVELS_BEGINNER) * 600 
					+(LEVELS_MED - LEVELS_LOW) * 800 + (LEVELS_HIGH-LEVELS_MED) * 900 +
					+ (int) (1000 * Math.random() * (getLevel()-LEVELS_HIGH) );	
		}
	}
	private boolean canSpawnMoreEnemies(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToEndLevel/6 && 
				timeSinceSpawnedLastWave >= timeUntilCanSpawnNextWave;
	}
	private boolean canSpawnMeteors(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToEndLevel/3;
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
	
	/**
	 * SpawnableWave objects need to be reinitialized at beginning of every level.
	 * This is due to the fact their probability weighting is a function of level, and as such changes
	 * as levels increment.
	 */
	private void reinitializeAllSpawnableWaves(){
		
		final SpawnableWave[] ALL_WAVES = {
			meteorShowersThatForceUserToLeft(),
			meteorShowersThatForceUserToRight(),
			meteorShowersThatForceUserToMiddle(),
			spawnGiantMeteor(),
			
			refreshArrayShooters(),
			trackingEnemy(),
			
			spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class,Shooting_DiagonalMovingView.getSpawningProbabilityWeight(getLevel())),

			boss1(),
			boss2(),
			boss3(),
			boss4(),
			spawnEnemyWithDefaultConstructorArugments(HorizontalMovement_FinalBoss.class,HorizontalMovement_FinalBoss.getSpawningProbabilityWeight(getLevel()) )
		};
		
		SpawnableWave.initializeSpawnableWaves(ALL_WAVES);
	}

	@Override
	public boolean isLevelFinishedSpawning() {
		return scoreGainedThisLevel() > scoreNeededToEndLevel;
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
