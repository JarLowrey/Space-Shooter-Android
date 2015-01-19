package guns;
   
import interfaces.Shooter;
import parents.Moving_ProjectileView;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.MainActivity;


public class Gun_TrackingGattling extends Gun_Tracking {

	public static double MAX_X_SPEED=15*MainActivity.getScreenDens();
	public static int DEFAULT_NUM_GATTLING_SHOTS=10;
	
	private int currentNumShots,cutoffTotalShots;
	private double originalBulletFreq;
	private Moving_ProjectileView shootTowardsMe;
	
	public Gun_TrackingGattling(Context context,Moving_ProjectileView shootingAtMe,Shooter theShooter,
			Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,
			double bulletDmg,int positionOnShooterAsAPercentage,
			int numGattlingShots) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg,positionOnShooterAsAPercentage);
		
		shootTowardsMe = shootingAtMe;
		originalBulletFreq=bulletFrequency;
		cutoffTotalShots=numGattlingShots;
		currentNumShots=0;
	}
	
	public boolean shoot(){
		
		if(currentNumShots>cutoffTotalShots){
			bulletFreq=originalBulletFreq;
			currentNumShots=0;
		}else{
			bulletFreq=400;
			currentNumShots++;
		}
		
		//create one bullet at center of shooter
		BulletView bullet = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);

		bullet.setPositionOnShooterAsAPercentage(this.posOnShooter);

		//set bullet's X speed 
		bullet.setSpeedX(getTrackingXSpeed(shooter,shootTowardsMe,this.bulletSpeedY));
		
		return false;
	}
	
}
