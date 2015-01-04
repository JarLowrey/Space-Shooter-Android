package com.jtronlabs.views;

import com.jtronlabs.new_proj.GameActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ProjectileView extends ImageView{

	public static int HOW_OFTEN_TO_MOVE=100;
	public static final int UP=0,RIGHT=1,DOWN=2,LEFT=3;
	
	double speedYUp,speedYDown,speedX, damage, health;
	public float threshold=-10;
	float screenDens,widthPixels,heightPixels;
	Context ctx;
	
	public ProjectileView(Context context,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX,double projectileDamage,double projectileHealth) {
		super(context);	
		
		ctx = context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
		
		speedYUp=projectileSpeedYUp*screenDens;
		speedYDown=projectileSpeedYDown*screenDens;
		speedX=projectileSpeedX*screenDens;
		damage=projectileDamage;
		health=projectileHealth;
	}
	
	public ProjectileView(Context context,AttributeSet at,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context,at);	
		
		ctx = context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
		
		speedYUp=projectileSpeedYUp*screenDens;
		speedYDown=projectileSpeedYDown*screenDens;
		speedX=projectileSpeedX*screenDens;
		damage=projectileDamage;
		health=projectileHealth;
	}
	
	/**
	 * show an explosion on top of this view
	 */
	public void createExplosion(){
		
	}
	
	/**
	 * Move the View on the screen according to is speedY or speedX
	 * @param direction-whichDirection the View should move. Input needs to be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT
	 * @return-true if a valid direction was passed in, false otherwise
	 */
	public boolean move(int direction,boolean canMoveOffScreen){
		boolean validDirection=false;
		float x =this.getX();
		float y =this.getY();
		
		switch(direction){
		case UP:
			y-=speedYUp;
			validDirection=true;
			break;
		case RIGHT:
			x+=speedX;
			validDirection=true;
			break;
		case DOWN:
			y+=speedYDown;
			validDirection=true;
			break;
		case LEFT:
			x-=speedX;
			validDirection=true;
			break;
		}
		if(canMoveOffScreen){
			this.setX(x);
			this.setY(y);
		}else {
			//cannot move off side of screen
			if(x>=0 && (x+this.getWidth()<=widthPixels)){
				this.setX(x);
			}
			if( ( y+getHeight() ) >=0){
				//Cannot move off top or bottom of screen or past lower threshold
				if(threshold>0){
					if(y<=threshold){this.setY(y);}
				}else{
					this.setY(y);
				}
			}
		}
		
		return validDirection;
	}

	public void takeDamage(double amountOfDamage){
		health-=amountOfDamage;
		if(health<=0){
			removeView(true);
		}else{
			createExplosion();			
		}
	}
	
	public void removeView(boolean showExplosion){
		if(showExplosion){createExplosion();}
		cleanUpThreads();
		GameActivity.gameScreen.removeView(this);
	}
	
	public void heal(double howMuchHealed){
		health+=howMuchHealed;
	}
	
	/**
	 * modify the vertical speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeSpeedYUp(double ratio){
		speedYUp*=ratio;
	}
	

	/**
	 * modify the speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeSpeedYDown(double ratio){
		speedYDown*=ratio;
	}
	
	/**
	 * modify the horizontal speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeSpeedX(double ratio){
		speedX*=ratio;
	}
	
	public double getSpeedY(){
		return speedYUp;
	}
	
	public double getSpeedYDown(){
		return speedYDown;
	}

	public double getSpeedX(){
		return speedX;
	}
	
	public double getHealth(){
		return health;
	}
	
	public void cleanUpThreads(){

	}
	public void restartThreads(){

	}

	public boolean collisionDetection(ProjectileView two){
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
}
