package com.jtronlabs.to_the_moon.ship_views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.misc.ProjectileView;

public class Shooting_MovingArrayView extends Gravity_ShootingView implements GameObjectInterface {

	public static final int DEFAULT_NUM_COLS=5,DEFAULT_NUM_ROWS=5;
	public static final boolean DEFAULT_STAGGERED=true;
	private static final float LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;
	private static final double DEFAULT_BULLET_SPEED_Y=10,DEFAULT_BULLET_SPEED_X=-10,DEFAULT_BULLET_DAMAGE=10;
	
	private static ArrayList<Integer> freePositions = new ArrayList<Integer>();
	public static ArrayList<Shooting_MovingArrayView> allSimpleShooters = new ArrayList<Shooting_MovingArrayView>();
	
	private static boolean staggered = false;
	private static int numRows=4,numCols=7;

	private int myPosition;

	// Constantly move all instances of this class in a square shape
	private final static int TOP_LEFT = 0, BOTTOM_LEFT = 1, BOTTOM_RIGHT = 2,
			TOP_RIGHT = 3;
	private static int currentPos = TOP_LEFT, howManyTimesMoved = 0;
	private static Handler simpleShooterHandler = new Handler();
	private static Runnable moveInASquareRunnable = new Runnable() {
		@Override
		public void run() {
			// loop through all living instances of this class
			for (int i = 0; i < allSimpleShooters.size(); i++) {
				switch (currentPos) {
				case TOP_LEFT:
					 allSimpleShooters.get(i).move(ProjectileView.DOWN);
					break;
				case BOTTOM_RIGHT:
					allSimpleShooters.get(i).move(ProjectileView.UP);
					break;
				case BOTTOM_LEFT:
					allSimpleShooters.get(i).move(ProjectileView.RIGHT);
					break;
				case TOP_RIGHT:
					allSimpleShooters.get(i).move(ProjectileView.LEFT);
					break;
				}
			}
			howManyTimesMoved++;
			if (howManyTimesMoved % 6 == 0) {
				currentPos = (currentPos + 1) % 4;
			}
			
			simpleShooterHandler.postDelayed(this,
					ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};

	public Shooting_MovingArrayView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			int backgroundId,float heightView,float widthView) {
		super(context, false,score, speedY, speedY,
				speedX, collisionDamage, health);

		this.setBulletProperties(Gravity_ShootingView.LASER_ONE,DEFAULT_BULLET_SPEED_Y,DEFAULT_BULLET_SPEED_X,DEFAULT_BULLET_DAMAGE);
		startShooting(bulletFreq);

		final int randPos = (int) (freePositions.size() * Math.random());
		myPosition = freePositions.remove(randPos);

		// set image background, width, and height
		this.setImageResource(backgroundId);
		this.setLayoutParams(new RelativeLayout.LayoutParams((int)widthView,
				(int)heightView));

		//set Y destination
		final float lowestPointOnScreen = heightPixels*LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE;//lowest row is at HeightPixels
		final float myRowNum = (myPosition / numCols) * heightView;//, multiply that by heightOfView to get top of row
		this.lowestPositionThreshold = (int) (lowestPointOnScreen - myRowNum);

		// set initial position
		final float marginOnSides = context.getResources().getDimension(R.dimen.activity_margin_med);
		final float shipXInterval = (widthPixels - marginOnSides)/ numCols;//divide the screen into number of columns
		final float myColPos = myPosition % numCols;//find this ships column
		float xPos = shipXInterval * myColPos + marginOnSides / 2;//x position is columInterval * this ships column. Here some left margin is also added
		if (staggered && myRowNum % 2 == 1) {//stagger
			xPos += marginOnSides / 2;
		}
		this.setX(xPos);
		this.setY(0);

		allSimpleShooters.add(this);
		
		
		cleanUpThreads();
		restartThreads();
	}


	/**
	 * Do not allow this to move past lower threshold
	 */
	@Override
	public boolean move(int direction){
		float y =this.getY();
		
		switch(direction){
		case ProjectileView.DOWN:
			y+=this.getSpeedYDown();

			if(lowestPositionThreshold!=ProjectileView.NO_THRESHOLD){
				if((y+getHeight())<=lowestPositionThreshold){
					this.setY(y);
					return false;
				}
				else{return true;}
			}
		default:
			return super.move(direction);			
		}
	}
	
	public int removeView(boolean showExplosion) {
		allSimpleShooters.remove(this);
		freePositions.add(myPosition);
		cleanUpThreads();

		return super.removeView(showExplosion);
	}
	
	public void cleanUpThreads(){
		super.cleanUpThreads();
	}
	/**
	 * The moveDiagonalRunnable will be posted when this reaches its destined spot from gravity. The GravityView super class checks for this specifically in its thread.
	 */
	public void restartThreads(){
		super.restartThreads();
	}

	public static void stopMovingAllShooters() {
		simpleShooterHandler.removeCallbacks(moveInASquareRunnable);
	}

	public static void startMovingAllShooters(){
		simpleShooterHandler.postDelayed(moveInASquareRunnable, ProjectileView.HOW_OFTEN_TO_MOVE);
	}
	public static int getMaxNumShips() {
		return numCols*numRows;
	}

	/**
	 * Reset all static variables for this class
	 * @param numberRows-the array of shooter will have this many rows
	 * @param numberCols-the array of shooter will have this many columns
	 * @param staggerShips-true for each row to be slightly offset from the next
	 * @return-true if successful there are no instances of this class alive, and thus the static portions can be reset. false otherwise
	 */
	public static boolean resetSimpleShooterArray(int numberRows,int numberCols,boolean staggerShips){
		if(!(freePositions.size()==0 || allSimpleShooters.size()==0)){
			return false;
		}else{
			//reset static variables
			numRows=numberRows;
			numCols=numberCols;
			staggered=staggerShips;
			howManyTimesMoved=0;
			currentPos = TOP_LEFT;
			simpleShooterHandler.removeCallbacks(moveInASquareRunnable);
			
			//reset the arraylist of free positions
			freePositions = new ArrayList<Integer>();
			for (int i = 0; i < getMaxNumShips(); i++) {
				freePositions.add(i);
			}
			
			//remove all simple shooters and their bullets from the arraylist containing them
			for(int i=allSimpleShooters.size()-1;i>=0;i--){
				Shooting_MovingArrayView temp = allSimpleShooters.get(i);
				for(int j=temp.myBullets.size();j>=0;j--){
					temp.myBullets.get(i).removeView(false);
				}
				allSimpleShooters.get(i).removeView(false);
			}
			allSimpleShooters = new ArrayList<Shooting_MovingArrayView>();
			return true;
		}
	}
	
	/**
	 * Reset all static variables for this class. Set these variables to their default values
	 * @return-true if successful there are no instances of this class alive, and thus the static portions can be reset. false otherwise
	 */
	public static boolean resetSimpleShooterArray(){
		return resetSimpleShooterArray(DEFAULT_NUM_COLS,DEFAULT_NUM_ROWS,DEFAULT_STAGGERED); 
	} 

}
