package enemies;

import guns.Gun;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.GameActivity;

public class Enemy_ShooterView extends EnemyView implements Shooter{
		

		//myGun needs to be set in a specific View's class
		private ArrayList<Gun> myGuns;
		private ArrayList<BulletView> myBullets;

		public Enemy_ShooterView(Context context,int scoreForKilling, double projectileSpeedY,double projectileSpeedX, 
				double projectileDamage,double projectileHealth,double probSpawnBeneficialObject) {
			super(context,scoreForKilling,projectileSpeedY,projectileSpeedX,
					projectileDamage,projectileHealth,probSpawnBeneficialObject);

			myGuns= new ArrayList<Gun>();
			myBullets = new ArrayList<BulletView>();
		}

		/**
		 * if a game object still has bullets on screen, it cannot be removed from the GameActivity list, as bullets would stop collision detection
		 */
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
			for(Gun gun: myGuns){
				gun.startShooting();
			}
		}

		@Override
		public void stopShooting() {
			for(Gun gun: myGuns){
				gun.stopShooting();
			}
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
