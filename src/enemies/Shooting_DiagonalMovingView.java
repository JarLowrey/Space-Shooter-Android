package enemies;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Shooting_DiagonalMovingView extends Enemy_ShooterView{
	
	public final static int DEFAULT_SCORE=10,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_diagonal_full_screen,
			DEFAULT_BULLET_FREQ_INTERVAL=1500;
	public final static double DEFAULT_SPEED_Y=1.8,
			DEFAULT_SPEED_X=10,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=40,
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
	
	public Shooting_DiagonalMovingView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double probSpawnBeneficialObjectOnDeath,int width,int height,int imageId) {
		super(context,score,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath, width, height, imageId);
		
		this.setThreshold((int) MainActivity.getHeightPixels());
		if(Math.random()<0.5){this.setSpeedX(this.getSpeedX() * -1);}
		
		this.setX((float) ( (MainActivity.getWidthPixels()-width)*Math.random()));
		this.setY(0);
		
		leftThreshold=0;//far left of screen
		rightThreshold=MainActivity.getWidthPixels()-this.getWidth();//far right of screen

		ConditionalHandler.postIfAlive(moveDiagonalRunnable, this);
	}
	
	public void restartThreads(){
		ConditionalHandler.postIfAlive(moveDiagonalRunnable,HOW_OFTEN_TO_MOVE, this);
		super.restartThreads();
	}
}
