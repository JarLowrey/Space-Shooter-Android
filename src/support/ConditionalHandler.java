package support;

import interfaces.MovingViewInterface;
import interfaces.Shooter;
/*
 * 
 * Any MovingView children that have RUNNABLES must include a condition for !MovingView.this.isRemoved() before running.
 * this ensures resources are not commited to an instance that has been removed from game.
 * Why? Handler.removeCallbacks(null) only removes pending messages. If  the Runnable has begun then it will get through,
 * and since all these game runnables have Handler.postDelayed(this,delayInMillisec) at the end, it will continue to execute
 * 
 * The same goes for the leveling system
 * 
 */
public class ConditionalHandler {
	
	// handler for Game Objects (removeable Views). View must be not removed
	/**
	 * Post Runnable r after a delay 
	 * @param r
	 * @param delayInMilliseconds
	 * @param theViewToPostTo
	 */
	public static void postIfAlive(Runnable r,long delayInMilliseconds,MovingViewInterface theViewToPostTo){
		if( ! theViewToPostTo.isRemoved()){
			theViewToPostTo.postDelayed(r, delayInMilliseconds);
		}
	}
	/**
	 * post Runnable r immediately
	 * @param r
	 * @param theViewToPostTo
	 */
	public static void postIfAlive(Runnable r,MovingViewInterface theViewToPostTo){
		if( ! theViewToPostTo.isRemoved()){
			theViewToPostTo.postDelayed(r, 0);
		}
	}
	
	// handler for Shooters. shooter must be alive and shooting
	/**
	 * post if alive and isShooting(). if a protagonist view then check canShoot()
	 * @param r
	 * @param shooter
	 */
	public static void postDelayedIfShooting(Runnable r,long bulletFreq,Shooter shooter){
		if( ! shooter.isRemoved() && shooter.isShooting()){
			shooter.postDelayed(r, bulletFreq);
		}
	}
	
}
