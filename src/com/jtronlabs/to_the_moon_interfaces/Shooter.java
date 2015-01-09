package com.jtronlabs.to_the_moon_interfaces;

import java.util.ArrayList;

import android.view.ViewGroup;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_Special;

public interface Shooter extends CollidableObjectWithHealthDamageEtc{

	public void upgradeOrDowngradeGun(boolean upgrade);
	
	public void giveSpecialGun(Gun_Special newGun, int ammo);
	
	public void startShooting();
	
	public void stopShooting();
	
	public ViewGroup getMyScreen();
	
	public boolean isFriendly();	

	//GET METHODS
	public double getBulletSpeedY();
	public double getBulletDamage();
	public double getBulletFreq();
	public Bullet getBulletType();
	public Gun getGun();
	public ArrayList<BulletView> getMyBullets();
	
	//SET METHODS
	public void setBulletFreq(double freq);
	public void setBulletSpeedY(double newSpeed);
	public void setBulletDamage(double newDamage);
	public void setBulletType(Bullet newBulletType);
//	public void setGun();
	public void setMyBullets(ArrayList<BulletView> bullets);
}
