package enemy_types;

import java.util.ArrayList;

import parents.Moving_ProjectileView;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Shooting_ArrayMovingView extends Enemy_ShooterView {

	public static final int DEFAULT_NUM_COLS=5,
			DEFAULT_NUM_ROWS=5, 
			DEFAULT_SCORE=10,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_array_shooter;
	public static final boolean DEFAULT_STAGGERED=true;
	public final static double DEFAULT_SPEED_Y=3,
			DEFAULT_SPEEDX=2,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=50,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.05;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10,
			DEFAULT_BULLET_FREQ_INTERVAL=5000;
	

	public static ArrayList<Shooting_ArrayMovingView> allSimpleShooters = new ArrayList<Shooting_ArrayMovingView>();
	

	private static final float LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;
	private static ArrayList<Integer> freePositions = new ArrayList<Integer>();
	
	private static boolean staggered = false;
	private static int numRows=4,numCols=7;

	private int myPosition;

	// Constantly move all instances of this class in a square shape
	private static int currentPos = 0, howManyTimesMoved = 0;
	private static Handler simpleShooterHandler = new Handler();
	private static Runnable moveInARectangleRunnable = new Runnable() {
		@Override
		public void run() {
			// loop through all living instances of this class
			for (int i = 0; i < allSimpleShooters.size(); i++) {
	    		//ensure view is not removed before moving
	    		if( ! allSimpleShooters.get(i).isRemoved()){
					switch (currentPos) {
					case 0:
						allSimpleShooters.get(i).moveDirection(Moving_ProjectileView.RIGHT);
						break;
					case 1:
						allSimpleShooters.get(i).moveDirection(Moving_ProjectileView.UP);
						break;
					case 2:
						allSimpleShooters.get(i).moveDirection(Moving_ProjectileView.LEFT);
						break;
					case 3:
						allSimpleShooters.get(i).moveDirection(Moving_ProjectileView.DOWN);
						break;
					}
	    		}
			}
			howManyTimesMoved++;
			if (howManyTimesMoved % 6 == 0) {
				currentPos = (currentPos + 1) % 4;
				howManyTimesMoved=0;
			}
			
			simpleShooterHandler.postDelayed(this,
					Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
		}
	};

	public Shooting_ArrayMovingView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health,
			float heightView,float widthView,double probSpawnBeneficialObject,double bulletFreq,double bulletDamage,double bulletVerticalSpeed) {
		super(context,score, speedY, speedX, collisionDamage, health,probSpawnBeneficialObject, bulletFreq, bulletDamage,bulletVerticalSpeed);

		final int randPos = (int) (freePositions.size() * Math.random());
		myPosition = freePositions.remove(randPos);

		// set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		this.setLayoutParams(new RelativeLayout.LayoutParams((int)widthView,
				(int)heightView));

		//set row destination
		final float lowestPointOnScreen = MainActivity.getHeightPixels()*LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE;//lowest row is at HeightPixels
		final float myRowNum = (myPosition / numCols) * heightView;//, multiply that by heightOfView to get top of row
		this.setThreshold((int) (lowestPointOnScreen - myRowNum));

		// set col position
		final float marginOnSides = context.getResources().getDimension(R.dimen.activity_margin_med);
		final float shipXInterval = (MainActivity.getWidthPixels() - marginOnSides)/ numCols;//divide the screen into number of columns
		final float myColPos = myPosition % numCols;//find this ships column
		float xPos = shipXInterval * myColPos + marginOnSides / 2;//x position is columInterval * this ships column. Here some left margin is also added
		if (staggered && myRowNum % 2 == 1) {//stagger
			xPos += marginOnSides / 2;
		}
		this.setX(xPos);
		this.setY(0);
 
		allSimpleShooters.add(this);
		
//		Gun_Special_ShootTowardsProjectileDualShot newGun = new
//				Gun_Special_ShootTowardsProjectileDualShot(context, GameActivity.rocket, this);
//		this.giveSpecialGun(newGun, 1000);

		
	}
	
	public void removeGameObject() {
		allSimpleShooters.remove(this);
		freePositions.add(myPosition);

		super.removeGameObject();
	}

	public static void stopMovingAllShooters() {
		simpleShooterHandler.removeCallbacks(moveInARectangleRunnable);
	}

	public static void startMovingAllShooters(){
		simpleShooterHandler.postDelayed(moveInARectangleRunnable, Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
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
			currentPos = 0;
			simpleShooterHandler.removeCallbacks(moveInARectangleRunnable);
			
			//reset the arraylist of free positions
			freePositions = new ArrayList<Integer>();
			for (int i = 0; i < getMaxNumShips(); i++) {
				freePositions.add(i);
			}
			
			//remove all simple shooters and their bullets from the arraylist containing them
			for(int i=allSimpleShooters.size()-1;i>=0;i--){
				Shooting_ArrayMovingView temp = allSimpleShooters.get(i);
				for(int j=temp.getMyBullets().size();j>=0;j--){
					temp.getMyBullets().get(i).removeGameObject();
				}
				allSimpleShooters.get(i).removeGameObject();
			}
			allSimpleShooters = new ArrayList<Shooting_ArrayMovingView>();
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
