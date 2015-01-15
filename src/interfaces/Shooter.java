package interfaces;

import guns.Gun;

import java.util.ArrayList;

import android.view.ViewGroup;
import bullets.BulletView;


public interface Shooter extends Projectile{

//	public void upgradeOrDowngradeGun(boolean upgrade);
	
	public void removeAllGuns();
	public void addGun(Gun newGun);
	public ArrayList<Gun> getAllGuns();
	
	public void startShooting();
	
	public void stopShooting();
	
	public boolean isFriendly();
	
	public boolean isShooting();

	//GET METHODS
	public ArrayList<BulletView> getMyBullets();
}
