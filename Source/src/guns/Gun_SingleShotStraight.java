package guns;
   

import helpers.MediaController;
import interfaces.Shooter;
import android.widget.RelativeLayout;
import bullets.BulletView;
import bullets.Bullet_Interface;


public class Gun_SingleShotStraight extends Gun {
	
	public Gun_SingleShotStraight(RelativeLayout layout,
			Shooter theShooter,
			Bullet_Interface bulletType,
			float bulletFrequency,
			float bulletSpeedVertical,
			int bulletDmg,
			int positionOnShooterAsAPercentage) {
		super(layout,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg,positionOnShooterAsAPercentage);
	}
	
	public boolean shoot(){
		MediaController.playSoundEffect(gameScreen.getContext(), MediaController.SOUND_LASER_SHOOT);
		
		//create one bullet at center of shooter
		BulletView bulletMid = myBulletType.getBullet(gameScreen, shooter,bulletSpeedY,bulletDamage);

		bulletMid.setXPositionOnShooterAsAPercentage(this.posOnShooter);
		
		return false;
	}
	
}
