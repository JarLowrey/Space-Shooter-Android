package guns;
   
import helpers.MediaController;
import interfaces.Shooter;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;
import bullets.BulletView;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;


public class Gun_TrackingGattling extends Gun_Tracking {

	public static float MAX_X_SPEED=15*MainActivity.getScreenDens();
	public static int DEFAULT_NUM_GATTLING_SHOTS=6;
	
	private int currentNumShots,cutoffTotalShots;
	private float originalBulletFreq;
	private Moving_ProjectileView shootTowardsMe;
	
	public Gun_TrackingGattling(RelativeLayout layout,Moving_ProjectileView shootingAtMe,Shooter theShooter,
			Bullet_Interface bulletType,float bulletFrequency,float bulletSpeedVertical,
			int bulletDmg,int positionOnShooterAsAPercentage,
			int numGattlingShots) {
		super(layout,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg,positionOnShooterAsAPercentage);
		
		shootTowardsMe = shootingAtMe;
		originalBulletFreq=bulletFrequency;
		cutoffTotalShots=numGattlingShots;
		currentNumShots= 1;
	}
	
	public boolean shoot(){ 
		MediaController.playSoundEffect(gameScreen.getContext(), MediaController.SOUND_LASER_SHOOT);
		
		
		if(currentNumShots >= cutoffTotalShots){
			bulletFreq=originalBulletFreq;
			currentNumShots=1;
		}else{
			bulletFreq=400;
			currentNumShots++;
		}
		
		//create one bullet at center of shooter
		BulletView bullet = myBulletType.getBullet(this.posOnShooter,gameScreen, shooter,bulletSpeedY,bulletDamage);

		//set bullet's X speed 
		bullet.setSpeedX(getTrackingXSpeed(shooter,shootTowardsMe,this.bulletSpeedY));
		
		return false;
	}
	
}
