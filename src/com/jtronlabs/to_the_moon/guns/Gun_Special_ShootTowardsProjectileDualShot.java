package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public  class Gun_Special_ShootTowardsProjectileDualShot extends Gun_Special {
	
	public static double MAX_X_SPEED=2.1*MainActivity.getScreenDens();
	
	private Moving_ProjectileView shootTowardsMe;
	
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,Moving_ProjectileView shootingAtMe,
			Shooter theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
		
		shootTowardsMe = shootingAtMe;
	}
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,Moving_ProjectileView shootingAtMe,
			Shooter theShooter) {
		super(context,theShooter);
		
		shootTowardsMe = shootingAtMe;
	}

	public Gun_Special_ShootTowardsProjectileDualShot(Context context,
			Shooter theShooter) {
		super(context,theShooter);
		
		shootTowardsMe=null;
	}
	
	public boolean shoot(){		
		//check if there is a target set
		if(shootTowardsMe==null){
			if(GameActivity.enemies.size()>0){
				//find oldest living enemy
				for(int i=GameActivity.enemies.size()-1;i>=0;i--){
					Moving_ProjectileView cast = (Moving_ProjectileView) GameActivity.enemies.get(i);
					//check if an enemy is living. if so, set him as target and stop looping
					if( ! cast.isRemoved()){
						shootTowardsMe= cast;
						break;
					}
				}
			}
		}

		double bulletSpeedX = 0;
		//if there is still no target set, shoot straight. Otherwise calculate horizontal speed such that a bullet shot from the 
		//left side of the ship would hit the View shot at, if it were stationary		
		if(shootTowardsMe!=null){
			
			//find the absolute value of vertical distance between the shooter and shot at
			double diffYAbs;
			if(shooter.isFriendly()){
				diffYAbs = Math.abs(shooter.getTop() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) );
			}else{
				diffYAbs = Math.abs( shooter.getBottom()- shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
			}
			
			//find horizontal distance between left side of shooter to middle of ShotAt. 
			//All spawned bullets will travel at same horizontal speed
			final double diffX = ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2 - shooter.getLeft();
			
			//set bulletSpeedX to bulletSpeedY scaled by the ratio of the differences of X/Y. 
			//You are forming a similar triangle to the one formed by the differences
			bulletSpeedX = shooter.getBulletSpeedY() *diffX/diffYAbs;
			
			//limit the x speed
			//Otherwise at the bottom of the screen, bullets are ridiculously quick in X direction 
			if( Math.abs(bulletSpeedX) > MAX_X_SPEED ){
				bulletSpeedX = MAX_X_SPEED * ( bulletSpeedX/Math.abs(bulletSpeedX) );
			}
		}

		//create 2 bullets at edges of shooter
		BulletView bulletLeft = shooter.getBulletType().getBullet(ctx, shooter,bulletSpeedX, Bullet.BULLET_LEFT);
		BulletView bulletRight = shooter.getBulletType().getBullet(ctx, shooter,bulletSpeedX,Bullet.BULLET_RIGHT);
		
		//add bullets to layout
		((RelativeLayout)shooter.getMyScreen()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getMyScreen()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}
