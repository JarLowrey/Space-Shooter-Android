package support;

import interfaces.GameObjectInterface;
import levels.LevelSystem;
import abstract_parents.MovingView;
import android.os.Handler;
/*
 * 
 * Any children that have RUNNABLES must include a condition for !MovingView.this.isRemoved() before running.
 * this ensures resources are not commited to an instance that has been removed from game.
 * Why? Handler.removeCallbacks(null) only removes pending messages. If  the Runnable has begun then it will get through,
 * and since all these game runnables have Handler.postDelayed(this,delayInMillisec) at the end, it will continue to execute
 * 
 * The same goes for the leveling system
 * 
 */
public class ConditionalHandler {
	
	/**
	 * Post Runnable r after a delay 
	 * @param r
	 * @param delayInMilliseconds
	 * @param theViewToPostTo
	 */
	public static void postIfAlive(Runnable r,long delayInMilliseconds,GameObjectInterface theViewToPostTo){
		if( ! theViewToPostTo.isRemoved()){
			theViewToPostTo.postDelayed(r, delayInMilliseconds);
		}
	}
	/**
	 * post Runnable r immediately
	 * @param r
	 * @param theViewToPostTo
	 */
	public static void postIfAlive(Runnable r,GameObjectInterface theViewToPostTo){
		if( ! theViewToPostTo.isRemoved()){
			theViewToPostTo.postDelayed(r, 0);
		}
	}

	private static Handler spawnHandler = new Handler();
	/**
	 * Post Runnable r immediately
	 * @param r
	 * @param delayInMilliseconds
	 */
	public static void postIfLevelResumed(Runnable r,int delayInMilliseconds){
		if( ! LevelSystem.isLevelPaused()){
			spawnHandler.postDelayed(r, delayInMilliseconds);
		}
	}
	/**
	 * Post Runnable r immediately
	 * @param r
	 */
	public static void postIfLevelResumed(Runnable r){
		if( ! LevelSystem.isLevelPaused()){
			spawnHandler.postDelayed(r, 0);
		}
	}
	public static void removeLevelHandlerCallbacks(){
		spawnHandler.removeCallbacks(null);
	}
}
