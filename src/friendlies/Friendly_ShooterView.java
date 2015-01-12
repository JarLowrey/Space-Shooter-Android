package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_StraightDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import bullets.BulletView;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.GameActivity;

public class Friendly_ShooterView extends FriendlyView implements Shooter{
	

	public final static double DEFAULT_SPEED_Y=12.5,
			DEFAULT_SPEEDX=14,
			DEFAULT_COLLISION_DAMAGE=100, 
			DEFAULT_HEALTH=1000,
			DEFAULT_BULLET_SPEED_Y=30,
			DEFAULT_BULLET_DAMAGE=10, 
			DEFAULT_BULLET_FREQ=500;
//	
//	boolean legalToShootAgain=true;
//	Runnable gunsCooldown = new Runnable(){
//		@Override
//		public void run() {
//			
//		}
//	};
	
	Context ctx;
	
	//myGun needs to be set in a specific View's class
	private ArrayList<Gun> myGuns;
	private ArrayList<BulletView> myBullets;
	
	private int bulletFreqWeight=0, 
		bulletDamageWeight=0,
		bullletVerticalSpeedWeight=0,
		currentGunConfiguration=0;

	public Friendly_ShooterView(Context context, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);
		
		ctx=context;//prob not necessary to hold onto this context. just saying, figure it out later
		
		myGuns= new ArrayList<Gun>();
		myBullets = new ArrayList<BulletView>();
 
		startShooting();//protagonist spawns through Attirbute set constructor, so it is safe to start shooting here 
	}

	public Friendly_ShooterView(Context context,AttributeSet at, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super(context, at,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);
		
		ctx=context;
		myGuns= new ArrayList<Gun>();
		myBullets = new ArrayList<BulletView>();
		
		this.removeCallbacks(null);
	}
	
	public void incrementBulletDamageWeight(){
		bulletDamageWeight++;
	}
	
	/**
	 * define the different levels of guns protagonist may have
	 */
	public void upgradeGun(){
		this.removeAllGuns();
		currentGunConfiguration++;
		
		switch(currentGunConfiguration){
		case 1:
			this.addGun(new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_LaserLong(),
					DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y) );
			break;
		case 2:
			this.addGun(new Gun_StraightDualShot(ctx, this, new Bullet_Basic_LaserLong(),
							DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y));
			break;
		case 3:
			this.addGun(new Gun_StraightDualShot(ctx, this, new Bullet_Basic_LaserLong(),
					DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y));
			break;
		case 4:
			this.addGun(new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),
					DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y));
			break;
		case 5:
			Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),
					DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
			Gun gun2 = new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_Missile(),
					DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
			this.addGun(gun1);
			this.addGun(gun2);
			break;
		}
	}

	public int getGunLevel(){
		return this.currentGunConfiguration;
	}
	public void incrementBulletSpeedYWeight(){
		bullletVerticalSpeedWeight++;
	}
	public void incrementBulletFreqWeight(){
		bulletFreqWeight++;
	}
	public int getBulletDamageWeight(){
		return bulletDamageWeight;
	}
	public int getBulletBulletSpeedYWeight(){
		return bullletVerticalSpeedWeight;
	}
	public int getBulletBulletFreqWeight(){
		return bulletFreqWeight;
	}
	
	
	@Override
	public void removeGameObject(){
		super.removeGameObject();
		
		stopShooting();
		if(this.getMyBullets().size()==0){
			GameActivity.friendlies.remove(this);			
		}
	}
	
	@Override
	public void restartThreads(){
		startShooting();
		super.restartThreads();
	}
	@Override
	public void setMyBullets(ArrayList<BulletView> bullets) {
		myBullets=bullets;
	}

	@Override
	public ArrayList<BulletView> getMyBullets() {
		return myBullets;
	}

	@Override
	public void startShooting() {
		for(Gun gun: myGuns){
			gun.startShooting();
		}
	}

	@Override
	public void stopShooting() {
		for(Gun gun: myGuns){
			gun.stopShooting();
		}
	}

	@Override
	public boolean isDead() {
		return super.isRemoved() || super.getHealth()<=0;
	}

	@Override
	public ViewGroup getMyScreen() {
		return (ViewGroup)this.getParent();
	}

	@Override
	public boolean isFriendly() {
		return true;
	}

	@Override
	public void addGun(Gun newGun) {
		myGuns.add(newGun);
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
	
	
	
	
}
