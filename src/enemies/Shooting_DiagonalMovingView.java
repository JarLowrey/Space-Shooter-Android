package enemies;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_DiagonalMovingView extends Enemy_ShooterView{
	
	public final static int DEFAULT_SCORE=10,
			DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*3,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_diagonal_full_screen,
			DEFAULT_BULLET_FREQ_INTERVAL=1500,
			DEFAULT_DIVE_BOMBER_COLUMNS=5;
	public final static float DEFAULT_SPEED_Y=(float) 1.8,
			DEFAULT_SPEED_X=5,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10;
	
	protected double leftThreshold,rightThreshold;
	
	Runnable moveDiagonalRunnable = new Runnable(){

		@Override
		public void run() {		
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
				Shooting_DiagonalMovingView.this.moveDirection(Moving_ProjectileView.SIDEWAYS);
				
				ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,Shooting_DiagonalMovingView.this);
		}
		
	};
	
	public Shooting_DiagonalMovingView(Context context) {
		super(context,DEFAULT_SCORE,
				DEFAULT_SPEED_Y,DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_height), 
				DEFAULT_BACKGROUND);
		
		init((int)context.getResources().getDimension(R.dimen.ship_diagonal_width));		
	}
	
	public Shooting_DiagonalMovingView(Context context,int numColumns) {
		super(context,DEFAULT_SCORE,
				DEFAULT_SPEED_Y,DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH,
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)context.getResources().getDimension(R.dimen.ship_diagonal_height), 
				DEFAULT_BACKGROUND);
		
		init((int)context.getResources().getDimension(R.dimen.ship_diagonal_width));	
		

		// overwrite left/write thresholds so that each diagonal mover can only stay in a column on the screen
		final float shipXInterval = (MainActivity.getWidthPixels() )/ numColumns;//divide the screen into number of columns
		final float myColPos = (int) (Math.random()*numColumns);//find this ships column
		float xPos = shipXInterval * myColPos;//x position is columInterval * this ships column. Here some left margin is also added
		this.setX(xPos);
		
		//set column boundaries
		leftThreshold=this.getSpeedX()+myColPos*shipXInterval;//farthest ship can move left is up to the boundary of the column it is in
		rightThreshold=(myColPos+1)*shipXInterval-this.getWidth()-this.getSpeedX();//farthest ship can move right is up to irs right side being at the right side of the column it is in
			
	}
	
	public Shooting_DiagonalMovingView(Context context,int score,float speedY, float speedX,int collisionDamage, 
			int health, float probSpawnBeneficialObjecyUponDeath,
			int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, width, height, imageId);
		
		init(width);		
	}
	
	private void init(int width){

		this.setThreshold((int) MainActivity.getHeightPixels()*2);//move Y to offscreen
		if(Math.random()<0.5){this.setSpeedX(this.getSpeedX() * -1);}
		
		this.setX((float) ( (MainActivity.getWidthPixels()-width)*Math.random()));
		
		leftThreshold=0;//far left of screen
		rightThreshold=MainActivity.getWidthPixels()-this.getWidth();//far right of screen

		ConditionalHandler.postIfAlive(moveDiagonalRunnable, this);
	}
	
	public void restartThreads(){
		ConditionalHandler.postIfAlive(moveDiagonalRunnable,HOW_OFTEN_TO_MOVE, this);
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
}
