package guns;
  
import interfaces.Shooter;
import levels.LevelSystem;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.util.Log;
import bullets.Bullet;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.MainActivity;

public  class Gun_ShootTowardsTargetSingleShot extends Gun {
	
	public static double MAX_X_SPEED=15*MainActivity.getScreenDens();
	
	private Moving_ProjectileView shootTowardsMe;
	
	public Gun_ShootTowardsTargetSingleShot(Context context, Moving_ProjectileView shootingAtMe,
			Shooter theShooter,Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg,int positionOnShooterAsAPercentage) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
		
		shootTowardsMe = shootingAtMe;
	}

	public Gun_ShootTowardsTargetSingleShot(Context context,
			Shooter theShooter,Bullet bulletType,double bulletFrequency,double bulletSpeedVertical,double bulletDmg,int positionOnShooterAsAPercentage) {
		super(context,theShooter,bulletType, bulletFrequency, bulletSpeedVertical, bulletDmg, positionOnShooterAsAPercentage);
		
		shootTowardsMe=null;
	}
	
	public boolean shoot(){		

		//check if there is a target set
		if(shootTowardsMe==null){
			if(LevelSystem.enemies.size()>0){//set target to to any living enemy
				shootTowardsMe= (Moving_ProjectileView) LevelSystem.enemies.get(0);
			}
		}

		//if there is still no target set, shoot straight. Otherwise calculate horizontal speed such that a bullet shot from the 
		//middle of the ship would hit the View shot at, if it were stationary		
		if(shootTowardsMe!=null){
			
			//find the absolute value of vertical distance between the shooter and shot at
			double diffYAbs;
			if(shooter.isFriendly()){
				diffYAbs = Math.abs(shooter.getY() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) );
			}else{
				diffYAbs = Math.abs( shooter.getY()+shooter.getHeight()- shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
			}
			
			//find horizontal distance between middle of shooter to middle of ShotAt. 
			//All spawned bullets will travel at same horizontal speed
			final double diffX = ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2 - ( shooter.getX()*2 +shooter.getWidth() )/2;
			
			//set bulletSpeedX to bulletSpeedY scaled by the ratio of the differences of X/Y. 
			//You are forming a similar triangle to the one formed by the differences
			this.bulletSpeedX = bulletSpeedY * diffX/diffYAbs;
			
			
			//limit the x speed
			//Otherwise at the bottom of the screen, bullets are ridiculously quick in X direction 
			if( Math.abs(this.bulletSpeedX) > MAX_X_SPEED ){
				this.bulletSpeedX = MAX_X_SPEED * ( this.bulletSpeedX/Math.abs(this.bulletSpeedX) );
			}

			Log.d("lowrey","x speed="+this.bulletSpeedX);
		}
		
		//create 2 bullets
		BulletView bullet = myBulletType.getBullet(ctx, shooter,bulletSpeedY,bulletDamage);
		
		//position bullets on edges of shooter
		bullet.setPositionOnShooterAsAPercentage(this.posOnShooter);
		
		//set bullet's X speed 
		bullet.setSpeedX(bulletSpeedX);
		
		return false;
	}
}
