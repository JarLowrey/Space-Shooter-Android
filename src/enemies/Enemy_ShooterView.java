package enemies;

import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import bullets.BulletView;
import bullets.Bullet_Basic_LaserShort;

public abstract class Enemy_ShooterView extends EnemyView implements Shooter{
		

		public static final float DEFAULT_BULLET_SPEED_Y=5,
				DEFAULT_BULLET_FREQ=2000;
		
		public static final int DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/15,
				DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/10;
				
		//myGun needs to be set in a specific View's class
		private ArrayList<Gun> myGuns;
		private ArrayList<BulletView> myBullets;
		
		private boolean isShooting=true;
		
		public Enemy_ShooterView(Context context,int scoreForKilling, float projectileSpeedY,float projectileSpeedX, 
				int projectileDamage,int projectileHealth,float probSpawnBeneficialObject,int width,int height,int imageId) {
			super(context,scoreForKilling,projectileSpeedY,projectileSpeedX,
					projectileDamage,projectileHealth,probSpawnBeneficialObject, width, height, imageId);

			myGuns= new ArrayList<Gun>();
			myBullets = new ArrayList<BulletView>();
			

			//add a default gun
			Gun defaultGun = new Gun_SingleShotStraight(context, this, new Bullet_Basic_LaserShort(),
					getShootingFreq(), DEFAULT_BULLET_SPEED_Y, DEFAULT_BULLET_DAMAGE,50);
			this.addGun(defaultGun);
			this.startShooting();
		} 
 
		public abstract float getShootingFreq();
		/**
		 * To be called on implementation of onRemoveGameObject
		 * NEW BEHAVIOR = drop references to guns and bullets
		 */
		@Override
		public void removeGameObject(){
			stopShooting();			
			myBullets=new ArrayList<BulletView>();
			for(Gun gun : myGuns){
				gun.destroyGun();
			}
			myGuns=new ArrayList<Gun>();
			
			super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks		
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
				gun.startShootingDelayed();
			}
		}

		@Override
		public void stopShooting() {
			isShooting=false;
			for(Gun gun: myGuns){
				gun.stopShooting();
			}
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
				myGuns.get(i).stopShooting();
				myGuns.remove(i);
			}
		}
		
		@Override 
		public boolean isShooting(){
			return isShooting;
		}

}
