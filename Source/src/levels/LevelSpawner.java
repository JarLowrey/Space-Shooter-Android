package levels;

import support.KillableRunnable;
import support.SpawnableWave;
import android.content.Context;
import android.util.Log;
import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	private long timeUntilCanSpawnNextWave,
		timeSinceSpawnedLastWave;
	public final SpawnableWave[][] 
			DIFFICULTY_SPAWNS = {
				{
					spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class),
					meteorShowersThatForceUserToLeft(),
					meteorShowersThatForceUserToRight(),
					meteorShowersThatForceUserToMiddle()
				},
				{
					spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class),
					meteorShowersThatForceUserToLeft(),
					meteorShowersThatForceUserToRight(),
					meteorShowersThatForceUserToMiddle(),
					
					refreshArrayShooters(),
					trackingEnemy()					
				}
			};
	
	public LevelSpawner(Context context) {
		super(context);
	}

	public int getMaxLevel() {
		return 10;
	}
	
	private void initScoreNeededToEndLevel(){
		scoreNeededToEndLevel = (int) (500 + 500 * Math.random() * getLevel() );
	}
	private boolean canSpawnMoreEnemies(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToEndLevel/6 && 
				timeSinceSpawnedLastWave >= timeUntilCanSpawnNextWave;
	}
	private boolean canSpawnMeteors(){
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToEndLevel/5;
	}
	
	public void startLevelSpawning(){
		initScoreNeededToEndLevel();
		
		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				timeSinceSpawnedLastWave += WAVE_SPAWNER_WAIT;
				
				if ( !isLevelFinishedSpawning() ) {
					spawningHandler.post( backgroundMeteors().runnableToSpawn() );
					
					if( canSpawnMoreEnemies() ){						
						SpawnableWave nextSpawn = DIFFICULTY_SPAWNS[difficulty()]
								[(int) (Math.random() * DIFFICULTY_SPAWNS[difficulty()].length)];
						
						timeUntilCanSpawnNextWave = nextSpawn.howLongUntilCanSpawnAgain();
						timeSinceSpawnedLastWave = 0;
						
						spawningHandler.post( nextSpawn.runnableToSpawn() );	//TODO change number of enemies spawned to be semi random and reflective of progress					
					}
					
					spawningHandler.postDelayed(this,WAVE_SPAWNER_WAIT);
				}
			}
			
		});
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
			return spawnEnemiesWithDefaultConstructorArguments( 2,1000, meteor );
		}else{
			return doNothing();
		}
	}
	
	
}
