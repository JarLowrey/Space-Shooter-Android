package bullets;

import interfaces.Shooter;
import android.widget.RelativeLayout;
import enemies_non_shooters.Gravity_MeteorView;
import friendlies.ProtagonistView;

public abstract class Bullet_Interface{
	
	public final static float 
		DEFAULT_BULLET_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y ) ;
	public final static int 
		WEAK_BULLET_DMG= ProtagonistView.DEFAULT_HEALTH/50;//Density Pixels per millisecond

	int width, height, backgroundId;
	
	public Bullet_Interface(int bulletWidth, int bulletHeight, int bulletBackgroundId){
		width = bulletWidth;
		height = bulletHeight;
		backgroundId = bulletBackgroundId;
	}

	public int getBulletWidth(){ return width; }
	public int getBulletHeight(){ return height; }
	public int getBulletBackgroundId(){ return backgroundId; }
	
	public abstract BulletView makeBullet(int posOnShooterAsAPercentage, RelativeLayout layout, Shooter shooter, float bulletSpeedY, int bulletDamage);
	
}
