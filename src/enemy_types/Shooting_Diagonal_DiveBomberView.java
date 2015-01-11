package enemy_types;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class Shooting_Diagonal_DiveBomberView extends Shooting_DiagonalMovingView{
	

	public static final int DEFAULT_DIVE_BOMBER_SCORE=15,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_dive_bomber,
			DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL=1250;
	
	public final static double DEFAULT_DIVE_BOMBER_SPEED_Y=1.8,
			DEFAULT_DIVE_BOMBER_SPEED_X=3,
			DEFAULT_DIVE_BOMBER_COLLISION_DAMAGE=20, 
			DEFAULT_DIVE_BOMBER_HEALTH=60,
			DEFAULT_DIVE_BOMBER_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.1;
	
	public final static double DEFAULT_DIVE_BOMBER_BULLET_SPEED_Y=10,
			DEFAULT_DIVE_BOMBER_BULLET_DAMAGE=10;
	
	
	private final static int NUM_DIVE_BOMBER_COLUMNS=6;
	
	public Shooting_Diagonal_DiveBomberView(Context context, int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjectOnDeath,
			double bulletFreq,double bulletVerticalSpeed,double bulletDamage) {
		super(context,score,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath, bulletFreq, bulletDamage, bulletVerticalSpeed);

		this.setImageResource(DEFAULT_BACKGROUND);
		final int height_int=(int)context.getResources().getDimension(R.dimen.dive_bomber_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.dive_bomber_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
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
