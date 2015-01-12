package interfaces;

import guns.Gun;

import java.util.ArrayList;

import android.view.ViewGroup;
import bullets.BulletView;


public interface Shooter extends CollidableObjectWithHealthDamageEtc{

//	public void upgradeOrDowngradeGun(boolean upgrade);
	
	public void removeAllGuns();
	public void addGun(Gun newGun);
	public ArrayList<Gun> getAllGuns();
	
	
	public void startShooting();
	
	public void stopShooting();
	
	public ViewGroup getMyScreen();
	
	public boolean isFriendly();	

	//GET METHODS
	public ArrayList<BulletView> getMyBullets();
	
	//SET METHODS
	public void setMyBullets(ArrayList<BulletView> bullets);
}
