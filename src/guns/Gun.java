package guns;
  
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet;


/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class Gun {
	
	Shooter shooter;
	Bullet myBulletType;
	Context ctx;
	
	
	
	/**
	 * Create bullets of the shooter's type at the shooter's position. Properties such as number of bullets, 
	 * direction of bullets, initial position of bullets, and more may be different
	 * @return true if gun is a special and is out of ammo. False otherwise.
	 */
	public abstract boolean shoot();	
	
	
	public Gun(Context context,Shooter theShooter,Bullet bulletType) {
		ctx=context;
		
		myBulletType = bulletType;
		shooter=theShooter;
	} 
	
	public Bullet getBulletType(){
		return myBulletType;
	}
	public void setBulletType(Bullet newBullet){
		myBulletType= newBullet;
	}
}
