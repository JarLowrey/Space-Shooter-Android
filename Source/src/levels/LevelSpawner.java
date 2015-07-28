package levels;

import support.KillableRunnable;
import android.content.Context;
import android.util.Log;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	
	public LevelSpawner(Context context) {
		super(context);
	}

	public int getMaxLevel() {
		return 10;
	}
	
	private void initScoreNeededToEndLevel(){
		if(getLevel()!=0){
			scoreNeededToEndLevel = (int) (1000 * getLevel() + 1000 * Math.random() * getLevel() );
		}else{
			scoreNeededToEndLevel = 500;
		}
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
//					spawningHandler.post( meteors() );
					
					if( canSpawnMoreEnemies() ){
						spawningHandler.post( spawnEnemyWithDefaultConstructorArguments(2, 1000, Orbiter_RectangleView.class) );
						spawningHandler.post( spawnEnemyWithDefaultConstructorArguments(2, 1000, Orbiter_CircleView.class) );
						spawningHandler.post( spawnEnemyWithDefaultConstructorArguments(2, 1000, Orbiter_TriangleView.class) );
						
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
	private KillableRunnable meteors(){
		//TODO Change meteors to conditionally spawn
//		if( true ){
		Log.d("lowrey",""+DEFAULT_WAVE_DURATION/500);
			final Class<? extends Gravity_MeteorView> meteorClass = (Math.random()<.5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class ;
			Class meteor = (Math.random() < .5) ? Meteor_SidewaysView.class : Gravity_MeteorView.class;
			return spawnEnemyWithDefaultConstructorArguments( 4,500, meteor );
//		}else{
//			return doNothing();
//		}
	}
	
	
}
