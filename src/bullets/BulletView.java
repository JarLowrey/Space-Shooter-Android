package bullets;
  
import interfaces.Shooter;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.GameActivity;

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
    		//ensure view is not removed before running
    		if( ! BulletView.this.isRemoved()){
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
    			BulletView.this.postDelayed(this,HOW_OFTEN_TO_MOVE);
    		}
    	}
	};
	
	public BulletView(Context context,Shooter shooter,
			int bulletWidth, int bulletHeight,double bulletSpeedY,double bulletDamage) {
		super(context,bulletSpeedY,
				DEFAULT_HORIZONTAL_SPEED ,bulletDamage,1);
	
		//set instance variables
		theOneWhoShotMe=shooter;
		
		//very important to set these layout params of height and width before setting position
		this.setLayoutParams(new LayoutParams(bulletWidth,bulletHeight));
		
		if(shooter.isFriendly()){
			this.setY(theOneWhoShotMe.getY());//top			
		}else{
			this.setY(theOneWhoShotMe.getY()+theOneWhoShotMe.getHeight());//bottom
		}
		setPositionOnShooterAsAPercentage(DEFAULT_POSITION_ON_SHOOTER_AS_A_PERCENTAGE);
		
		shooter.getMyBullets().add(this);
		this.post(moveBulletRunnable);
	}
	
	public void setBulletRotation(){	
		float rotVal=0;
		
		
		double 
		arcTan = Math.atan(this.getSpeedX()/this.getSpeedY());
		if( ! theOneWhoShotMe.isFriendly()){
			arcTan = Math.atan(-this.getSpeedX()/this.getSpeedY());
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
		final double posRelativeToShooter= theOneWhoShotMe.getWidth() * positionOnShooterAsAPercentageOfWidthFromTheLeftSide/100.0;
		final float middleOfBulletOnShootingPos = (float) (posRelativeToShooter+theOneWhoShotMe.getX()-bulletWidth/2.0);
		this.setX(middleOfBulletOnShootingPos);
	}
	
	/**
	 * Remove bullet from Shooter's list of bullets. Check if Shooter is dead and all bullets are gone, if so remove Shooter from GameActivity.enemies. call super
	 */
	public void removeGameObject(){
		theOneWhoShotMe.getMyBullets().remove(this);
		if(theOneWhoShotMe.isDead() && theOneWhoShotMe.getMyBullets().size()==0){
			GameActivity.enemies.remove(theOneWhoShotMe);
		}
		
		super.removeGameObject();
	}
	

	@Override
	public void restartThreads() {
		this.post(moveBulletRunnable);
		super.restartThreads();
	}
}
