package com.jtronlabs.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.jtronlabs.views.RocketView;
/**
 * A ProjectileView with a constant downwards force that is a different speed than ProjectileView's SpeedY
 * @author JAMES LOWREY
 *
 */
public class GravityView extends ProjectileView implements GameObject{
	
	public GravityView(Context context,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,projectileDamage,projectileHealth);

		gravityHandler.post(gravityRunnable);
	}
	
	public GravityView(Context context,AttributeSet at,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context,at,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,projectileDamage,projectileHealth);

		gravityHandler.post(gravityRunnable);
	}

    //GRAVITY RUNNABLE
    Handler gravityHandler = new Handler();
    Runnable gravityRunnable = new Runnable(){
    	@Override
        public void run() {
			float y=GravityView.this.getY();
    		//if object is off the screen, stop wasting resources on it and mark it for removal. 
			//Otherwise, shift it downwards and repost gravityHandler
    		if(y>heightPixels){
    			gravityHandler.removeCallbacks(this);
    			removeView(false);
    		}else{
        		boolean atThreshold=move(ProjectileView.DOWN,false);//move the View downwards
        		
        		if(atThreshold && !(GravityView.this instanceof RocketView)){//if View is at threshold and is not the protagonist, stop running this Runnable
        			gravityHandler.removeCallbacks(this);
        		}else{
        			gravityHandler.postDelayed(this, ProjectileView.HOW_OFTEN_TO_MOVE);
        		}
    		}
    	}
    };

	public void removeView(boolean showExplosion){
		super.removeView(showExplosion);
		cleanUpThreads();
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
		gravityHandler.removeCallbacks(gravityRunnable);
	}
	public void restartThreads(){
		super.restartThreads();
		gravityHandler.post(gravityRunnable);
	}
	
}
