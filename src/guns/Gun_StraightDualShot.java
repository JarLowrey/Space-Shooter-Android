package guns;
  
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;


public class Gun_StraightDualShot extends Gun {
	
	public Gun_StraightDualShot(Context context,
			Shooter theShooter,Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg);
	}
	public boolean shoot(){
		//create 2 bullets
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//position bullets on edges of shooter
		bulletLeft.setPositionOnShooterAsAPercentage(Bullet.BULLET_LEFT);
		bulletRight.setPositionOnShooterAsAPercentage(Bullet.BULLET_RIGHT);

		return false;
	}
	
}
