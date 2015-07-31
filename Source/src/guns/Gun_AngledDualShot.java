package guns;
  
import com.jtronlabs.to_the_moon.R;

import helpers.MediaController;
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet_Interface;
import bullets.BulletView;


public  class Gun_AngledDualShot extends Gun {
	
	private static final float DEFAULT_ANGLE=(float) 12.5;
	
	public Gun_AngledDualShot(Context context,
			Shooter theShooter,Bullet_Interface bulletType,float bulletFrequency,float bulletSpeedVertical,int bulletDmg,
			int positionOnShooterAsAPercentage) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
	}
	
	public boolean shoot(){
		MediaController.playSoundEffect(ctx,MediaController.SOUND_LASER_SHOOT2);
		
		//travel horizontally at a speed such that the bullets will move in DEFAULT_ANGLE direction
		float bulletSpeedX = (float) (bulletSpeedY * Math.tan(Math.toRadians(DEFAULT_ANGLE)));

		//create left and right bullets
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//set position on shooter
		bulletLeft.setPositionOnShooterAsAPercentage(this.posOnShooter);
		bulletRight.setPositionOnShooterAsAPercentage(this.posOnShooter);
		
		//set bullets speed x to non default value
		bulletLeft.setSpeedX(bulletSpeedX * -1);
		bulletRight.setSpeedX(bulletSpeedX);
		
		return false;
	}
}
