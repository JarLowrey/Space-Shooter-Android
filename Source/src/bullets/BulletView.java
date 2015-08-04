package bullets;
  
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.Shooter;
import levels.LevelSystem;
import parents.Moving_ProjectileView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * By default, a bullet moves straight and spawns in the middle of its shooter
 * @author lowre_000
 *
 */
public class BulletView extends Moving_ProjectileView{

	public static final int DEFAULT_HORIZONTAL_SPEED=0,
			DEFAULT_POSITION_ON_SHOOTER_AS_A_PERCENTAGE=50;
	protected Shooter theOneWhoShotMe;
	
	public BulletView(RelativeLayout layout,Shooter shooter,float bulletSpeedY,
			int bulletDamage,int width,int height,int imageId) {
		super(layout,bulletSpeedY,
				DEFAULT_HORIZONTAL_SPEED ,bulletDamage,1, width, height, imageId);
	
		//set instance variables
		theOneWhoShotMe=shooter;
		 
		//position bullet behind shooter
		ViewGroup parent = (ViewGroup)theOneWhoShotMe.getParent();
		if(parent!=null){
			parent.removeView(this);//bullet already added to parent in MovingView's instantiation
			int shooterIndex = parent.indexOfChild( (View) theOneWhoShotMe);
			parent.addView(this, shooterIndex);
			
			//position bullet in middle of shooter
//			if(shooter.isFriendly()){
				this.setY(theOneWhoShotMe.getY() + theOneWhoShotMe.getHeight()/2);//middle			
//			}else{
//				this.setY(theOneWhoShotMe.getY() - theOneWhoShotMe.getHeight()/2);//middle
//			}
			setXPositionOnShooterAsAPercentage(DEFAULT_POSITION_ON_SHOOTER_AS_A_PERCENTAGE);
	
	
			if(theOneWhoShotMe.isFriendly()){
				setSpeedY( - Math.abs(bulletSpeedY) );
				LevelSystem.friendlyBullets.add(this);
			}else{
				setSpeedY( Math.abs(bulletSpeedY) );
				LevelSystem.enemyBullets.add(this);			
			}
			theOneWhoShotMe.getMyBullets().add(this);
		}else{
			this.removeGameObject();
			//for some reason, shooters continue to fire even after being killed. This simple check ensure that the shooter
			//has not been removed from his parent. If he has, then remove this bullet
		}
		

		reassignMoveRunnable(new KillableRunnable(){
	    	@Override
	        public void doWork() {
				BulletView.this.setBulletRotation();
				move();
				ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,BulletView.this);
			}
		});
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
	
	/**
	 * 
	 * @param positionOnShooterAsAPercentageOfWidthFromTheLeftSide 100 indicates right side of shoot, 0 is left side, and 50 is middle
	 */
	public void setXPositionOnShooterAsAPercentage(int positionOnShooterAsAPercentageOfWidthFromTheLeftSide) throws IllegalArgumentException{
		if(positionOnShooterAsAPercentageOfWidthFromTheLeftSide < 0 || positionOnShooterAsAPercentageOfWidthFromTheLeftSide > 100){
			throw new IllegalArgumentException("Not a valid percentage");
		}
		final int bulletWidth = this.getLayoutParams().width;
		final float posRelativeToShooter= (float) (theOneWhoShotMe.getWidth() * positionOnShooterAsAPercentageOfWidthFromTheLeftSide/100.0);
		final float middleOfBulletOnShootingPos = (float) (posRelativeToShooter+theOneWhoShotMe.getX()-bulletWidth/2.0);
		this.setX(middleOfBulletOnShootingPos);
	}
	
	/**
	 * Remove bullet from Shooter's list of bullets  and GameActivity's list
	 */
	public void removeGameObject(){		
		if(theOneWhoShotMe.isFriendly()){
			LevelSystem.friendlyBullets.remove(this);
		}else{
			LevelSystem.enemyBullets.remove(this);			
		}
		theOneWhoShotMe.getMyBullets().remove(this);
		this.defaultCleanupOnRemoval();//needs to be the last thing called for handler to remove all callbacks
	}
	

	@Override
	public void restartThreads() {
		super.restartThreads();
	}
}
