package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import bullets.BulletView;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.MainActivity;

public abstract class Friendly_ShooterView extends FriendlyView implements Shooter{
	
	public final static double BULLET_DAMAGE_WEIGHT=6,
			BULLET_SPEED_WEIGHT=MainActivity.getScreenDens() * 1.5,
			BULLET_FREQ_WEIGHT=50;

	public final static double DEFAULT_SPEED_Y=10,
			DEFAULT_SPEED_X=10,
			DEFAULT_COLLISION_DAMAGE=100, 
			DEFAULT_HEALTH=1000,
			DEFAULT_BULLET_SPEED_Y=9,
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
		for(Gun gun : myGuns){
			gun.destroyGun();
		}
		myGuns=new ArrayList<Gun>();
		
		super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks	
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
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,50) );
			break;
		case 2:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,80) );
			break;
		case 3:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,80) );
			break;
		case 4:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,dmg,speed,80) );
			break;
		case 5:
			Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),freq,dmg,speed,50) ;
			Gun gun2 = new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_Missile(),freq,dmg,speed,50) ;
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
			myGuns.get(i).stopShooting();
			myGuns.remove(i);
		}
	}
	
	public boolean isShooting(){
		return this.isShooting;
	}
	
	
	
}
