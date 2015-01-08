package com.jtronlabs.to_the_moon.views;

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
/**
 * A ProjectileView with a constant downwards force that is a different speed than ProjectileView's SpeedY
 * @author JAMES LOWREY
 *
 */
public class Projectile_GravityView extends ProjectileView implements GameObjectInterface{
	
	public double offLowerScreen =Double.MAX_VALUE/2, offUpperScreen = -Double.MAX_VALUE/2;
	
	public Projectile_GravityView(Context context,int scoreValue,double projectileSpeedYUp,
			double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,
			double projectileHealth,double probSpawnBeneficialObjectOnDeath) {
		super(context,scoreValue,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,
				projectileDamage,projectileHealth, probSpawnBeneficialObjectOnDeath);

		this.post(gravityRunnable);
	}
	
	public Projectile_GravityView(Context context,AttributeSet at,int scoreValue,double projectileSpeedYUp,
			double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth,
			double probSpawnBeneficialObjectOnDeath) {
		super(context,at,scoreValue,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,projectileDamage,
				projectileHealth, probSpawnBeneficialObjectOnDeath);

		this.post(gravityRunnable);
	}

    //GRAVITY RUNNABLE
    Runnable gravityRunnable = new Runnable(){
    	@Override
        public void run() {
			float y=Projectile_GravityView.this.getY();
    		//if object is off the screen, stop wasting resources on it and mark it for removal. 
			//Otherwise, shift it downwards and repost gravityHandler
    		if(y>heightPixels){
    			removeView(false);
    		}else{
        		boolean atThreshold=move(ProjectileView.DOWN);//move the View downwards
        		
        		if(atThreshold){//if View is at threshold stop reposting runnable
        				stopGravity();
        		}else{
        			Projectile_GravityView.this.postDelayed(this, ProjectileView.HOW_OFTEN_TO_MOVE/2);
        		}
    		} 
    	}
    };

	public int removeView(boolean showExplosion){
		cleanUpThreads();
		return super.removeView(showExplosion);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		stopGravity();
	}
	public void restartThreads(){
		super.restartThreads();
		startGravity();
	}
	public void stopGravity(){
		this.removeCallbacks(gravityRunnable);		
	}
	public void startGravity(){
		this.postDelayed(gravityRunnable,ProjectileView.HOW_OFTEN_TO_MOVE);		
	}
	
}