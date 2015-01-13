package guns;
   
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;


public class Gun_StraightSingleShot extends Gun {
	
	public Gun_StraightSingleShot(Context context,Shooter theShooter,
			Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg);
	}
	
	public boolean shoot(){
		//create one bullet at center of shooter
		BulletView bulletMid = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletMid,1);
		
		return false;
	}
	
}
