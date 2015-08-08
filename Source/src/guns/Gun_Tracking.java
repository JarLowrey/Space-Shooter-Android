package guns;

import interfaces.Shooter;
import levels.LevelSystem;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;

public abstract class Gun_Tracking extends Gun{

	public Gun_Tracking(RelativeLayout layout, Shooter theShooter, Bullet_Interface bulletType,
			float bulletFrequency, float bulletSpeedVertical,
			int bulletDmg, int positionOnShooterAsAPercentage) {
		super(layout, theShooter, bulletType, bulletFrequency, bulletSpeedVertical,
				bulletDmg, positionOnShooterAsAPercentage);
	}

	public static float MAX_X_SPEED=15*MainActivity.getScreenDens();
	
	protected float getTrackingXSpeed(Shooter shooter, Moving_ProjectileView shootTowardsMe,float bulletSpeedY){
		
		float bulletSpeedX=0;
		
		//check if there is a target set
		if(shootTowardsMe==null){
			if(GameLoop.enemies.size()>0){//set target to to any living enemy
				shootTowardsMe= (Moving_ProjectileView) GameLoop.enemies.get(0);
			}
		}

		//if there is still no target set, shoot straight. Otherwise calculate horizontal speed such that a bullet shot from the 
		//middle of the ship would hit the View shot at, if it were stationary		
		if(shootTowardsMe!=null){
			
			//find the absolute value of vertical distance between the shooter and shot at
			float diffYAbs;
			if(shooter.isFriendly()){
				diffYAbs = Math.abs(shooter.getY() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) );
			}else{
				diffYAbs = Math.abs( shooter.getY()+shooter.getHeight()- shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
			}
			
			//find horizontal distance between middle of shooter to middle of ShotAt. 
			//All spawned bullets will travel at same horizontal speed
			final float diffX = ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2 - ( shooter.getX()*2 +shooter.getWidth() )/2;
			
			//set bulletSpeedX to bulletSpeedY scaled by the ratio of the differences of X/Y. 
			//You are forming a similar triangle to the one formed by the differences
			bulletSpeedX = bulletSpeedY * diffX/diffYAbs;
			
			
			//limit the x speed
			//Otherwise at the bottom of the screen, bullets are ridiculously quick in X direction 
			if( Math.abs(bulletSpeedX) > MAX_X_SPEED ){
				bulletSpeedX = MAX_X_SPEED * ( bulletSpeedX/Math.abs(bulletSpeedX) );
			}
		}
		
		return bulletSpeedX;
	}
}
