package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon_interfaces.Shooter;

/**
 * All instances of this class do not have ammo, and are reverted to when a shooter has a Gun_Special that runs out of ammo
 * @author JAMES LOWREY
 *
 */
public abstract class Gun_Upgradeable extends Gun {
	
	public Gun_Upgradeable(Context context,Shooter theShooter) {
		super(context,theShooter);
	}
	
	public abstract Gun_Upgradeable getUpgradeGun();
	public abstract Gun_Upgradeable getDowngradedGun();
	
}
