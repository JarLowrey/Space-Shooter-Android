package guns;
  
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;


public class Gun_StraightDualShot extends Gun {
	
	public Gun_StraightDualShot(Context context,
			Shooter theShooter,Bullet bulletType) {
		super(context,theShooter,bulletType);
	}
	public boolean shoot(){
		//create 2 bullets
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter);
		
		//position bullets on edges of shooter
		bulletLeft.setPositionOnShooterAsAPercentage(0);
		bulletRight.setPositionOnShooterAsAPercentage(100);

		//add bullets to layout
		shooter.getMyScreen().addView(bulletLeft,1);
		shooter.getMyScreen().addView(bulletRight,1);

		return false;
	}
	
}