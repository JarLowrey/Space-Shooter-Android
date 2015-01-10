package friendly_types;

import guns.Gun;
import guns.Gun_StraightSingleShot;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import bullets.BulletView;
import bullets.Bullet_LaserShort;

import com.jtronlabs.to_the_moon.GameActivity;

public class Friendly_ShooterView extends FriendlyView implements Shooter{
	

	//myGun needs to be set in a specific View's class
	private ArrayList<Gun> myGuns;
	private ArrayList<BulletView> myBullets;
	
	private double bulletFreq,bulletSpeedY,bulletDamage;
	

  Runnable shootingRunnable = new Runnable(){
  	@Override
      public void run() {
  			//ensure shooter is not removed before running
	  		if( ! isRemoved() ){
	  			for(Gun currentGun : myGuns){
	  				currentGun.shoot();
	  			}
	  			
	  			Friendly_ShooterView.this.postDelayed(this, (long) bulletFreq);
	  		}
  		}
	};

	public Friendly_ShooterView(Context context, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth,
			double bulletFrequency,double bulletDmg,double bulletVerticalSpeed) {
		super(context,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);

		myGuns= new ArrayList<Gun>();
		bulletFreq=bulletFrequency;
		bulletDamage=bulletDmg;
		bulletSpeedY=bulletVerticalSpeed;
		myBullets = new ArrayList<BulletView>();


		Gun defaultGun = new Gun_StraightSingleShot(context,this,new Bullet_LaserShort());
		myGuns.add(defaultGun);
		
		startShooting();//protagonist spawns through Attirbute set constructor, so this is safe
	}

	public Friendly_ShooterView(Context context,AttributeSet at, double projectileSpeedY,double projectileSpeedX, 
			double projectileDamage,double projectileHealth,
			double bulletFrequency,double bulletDmg,double bulletVerticalSpeed) {
		super(context, at,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth);

		myGuns= new ArrayList<Gun>();
		bulletFreq=bulletFrequency;
		bulletDamage=bulletDmg;
		bulletSpeedY=bulletVerticalSpeed;
		myBullets = new ArrayList<BulletView>();
		
		Gun defaultGun = new Gun_StraightSingleShot(context,this,new Bullet_LaserShort());
		myGuns.add(defaultGun);
		
		this.removeCallbacks(null);
	}
	
	@Override
	public void removeGameObject(){
		stopShooting();
		if(this.getMyBullets().size()==0){
			GameActivity.friendlies.remove(this);			
		}
		super.removeGameObject();
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
		this.postDelayed(shootingRunnable,(long) getBulletFreq());
	}

	@Override
	public void stopShooting() {
		this.removeCallbacks(shootingRunnable);
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
	public double getBulletSpeedY() {
		return bulletSpeedY;
	}

	@Override
	public double getBulletDamage() {
		return bulletDamage;
	}

	@Override
	public double getBulletFreq() {
		return bulletFreq;
	}
	
	@Override
	public void setBulletFreq(double freq) {
		bulletFreq=freq;
	}

	@Override
	public void setBulletSpeedY(double newSpeed) {
		bulletSpeedY=newSpeed;
	}

	@Override
	public void setBulletDamage(double newDamage) {
		bulletDamage = newDamage;
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
	public void giveNewGun(Gun newGun) {
		for(int i=myGuns.size()-1; i>=0;i--){
			myGuns.remove(i);
		}
		myGuns.add(newGun);
	}
	
	
	
	
}
