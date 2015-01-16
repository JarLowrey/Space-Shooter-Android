package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_StraightDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import bullets.BulletView;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

public abstract class Friendly_ShooterView extends FriendlyView implements Shooter{
	
	public final static int BULLET_DAMAGE_WEIGHT=6,
			BULLET_SPEED_WEIGHT=4,
			BULLET_FREQ_WEIGHT=50;

	public final static double DEFAULT_SPEED_Y=14,
			DEFAULT_SPEED_X=14,
			DEFAULT_COLLISION_DAMAGE=100, 
			DEFAULT_HEALTH=1000,
			DEFAULT_BULLET_SPEED_Y=30,
			DEFAULT_BULLET_DAMAGE=10, 
			DEFAULT_BULLET_FREQ=850;
	
	Context ctx;
	
	//myGun needs to be set in a specific View's class
	protected ArrayList<Gun> myGuns;
	protected ArrayList<BulletView> myBullets;
	
	protected int bulletFreqLevel=0, 
		bulletDamageLevel=0,
		bullletVerticalSpeedLevel=0,
		currentGunConfiguration=0;
	
	protected boolean isShooting;

	public Friendly_ShooterView(Context context, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth,int width,int height,int imageId) {
		super(context,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth, width, height, imageId);
		
		ctx=context;//prob not necessary to hold onto this context. just saying, figure it out later
		isShooting=false;
		myGuns= new ArrayList<Gun>();
		myBullets = new ArrayList<BulletView>();
	}

	@Override
	public void removeGameObject(){		
		stopShooting();
		myBullets=new ArrayList<BulletView>();
		myGuns=new ArrayList<Gun>();
		
		super.removeGameObject();//needs to be called last for all pending callbacks to 'this' to be removed
	}
	/**
	 * define the different levels of guns protagonist may have
	 */
	public void upgradeGun(){
		currentGunConfiguration++;
		createGunSet();
	}
	
	public double getShootingDelay(){
		return DEFAULT_BULLET_FREQ - bulletFreqLevel * BULLET_FREQ_WEIGHT;
	}
	
	private void createGunSet(){
		this.removeAllGuns();
		
		final double freq = getShootingDelay();
		final double dmg = DEFAULT_BULLET_DAMAGE + bulletDamageLevel * BULLET_DAMAGE_WEIGHT;
		final double speed = DEFAULT_BULLET_SPEED_Y + bullletVerticalSpeedLevel * BULLET_SPEED_WEIGHT;
		
		switch(currentGunConfiguration){
		case 1:
			this.addGun(new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed) );
			break;
		case 2:
			this.addGun(new Gun_StraightDualShot(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed) );
			break;
		case 3:
			this.addGun(new Gun_StraightDualShot(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed) );
			break;
		case 4:
			this.addGun(new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),freq,dmg,speed) );
			break;
		case 5:
			Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),freq,dmg,speed) ;
			Gun gun2 = new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_Missile(),freq,dmg,speed) ;
			this.addGun(gun1);
			this.addGun(gun2);
			break;
		}		
	}
	
	public void incrementBulletSpeedYLevel(){
		bullletVerticalSpeedLevel++;
		createGunSet();
	}
	public void incrementBulletFreqLevel(){
		bulletFreqLevel++;
		createGunSet();
	}

	public void incrementBulletDamageLevel(){
		bulletDamageLevel++;
		createGunSet();
	}
	public int getGunLevel(){
		return this.currentGunConfiguration;
	}
	public int getBulletDamageLevel(){
		return bulletDamageLevel;
	}
	public int getBulletSpeedYLevel(){
		return bullletVerticalSpeedLevel;
	}
	public int getBulletBulletFreqLevel(){
		return bulletFreqLevel;
	}
	
	@Override
	public void restartThreads(){
		if( ! (this instanceof ProtagonistView) ) {
			startShooting();			
		}
		super.restartThreads();
	}
	@Override
	public ArrayList<BulletView> getMyBullets() {
		return myBullets;
	}

	@Override
	public void startShooting() {
		isShooting=true;
		for(Gun gun: myGuns){
			gun.startShootingDelayed();
		}
	}

	@Override
	public void stopShooting() {
		isShooting=false;
		for(Gun gun: myGuns){
			gun.stopShooting();
		}
	}

	@Override
	public boolean isFriendly() {
		return true;
	}

	@Override
	public void addGun(Gun newGun) {
		myGuns.add(newGun);
		//do not fire by default
	}

	@Override
	public ArrayList<Gun> getAllGuns() {
		return myGuns;
	}

	@Override
	public void removeAllGuns() {
		for(int i=myGuns.size()-1; i>=0;i--){
			myGuns.remove(i);
		}
	}
	
	public boolean isShooting(){
		return this.isShooting;
	}
	
	
	
}
