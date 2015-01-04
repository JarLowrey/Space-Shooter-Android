package com.jtronlabs.views;

import java.util.ArrayList;

import android.content.Context;

public class SideToSideMovingShooter extends ShootingView{

	public SideToSideMovingShooter(Context context, double projectileSpeedY,
			double projectileSpeedX, double projectileDamage,
			double projectileHealth, double bulletSpd, double bulletDmg,
			double bulletFrequency, int bulletBackground) {
		super(context, projectileSpeedY, projectileSpeedX, projectileDamage,
				projectileHealth, bulletSpd, bulletDmg, bulletFrequency,
				bulletBackground);
		// TODO Auto-generated constructor stub
	}
	public static ArrayList<SideToSideMovingShooter> allMovingShooters= new ArrayList<SideToSideMovingShooter>();
	
	
	
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
	}
	public void restartThreads(){
		super.restartThreads();
	}
	
}
