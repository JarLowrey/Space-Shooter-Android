package com.jtronlabs.friendly_types;

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;

public class RocketView extends Friendly_ShooterView{
	
	public final static double DEFAULT_SPEED_Y=12.5,
			DEFAULT_SPEEDX=14,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=1000,
			DEFAULT_BULLET_SPEED_Y=15,
			DEFAULT_BULLET_DAMAGE=10, 
			DEFAULT_BULLET_FREQ=350;
	
	private final int HOW_OFTEN_TO_MOVE_ROCKET=50;
	private int directionMoving=Moving_ProjectileView.LEFT;
	
	public RocketView(Context context, AttributeSet at) {
		super(context, at,DEFAULT_SPEED_Y,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);

		this.stopShooting();
	}

	public RocketView(Context context) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);

		this.stopShooting();
		
	}
	
//	private ImageView rocket_exhaust;
//	private boolean exhaust_visible=false,removeRunnablePosted=false;
//	
//    Runnable removeExhaustRunnable = new Runnable() {
//        @Override
//        public void run() {
//        	exhaust_visible=false;
//        	if(rocket_exhaust!=null){rocket_exhaust.setVisibility(View.GONE);}
//        	RocketView.this.removeCallbacks(showRocketExhaustRunnable);//clean up the runnable moving the fire
//        	RocketView.this.removeCallbacks(this);//just in case this runnable was posted multiple times, remove all callbacks
//        	removeRunnablePosted=false;
//        }
//    };
//    
//    Runnable showRocketExhaustRunnable = new Runnable(){
//    	 @Override
//         public void run() {
//			//ensure view is not removed before running
//			if( ! RocketView.this.isRemoved()){
//	    		 //position the exhaust
//				float y=RocketView.this.getY()+RocketView.this.getHeight(); //set the fire's Y pos to behind rocket
//				float averageRocketsX= (RocketView.this.getX()+(RocketView.this.getX()+RocketView.this.getWidth()))/2;//find average of rocket's left and right x pos
//				float x = averageRocketsX-rocket_exhaust.getWidth()/2;//fire's new X pos should set the middle of fire to middle of rocket
//				rocket_exhaust.setY(y);
//				rocket_exhaust.setX(x);
//	
//	    		//make rocket exhaust visible on first runnable.
//				//This is required after setting location to prevent exhaust from flashing in the wrong location when it first becomes visible
//				//it is in an if() to improve performance
//				if(!exhaust_visible){
//		 			rocket_exhaust.setVisibility(View.VISIBLE);
//		 			exhaust_visible=true;
//				}
//				
//				RocketView.this.postDelayed(this, 20);//repost this runnable so the exhaust will reposition in 20milliseconds
//				//post the removal of the exhaust after 0.5 seconds. Utilize if() to ensure the removeRunnable is not posted multiple times
//				if(!removeRunnablePosted){
//					RocketView.this.postDelayed(removeExhaustRunnable, 500);
//					removeRunnablePosted=true;
//				}
//			}
//         }
//    };
//	/**
//	 * Flash an image of exhaust behind this rocket.
//	 * @param rocketExhaustView - the image of exhaust that is flashed behind the rocket
//	 */
//	public void runRocketExhaust(ImageView rocketExhaustView){
//		rocket_exhaust=rocketExhaustView;
//		this.post(showRocketExhaustRunnable);
//	}

	private Runnable moveRunnable = new Runnable(){
		@Override
		public void run() {
			RocketView.this.moveDirection(directionMoving);
			RocketView.this.postDelayed(moveRunnable,HOW_OFTEN_TO_MOVE_ROCKET);
		}
		
	};
	
	public void restartThreads(){
		super.restartThreads();
		this.stopShooting();//super will start the gun shooting. For the protagonist, gun must not be shooting on restart
	}
	
	/**
	 * Do not allow the rocket to move off the sides of the screen
	 */
	@Override
	public boolean moveDirection(int direction){
		float x =this.getX();
		
		switch(direction){
		case Moving_ProjectileView.RIGHT:
			x+=this.getSpeedX();
			if((x+this.getWidth())<=MainActivity.getWidthPixels()){this.setX(x);}
			break;
		case Moving_ProjectileView.LEFT:
			x-=this.getSpeedX();
			if(x>=0){this.setX(x);}			
			break;
		default:
			return super.moveDirection(direction);			
		}
		return false;
	}
	
	public void beginMoving(int direction){
		directionMoving = direction;
		this.post(moveRunnable);
	}
	public void stopMoving(){
		this.removeCallbacks(moveRunnable);
	}
}
