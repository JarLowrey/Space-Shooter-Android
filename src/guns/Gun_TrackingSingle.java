package guns;
  
import interfaces.Shooter;
import parents.Moving_ProjectileView;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.MainActivity;

public  class Gun_TrackingSingle extends Gun_Tracking {
	
	public static double MAX_X_SPEED=15*MainActivity.getScreenDens();
	
	private Moving_ProjectileView shootTowardsMe;
	
	public Gun_TrackingSingle(Context context, Moving_ProjectileView shootingAtMe,
			Shooter theShooter,Bullet bulletType,float bulletFrequency,float bulletSpeedVertical,int bulletDmg,int positionOnShooterAsAPercentage) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
		
		shootTowardsMe = shootingAtMe;
	}

	public Gun_TrackingSingle(Context context,
			Shooter theShooter,Bullet bulletType,float bulletFrequency,float bulletSpeedVertical,int bulletDmg,int positionOnShooterAsAPercentage) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
		
		shootTowardsMe=null;
	}
	
	public boolean shoot(){		
		//create 2 bullets
		BulletView bullet = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//position bullets on edges of shooter
		bullet.setPositionOnShooterAsAPercentage(this.posOnShooter);
		
		//set bullet's X speed 
		bullet.setSpeedX(getTrackingXSpeed(shooter,shootTowardsMe,this.bulletSpeedY));
		
		return false;
	}
}
