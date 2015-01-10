package guns;
  
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;
import bullets.BulletView;


public  class Gun_AngledDualShot extends Gun {
	
	private static final double DEFAULT_ANGLE=9;
	
	public Gun_AngledDualShot(Context context,
			Shooter theShooter,Bullet bulletType) {
		super(context,theShooter,bulletType);
	}
	public boolean shoot(){
		//travel horizontally at a speed such that the bullets will move in DEFAULT_ANGLE direction
		double bulletSpeedX = shooter.getBulletSpeedY() * Math.tan(Math.toRadians(DEFAULT_ANGLE));

		//create left and right bullets at center of shooter
		BulletView bulletLeft = myBulletType.getBullet(ctx, shooter);
		BulletView bulletRight = myBulletType.getBullet(ctx, shooter);
		
		//set bullets speed x to non default value
		bulletLeft.setSpeedX(bulletSpeedX * -1);
		bulletRight.setSpeedX(bulletSpeedX);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletLeft,1);
		shooter.getMyScreen().addView(bulletRight,1);

		//decrement special ammo and return result
		return false;
	}
}
