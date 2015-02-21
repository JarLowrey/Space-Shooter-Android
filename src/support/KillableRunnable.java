package support;

/**
 * Runnable that can be stopped from executing
 * @author JTRONLABS
 */
public abstract class KillableRunnable implements Runnable{
	
	private boolean isKilled=false;
	
	/**
	 * Instead of Overriding run(), override this method to perform a Runnable operation. This will allow editing instance variables
	 * in the class that this Runnable is defined
	 */
	public abstract void doWork();
	
	//The handler that posts this Runnable will call this method. By default, check if it has been killed. Change the method to Override 
	//for callers of this KillableRunnable class
	@Override
	final public void run(){
		if(!isKilled){
			doWork();
		}
	}
	
	final public void kill(){
		isKilled=true;
	}
	
	final public void revive(){
		isKilled=false;
	}
}
