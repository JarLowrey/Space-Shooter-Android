package enemies_orbiters;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Orbiter_Rectangle_Array extends Orbiter_RectangleView{

	public final static int DEFAULT_NUM_ROWS=4,//5
			DEFAULT_NUM_COLS=5, //6
			DEFAULT_SCORE = 50,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_array_shooter,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 1.9);
	
	public final static boolean DEFAULT_STAGGERED=false;
	
	public final static float 
			DEFAULT_SPEED_Y = 10,
			DEFAULT_SPEED_X = DEFAULT_SPEED_Y,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .05,
			LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;

	public static ArrayList<Orbiter_Rectangle_Array> allSimpleShooters = new ArrayList<Orbiter_Rectangle_Array>();
	private static boolean[] occupiedPositions;
	
	// Constantly move all instances of this class in a square shape
	private static int numRows = DEFAULT_NUM_ROWS, 
			numCols = DEFAULT_NUM_COLS;

	private int myPosition;
	
	public Orbiter_Rectangle_Array(Context context, int difficulty) {
		super(context,difficulty);
		
		//change default background (parent has its own default background
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = (int)context.getResources().getDimension(R.dimen.ship_array_shooter_height);
		params.width = (int)context.getResources().getDimension(R.dimen.ship_array_shooter_width);
		this.setLayoutParams(params);
		this.setImageResource(DEFAULT_BACKGROUND);
		this.setHealth( (int) scaledValue(DEFAULT_HEALTH,difficulty,SMALL_SCALING) );


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
		final float myRowPixel = myRow * getContext().getResources().getDimension(R.dimen.ship_array_shooter_height);//, multiply that by heightOfView to get top of row
		this.setThreshold((int) (lowestPointOnScreen - myRowPixel - heightPadding));

		// set col destination
		final float staggeredMargin = getContext().getResources().getDimension(R.dimen.activity_margin_med);
		final float shipXInterval = MainActivity.getWidthPixels()/ numCols;//divide the screen into number of columns
		final float myColPos = myPosition % numCols;//find this ships column
		float xPos = shipXInterval * myColPos ;//x position is columInterval * this ships column. Here some left margin is also added
		if (difficulty > 2 && myRow % 2 == 1) {//stagger the ship positions
			xPos += staggeredMargin / 2;
		}
		this.setX(xPos);

		allSimpleShooters.add(this);
	}

	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
	}

	/**
	 * Use defaults
	 * Can only spawn an array if all shooters from previous array are dead
	 * @param ctx
	 * @param numberRows
	 * @param numberCols
	 * @param isStaggered
	 */
	public static void refreshSimpleShooterArray(Context ctx, int difficulty){
		if(allSimpleShooters==null || allSimpleShooters.size()==0){
			numRows=DEFAULT_NUM_ROWS;
			numCols=DEFAULT_NUM_COLS;
			
			occupiedPositions = new boolean[getMaxNumShips()];
			allSimpleShooters = new ArrayList<Orbiter_Rectangle_Array>();
			
			for(int i=allSimpleShooters.size();i<getMaxNumShips();i++){
				new Orbiter_Rectangle_Array(ctx,difficulty);
			}
			
		}
	}
	

	public void removeGameObject() {
		allSimpleShooters.remove(this);
		occupiedPositions[myPosition] = false;

		super.removeGameObject();//needs to be the last thing called for handler to remove all callbacks
	}

	public static int getMaxNumShips() {
		return numCols*numRows;
	}
	

	@Override
	public void reachedGravityPosition() {
		boolean canBeginRectMovement = true;
		for(Orbiter_Rectangle_Array en : allSimpleShooters){
			canBeginRectMovement = canBeginRectMovement && ( en.isRemoved() || en.hasReachedGravityThreshold() );
		}
		
		//begin moving once lowest shooter has reached correct position from gravity		
		if( canBeginRectMovement ){
			for (int i = 0; i < allSimpleShooters.size(); i++) {
				final Orbiter_Rectangle_Array enemy =  allSimpleShooters.get(i);
				enemy.assignRectangularMoveRunnable(Orbiter_Rectangle_Array.DEFAULT_SPEED_X,Orbiter_Rectangle_Array.DEFAULT_SPEED_Y);
			}
		}
	}
	

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		if(Orbiter_Rectangle_Array.allSimpleShooters.size() < Orbiter_Rectangle_Array.getMaxNumShips()/4){//only refresh if a few left
			if( (level/5) > 0){
				probabilityWeight = 30-(level/5) * 3;
				probabilityWeight = Math.max(probabilityWeight, 5);//always have non-zero probability
			}
		}
		
		return probabilityWeight;
	}
}
