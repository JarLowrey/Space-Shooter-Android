package support;

import parents.Moving_ProjectileView;
import android.content.Context;
import android.os.Handler;

/**
 * Experiment to create prevent the repeated code that is currently present in Factory_Waves for default enemy types
 * @author JTRONLABS
 */
public class SpawnDefaultEnemyRunnable extends KillableRunnable{

	private int numEnemies, milliSecBetween;
	private Class classToSpawn;
	private Handler h;
	private Context ctx;
	
	
	private int numSpawned=0;
	
	
	public SpawnDefaultEnemyRunnable(int numEnemiesToSpawn, 
			int millisecondsBetweenEachSpawn,
			Class classOfEnemyToSpawn,
			Handler spawningHandler,
			Context context){
		numEnemies = numEnemiesToSpawn;
		milliSecBetween = millisecondsBetweenEachSpawn;
		classToSpawn = classOfEnemyToSpawn;
		h=spawningHandler;
		ctx=context;
		
		
		doWork();
	}
	
	public void doWork(){
		try {
			Class [] constructorArgs = new Class[] {Context.class,Moving_ProjectileView.class};
			classToSpawn.getDeclaredConstructor(constructorArgs).newInstance(ctx);
		} catch (Exception e){
			e.printStackTrace();
		}
		numSpawned++;
		
		if(numSpawned<numEnemies){
			h.postDelayed(this, milliSecBetween);
		}
	}
	
}
