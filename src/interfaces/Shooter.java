package interfaces;

import guns.Gun;

import java.util.ArrayList;

import android.view.ViewGroup;
import bullets.BulletView;


public interface Shooter extends CollidableObjectWithHealthDamageEtc{

//	public void upgradeOrDowngradeGun(boolean upgrade);
	
	public void giveNewGun(Gun newGun);
	public void addGun(Gun newGun);
	public ArrayList<Gun> getAllGuns();
	
	
	public void startShooting();
	
	public void stopShooting();
	
	public ViewGroup getMyScreen();
	
	public boolean isFriendly();	

	//GET METHODS
	public double getBulletSpeedY();
	public double getBulletDamage();
	public double getBulletFreq();
	public ArrayList<BulletView> getMyBullets();
	
	//SET METHODS
	public void setBulletFreq(double freq);
	public void setBulletSpeedY(double newSpeed);
	public void setBulletDamage(double newDamage);
	public void setMyBullets(ArrayList<BulletView> bullets);
}
