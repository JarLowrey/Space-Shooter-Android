package guns;
  
import helpers.MediaController;
import interfaces.Shooter;
import android.widget.RelativeLayout;
import bullets.BulletView;
import bullets.Bullet_Interface;
 
public  class Gun_AngledDualShot extends Gun {
	
	private static final float DEFAULT_ANGLE=(float) 12.5;
	
	public Gun_AngledDualShot(RelativeLayout layout,
			Shooter theShooter,Bullet_Interface bulletType,float bulletFrequency,float bulletSpeedVertical,int bulletDmg,
			int positionOnShooterAsAPercentage) {
		super(layout,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
	}
	
	public boolean shoot(){
		MediaController.playSoundEffect(gameScreen.getContext(),MediaController.SOUND_LASER_SHOOT);
		
		//travel horizontally at a speed such that the bullets will movePhysicalPosition in DEFAULT_ANGLE direction
		float bulletSpeedX = (float) (bulletSpeedY * Math.tan(Math.toRadians(DEFAULT_ANGLE)));

		//create left and right bullets
		BulletView bulletLeft = myBulletType.getBullet(this.posOnShooter,gameScreen, shooter,bulletSpeedY,bulletDamage);
		BulletView bulletRight = myBulletType.getBullet(this.posOnShooter,gameScreen, shooter,bulletSpeedY,bulletDamage);

		//set bullets speed x to non default value
		bulletLeft.setSpeedX(bulletSpeedX * -1);
		bulletRight.setSpeedX(bulletSpeedX);
		
		return false;
	}
}
