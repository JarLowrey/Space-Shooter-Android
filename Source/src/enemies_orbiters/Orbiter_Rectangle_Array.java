package enemies_orbiters;

import java.util.ArrayList;

import levels.AttributesOfLevels;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.Enemy_ShooterView;
import enemies.Shooting_DiagonalMovingView;
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
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .05,
			LOWEST_POSSIBLE_SPOT_ON_SCREEN_AS_A_PERCENTAGE_OF_TOTAL_SCREEN_SIZE=(float) .33;

	public static ArrayList<Orbiter_Rectangle_Array> allSimpleShooters = new ArrayList<Orbiter_Rectangle_Array>();
	private static boolean[] occupiedPositions;
	
	// Constantly move all instances of this class in a square shape
	private static int numRows = DEFAULT_NUM_ROWS, 
			numCols = DEFAULT_NUM_COLS;

	private int myPosition;
	
	public Orbiter_Rectangle_Array(RelativeLayout layout, int level) {
		super(layout,level);
		
		//change default background (parent has its own default background
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.height = (int)getContext().getResources().getDimension(R.dimen.ship_array_shooter_height);
		params.width = (int)getContext().getResources().getDimension(R.dimen.ship_array_shooter_width);
		this.setLayoutParams(params);
		this.setImageResource(DEFAULT_BACKGROUND);
		this.setHealth( scaleHealth(level, getDefaultHealth(level) ) );


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
		this.setGravityThreshold((int) (lowestPointOnScreen - myRowPixel - heightPadding));

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
		Gun defaultGun = new Gun_SingleShotStraight(getMyLayout(), this, 
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

	public static void refreshSimpleShooterArray(RelativeLayout layout, int level){
		if(allSimpleShooters==null || allSimpleShooters.size()==0){
			numRows=DEFAULT_NUM_ROWS;
			numCols=DEFAULT_NUM_COLS;
			
			occupiedPositions = new boolean[getMaxNumShips()];
			allSimpleShooters = new ArrayList<Orbiter_Rectangle_Array>();
			
			for(int i=allSimpleShooters.size();i<getMaxNumShips();i++){
				new Orbiter_Rectangle_Array(layout,level);
			}
			
		}
	}
	 

	@Override
	public void updateViewSpeed(long deltaTime) {
		//only start the rectangle movement once all the array orbiters have reached their position
		boolean canBeginMoving = true;
		for(Orbiter_Rectangle_Array enemy : allSimpleShooters){
			canBeginMoving = canBeginMoving && enemy.hasReachedGravityThreshold();
		}
		if(canBeginMoving){
			super.updateViewSpeed(deltaTime);//begin rectangular motion
		}else if(hasReachedGravityThreshold()){
			this.setSpeedY(0);
		}else{
			//maintain constant speed (gravity) downwards
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
//	
//
//	@Override
//	public void reachedGravityPosition() {
//		boolean canBeginRectMovement = true;
//		for(Orbiter_Rectangle_Array en : allSimpleShooters){
//			canBeginRectMovement = canBeginRectMovement && ( en.isRemoved() || en.hasReachedGravityThreshold() );
//		}
//		
//		//begin moving once lowest shooter has reached correct position from gravity		
//		if( canBeginRectMovement ){
//			for (int i = 0; i < allSimpleShooters.size(); i++) {
//				final Orbiter_Rectangle_Array enemy =  allSimpleShooters.get(i);
//				enemy.assignRectangularMoveRunnable(Orbiter_Rectangle_Array.DEFAULT_SPEED_X,Orbiter_Rectangle_Array.DEFAULT_SPEED_Y);
//			}
//		}
//	}
//	
	private static int getDefaultHealth(int level){
		if( level < AttributesOfLevels.LEVELS_MED){
			return DEFAULT_HEALTH;
		}else{
			return (int) (DEFAULT_HEALTH * 1.7);
		}
	}
	

	public static int getSpawningProbabilityWeight(int level) {
		//NOTE: This has a relatively low probability as it spawns so many enemies that last for a long time and take up a lot of the screen
		int probabilityWeight = 0;
		
		if(Orbiter_Rectangle_Array.allSimpleShooters.size() < Orbiter_Rectangle_Array.getMaxNumShips()/4 
				&& level > AttributesOfLevels.LEVELS_BEGINNER){//only refresh-able if a few left
			if(level < AttributesOfLevels.LEVELS_MED ){
				probabilityWeight = Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) / 9;
			}else {
				probabilityWeight = Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) / 7;
			}
		}
		
		return probabilityWeight;
	}
}
