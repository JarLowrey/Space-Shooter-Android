package bullets;

import friendlies.ProtagonistView;
import interfaces.Shooter;
import android.widget.RelativeLayout;

public class Bullet_Duration extends Bullet_Interface{
	
	public static final int DEFAULT_BULLET_DAMAGE = ProtagonistView.DEFAULT_HEALTH / 90,
			DEFAULT_BULLET_DURATION = 1200;

	private long bulletLifeSpan;
	private int posOnShooterAsAPercentage;
	
	public Bullet_Duration(int bulletWidth, int bulletHeight, int bulletBackgroundId, 
			long bulletsLifeSpan, int positionOnShooterAsAPercentage){
		super(bulletWidth,bulletHeight,bulletBackgroundId);
		
		posOnShooterAsAPercentage = positionOnShooterAsAPercentage;
		bulletLifeSpan = bulletsLifeSpan;
	}
	
	//no threads needed, so no cleanup needed
	public void removeBulletType(){}
	
	public BulletView getBullet(int posOnShooterAsAPercentage,RelativeLayout layout,Shooter shooter,float bulletSpeedY,int bulletDamage){
		Bullet_HasDurationView bullet = new Bullet_HasDurationView(posOnShooterAsAPercentage,layout,shooter, bulletSpeedY,
				bulletDamage,width,height,backgroundId,
				bulletLifeSpan);
		
		return bullet;
	}
}
