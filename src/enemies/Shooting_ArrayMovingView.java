package enemies;

import java.util.ArrayList;

import parents.Moving_ProjectileView;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_ArrayMovingView extends Enemy_ShooterView {

	public final static int DEFAULT_NUM_ROWS=4,//5
			DEFAULT_NUM_COLS=5, //6
			DEFAULT_SCORE=30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_array_shooter,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*2,
			DEFAULT_BULLET_FREQ_INTERVAL=1500;
	
	public final static boolean DEFAULT_STAGGERED=true;
	
	public final static float DEFAULT_SPEED_Y=3,
			DEFAULT_SPEED_X=3,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .05,
			LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;

	public static ArrayList<Shooting_ArrayMovingView> allSimpleShooters;
	private static boolean[] occupiedPositions;
	
	// Constantly move all instances of this class in a square shape
	private static boolean staggered = false,
			isMoving=false;
	private static int numRows,
			numCols,
			currentPos, 
			howManyTimesMoved;
	private static Handler staticArrayMovementHandler = new Handler();
	private static Runnable moveInARectangleRunnable = new Runnable() {
		@Override
		public void run() {
			//ensure array of shooters is non empty on run()
			if(allSimpleShooters.size()!=0){
				// loop through all living instances of this class
				for (int i = 0; i < allSimpleShooters.size(); i++) {
					
		    		//double check view is not removed before moving
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
				
				//increment position, and check for change of direction
				howManyTimesMoved++;
				if (howManyTimesMoved % 6 == 0) {
					currentPos = (currentPos + 1) % 4;
					howManyTimesMoved=0;
				}
				
				staticArrayMovementHandler.postDelayed(this,
					Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}else{
				isMoving=false;
			}
		}
	};

	private int myPosition;
	
	private Shooting_ArrayMovingView(Context context) {
		super(context,DEFAULT_SCORE, DEFAULT_SPEED_Y, DEFAULT_SPEED_X, DEFAULT_COLLISION_DAMAGE, 
				DEFAULT_HEALTH,DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				(int)context.getResources().getDimension(R.dimen.ship_array_shooter_width),
				(int)context.getResources().getDimension(R.dimen.ship_array_shooter_height), 
				DEFAULT_BACKGROUND);
		
		//find an open spot for this shooter to go
		for(int i=0;i<occupiedPositions.length;i++){
			if( ! occupiedPositions[i] ){
				myPosition = i;
				occupiedPositions[i] = true;
				break;
			}
		}

		//set row destination
		final int myRow = (myPosition / numCols);
		final double heightPadding = 5 * MainActivity.getScreenDens() * myRow; 
		final float lowestPointOnScreen = MainActivity.getHeightPixels()*LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE;//lowest row is at HeightPixels
		final float myRowPixel = myRow * context.getResources().getDimension(R.dimen.ship_array_shooter_height);//, multiply that by heightOfView to get top of row
		this.setThreshold((int) (lowestPointOnScreen - myRowPixel - heightPadding));

		// set col destination
		final float staggeredMargin = context.getResources().getDimension(R.dimen.activity_margin_med);
		final float shipXInterval = MainActivity.getWidthPixels()/ numCols;//divide the screen into number of columns
		final float myColPos = myPosition % numCols;//find this ships column
		float xPos = shipXInterval * myColPos ;//x position is columInterval * this ships column. Here some left margin is also added
		if (staggered && myRowPixel % 2 == 1) {//stagger
			xPos += staggeredMargin / 2;
		}
		this.setX(xPos);

		allSimpleShooters.add(this);
	}
	
	public void removeGameObject() {
		allSimpleShooters.remove(this);
		occupiedPositions[myPosition] = false;
		
		if(allSimpleShooters.size()==0){
			staticArrayMovementHandler.removeCallbacks(moveInARectangleRunnable);
			isMoving=false;
			howManyTimesMoved=0;
			currentPos=0;
		}

		super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks
	}

	public static int getMaxNumShips() {
		return numCols*numRows;
	}

	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 10* DEFAULT_BULLET_FREQ * Math.random());
	}
	
	/**
	 * Can only spawn an array if all shooters from previous array are dead
	 * @param ctx
	 * @param numberRows
	 * @param numberCols
	 * @param isStaggered
	 */
	public static void refreshSimpleShooterArray(Context ctx,int numberRows,int numberCols,boolean isStaggered){
		if(allSimpleShooters==null || allSimpleShooters.size()==0){
			numRows=numberRows;
			numCols=numberCols;
			staggered=isStaggered;
			howManyTimesMoved=0;
			currentPos = 0;	
			
			occupiedPositions = new boolean[getMaxNumShips()];
			allSimpleShooters = new ArrayList<Shooting_ArrayMovingView>();
			
			for(int i=allSimpleShooters.size();i<getMaxNumShips();i++){
				new Shooting_ArrayMovingView(ctx);
			}
			
		}
		
		Log.d("lowrey","shooting Array Refreshed");
	}

	/**
	 * Use default number of shooters
	 * Can only spawn an array if all shooters from previous array are dead
	 * @param ctx
	 * @param numberRows
	 * @param numberCols
	 * @param isStaggered
	 */
	public static void refreshSimpleShooterArray(Context ctx){
		refreshSimpleShooterArray(ctx,DEFAULT_NUM_ROWS,DEFAULT_NUM_COLS,DEFAULT_STAGGERED);
	}

	@Override
	public void reachedGravityPosition() {
		final int myRow = (myPosition / numCols);
		
		//begin moving once lowest shooter has reached correct position from gravity		
		if(!isMoving && myRow==0){
			isMoving = true;
			staticArrayMovementHandler.post(moveInARectangleRunnable);
		}
	}

}
