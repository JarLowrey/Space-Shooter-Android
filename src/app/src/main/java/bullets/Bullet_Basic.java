package bullets;

import interfaces.Shooter;
import android.widget.RelativeLayout;


  

public class Bullet_Basic extends Bullet_Interface{
	
	public Bullet_Basic(int bulletWidth, int bulletHeight, int bulletBackgroundId){
		super(bulletWidth,bulletHeight,bulletBackgroundId);
	}
	
	//no threads needed for a non fancy bullet
	public void removeBulletType(){}
	
	public BulletView getBullet(int posOnShooterAsAPercentage,RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage){
		BulletView bullet = new BulletView(posOnShooterAsAPercentage,layout,shooter, bulletSpeedY, bulletDamage,width,height,backgroundId);
		
		return bullet;
	}
}
