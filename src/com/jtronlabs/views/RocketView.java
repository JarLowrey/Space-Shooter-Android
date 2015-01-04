package com.jtronlabs.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class RocketView extends GravityView{
	
	private ImageView rocket_exhaust;
	private boolean exhaust_visible=false,removeRunnablePosted=false;
    Handler rocketHandler = new Handler(); 
    Runnable removeExhaustRunnable = new Runnable() {
        @Override
        public void run() {
        	exhaust_visible=false;
        	if(rocket_exhaust!=null){rocket_exhaust.setVisibility(View.GONE);}
        	rocketHandler.removeCallbacks(showRocketExhaustRunnable);//clean up the runnable moving the fire
        	rocketHandler.removeCallbacks(this);//just in case this runnable was posted multiple times, remove all callbacks
        	removeRunnablePosted=false;
        }
    };
    
    Runnable showRocketExhaustRunnable = new Runnable(){
    	 @Override
         public void run() {
    		 //position the exhaust
			float y=RocketView.this.getY()+RocketView.this.getHeight(); //set the fire's Y pos to behind rocket
			float averageRocketsX= (RocketView.this.getX()+(RocketView.this.getX()+RocketView.this.getWidth()))/2;//find average of rocket's left and right x pos
			float x = averageRocketsX-rocket_exhaust.getWidth()/2;//fire's new X pos should set the middle of fire to middle of rocket
			rocket_exhaust.setY(y);
			rocket_exhaust.setX(x);

    		//make rocket exhaust visible on first runnable.
			//This is required after setting location to prevent exhaust from flashing in the wrong location when it first becomes visible
			//it is in an if() to improve performance
			if(!exhaust_visible){
	 			rocket_exhaust.setVisibility(View.VISIBLE);
	 			exhaust_visible=true;
			}
			
			rocketHandler.postDelayed(this, 20);//repost this runnable so the exhaust will reposition in 20milliseconds
			//post the removal of the exhaust after 0.5 seconds. Utilize if() to ensure the removeRunnable is not posted multiple times
			if(!removeRunnablePosted){
				rocketHandler.postDelayed(removeExhaustRunnable, 500);
				removeRunnablePosted=true;
			}
         }
    };
    
    public RocketView(Context context) {
		super(context);
	}
	public RocketView(Context context,AttributeSet atSet) {
		super(context,atSet);
	}
	
	/**
	 * method to move rocket right or left of its current position.
	 * @param moveLeft true if the rocket needs to be moved left, false if right
	 */
	public void moveRocketToSide(boolean moveLeft){
		float x =this.getX();
		if(moveLeft){
			x-=speedX;
		}else{
			x+=speedX;
		}
		
		//cannot move rocket off of screen
		if(x>0 && (x+this.getWidth()<widthPixels)){
			this.setX(x);
		}
	}
	
	public void moveRocketUp(){
		float y=this.getY();
		y-=speedY;
		//Cannot move rocket off top of screen
		if(y>=0){
			setY(y);
		}
	}
	@Override
	public void cleanUpThreads(){
		super.cleanUpThreads();
		rocketHandler.post(removeExhaustRunnable);
	}
	/**
	 * Flash an image of exhaust behind this rocket.
	 * @param rocketExhaustView - the image of exhaust that is flashed behind the rocket
	 */
	public void runRocketExhaust(ImageView rocketExhaustView){
		rocket_exhaust=rocketExhaustView;
		rocketHandler.post(showRocketExhaustRunnable);
	}
}
