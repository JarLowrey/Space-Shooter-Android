package com.jtronlabs.to_the_moon.misc;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;

public class ProjectileView extends ImageView implements GameObject{

	public static int HOW_OFTEN_TO_MOVE=100;
	public static final int UP=0,RIGHT=1,DOWN=2,LEFT=3;
	
	int score;
	double speedYUp,speedYDown,speedX, damage, health;
	public float lowestPositionThreshold=-1,highestPositionThreshold=-1;
	public static float screenDens,widthPixels,heightPixels;
	protected Context ctx;
	
    private Runnable setBackgroundTransparentRunnable = new Runnable(){
		@Override
		public void run() {
			ProjectileView.this.setBackgroundColor(Color.TRANSPARENT);
		}
    };
	
	public ProjectileView(Context context,int scoreValue,double projectileSpeedYUp,
			double projectileSpeedYDown,double projectileSpeedX,double projectileDamage,
			double projectileHealth) {
		super(context);	
		
		ctx = context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
		
	    score=scoreValue;
		speedYUp=projectileSpeedYUp*screenDens;
		speedYDown=projectileSpeedYDown*screenDens;
		speedX=projectileSpeedX*screenDens;
		damage=projectileDamage;
		health=projectileHealth;
	}
	
	public ProjectileView(Context context,AttributeSet at,int scoreValue,double projectileSpeedYUp,double projectileSpeedYDown,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context,at);	
		
		ctx = context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
		
	    score=scoreValue;
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
	 * @return-true if ProjectileView is at threshold
	 */
	public boolean move(int direction) throws IllegalArgumentException{
		if(direction!=UP && direction!=RIGHT && direction!=DOWN && direction!=LEFT){
			throw new IllegalArgumentException("direction argument must be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT");
		}
		
		float x =this.getX();
		float y =this.getY();
		
		boolean atThreshold=false;
		switch(direction){
		case UP:
			y-=speedYUp;
			if(highestPositionThreshold>=-this.getHeight()){
				if(y>=highestPositionThreshold){this.setY(y);}
				else{atThreshold=true;}					
			}
			break;
		case RIGHT:
			x+=speedX;
			if((x+this.getWidth())<=widthPixels){this.setX(x);}
			break;
		case DOWN:
			y+=speedYDown;
			if(lowestPositionThreshold>0){
				if((y+getHeight())<=lowestPositionThreshold){this.setY(y);}
				else{atThreshold=true;}
			}
			break;
		case LEFT:
			x-=speedX;
			if(x>=0){this.setX(x);}
			break;
		}
		
		return atThreshold;
	}

	/** 
	 * Subtract @param/amountOfDamage from this View's health. Flash the View's background red to indicate damage taken. Return true if the view dies and remove it from the game. Otherwise, return false and create an explosion.
	 * @param amountOfDamage-how much the view's health should be subtracted
	 * @return-true if the view 'dies', false otherwise
	 */
	public boolean takeDamage(double amountOfDamage){
		boolean viewDies=false;
		health-=amountOfDamage;
		
		if(health<=0){
			removeView(true);
			viewDies= true;
		}else{
			//set the background behind this view, and then remove it after howLongBackgroundIsApplied milliseconds
			this.setBackgroundResource(R.drawable.danger);
			final int howLongBackgroundIsApplied=200;
			this.postDelayed(setBackgroundTransparentRunnable, howLongBackgroundIsApplied);
			
			createExplosion();
		}
		
		return viewDies;
	}
	
	public int removeView(boolean showExplosion){
		if(showExplosion){createExplosion();}//show explosion
		if(GameActivity.enemies.contains(this)){GameActivity.enemies.remove(this);}//remove from list of enemies
		cleanUpThreads();//destroy all threads
		
		ViewGroup parent = (ViewGroup)this.getParent();
		if(parent!=null){
			parent.removeView(this);
		}
		
		return score;
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
	
	public double getDamage(){
		return damage;
	}
	
	public void setDamage(double newDamage){
		damage=newDamage;
	}
	
	public void setScoreValue(int newScore){
		score=newScore;
	}
	
	public void cleanUpThreads(){
		this.removeCallbacks(setBackgroundTransparentRunnable);
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
