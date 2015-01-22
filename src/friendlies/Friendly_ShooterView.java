package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import bullets.BulletView;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;

public abstract class Friendly_ShooterView extends FriendlyView implements Shooter{
	
	public final static float DEFAULT_SPEED_Y=10,
			DEFAULT_SPEED_X=10;
	public final static int DEFAULT_COLLISION_DAMAGE=100, 
			DEFAULT_HEALTH=10000,
			DEFAULT_BULLET_SPEED_Y=9,
			DEFAULT_BULLET_DAMAGE=DEFAULT_HEALTH/20, 
			DEFAULT_BULLET_FREQ=850;

	public final static float BULLET_DAMAGE_WEIGHT=DEFAULT_BULLET_DAMAGE/10,
			BULLET_SPEED_WEIGHT=(float) (MainActivity.getScreenDens() * 1.5),
			BULLET_FREQ_WEIGHT=50;

	
	Context ctx;
	
	//myGun needs to be set in a specific View's class
	protected ArrayList<Gun> myGuns;
	protected ArrayList<BulletView> myBullets;
	
	protected int bulletFreqLevel=0, 
		bulletDamageLevel=0,
		bullletVerticalSpeedLevel=0,
		currentGunConfiguration=0;
	
	protected boolean isShooting;

	public Friendly_ShooterView(Context context, float projectileSpeedY,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
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
	public float getShootingDelay(){
		return DEFAULT_BULLET_FREQ - bulletFreqLevel * BULLET_FREQ_WEIGHT;
	}
	
	public void createGunSet(){
		this.removeAllGuns();

		//load state from preferences
		SharedPreferences gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		setGunConfig(gameState.getInt(GameActivity.STATE_GUN_CONFIG,0));
		setBulletDamageLevel(gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0));
		setBulletSpeedLevel(gameState.getInt(GameActivity.STATE_BULLET_SPEED_LEVEL, 0));
		setBulletFreqLevel(gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0));
		
		final float freq = getShootingDelay();
		final int dmg = (int) (DEFAULT_BULLET_DAMAGE + bulletDamageLevel * BULLET_DAMAGE_WEIGHT);
		final float speed = DEFAULT_BULLET_SPEED_Y + bullletVerticalSpeedLevel * BULLET_SPEED_WEIGHT;
		
		switch(currentGunConfiguration){
		case 1:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,50) );
			break;
		case 2:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		case 3:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		case 4:
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,20) );
			this.addGun(new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_LaserLong(),freq,speed,dmg,80) );
			break;
		default:
			Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),freq,speed,dmg,50) ;
			Gun gun2 = new Gun_SingleShotStraight(ctx, this, new Bullet_Basic_Missile(),freq,speed,dmg,50) ;
			this.addGun(gun1);
			this.addGun(gun2);
			break;
		}		
	}
	
	public void setGunConfig(int gunConfig){
		currentGunConfiguration=gunConfig;
	}
	
	public void setBulletDamageLevel(int level){
		this.bulletDamageLevel=level;
	}
	public void setBulletFreqLevel(int level){
		this.bulletFreqLevel=level;
	}
	public void setBulletSpeedLevel(int level){
		this.bullletVerticalSpeedLevel=level;
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
