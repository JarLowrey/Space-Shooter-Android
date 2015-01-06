package com.jtronlabs.to_the_moon.ship_views;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObject;
import com.jtronlabs.to_the_moon.misc.ProjectileView;

public class MovingShooterArrayView extends ShootingView implements GameObject {

	private final static int DEFAULT_SCORE = 15;
	public final static double DEFAULT_SPEED_UP = 3, DEFAULT_SPEED_DOWN = 3,
			DEFAULT_SPEEDX = 3, DEFAULT_COLLISION_DAMAGE = 20,
			DEFAULT_HEALTH = 10;

	public final static int NUM_SHOOTERS_IN_A_ROW = 7, NUM_ROWS = 4;

	private static ArrayList<Integer> freePositions = new ArrayList<Integer>();
	private static boolean staggered = false;

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

	public static ArrayList<MovingShooterArrayView> allSimpleShooters = new ArrayList<MovingShooterArrayView>();

	public MovingShooterArrayView(Context context) {
		super(context, DEFAULT_SCORE, DEFAULT_SPEED_UP, DEFAULT_SPEED_DOWN,
				DEFAULT_SPEEDX, DEFAULT_COLLISION_DAMAGE, DEFAULT_HEALTH);

		final double bulletFreq = (5000 + Math.random() * 4000);
		spawnBulletsAutomatically(bulletFreq);

		final int randPos = (int) (freePositions.size() * Math.random());
		myPosition = freePositions.remove(randPos);

		// ships move to a certain position on screen. There a set number of
		// ships in each row, if the number is exceeded move to the next row
		final float height = context.getResources().getDimension(
				R.dimen.simple_enemy_shooter_height);
		final float lowestPointOnScreen = heightPixels / NUM_ROWS;
		final float myRowNum = (myPosition / NUM_SHOOTERS_IN_A_ROW) * height;
		this.lowestPositionThreshold = lowestPointOnScreen - myRowNum;

		// set image background, width, and height
		this.setImageResource(R.drawable.ufo);
		final int height_int = (int) height;
		int width_int = (int) context.getResources().getDimension(
				R.dimen.simple_enemy_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,
				height_int));

		// set initial position
		final float marginOnSides = screenDens * 30;
		final float shipXInterval = (widthPixels - marginOnSides)
				/ NUM_SHOOTERS_IN_A_ROW;
		final float myColPos = myPosition % NUM_SHOOTERS_IN_A_ROW;
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

	public static boolean toggleStaggered() {
		staggered = !staggered;
		return staggered;
	}

	public static void beginMovingAllShootersInASquare() {
		simpleShooterHandler.post(moveInASquareRunnable);
	}

	public static void stopMovingAllShooters() {
		simpleShooterHandler.removeCallbacks(moveInASquareRunnable);
	}

	public static int getMaxNumShips() {
		return NUM_SHOOTERS_IN_A_ROW * NUM_ROWS;
	}

	public static void resetSimpleShooterArray() {
		for (int i = 0; i < getMaxNumShips(); i++) {
			freePositions.add(i);
		}
		allSimpleShooters = new ArrayList<MovingShooterArrayView>();
	}

}
