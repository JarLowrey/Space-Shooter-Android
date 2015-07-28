package levels;

import support.KillableRunnable;
import android.content.Context;
import android.util.Log;
import enemies.Shooting_PauseAndMove;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;

public class LevelSpawner extends Factory_Bosses{
	
	private int scoreNeededToEndLevel;
	
	public LevelSpawner(Context context) {
		super(context);
	}

	public int getMaxLevel() {
		return 10;
	}
	
	private void initScoreNeededToEndLevel(){
		scoreNeededToEndLevel = (int) (1000 * getLevel() + 1000 * Math.random() * getLevel() );
	}
	
	public void startLevelSpawning(){
		initScoreNeededToEndLevel();

		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				if ( !isLevelFinishedSpawning() ) {
					spawningHandler.post( meteors() );
					
//					KillableRunnable r = spawnEnemyWithDefaultConstructorParameters(3, 500, Shooting_PauseAndMove.class);
//					spawningHandler.post( r );
					
					
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
