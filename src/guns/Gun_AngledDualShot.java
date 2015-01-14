package guns;
  
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;


public  class Gun_AngledDualShot extends Gun {
	
	private static final double DEFAULT_ANGLE=30;
	
	public Gun_AngledDualShot(Context context,
			Shooter theShooter,Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg);
	}
	public boolean shoot(){
		//travel horizontally at a speed such that the bullets will move in DEFAULT_ANGLE direction
		double bulletSpeedX = bulletSpeedY * Math.tan(Math.toRadians(DEFAULT_ANGLE));

		//create left and right bullets at center of shooter
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//set bullets speed x to non default value
		bulletLeft.setSpeedX(bulletSpeedX * -1);
		bulletRight.setSpeedX(bulletSpeedX);
		
		return false;
	}
}
