package com.jtronlabs.to_the_moon.parents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
/**
 * A ProjectileView with a constant downwards force. This force is removed when the instance reaches its lowest threshold. 
 * The downward force may be different from the upward speed.
 * 
 * @author JAMES LOWREY
 *
 */
public class MovingView extends ImageView implements GameObjectInterface{

	public static final int HOW_OFTEN_TO_MOVE=100,
			UP=0,SIDEWAYS=1,DOWN=2,LEFT=3,RIGHT=4,
			NOT_DEAD=-1;

	boolean isRemoved;
	double speedY,speedX;
	
	public MovingView(Context context,double movingSpeedY,double movingSpeedX) {
		super(context);

		speedY=Math.abs(movingSpeedY)*MainActivity.getScreenDens();
		speedX=movingSpeedX*MainActivity.getScreenDens();
		isRemoved=false;
	}
	
	public MovingView(Context context,AttributeSet at,double movingSpeedY,double movingSpeedX) {
		super(context,at);

		speedY=Math.abs(movingSpeedY)*MainActivity.getScreenDens();
		speedX=movingSpeedX*MainActivity.getScreenDens();
		isRemoved=false;
	}
	
	public boolean collisionDetection(View two){
		float left1,right1,top1,bottom1;
		float left2,right2,top2,bottom2;
		
		//find the values of the x,y positions of the two views
		left1=getX();
		right1=getX()+getWidth();
		top1=getY();
		bottom1=getY()+getHeight();

		left2=two.getX();
		right2=two.getX()+two.getWidth();
		top2=two.getY();
		bottom2=two.getY()+two.getHeight();
		
		//Simple collision detection - determine if the two rectangular areas intersect
		//http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
		return !((bottom1 < top2) ||(top1 > bottom2) || (left1>right2) || (right1<left2));
	}
	
	/**
	 * Move the View on the screen according to is speedY or speedX.
	 * @param direction-whichDirection the View should move. Input needs to be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT
	 * @return Always returns false, overwrite for different behavior
	 */
	public boolean moveDirection(int direction) throws IllegalArgumentException{
		if(direction!=UP && direction!=SIDEWAYS && direction!=DOWN && direction!=LEFT && direction != RIGHT){
			throw new IllegalArgumentException("direction argument must be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT");
		}

		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x =this.getX();
		float y =this.getY();
		switch(direction){
		case UP:
			y-=Math.abs(speedY);
			if(y< -getHeight()){
    			removeGameObject();
    			return true;
    		}
			this.setY(y);
			break;
		case DOWN:
			y+=Math.abs(speedY);
			if(y>MainActivity.getHeightPixels()){
    			removeGameObject();
    			return true;
    		}
			this.setY(y);
			break;
		//move in X direction at speed X, move right if speed X positive and left otherwise
		case SIDEWAYS:
			x+=speedX;
			this.setX(x);
			break;
		//move Left or right on screen, use abs(speedX)
		case LEFT:
			x-=Math.abs(speedX);
			this.setX(x);
			break;
		case RIGHT:
			x+=Math.abs(speedX);
			this.setX(x);
			break;
		}
			
		return false;
	}

	
	
	public void setSpeedX(double newSpeed){
		this.speedX=newSpeed;
	}
	public double getSpeedX(){
		return speedX;
	}
	public boolean isRemoved(){
		return isRemoved;
	}
	public double getSpeedY(){
		return speedY;
	}
	public void setSpeedY(double newSpeed){
		this.speedY=newSpeed;
	}
	@Override
	public void restartThreads() {
		//do nothing yet
	}

	/**
	 * Remove all threads posted to this View and mark as removed. Show an explosion if needed. Remove view from parent layour
	 */
	@Override
	public void removeGameObject() {
		isRemoved=true;
		this.removeCallbacks(null);
		ViewGroup parent = (ViewGroup)this.getParent();
		if(parent!=null){parent.removeView(this);}
	}
}
