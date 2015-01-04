package com.jtronlabs.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class ProjectileView extends ImageView{
	
	double speedY,speedX, damage, health;
	float screenDens,widthPixels,heightPixels;
	boolean removeView=false;
	Context ctx;
	
	public ProjectileView(Context context) {
		super(context);
		initProjectileView(context);
	}
	
	public ProjectileView(Context context, AttributeSet attSet){
		super(context,attSet);
		initProjectileView(context);
	}
	
	/**
	 * Create a new projectileView with given base speed, damage, and health
	 * @param context
	 * @param projectileSpeed
	 * @param projectileDamage
	 * @param projectileHealth
	 */
	public ProjectileView(Context context,double projectileSpeedY,double projectileSpeedX, double projectileDamage,double projectileHealth) {
		super(context);
		initProjectileView(context);
		
		speedY=projectileSpeedY*screenDens;
		speedX=projectileSpeedX*screenDens;
		damage=projectileDamage;
		health=projectileHealth;
	}
	
	/**
	 * show an explosion on top of this view
	 */
	public void explode(){
		
	}

	public void takeDamage(double amountOfDamage){
		health-=amountOfDamage;
		if(health<=0){
			removeView=true;
		}
	}
	
	public void heal(double howMuchHealed){
		health+=howMuchHealed;
	}
	
	/**
	 * modify the vertical speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeSpeedY(double ratio){
		speedY*=ratio;
	}
	
	/**
	 * modify the horizontal speed by a given ratio amount
	 * @param ratio-speed will change by amount equal to @param/ratio. So, to double the speed, ratio should be 2
	 */
	public void changeSpeedX(double ratio){
		speedX*=ratio;
	}
	
	public double getSpeedY(){
		return speedY;
	}

	public double getSpeedX(){
		return speedX;
	}
	
	public double getHealth(){
		return health;
	}
	/**
	 * Freeing up Views to be removed improves memory performance
	 * @return-Views should be removed if they are dead or offscreen
	 */
	public boolean toRemoveView(){
		return removeView;
	}
	
	public void cleanUpThreads(){

	}
	public void restartThreads(){

	}
	
	/**
	 * Set speed, damage, and health to default values, find properties of user device's screen, and post the effect of gravity on this ProjectileView
	 * @param context
	 */
	private void initProjectileView(Context context){		
		ctx = context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
	    
		speedY=12.5*screenDens;
		speedX=12.5*screenDens;
		damage=10;
		health=100;
	}
	

	public boolean collisionDetection( View two){
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
