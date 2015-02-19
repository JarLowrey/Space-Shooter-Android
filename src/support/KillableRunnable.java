package support;

public abstract class KillableRunnable implements Runnable{
	private boolean killMe=false;
	
	/**
	 * Instead of Overriding run(), override this method to perform a Runnable operation
	 */
	public abstract void doWork();
	
	//Now, in Runnable always check that this Runnable has not been killed. If possible, continue in its operation
	@Override
	public final void run(){
		if(!killMe){
			doWork();
		}
	}
	
	public void kill(){
		killMe=true;
	}
}
