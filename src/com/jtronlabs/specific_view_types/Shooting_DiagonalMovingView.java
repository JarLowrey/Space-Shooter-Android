package com.jtronlabs.specific_view_types;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_ShootTowardsProjectileDualShot;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable_StraightDualShot;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class Shooting_DiagonalMovingView extends Gravity_ShootingView implements GameObjectInterface{
	
	public final static int DEFAULT_SCORE=10,DEFAULT_BACKGROUND=R.drawable.ufo,DEFAULT_BULLET_FREQ_INTERVAL=3000;
	public final static double DEFAULT_SPEED_Y=1.8,DEFAULT_SPEED_X=10,
			DEFAULT_COLLISION_DAMAGE=20, DEFAULT_HEALTH=10,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,DEFAULT_BULLET_DAMAGE=10;
	
	private boolean travelingRight;
	private double leftThreshold,rightThreshold;
	
	Runnable moveDiagonalRunnable = new Runnable(){

		@Override
		public void run() {
			boolean offScreen = Shooting_DiagonalMovingView.this.move(ProjectileView.DOWN);
			if(offScreen){
				Shooting_DiagonalMovingView.this.removeView(false);
				Shooting_DiagonalMovingView.this.removeCallbacks(this);
				return;
			}
			if(travelingRight){
				Shooting_DiagonalMovingView.this.move(ProjectileView.RIGHT);
				final double rightSideOfShip = Shooting_DiagonalMovingView.this.getX()+Shooting_DiagonalMovingView.this.getWidth();
				if(rightSideOfShip>=rightThreshold){//ship is on far right portion of screen
					travelingRight=false;
				}				
			}else{
				Shooting_DiagonalMovingView.this.move(ProjectileView.LEFT);
				final double leftSideOfShip = Shooting_DiagonalMovingView.this.getX();
				if(leftSideOfShip <= leftThreshold){//ship is on far left portion of screen
					travelingRight=true;
				}		
			}
			
			Shooting_DiagonalMovingView.this.postDelayed(this,ProjectileView.HOW_OFTEN_TO_MOVE);
		}
		
	};
	
	public Shooting_DiagonalMovingView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,float heightView,float widthView,double probSpawnBeneficialObjectOnDeath,
			double bulletVerticalSpeed,double bulletDamage) {
		super(context,false,score,speedY,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);
		
		this.myGun=new Gun_Upgradeable_StraightDualShot(context, this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
//		this.upgradeOrDowngradeGun(true);
		Gun_Special newGun = new Gun_Special_ShootTowardsProjectileDualShot(context, GameActivity.rocket,this, false, bulletVerticalSpeed, bulletDamage, bulletFreq);
		this.giveSpecialGun(newGun, Integer.MAX_VALUE);
		
		this.lowestPositionThreshold=(int) heightPixels;
		
		//set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		travelingRight = (Math.random()<.5);//50% chance of moving right or left
		this.setX((float) (widthPixels*Math.random()));
		this.setY(0);
		
		leftThreshold=this.getSpeedX();//far left of screen
		rightThreshold=widthPixels-this.getWidth()-this.getSpeedX();//far right of screen

		this.post(moveDiagonalRunnable);
	}
	
	public void restartThreads(){
		super.restartThreads();
		this.postDelayed(moveDiagonalRunnable, ProjectileView.HOW_OFTEN_TO_MOVE);
	}
	
	public static final int DEFAULT_DIVE_BOMBER_SCORE=15,DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL=1500;
	public final static double DEFAULT_DIVE_BOMBER_SPEED_Y=1.9,DEFAULT_DIVE_BOMBER_SPEED_X=3,
			DEFAULT_DIVE_BOMBER_COLLISION_DAMAGE=20, DEFAULT_DIVE_BOMBER_HEALTH=20,
			DEFAULT_DIVE_BOMBER_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.1;
	public final static double DEFAULT_DIVE_BOMBER_BULLET_SPEED_Y=10,
			DEFAULT_DIVE_BOMBER_BULLET_DAMAGE=10;
	
	private final static int NUM_DIVE_BOMBER_COLUMNS=6;
	
	public void setDiveBomber(){
		// set col position
		final float shipXInterval = (widthPixels )/ NUM_DIVE_BOMBER_COLUMNS;//divide the screen into number of columns
		final float myColPos = (int) (Math.random()*NUM_DIVE_BOMBER_COLUMNS);//find this ships column
		float xPos = shipXInterval * myColPos;//x position is columInterval * this ships column. Here some left margin is also added
		this.setX(xPos);
		
		//set column boundaries
		leftThreshold=this.getSpeedX()+myColPos*shipXInterval;//farthest ship can move left is up to the boundary of the column it is in
		rightThreshold=(myColPos+1)*shipXInterval-this.getWidth()-this.getSpeedX();//farthest ship can move right is up to irs right side being at the right side of the column it is in

		this.upgradeOrDowngradeGun(true);
	}
}
