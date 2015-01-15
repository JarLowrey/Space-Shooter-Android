package enemies;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_Diagonal_DiveBomberView extends Shooting_DiagonalMovingView{
	

	public static final int DEFAULT_SCORE=15,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_dive_bomber,
			DEFAULT_BULLET_FREQ_INTERVAL=1250;
	
	public final static double DEFAULT_SPEED_Y=1.2,
			DEFAULT_SPEED_X=3,
			DEFAULT_HEALTH=60,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.1;
	
	
	private final static int NUM_DIVE_BOMBER_COLUMNS=6;
	
	public Shooting_Diagonal_DiveBomberView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjectOnDeath,int width,int height,int imageId) {
		super(context,score,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath, width, height, imageId);
		
		// set col position
		final float shipXInterval = (MainActivity.getWidthPixels() )/ NUM_DIVE_BOMBER_COLUMNS;//divide the screen into number of columns
		final float myColPos = (int) (Math.random()*NUM_DIVE_BOMBER_COLUMNS);//find this ships column
		float xPos = shipXInterval * myColPos;//x position is columInterval * this ships column. Here some left margin is also added
		this.setX(xPos);
		
		//set column boundaries
		leftThreshold=this.getSpeedX()+myColPos*shipXInterval;//farthest ship can move left is up to the boundary of the column it is in
		rightThreshold=(myColPos+1)*shipXInterval-this.getWidth()-this.getSpeedX();//farthest ship can move right is up to irs right side being at the right side of the column it is in
	}
}
