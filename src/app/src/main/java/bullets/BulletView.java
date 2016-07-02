package bullets;
  
import interfaces.Shooter;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;

import java.util.ArrayList;

/**
 * By default, a bullet moves straight and spawns in the middle of its shooter
 * @author lowre_000
 *
 */
public class BulletView extends Moving_ProjectileView{

	protected Shooter theOneWhoShotMe;
	protected static ArrayList<BulletView> bulletPool = new ArrayList<BulletView>();

	public static BulletView getBullet(int xPosOnShooterAsPercentage,RelativeLayout layout,Shooter shooter,float bulletSpeedY,
							int bulletDamage,int width,int height,int imageId){
		for(BulletView b: bulletPool){
			if(b.isRemoved()){
				b.unRemove();
			}
		}
		return new BulletView(xPosOnShooterAsPercentage,layout,shooter,bulletSpeedY,bulletDamage,width,height,imageId);
	}

	public BulletView(int xPosOnShooterAsPercentage,RelativeLayout layout,Shooter shooter,float bulletSpeedY,
			int bulletDamage,int width,int height,int imageId) {
		super(xPosOnShooterAsPercentageToGlobalXPos(xPosOnShooterAsPercentage,shooter,width),
				((MovingView)shooter).getMidY(),
				layout,
				bulletSpeedY,
				0 ,bulletDamage,1, width, height, imageId);
	
		//set instance variables
		theOneWhoShotMe=shooter;
		 
		//position bullet behind shooter
		ViewGroup parent = (ViewGroup)theOneWhoShotMe.getParent();
		if(parent!=null){
			parent.removeView(this);//bullet already added to parent in MovingView's instantiation
			int shooterIndex = parent.indexOfChild( (View) theOneWhoShotMe);
			parent.addView(this, shooterIndex);

			if(theOneWhoShotMe.isFriendly()){
				setSpeedY( - Math.abs(bulletSpeedY) );
				GameLoop.friendlyBullets.add(this);
			}else{
				setSpeedY( Math.abs(bulletSpeedY) );
				GameLoop.enemyBullets.add(this);			
			}
			theOneWhoShotMe.getMyBullets().add(this);
		}else{
			this.setViewToBeRemovedOnNextRendering();
			//for some reason, shooters continue to fire even after being killed. This simple check ensure that the shooter
			//has not been removed from his parent. If he has, then remove this bullet
		}
	}
	
	public void setBulletRotation(){	
		float rotVal=0;
		
		double arcTan = Math.atan(this.getSpeedX()/Math.abs(this.getSpeedY()) );
		if( ! theOneWhoShotMe.isFriendly()){
			arcTan = Math.atan(-this.getSpeedX()/Math.abs(this.getSpeedY()) );
			arcTan+=Math.PI;//flip bullet image around so it is pointing downwards
		}
		rotVal = (float) Math.toDegrees(arcTan);
			
		this.setRotation(rotVal);
	}


	private static float xPosOnShooterAsPercentageToGlobalXPos(int positionOnShooterAsAPercentageOfWidthFromTheLeftSide,Shooter shooter, float bulletWidth){
		if(positionOnShooterAsAPercentageOfWidthFromTheLeftSide < 0 || positionOnShooterAsAPercentageOfWidthFromTheLeftSide > 100){
			throw new IllegalArgumentException("Not a valid percentage");
		}
		final float posRelativeToShooter= (float) (shooter.getWidth() * positionOnShooterAsAPercentageOfWidthFromTheLeftSide/100.0);
		final float middleOfBulletOnShootingPos = (float) (posRelativeToShooter+shooter.getX()-bulletWidth/2.0);
		return (middleOfBulletOnShootingPos);

	}
	/**
	 *
	 * @param positionOnShooterAsAPercentageOfWidthFromTheLeftSide 100 indicates right side of shoot, 0 is left side, and 50 is middle
	 */
	public void setXPositionOnShooterAsAPercentage(int positionOnShooterAsAPercentageOfWidthFromTheLeftSide) throws IllegalArgumentException{
		setX(xPosOnShooterAsPercentageToGlobalXPos(positionOnShooterAsAPercentageOfWidthFromTheLeftSide,theOneWhoShotMe,this.getLayoutParams().width));
	}

	/**
	 * Remove bullet from Shooter's list of bullets  and GameActivity's list
	 */
	public void removeGameObject(){
		theOneWhoShotMe.getMyBullets().remove(this);

		super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks
	}
	

	@Override
	public void updateViewSpeed(long deltaTime) {
		//do nothing - movePhysicalPosition at constant speed
	}
	
	@Override 
	public void movePhysicalPosition(long deltaTime){
		setBulletRotation();
		super.movePhysicalPosition(deltaTime);
	}
}
