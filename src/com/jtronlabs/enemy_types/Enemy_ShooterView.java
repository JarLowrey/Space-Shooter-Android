package com.jtronlabs.enemy_types;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserShort;
import com.jtronlabs.to_the_moon.guns.Gun;
import com.jtronlabs.to_the_moon.guns.Gun_StraightSingleShot;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Enemy_ShooterView extends EnemyView implements Shooter{
		

		//myGun needs to be set in a specific View's class
		private ArrayList<Gun> myGuns;
		private ArrayList<BulletView> myBullets;
		
		private double bulletFreq,bulletSpeedY,bulletDamage;
		

	  Runnable shootingRunnable = new Runnable(){
	  	@Override
	      public void run() {
	  			//ensure shooter is not removed before running
		  		if( ! isRemoved() ){
		  			for(Gun currentGun : myGuns){
		  				currentGun.shoot();
		  			}
		  			
		  			Enemy_ShooterView.this.postDelayed(this, (long)bulletFreq);
		  		}
	  		}
		};

		public Enemy_ShooterView(Context context,int scoreForKilling, double projectileSpeedY,double projectileSpeedX, 
				double projectileDamage,double projectileHealth,double probSpawnBeneficialObject,
				double bulletFrequency,double bulletDmg,double bulletVerticalSpeed) {
			super(context,scoreForKilling,projectileSpeedY,projectileSpeedX,
					projectileDamage,projectileHealth,probSpawnBeneficialObject);

			myGuns= new ArrayList<Gun>();
			bulletFreq=bulletFrequency;
			bulletDamage=bulletDmg;
			bulletSpeedY=bulletVerticalSpeed;
			myBullets = new ArrayList<BulletView>();


			Gun defaultGun = new Gun_StraightSingleShot(context,this,new Bullet_LaserShort());
			myGuns.add(defaultGun);
			
			startShooting();//protagonist spawns through Attirbute set constructor, so this is safe
		}

		@Override
		public void removeGameObject(){
			stopShooting();
			if(this.getMyBullets().size()==0){
				GameActivity.enemies.remove(this);			
			}
			super.removeGameObject();
		}
		
		@Override
		public void restartThreads(){
			startShooting();
			super.restartThreads();
		}
	//	
//		/**
//		 * Takes care of upgrading or downgrading this instance's Gun_Upgradeable
//		 * @param upgrade True to upgrade gun, False to downgrade
//		 */
//		public void upgradeOrDowngradeGun(boolean upgrade){
//			//create new upgrade
//			Gun_Upgradeable nextGun;
//			if(upgrade){
//				nextGun = this.myGun.getMostRecentUpgradeableGun().getUpgradeGun();
//			}else{
//				nextGun = this.myGun.getMostRecentUpgradeableGun().getDowngradedGun();			
//			}
//			
//			if(this.myGun instanceof Gun_Special){
//				this.myGun.setPreviousUpgradeableGun(nextGun);//set it up so once special disappears, new gun will take over
//			}else if( ! this.myGun.getClass().equals(nextGun.getClass())){
//				this.myGun=nextGun;
//			}
//		}
	//	
//		/**
//		 * Takes care of changing this instance's gun to a given Special Gun with a set amount of amunition
//		 * @param newGun Special Gun to switch to
//		 * @param ammo Amount of ammo new special gun will have
//		 */
//		public void giveSpecialGun(Gun_Special newGun, int ammo){
//			newGun.setPreviousUpgradeableGun(this.myGun.getMostRecentUpgradeableGun());
//			this.myGun=newGun;
//			newGun.setAmmo(ammo);
//		}
	//	
		@Override
		public void setMyBullets(ArrayList<BulletView> bullets) {
			myBullets=bullets;
		}

		@Override
		public ArrayList<BulletView> getMyBullets() {
			return myBullets;
		}

		@Override
		public void startShooting() {
			this.postDelayed(shootingRunnable,(long) getBulletFreq());
		}

		@Override
		public void stopShooting() {
			this.removeCallbacks(shootingRunnable);
		}

		@Override
		public boolean isDead() {
			return super.isRemoved() || super.getHealth()<=0;
		}

		@Override
		public ViewGroup getMyScreen() {
			return (ViewGroup)this.getParent();
		}

		@Override
		public boolean isFriendly() {
			return false;
		}

		@Override
		public double getBulletSpeedY() {
			return bulletSpeedY;
		}

		@Override
		public double getBulletDamage() {
			return bulletDamage;
		}

		@Override
		public double getBulletFreq() {
			return bulletFreq;
		}
		
		@Override
		public void setBulletFreq(double freq) {
			bulletFreq=freq;
		}

		@Override
		public void setBulletSpeedY(double newSpeed) {
			bulletSpeedY=newSpeed;
		}

		@Override
		public void setBulletDamage(double newDamage) {
			bulletDamage = newDamage;
		}

		@Override
		public void addGun(Gun newGun) {
			myGuns.add(newGun);
		}

		
		
		@Override
		public ArrayList<Gun> getAllGuns() {
			return myGuns;
		}

		@Override
		public void giveNewGun(Gun newGun) {
			for(int i=myGuns.size()-1; i>=0;i--){
				myGuns.remove(i);
			}
			myGuns.add(newGun);
		}

}
