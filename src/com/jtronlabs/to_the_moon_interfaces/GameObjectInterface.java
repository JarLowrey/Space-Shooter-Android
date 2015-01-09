package com.jtronlabs.to_the_moon_interfaces;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public interface GameObjectInterface {

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
	
	
}
