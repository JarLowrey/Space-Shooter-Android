package levels;

import support.KillableRunnable;
import android.content.Context;
import android.util.Log;
import enemies.Shooting_DiagonalMovingView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	public final static Class[] 
			DIFFICULTY_ZERO_ENEMIES = {Shooting_DiagonalMovingView.class},
			DIFFICULTY_ONE_ENEMIES = {},
			DIFFICULTY_TWO_ENEMIES = {},
			DIFFICULTY_THREE_ENEMIES = {},
			DIFFICULTY_FOUR_ENEMIES = {},
			DIFFICULTY_FIVE_ENEMIES = {};
	
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
						Class nextEnemy = null;
						switch(difficulty()){
						case 0:
							nextEnemy = DIFFICULTY_ZERO_ENEMIES[(int) (Math.random() * DIFFICULTY_ZERO_ENEMIES.length)];
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
						spawningHandler.post( spawnEnemyWithDefaultConstructorArguments( 3 , nextEnemy) );	//TODO change number of enemies spawned to be semi random and reflective of progress					
					}
					spawningHandler.postDelayed(this,DEFAULT_WAVE_DURATION);
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
		Log.d("lowrey",""+DEFAULT_WAVE_DURATION/500);
			final Class<? extends Gravity_MeteorView> meteorClass = (Math.random()<.5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class ;
			Class meteor = (Math.random() < .5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class;
			return spawnEnemyWithDefaultConstructorArguments( 2,1000, meteor );
//		}else{
//			return doNothing();
//		}
	}
	
	
}
