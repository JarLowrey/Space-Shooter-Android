package guns;
  
import bullets.BulletView;
import bullets.Bullet_Basic;
import bullets.Bullet_HasDurationView;
import bullets.Bullet_TrackingView;
import helpers.KillableRunnable;
import interfaces.Shooter;

import android.util.Log;
import android.widget.RelativeLayout;
import bullets.Bullet_Interface;
import parents.MovingView;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;


/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class Gun {
	
	/**
	 * Create bullets of the shooter's type at the shooter's position. Properties such as number of bullets, 
	 * direction of bullets, initial position of bullets, and more may be different
	 * @return true if gun is a special and is out of ammo. False otherwise.
	 */
	public abstract boolean shoot();	
	
	Shooter shooter;
	Bullet_Interface myBulletType;
	RelativeLayout gameScreen;
	
 
	protected float bulletFreq,bulletSpeedY,bulletSpeedX;
	protected int bulletDamage,posOnShooter;
	
	private KillableRunnable shootingRunnable = new KillableRunnable(){
		  	@Override
		      public void doWork() {
	  				Gun.this.shoot();
	  				postDelayedIfShooting(this, (long) bulletFreq,shooter);
		  		}
			};
	
	public Gun(RelativeLayout layout,Shooter theShooter,Bullet_Interface bulletType,
			float bulletFrequency,float bulletSpeedVertical,int bulletDmg,int positionOnShooterAsAPercentage) {
		gameScreen = layout; 
		
		posOnShooter=positionOnShooterAsAPercentage;
		bulletFreq=bulletFrequency;
		bulletSpeedX=0;
		bulletSpeedY=bulletSpeedVertical*MainActivity.getScreenDens();
		bulletDamage=bulletDmg;
		myBulletType = bulletType;
		shooter=theShooter;
	} 

	protected BulletView getBullet(){
		for(BulletView b: GameLoop.bulletPool){
			if( b.isRemoved() && myBulletType.getClass().equals(b.getClass()) ){
				//renew bullets properties (like speed, position, etc)
				b.unRemoveBullet(posOnShooter,
						gameScreen,
						bulletSpeedY,
						myBulletType.getBulletWidth(),
						myBulletType.getBulletHeight(),
						myBulletType.getBulletBackgroundId(),
						shooter,
						bulletDamage
				);
				b.initBullet(shooter);
			}
		}

		BulletView b = myBulletType.makeBullet(this.posOnShooter,gameScreen,shooter,bulletSpeedY,bulletDamage);
		GameLoop.bulletPool.add(b);

		return b;
	}

	public void startShootingImmediately(){
		postDelayedIfShooting(shootingRunnable,0,shooter);
	}
	public void startShootingDelayed(){
		postDelayedIfShooting(shootingRunnable,(long)bulletFreq,shooter);	
	}
	
	public void stopShooting(){
		shooter.removeCallbacks(shootingRunnable);
	}
	
	public Bullet_Interface getBulletType(){
		return myBulletType;
	}
	
	public void setBulletType(Bullet_Interface newBullet){
		myBulletType= newBullet;
	}
	
	public double getBulletSpeedY() {
		return bulletSpeedY;
	}

	public double getBulletDamage() {
		return bulletDamage;
	}

	public double getBulletFreq() {
		return bulletFreq;
	}
	
	public void setBulletFreq(float freq) {
		bulletFreq=freq;
	}

	public void setBulletSpeedY(float newSpeed) {
		bulletSpeedY=newSpeed;
	}

	public void setBulletDamage(int newDamage) {
		bulletDamage = newDamage;
	}
	
	private void postDelayedIfShooting(Runnable r, long delay, Shooter theShooter){
		if(theShooter.isShooting()){
			theShooter.postDelayed(r, delay);
		}
	}
	

}
