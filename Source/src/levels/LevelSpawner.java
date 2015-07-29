package levels;

import helpers.KillableRunnable;
import helpers.SpawnableWave;
import android.content.Context;
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
	}

	public int getMaxLevel() {
		return 20;
	}
	
	private void initScoreNeededToEndLevel(){
		scoreNeededToEndLevel = (int) (500 + 500 * Math.random() * getLevel() );
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
		int diagonalMoverProbabilityWeight = 30-difficulty()*2;
		diagonalMoverProbabilityWeight = Math.min(diagonalMoverProbabilityWeight, 10);
		
		final SpawnableWave[] ALL_WAVES = {
			meteorShowersThatForceUserToLeft(),
			meteorShowersThatForceUserToRight(),
			meteorShowersThatForceUserToMiddle(),
			spawnGiantMeteor(),
			
			refreshArrayShooters(),
			trackingEnemy(),
			
			spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class,diagonalMoverProbabilityWeight),

			boss1(),
			boss2(),
			boss3(),
			boss4(),
			boss5()
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
