package com.jtronlabs.to_the_moon.ship_views;

import com.jtronlabs.to_the_moon.misc.GameObject;

import android.content.Context;
import android.util.AttributeSet;
/**
 * A ProjectileView with a constant downwards force that is a different speed than ProjectileView's SpeedY
 * @author JAMES LOWREY
 *
 */
public class OrbitingView extends GravityView implements GameObject{
	
	private float orbitX,orbitY;
	private int orbitDist;
	
	public OrbitingView(Context context,int scoreValue,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth, float orbitXPixel,float orbitYPixel,int orbitingDistance) {
		super(context,scoreValue,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,projectileDamage,projectileHealth);
	
		orbitDist=orbitingDistance;
		orbitX=orbitXPixel;
		orbitY=orbitYPixel;
	}
	
	public OrbitingView(Context context,AttributeSet at,int scoreValue,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth,float orbitXPixel,float orbitYPixel,int orbitingDistance) {
		super(context,at,scoreValue,projectileSpeedYUp,projectileSpeedYDown,projectileSpeedX,projectileDamage,projectileHealth);
	
		orbitDist=orbitingDistance;
		orbitX=orbitXPixel;
		orbitY=orbitYPixel;
	}
	
    Runnable orbitingRunnable = new Runnable(){
    	@Override
        public void run() {
    		
    	}
    };
    
    
}
