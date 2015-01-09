package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

/**
 * All instances of this class have an ammo count and are thus temporary
 * @author JAMES_LOWREY
 *
 */
public abstract  class Gun_Special extends Gun {
	
	private int ammo;
	
	public Gun_Special(Context context,Shooter theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
		
		ammo=-1;
	}
	public Gun_Special(Context context,Shooter theShooter) {
		super(context,theShooter);
		
		ammo=-1;
	}
	
	/**
	 * Set the amount of special ammo the rocket has. New value must be positive
	 * @param newAmmoAmount A postive, nonzero number representing this RocketView's store of special ammunition
	 * @return True if newAmmoAmount was >0 and thus ammo was properly set. False otherwise
	 */
	public boolean setAmmo(int newAmmoAmount){
		if(newAmmoAmount>0){
			ammo=newAmmoAmount;
			if(shooter == GameActivity.rocket){
				GameActivity.showAmmoText(newAmmoAmount);
			}
			return true;
		}
		return false;
	}
	/**
	 * Decrement the amount of special ammo the View has. If View is out of ammo, revert to single shot and previous damage attributes
	 * @param newAmmoAmount 
	 * @return True if the Gun has run out of ammo (previously ammo>0 and after ammo<=0), false otherwise
	 */
	protected boolean decrementAmmo(){
		boolean ranOutOfAmmo=false;
		if(ammo>0){
			ammo--;
			if(ammo<=0){
				ranOutOfAmmo=true;
			}		
		}
		if(shooter == GameActivity.rocket){GameActivity.setAmmoText(ammo);}	
		return ranOutOfAmmo;
		
	}
	public int getAmmo(){
		return ammo;
	}
}
