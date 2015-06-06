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

	public static final float DEFAULT_BULLET_SPEED_Y = 27;

	public final static float DEFAULT_SPEED_Y=20,
			DEFAULT_SPEED_X=20;
	
	public final static int DEFAULT_COLLISION_DAMAGE=Integer.MAX_VALUE, 
			DEFAULT_HEALTH=10000,
			DEFAULT_BULLET_DAMAGE=DEFAULT_HEALTH/20, 
			DEFAULT_BULLET_FREQ=850;

	public final static float BULLET_DAMAGE_WEIGHT=DEFAULT_BULLET_DAMAGE/10,
			BULLET_SPEED_WEIGHT=(float) (MainActivity.getScreenDens() * 1.5),
			BULLET_FREQ_WEIGHT=50;
 

	
	//myGun needs to be set in a specific View's class
	protected ArrayList<Gun> myGuns;
	protected ArrayList<BulletView> myBullets;
	
	protected boolean isShooting;

	public Friendly_ShooterView(Context context, float projectileSpeedY,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super(context,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth, width, height, imageId);
		
		isShooting=false;
		myGuns= new ArrayList<Gun>();
		myBullets = new ArrayList<BulletView>();
	}
	

	/**
	 * define the different levels of guns protagonist may have
	 */
	
	public void createGunSet(float freq, int dmg,int gunLevel){
		this.removeAllGuns();
		
		switch(gunLevel){
		case 0:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,dmg,50) );
			break;
		case 1:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,(int) (dmg*.6),20) );
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,(int) (dmg*.6),80) );
			break;
		case 2:
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,dmg,80) );
			break;
		case 3:
			this.addGun(new Gun_AngledDualShot(getContext(),this,new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,dmg,50));
			this.addGun(new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_LaserLong(),freq,DEFAULT_BULLET_SPEED_Y,dmg,50) );
			break;
		case 4:
			this.addGun( new Gun_AngledDualShot(getContext(), this, new Bullet_Basic_LaserShort(),freq,DEFAULT_BULLET_SPEED_Y,dmg,50) );
			this.addGun( new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic_Missile(),freq,DEFAULT_BULLET_SPEED_Y,dmg*2,50) );
			break;
		}
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
