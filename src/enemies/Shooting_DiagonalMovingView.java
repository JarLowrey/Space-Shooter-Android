package enemies;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_DiagonalMovingView extends Enemy_ShooterView{
	
	public final static int DEFAULT_SCORE=10,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_diagonal_full_screen,
			DEFAULT_BULLET_FREQ_INTERVAL=1500;
	public final static double DEFAULT_SPEED_Y=1.8,
			DEFAULT_SPEED_X=10,
			DEFAULT_COLLISION_DAMAGE= ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*3,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=10,
			DEFAULT_BULLET_DAMAGE=10;
	
	protected double leftThreshold,rightThreshold;
	
	Runnable moveDiagonalRunnable = new Runnable(){

		@Override
		public void run() {		
				final double rightSideOfShip = Shooting_DiagonalMovingView.this.getX()+Shooting_DiagonalMovingView.this.getWidth();
				final double leftSideOfShip = Shooting_DiagonalMovingView.this.getX();
				double mySpeedX = Shooting_DiagonalMovingView.this.getSpeedX();
				
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
	
	public Shooting_DiagonalMovingView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double probSpawnBeneficialObjecyUponDeath,
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
}
