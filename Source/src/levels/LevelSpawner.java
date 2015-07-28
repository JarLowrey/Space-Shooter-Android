package levels;

import support.KillableRunnable;
import android.content.Context;
import android.util.Log;
import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	public final KillableRunnable[] 
			DIFFICULTY_ZERO_SPAWNS = {
				spawnEnemyWithDefaultConstructorArugments(Shooting_DiagonalMovingView.class),
				meteorShowersThatForceUserToLeft(),
				meteorShowersThatForceUserToRight(),
				meteorShowersThatForceUserToMiddle()
			},
			DIFFICULTY_ONE_SPAWNS = {},
			DIFFICULTY_TWO_SPAWNS = {},
			DIFFICULTY_THREE_SPAWNS = {},
			DIFFICULTY_FOUR_SPAWNS = {},
			DIFFICULTY_FIVE_SPAWNS = {};
	
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
		return LevelSystem.totalSumOfLivingEnemiesScore() < scoreNeededToEndLevel/10;
	}
	
	public void startLevelSpawning(){
		initScoreNeededToEndLevel();

		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				if ( !isLevelFinishedSpawning() ) {
					spawningHandler.post( backgroundMeteors() );
					
					if( canSpawnMoreEnemies() ){
						KillableRunnable nextSpawn = doNothing();
						switch(difficulty()){
						case 0:
							nextSpawn = DIFFICULTY_ZERO_SPAWNS[(int) (Math.random() * DIFFICULTY_ZERO_SPAWNS.length)];
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4: 
							
							break;
						default:
							
							break;
						}
						spawningHandler.post( nextSpawn );	//TODO change number of enemies spawned to be semi random and reflective of progress					
					}
					spawningHandler.postDelayed(this,LEVEL_SPAWNER_WAIT);
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
	private KillableRunnable backgroundMeteors(){
		//TODO Change meteors to conditionally spawn
//		if( true ){
		Log.d("lowrey",""+LEVEL_SPAWNER_WAIT/500);
			final Class<? extends Gravity_MeteorView> meteorClass = (Math.random()<.5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class ;
			Class meteor = (Math.random() < .5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class;
			return spawnEnemiesWithDefaultConstructorArguments( 2,1000, meteor );
//		}else{
//			return doNothing();
//		}
	}
	
	
}
