package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.bullets.Bullet_Interface;
import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public  class Gun_Special_ShootTowardsProjectileDualShot extends Gun_Special {
	
	public static double MAX_X_SPEED=2.1*MainActivity.getScreenDens();
	private ProjectileView shootTowardsMe;
	boolean noSetTarget=false;
	
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,ProjectileView shootingAtMe,
			Gravity_ShootingView theShooter,boolean shootingUpwards,double bulletSpeedVertical,
			double bulletDamage,double bulletFrequency) {
		super(context,theShooter,shootingUpwards,bulletSpeedVertical,bulletDamage,bulletFrequency);
		
		noSetTarget=false;
		 shootTowardsMe = shootingAtMe;
	}
	public Gun_Special_ShootTowardsProjectileDualShot(Context context,ProjectileView shootingAtMe,
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
		
		noSetTarget=false;
		shootTowardsMe = shootingAtMe;
	}

	public Gun_Special_ShootTowardsProjectileDualShot(Context context,
			Gravity_ShootingView theShooter) {
		super(context,theShooter);
		
		noSetTarget=true;
		shootTowardsMe=null;
	}
	
	public boolean shoot(){
		double bulletSpeedX = 0;
		
		if(noSetTarget){
			if(GameActivity.enemies.size()>0){
				//shoot at oldest living enemy
				for(int i=GameActivity.enemies.size()-1;i>=0;i--){
					ProjectileView cast = (ProjectileView) GameActivity.enemies.get(i);
					//check if an enemy is living. if so, set him as target and stop looping
					if( ! cast.isRemoved()){
						shootTowardsMe= cast;
						break;
					}
					//if enemy is not living and end of loop has been reached, set shootTowardsMe to null
					else if(i==0){
						shootTowardsMe=null;
					}
				}
			}else{
				shootTowardsMe=null;
			}
		}
		
		//calculate a new bulletSpeedX
		if(shootTowardsMe!=null){
			//create left and right bullets that travel at same angles, such that left bullet will hit View that is being shot at (if it were stationary)
			double diffYAbs = (shootingUp) ? 
				Math.abs(shooter.getY() - ( shootTowardsMe.getY()+shootTowardsMe.getHeight() ) ) ://top of shooter - bottom of ShotAt
				Math.abs( ( shooter.getY()+shooter.getHeight() ) - shootTowardsMe.getY() );//bottom of shooter - top of ShotAt
				
			final double diffX = ( shootTowardsMe.getX()*2 +shootTowardsMe.getWidth() )/2 - shooter.getX();//from left side of shooter to middle of ShotAt
			final double angleRadians = Math.atan(diffX/diffYAbs);
			bulletSpeedX = shooter.myGun.getBulletSpeedY() * Math.tan(angleRadians);
			
			//limit the x speed
			//Otherwise at the bottom of the screen, bullets are ridiculously quick in X direction.adjust new speed for left/right direction 
			if( Math.abs(bulletSpeedX) > MAX_X_SPEED ){
				bulletSpeedX = MAX_X_SPEED * ( bulletSpeedX/Math.abs(bulletSpeedX) );
			}
	
//			bulletSpeedX = (shootingUp) ? -bulletSpeedX : bulletSpeedX;//
		}
		
		Projectile_BulletView bulletLeft = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				bulletSpeedX,bulletDamage, Bullet_Interface.BULLET_LEFT);
		Projectile_BulletView bulletRight = shooter.myBulletType.getBullet(ctx, shooter, shootingUp, bulletSpeedY, 
				bulletSpeedX,bulletDamage, Bullet_Interface.BULLET_RIGHT);
		
		//add bullets to layout
		((RelativeLayout)shooter.getParent()).addView(bulletLeft,1);
		((RelativeLayout)shooter.getParent()).addView(bulletRight,1);

		//add bullets to shooter's list of bullets
		myBullets.add(bulletLeft);
		myBullets.add(bulletRight);
		
		return decrementAmmo();
	}
}
