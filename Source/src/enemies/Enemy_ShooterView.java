package enemies;

import friendlies.ProtagonistView;
import guns.Gun;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.R;

public abstract class Enemy_ShooterView extends EnemyView implements Shooter{

		public static final float DEFAULT_BULLET_FREQ=1500;
		
		public static final int DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/15,
				DEFAULT_COLLISION_DAMAGE = ProtagonistView.DEFAULT_HEALTH/12;
				
		//myGun needs to be set in a specific View's class
		private ArrayList<Gun> myGuns;
		private ArrayList<BulletView> myBullets;
		
		private boolean isShooting=true;
		
		public Enemy_ShooterView(Context context,int level,int scoreForKilling, float projectileSpeedY,float projectileSpeedX, 
				int projectileDamage,int projectileHealth,float probSpawnBeneficialObject,int width,int height,int imageId) {
			super(context,level,scoreForKilling,projectileSpeedY,projectileSpeedX,
					projectileDamage,projectileHealth,probSpawnBeneficialObject, width, height, imageId);

			myGuns= new ArrayList<Gun>();
			myBullets = new ArrayList<BulletView>();
		} 
 		
		/**
		 * To be called on implementation of onRemoveGameObject
		 * NEW BEHAVIOR = drop references to guns and bullets
		 */
		@Override
		public void removeGameObject(){
			removeAllGuns();
			
			super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks		
		}
		@Override   
		public boolean takeDamage(int howMuchDamage){ 
			boolean isDead = super.takeDamage(howMuchDamage);
			
			if(isDead){ 
				final long vibrationPattern[] = {0,40};
				createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1,vibrationPattern);
			}
			
			return isDead;
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
