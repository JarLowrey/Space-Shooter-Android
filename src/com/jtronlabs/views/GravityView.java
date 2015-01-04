package com.jtronlabs.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

public class GravityView extends ProjectileView{

	double downwardForce=9.8;
	public float threshold=-10;
	private final int HOW_OFTEN_GRAVITY_IS_APPLIED=200;
	
	public GravityView(Context context) {
		super(context);
		initGravityView();
	}
	public GravityView(Context context,AttributeSet atSet) {
		super(context,atSet);
		initGravityView();
	}
	public GravityView(Context context,double projectileSpeedY,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context,projectileSpeedY,projectileSpeedX,projectileDamage,projectileHealth);
		initGravityView();
	}	
	private void initGravityView(){
		gravityHandler.post(gravityRunnable);
	}

    //GRAVITY RUNNABLE
    Handler gravityHandler = new Handler();
    Runnable gravityRunnable = new Runnable(){
    	@Override
        public void run() {
			float y=GravityView.this.getY();
    		y+=GravityView.this.getDownwardForce();
    		//if object is off the screen, stop wasting resources on it and mark it for removal. 
			//Otherwise, shift it downwards and repost gravityHandler
    		if(y>heightPixels){
    			gravityHandler.removeCallbacks(this);
    			removeView=true;
    		}else{
        		//threshold marks maximum distance downwards. if y is past threshold, do not apply gravity
        		//if threshold is negative, ignore it
        		if(threshold>0){
    				if((y+10*screenDens)<threshold){//add a few pixels so the View will peak above threshold. the exact number is chosen because it looks good
    	    			GravityView.this.setY(y);
    				}
        		}else{
        			GravityView.this.setY(y);
        		}
	    		gravityHandler.postDelayed(this, HOW_OFTEN_GRAVITY_IS_APPLIED);
    		}
    	}
    };
    
	/**
	 * modify the speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeDownwardForce(double ratio){
		downwardForce*=ratio;
	}
	
	public double getDownwardForce(){
		return downwardForce;
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
