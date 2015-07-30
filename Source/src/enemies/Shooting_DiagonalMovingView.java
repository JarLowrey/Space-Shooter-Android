package enemies;

import levels.AttributesOfLevels;
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import parents.Moving_ProjectileView;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_DiagonalMovingView extends Enemy_ShooterView{
	
	public final static int DEFAULT_SCORE=70,
			DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*3,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_diagonal_full_screen,
			DEFAULT_BULLET_FREQ_INTERVAL=1500;
	public final static float 
			DEFAULT_SPEED_Y=12,
			DEFAULT_SPEED_X=DEFAULT_SPEED_Y,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10;
	
	protected double leftThreshold,rightThreshold;
	
	public Shooting_DiagonalMovingView(Context context, int level) {
		super(context,
				scaleScore(level),
				scaleSpeedY(level),
				scaleSpeedX(level),
				scaleCollisionDamage(level),
				scaleHealth(level),
				scaleBeneficialObjectOnDeath(level), 
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_height), 
				DEFAULT_BACKGROUND); 
		
		int width = (int)context.getResources().getDimension(R.dimen.ship_diagonal_width);
		init(width);
		

		//randomly select the new enemy to diagonally traverse the whole screen or to divide a column within the screen
		if(Math.random() < 0.5){
			final int DEFAULT_DIVE_BOMBER_COLUMNS = (int)(MainActivity.getWidthPixels() / width) - 1; //standardize column size
			
			// overwrite left/write thresholds so that each diagonal mover can only stay in a column on the screen
			final float shipXInterval = (MainActivity.getWidthPixels() )/ DEFAULT_DIVE_BOMBER_COLUMNS;//divide the screen into number of columns
			final float myColPos = (int) (Math.random()*DEFAULT_DIVE_BOMBER_COLUMNS);//find this ships column
			float xPos = shipXInterval * myColPos;//x position is columInterval * this ships column. Here some left margin is also added
			this.setX(xPos);
			
			//set column boundaries
			leftThreshold=this.getSpeedX()+myColPos*shipXInterval;//farthest ship can move left is up to the boundary of the column it is in
			rightThreshold=(myColPos+1)*shipXInterval-this.getWidth()-this.getSpeedX();//farthest ship can move right is up to irs right side being at the right side of the column it is in
		}
	}
	
	private void init(int width){

		this.setThreshold((int) MainActivity.getHeightPixels()*2);//move Y to offscreen
		if(Math.random()<0.5){this.setSpeedX(this.getSpeedX() * -1);}
		
		this.setX((float) ( (MainActivity.getWidthPixels()-width)*Math.random()));
		
		leftThreshold=0;//far left of screen
		rightThreshold=MainActivity.getWidthPixels()-this.getWidth();//far right of screen


		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {		
					final float rightSideOfShip = Shooting_DiagonalMovingView.this.getX()+Shooting_DiagonalMovingView.this.getWidth();
					final float leftSideOfShip = Shooting_DiagonalMovingView.this.getX();
					float mySpeedX = Shooting_DiagonalMovingView.this.getSpeedX();
					
					final boolean pastRightSide  = rightSideOfShip>=rightThreshold;
					final boolean pastLeftSide = leftSideOfShip<=leftThreshold;
					if(pastRightSide){
						mySpeedX = Math.abs(mySpeedX) * -1;
						Shooting_DiagonalMovingView.this.setSpeedX(mySpeedX);
					}else if(pastLeftSide){
						mySpeedX = Math.abs(mySpeedX);
						Shooting_DiagonalMovingView.this.setSpeedX(mySpeedX);
					}
					
					move();
					ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,Shooting_DiagonalMovingView.this);
			}
		});
	}
	
	public void restartThreads(){
		super.restartThreads();
	}

	@Override
	public float getShootingFreq(){
		return (float) (DEFAULT_BULLET_FREQ + 5 * DEFAULT_BULLET_FREQ * Math.random());
	}

	@Override
	public void reachedGravityPosition() {
		removeGameObject();
	}

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 30-(level/5)*2;
		probabilityWeight = Math.min(probabilityWeight, 10);
		
		return probabilityWeight;
	}
	
	//SCALING METHODS 
	private static int scaleHealth(int level){
		int value = 0;
		
		if(level < AttributesOfLevels.LEVELS_BEGINNER){
			value = DEFAULT_HEALTH;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			value = (int) (DEFAULT_HEALTH * ( (level/5) * SMALL_SCALING ));
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (DEFAULT_HEALTH * ( (level/7) * SMALL_SCALING ));			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (DEFAULT_HEALTH * ( (level/9) * SMALL_SCALING ));			
		}else{
			value = (int) (DEFAULT_HEALTH * ( (level/12) * SMALL_SCALING ));			
		}
		
		return value;
	}
	
	private static int scaleScore(int level){
		int value = 0;
		
		if(level < AttributesOfLevels.LEVELS_BEGINNER){
			value = DEFAULT_SCORE;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			value = (int) (DEFAULT_SCORE * ( (level/5) * SMALL_SCALING ));
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (DEFAULT_SCORE * ( (level/7) * SMALL_SCALING ));			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (DEFAULT_SCORE * ( (level/9) * SMALL_SCALING ));			
		}else{
			value = (int) (DEFAULT_SCORE * ( (level/12) * SMALL_SCALING ));			
		}
		
		return value;
	}
	
	private static float scaleSpeedY(int level){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_MED){
			value = DEFAULT_SPEED_Y;
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (DEFAULT_SPEED_Y * 1.05);		
		}else{
			value = (float) (DEFAULT_SPEED_Y * 1.1);	
		}
		
		return value;
	}
	
	private static float scaleSpeedX(int level){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_MED){
			value = DEFAULT_SPEED_X;
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (DEFAULT_SPEED_X * 1.05);		
		}else{
			value = (float) (DEFAULT_SPEED_X * 1.1);	
		}
		
		return value;
	}
	

	private static int scaleCollisionDamage(int level){
		int value = 0;
		
		if(level < AttributesOfLevels.LEVELS_LOW){
			value = DEFAULT_COLLISION_DAMAGE;
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (DEFAULT_COLLISION_DAMAGE * 1.5);			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (DEFAULT_COLLISION_DAMAGE * 2.5);			
		}else{
			value = (int) (DEFAULT_COLLISION_DAMAGE * 4);			
		}
		
		return value;
	}
	

	private static float scaleBeneficialObjectOnDeath(int level){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_BEGINNER){
			value = DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			value = (float) (DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH * .95 );
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (float) (DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH * .85);			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH * .7);			
		}else{
			value = (float) (DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH * .5);			
		}
		
		return value;
	}
	
}
