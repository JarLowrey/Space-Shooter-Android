package bullets;

import interfaces.Shooter;
import android.widget.RelativeLayout;

public class Bullet_Duration extends Bullet_Interface{

	private long bulletLifeSpan;
	private int positionOnShooterAsAPercentage;
	
	public Bullet_Duration(int bulletWidth, int bulletHeight, int bulletBackgroundId, 
			long bulletsLifeSpan,int positionOnShooterAsAPercentageOfWidthFromTheLeftSide){
		super(bulletWidth,bulletHeight,bulletBackgroundId);
		
		positionOnShooterAsAPercentage = positionOnShooterAsAPercentageOfWidthFromTheLeftSide;
		bulletLifeSpan = bulletsLifeSpan;
	}
	
	//no threads needed, so no cleanup needed
	public void removeBulletType(){}
	
	public BulletView getBullet(RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage){
		Bullet_HasDurationView bullet = new Bullet_HasDurationView(layout,shooter, bulletSpeedY,
				bulletDamage,width,height,backgroundId,
				bulletLifeSpan,positionOnShooterAsAPercentage);
		
		return bullet;
	}
}
