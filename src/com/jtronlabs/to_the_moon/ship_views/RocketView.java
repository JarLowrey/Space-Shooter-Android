package com.jtronlabs.to_the_moon.ship_views;

import android.content.Context;
import android.util.AttributeSet;

import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.misc.ProjectileView;

public class RocketView extends Gravity_ShootingView implements GameObjectInterface{
	
	private final static int DEFAULT_SCORE=0;
	public final static double DEFAULT_SPEED_UP=12.5, DEFAULT_SPEED_DOWN=2.7,DEFAULT_SPEEDX=14,DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=100,DEFAULT_BULLET_SPEED_Y=10,DEFAULT_BULLET_SPEED_X=-10,DEFAULT_BULLET_DAMAGE=10;
	
	private final int HOW_OFTEN_TO_MOVE=50;
	private int directionMoving=ProjectileView.LEFT;
	private double bulletSpawnFreqInMilliseconds=200;
	
	public RocketView(Context context, AttributeSet at) {
		super(context, at,true,DEFAULT_SCORE,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH);
		
		this.highestPositionThreshold=(int)(heightPixels/3);
		this.setMyBulletProperties(Gravity_ShootingView.LASER_ONE,DEFAULT_BULLET_SPEED_Y,DEFAULT_BULLET_SPEED_X,
				DEFAULT_BULLET_DAMAGE);
		this.stopGravity();
	}

	public RocketView(Context context) {
		super(context,true,DEFAULT_SCORE,DEFAULT_SPEED_UP,DEFAULT_SPEED_DOWN,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH);
		
		this.highestPositionThreshold=(int)(heightPixels/3);
		this.setMyBulletProperties(Gravity_ShootingView.LASER_ONE,DEFAULT_BULLET_SPEED_Y,DEFAULT_BULLET_SPEED_X,
				DEFAULT_BULLET_DAMAGE);
		this.stopGravity();
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
//    		 //position the exhaust
//			float y=RocketView.this.getY()+RocketView.this.getHeight(); //set the fire's Y pos to behind rocket
//			float averageRocketsX= (RocketView.this.getX()+(RocketView.this.getX()+RocketView.this.getWidth()))/2;//find average of rocket's left and right x pos
//			float x = averageRocketsX-rocket_exhaust.getWidth()/2;//fire's new X pos should set the middle of fire to middle of rocket
//			rocket_exhaust.setY(y);
//			rocket_exhaust.setX(x);
//
//    		//make rocket exhaust visible on first runnable.
//			//This is required after setting location to prevent exhaust from flashing in the wrong location when it first becomes visible
//			//it is in an if() to improve performance
//			if(!exhaust_visible){
//	 			rocket_exhaust.setVisibility(View.VISIBLE);
//	 			exhaust_visible=true;
//			}
//			
//			RocketView.this.postDelayed(this, 20);//repost this runnable so the exhaust will reposition in 20milliseconds
//			//post the removal of the exhaust after 0.5 seconds. Utilize if() to ensure the removeRunnable is not posted multiple times
//			if(!removeRunnablePosted){
//				RocketView.this.postDelayed(removeExhaustRunnable, 500);
//				removeRunnablePosted=true;
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
			RocketView.this.move(directionMoving);
			RocketView.this.postDelayed(moveRunnable,HOW_OFTEN_TO_MOVE);
		}
		
	};
	
	/**
	 * Do not allow the rocket to move off the sides of the screen
	 */
	@Override
	public boolean move(int direction){
		float x =this.getX();
		
		switch(direction){
		case ProjectileView.RIGHT:
			x+=this.getSpeedX();
			if((x+this.getWidth())<=widthPixels){this.setX(x);}
			break;
		case ProjectileView.LEFT:
			x-=this.getSpeedX();
			if(x>=0){this.setX(x);}			
			break;
		default:
			return super.move(direction);			
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
	
	public void startShootingImmediately(){
		super.startShootingImmediately(bulletSpawnFreqInMilliseconds);
	}
	@Override
	public void cleanUpThreads(){
		super.cleanUpThreads();
		this.removeCallbacks(moveRunnable);
//		this.post(removeExhaustRunnable); 
	}
	@Override
	public void restartThreads(){
		super.restartThreads();
		stopShooting();
	}
	
	public void removeView(){
		super.removeView(true);
		cleanUpThreads();
	}
}
