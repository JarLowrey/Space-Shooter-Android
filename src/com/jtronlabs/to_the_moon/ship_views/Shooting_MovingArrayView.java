package com.jtronlabs.to_the_moon.ship_views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.misc.ProjectileView;

public class Shooting_MovingArrayView extends Gravity_ShootingView implements GameObjectInterface {

	public static final int DEFAULT_NUM_COLS=7,DEFAULT_NUM_ROWS=4;
	public static final boolean DEFAULT_STAGGERED=true;
	
	private static ArrayList<Integer> freePositions = new ArrayList<Integer>();
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
				case BOTTOM_LEFT:
					allSimpleShooters.get(i).move(ProjectileView.RIGHT);
					break;
				case BOTTOM_RIGHT:
					allSimpleShooters.get(i).move(ProjectileView.UP);
					break;
				case TOP_RIGHT:
					allSimpleShooters.get(i).move(ProjectileView.LEFT);
					break;
				}
			}
			howManyTimesMoved++;
			if (howManyTimesMoved % 6 == 0) {
				currentPos = (currentPos + 1) % 4;
			}// after moving twice, change direction of movement
			simpleShooterHandler.postDelayed(this,
					ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};

	public static ArrayList<Shooting_MovingArrayView> allSimpleShooters = new ArrayList<Shooting_MovingArrayView>();

	public Shooting_MovingArrayView(Context context, int score,double speedY, double speedX,double collisionDamage, double health, double bulletFreq,
			int backgroundId,int whichBullet,float height,float width) {
		super(context, score, speedY, speedY,
				speedX, collisionDamage, health);

		this.setMyBulletType(whichBullet);
		spawnBulletsAutomatically(bulletFreq);

		final int randPos = (int) (freePositions.size() * Math.random());
		myPosition = freePositions.remove(randPos);

		final float lowestPointOnScreen = heightPixels / numRows;
		final float myRowNum = (myPosition / numCols) * height;
		this.lowestPositionThreshold = lowestPointOnScreen - myRowNum;

		// set image background, width, and height
		this.setImageResource(backgroundId);
		this.setLayoutParams(new RelativeLayout.LayoutParams((int)width,
				(int)height));

		// set initial position
		final float marginOnSides = screenDens * 30;
		final float shipXInterval = (widthPixels - marginOnSides)
				/ numCols;
		final float myColPos = myPosition % numCols;
		float xPos = shipXInterval * myColPos + marginOnSides / 2;
		if (staggered && myRowNum % 2 == 1) {
			xPos += marginOnSides / 2;
		}
		this.setX(xPos);
		this.setY(0);

		allSimpleShooters.add(this);
		cleanUpThreads();
		restartThreads();
	}

	public int removeView(boolean showExplosion) {
		allSimpleShooters.remove(this);
		freePositions.add(myPosition);
		cleanUpThreads();

		return super.removeView(showExplosion);
	}

	public static void beginMovingAllShootersInASquare() {
		simpleShooterHandler.post(moveInASquareRunnable);
	}

	public static void stopMovingAllShooters() {
		simpleShooterHandler.removeCallbacks(moveInASquareRunnable);
	}

	public static int getMaxNumShips() {
		return numCols*numRows;
	}

	public static void resetSimpleShooterArray(int numRowsOfShooters,int numShootersInARow,boolean staggerEnemies) throws UnsupportedOperationException{
		if(!canReset()){
			throw new UnsupportedOperationException("Not all instances of this class are dead");
		}else{
			numRows=numRowsOfShooters;
			numCols=numShootersInARow;
			staggered=staggerEnemies;
			
			for (int i = 0; i < getMaxNumShips(); i++) {
				freePositions.add(i);
			}
			allSimpleShooters = new ArrayList<Shooting_MovingArrayView>();
		}
	}
	
	public static void resetSimpleShooterArray() throws UnsupportedOperationException{
		if(!canReset()){
			throw new UnsupportedOperationException("Not all instances of this class are dead");
		}else{
			numRows=DEFAULT_NUM_ROWS;
			numCols=DEFAULT_NUM_COLS;
			staggered=DEFAULT_STAGGERED;
			
			for (int i = 0; i < getMaxNumShips(); i++) {
				freePositions.add(i);
			}
			allSimpleShooters = new ArrayList<Shooting_MovingArrayView>();
		}
	}
	
	public static boolean canReset(){
		return freePositions.size()==0 || allSimpleShooters.size()==0;
	}

}
