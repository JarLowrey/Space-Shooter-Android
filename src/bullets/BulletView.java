package bullets;
  
import interfaces.Shooter;
import levels.LevelSystem;
import parents.MovingView;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * By default, a bullet moves straight and spawns in the middle of its shooter
 * @author lowre_000
 *
 */
public class BulletView extends Moving_ProjectileView{

	public static final int DEFAULT_HORIZONTAL_SPEED=0,
			DEFAULT_POSITION_ON_SHOOTER_AS_A_PERCENTAGE=50;
	private Shooter theOneWhoShotMe;
	
	Runnable moveBulletRunnable = new Runnable(){
    	@Override
        public void run() {
    		//move up and down
    		if(theOneWhoShotMe.isFriendly()){
    			BulletView.this.moveDirection(MovingView.UP);
    		}else{
    			BulletView.this.moveDirection(MovingView.DOWN);
    		}
    		
    		//move sideways only if horizontal speed is not 0. This is not needed (since 0 horizontal speed results in 0 movement),
    		//but I'd like to think it saves some resources
			if( (Math.abs(BulletView.this.getSpeedX())>0.0001)){
				//move sideways
    			BulletView.this.moveDirection(MovingView.SIDEWAYS);
    		}

			BulletView.this.setBulletRotation();
			ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,BulletView.this);
		}
	};
	
	public BulletView(Context context,Shooter shooter,
			int bulletWidth, int bulletHeight,float bulletSpeedY,int bulletDamage,int width,int height,int imageId) {
		super(context,bulletSpeedY,
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
			setPositionOnShooterAsAPercentage(DEFAULT_POSITION_ON_SHOOTER_AS_A_PERCENTAGE);
	
			ConditionalHandler.postIfAlive(moveBulletRunnable, this);
	
			if(theOneWhoShotMe.isFriendly()){
				LevelSystem.friendlyBullets.add(this);
			}else{
				LevelSystem.enemyBullets.add(this);			
			}
			theOneWhoShotMe.getMyBullets().add(this);
		}else{
			this.removeGameObject();
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
	
	/**
	 * 
	 * @param positionOnShooterAsAPercentageOfWidthFromTheLeftSide 100 indicates right side of shoot, 0 is left side, and 50 is middle
	 */
	public void setPositionOnShooterAsAPercentage(int positionOnShooterAsAPercentageOfWidthFromTheLeftSide) throws IllegalArgumentException{
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
		this.deaultCleanupOnRemoval(false);//needs to be the last thing called for handler to remove all callbacks
	}
	

	@Override
	public void restartThreads() {
		ConditionalHandler.postIfAlive(moveBulletRunnable, this);
		super.restartThreads();
	}
}
