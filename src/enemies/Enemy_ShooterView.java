package enemies;

import guns.Gun;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import bullets.BulletView;

public abstract class Enemy_ShooterView extends EnemyView implements Shooter{
		
		
		//myGun needs to be set in a specific View's class
		private ArrayList<Gun> myGuns;
		private ArrayList<BulletView> myBullets;
		
		private boolean isShooting=true;
		
		public Enemy_ShooterView(Context context,int scoreForKilling, double projectileSpeedY,double projectileSpeedX, 
				double projectileDamage,double projectileHealth,double probSpawnBeneficialObject,int width,int height,int imageId) {
			super(context,scoreForKilling,projectileSpeedY,projectileSpeedX,
					projectileDamage,projectileHealth,probSpawnBeneficialObject, width, height, imageId);

			myGuns= new ArrayList<Gun>();
			myBullets = new ArrayList<BulletView>();
		}
 
		/**
		 * To be called on implementation of onRemoveGameObject
		 * NEW BEHAVIOR = drop references to guns and bullets
		 */
		protected void removeEnemyShooterGameObject(){
			stopShooting();			
			myBullets=new ArrayList<BulletView>();
			myGuns=new ArrayList<Gun>();
			
			this.removeGameObject();//needs to be the last thing called for handler to remove all callbacks			
		}
		
		@Override
		public void restartThreads(){
			startShooting();
			super.restartThreads();
		}

		@Override
		public ArrayList<BulletView> getMyBullets() {
			return myBullets;
		}

		@Override
		public void startShooting() {
			isShooting=true;
			for(Gun gun: myGuns){
				gun.startShooting();
			}
		}

		@Override
		public void stopShooting() {
			isShooting=false;//no need to loop through, the ConditionalHandler will stop callbacks
//			for(Gun gun: myGuns){
//				gun.stopShooting();
//			}
		}
		
		@Override
		public boolean isFriendly() {
			return false;
		}

		@Override
		public void addGun(Gun newGun) {
			myGuns.add(newGun);
//			this.stopShooting();//reset shooting on adding a gun
//			this.startShooting();
		}

		@Override
		public ArrayList<Gun> getAllGuns() {
			return myGuns;
		}
		
		@Override
		public void removeAllGuns() {
			for(int i=myGuns.size()-1; i>=0;i--){
				myGuns.remove(i);
			}
		}
		
		@Override 
		public boolean isShooting(){
			return isShooting;
		}

}
