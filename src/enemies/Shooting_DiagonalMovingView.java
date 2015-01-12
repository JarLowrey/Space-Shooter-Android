package enemies;

import abstract_parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout;

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
    		//ensure view is not removed before running
    		if( ! Shooting_DiagonalMovingView.this.isRemoved()){			
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
				
				Shooting_DiagonalMovingView.this.postDelayed(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
    		}
		}
		
	};
	
	public Shooting_DiagonalMovingView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health, double probSpawnBeneficialObjectOnDeath) {
		super(context,score,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);
		
		this.setThreshold((int) MainActivity.getHeightPixels());
		if(Math.random()<0.5){this.setSpeedX(this.getSpeedX() * -1);}
		
		
		//set image background, width, and height
		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.diagonal_shooter_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.diagonal_shooter_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setX((float) (MainActivity.getWidthPixels()*Math.random()));
		this.setY(0);
		
		leftThreshold=0;//far left of screen
		rightThreshold=MainActivity.getWidthPixels()-this.getWidth();//far right of screen

		this.post(moveDiagonalRunnable);
	}
	
	public void restartThreads(){
		this.postDelayed(moveDiagonalRunnable, Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
		super.restartThreads();
	}
}
