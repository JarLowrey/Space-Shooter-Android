package enemies_orbiters;

import java.util.ArrayList;

import levels.AttributesOfLevels;
import android.content.Context;
import android.view.ViewGroup;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;
import guns.Gun;
import guns.Gun_SingleShotStraight;

public class Orbiter_Rectangle_Array extends Orbiter_RectangleView{

	public final static int DEFAULT_NUM_ROWS=4,//5
			DEFAULT_NUM_COLS=5, //6
			DEFAULT_SCORE = 50,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_array_shooter,
			DEFAULT_HEALTH=(int) ( ProtagonistView.DEFAULT_BULLET_DAMAGE * 3.5 ),
			DEFAULT_BULLET_DAMAGE = Enemy_ShooterView.DEFAULT_BULLET_DAMAGE;
	
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
	
	public Orbiter_Rectangle_Array(Context context, int level) {
		super(context,level);
		
		//change default background (parent has its own default background
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = (int)context.getResources().getDimension(R.dimen.ship_array_shooter_height);
		params.width = (int)context.getResources().getDimension(R.dimen.ship_array_shooter_width);
		this.setLayoutParams(params);
		this.setImageResource(DEFAULT_BACKGROUND);
		this.setHealth( scaleHealth(level, DEFAULT_HEALTH) );


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
		final float shipXInterval = MainActivity.getWidthPixels()/ numCols;//divide the screen into number of columns
		final float myColPos = myPosition % numCols;//find this ships column
		float xPos = shipXInterval * myColPos ;//x position is columInterval * this ships column. Here some left margin is also added
		if(level > AttributesOfLevels.LEVELS_LOW){//stagger the ship positions
			final float staggeredMargin = getContext().getResources().getDimension(R.dimen.activity_margin_med);
			if (myRow % 2 == 1) {
				xPos += staggeredMargin / 2;
			}
		}
		this.setX(xPos);

		allSimpleShooters.add(this);
		
		//add guns
		removeAllGuns();
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ*1.5 + 3 * DEFAULT_BULLET_FREQ * Math.random());
		Gun defaultGun = new Gun_SingleShotStraight(getContext(), this, 
				new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_med_length), 
				R.drawable.bullet_laser_round_red),
				bulletFreq, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				DEFAULT_BULLET_DAMAGE,50);
		this.addGun(defaultGun);
		this.startShooting();
	}

	public static void refreshSimpleShooterArray(Context ctx, int level){
		if(allSimpleShooters==null || allSimpleShooters.size()==0){
			numRows=DEFAULT_NUM_ROWS;
			numCols=DEFAULT_NUM_COLS;
			
			occupiedPositions = new boolean[getMaxNumShips()];
			allSimpleShooters = new ArrayList<Orbiter_Rectangle_Array>();
			
			for(int i=allSimpleShooters.size();i<getMaxNumShips();i++){
				new Orbiter_Rectangle_Array(ctx,level);
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
			//start at 1/4 giant meteor, decrease a little every 10 levels until equal to 1/8 giant meteor
			//NOTE: This has a relatively low probability as it spawns so many enemies that last for a long time and take up a lot of the screen
			if(level > AttributesOfLevels.LEVELS_BEGINNER){
				probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT / 3.0 - 
						(level/10) * AttributesOfLevels.STANDARD_PROB_WEIGHT/10.0);
				
				probabilityWeight = Math.max(probabilityWeight, AttributesOfLevels.STANDARD_PROB_WEIGHT / 7);
			}
		}
		
		return probabilityWeight;
	}
}
