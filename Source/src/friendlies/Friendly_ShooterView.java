package friendlies;

import guns.Gun;
import interfaces.Shooter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;
import bullets.BulletView;
import bullets.Bullet_HasDurationView;

public abstract class Friendly_ShooterView extends FriendlyView implements Shooter{

	public final static float DEFAULT_SPEED_Y=20,
			DEFAULT_SPEED_X=20;
	
	public final static int DEFAULT_COLLISION_DAMAGE=Integer.MAX_VALUE;

	
	//myGun needs to be set in a specific View's class
	protected ArrayList<Gun> myGuns;
	protected ArrayList<BulletView> myBullets;
	
	protected boolean isShooting;

	public Friendly_ShooterView(RelativeLayout layout, float projectileSpeedY,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super(layout,projectileSpeedY,projectileSpeedX,
				projectileDamage,projectileHealth, width, height, imageId);
		
		isShooting=false;
		myGuns= new ArrayList<Gun>();
		myBullets = new ArrayList<BulletView>();   
	}
	
	@Override
	public void removeGameObject(){		
		removeAllGuns();
		
		for(BulletView b : myBullets){
			if(b instanceof Bullet_HasDurationView){
				b.removeGameObject();
			}
		}
		
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
