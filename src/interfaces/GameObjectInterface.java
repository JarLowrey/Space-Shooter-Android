package interfaces;

import parents.MovingView;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public interface GameObjectInterface{

	/**
	 * Manage the execution of threads. onPause all callbacks will be 
	 * removed from Views, but onResume it may be that not every thread needs restarted
	 */
	public void restartThreads();
	
	/**
	 * Cover the special circumstances when removing a view
	 * @return Score for killing this view. 
	 */
	public void removeGameObject();
	
	public boolean isRemoved();
	
	//default, built in, final, Android interface methods
	public float getX();	
	public int getWidth();	
	public float getY();	
	public int getHeight();
	public boolean removeCallbacks(Runnable run);
	public boolean postDelayed(Runnable r, long millisecondDelay);
	public boolean post(Runnable r);
}
