package enemies;

import java.util.ArrayList;

import parents.MovingView;
import support.KillableRunnable;
import android.content.Context;
import android.os.Handler;

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
	
	public final static float 
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .05,
			LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;

	public static ArrayList<Shooting_ArrayMovingView> allSimpleShooters;
	private static boolean[] occupiedPositions;
	
	// Constantly move all instances of this class in a square shape
	private static boolean staggered = false,
			wholeArrayIsCurrentlyMovingInRectangle=false;
	private static int numRows,
			numCols,
			currentPos, 
			howManyTimesMoved;
	private static Handler myHandler = new Handler();

	private int myPosition;
	
	private Shooting_ArrayMovingView(Context context) {
		super(context,DEFAULT_SCORE, DEFAULT_SPEED_Y, 0, DEFAULT_COLLISION_DAMAGE, 
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
			wholeArrayIsCurrentlyMovingInRectangle=false;
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
		boolean canBeingRectMovement = true;
		for(Shooting_ArrayMovingView en : allSimpleShooters){
			canBeingRectMovement = canBeingRectMovement && (en.isRemoved() || en.hasReachedGravityThreshold());
		}
		
		//begin moving once lowest shooter has reached correct position from gravity		
		if( canBeingRectMovement ){
			wholeArrayIsCurrentlyMovingInRectangle = true;
			

			for (int i = 0; i < allSimpleShooters.size(); i++) {
				final int i_copy=i;
				allSimpleShooters.get(i).reassignMoveRunnable(new KillableRunnable(){
					int timesMove = 0;
					int curPos = 0;
					@Override
					public void doWork(){
						//increment position, and check for change of direction. If it changes this change the speeds
						if (timesMove % 6 == 0) {
							curPos = (curPos + 1) % 4;
							timesMove=0;
		
							for (int i = 0; i < allSimpleShooters.size(); i++) {
								switch (curPos) {
								case 0:
									allSimpleShooters.get(i_copy).setSpeedY(0);
									allSimpleShooters.get(i_copy).setSpeedX(DEFAULT_SPEED_X);
									break;
								case 1:
									allSimpleShooters.get(i_copy).setSpeedX(0);
									allSimpleShooters.get(i_copy).setSpeedY( - DEFAULT_SPEED_Y);
									break;
								case 2:
									allSimpleShooters.get(i_copy).setSpeedY(0);
									allSimpleShooters.get(i_copy).setSpeedX( - DEFAULT_SPEED_X);
									break;
								case 3:
									allSimpleShooters.get(i_copy).setSpeedX(0);
									allSimpleShooters.get(i_copy).setSpeedY(DEFAULT_SPEED_Y);
									break;
								}
							}
						}
						timesMove++;
						
						allSimpleShooters.get(i_copy).move();
						allSimpleShooters.get(i_copy).postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
					}	
				});
			}
			
//			myHandler.post(new KillableRunnable(){
//				@Override
//				public void doWork(){
//					//increment position, and check for change of direction. If it changes this change the speeds
//					howManyTimesMoved++;
//					if (howManyTimesMoved % 6 == 0) {
//						currentPos = (currentPos + 1) % 4;
//						howManyTimesMoved=0;
//
//						for (int i = 0; i < allSimpleShooters.size(); i++) {
//							switch (currentPos) {
//							case 0:
//								allSimpleShooters.get(i).setSpeedY(0);
//								allSimpleShooters.get(i).setSpeedX(DEFAULT_SPEED_X);
//								break;
//							case 1:
//								allSimpleShooters.get(i).setSpeedX(0);
//								allSimpleShooters.get(i).setSpeedY( - DEFAULT_SPEED_Y);
//								break;
//							case 2:
//								allSimpleShooters.get(i).setSpeedY(0);
//								allSimpleShooters.get(i).setSpeedX( - DEFAULT_SPEED_X);
//								break;
//							case 3:
//								allSimpleShooters.get(i).setSpeedX(0);
//								allSimpleShooters.get(i).setSpeedY(DEFAULT_SPEED_Y);
//								break;
//							}
//						}
////						Log.d("lowrey","xspd= "+allSimpleShooters.get(0).getSpeedX());
//					}
//					
//					myHandler.postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
//				}
//			});
//		
		}
	}

}
