package enemies;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import friendlies.ProtagonistView;

public class Shooting_Diagonal_DiveBomberView extends Shooting_DiagonalMovingView{
	

	public static final int DEFAULT_SCORE=100,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_dive_bomber,
			DEFAULT_BULLET_FREQ_INTERVAL=1250,
			NUM_DIVE_BOMBER_COLUMNS=5;
	
	public final static double DEFAULT_SPEED_Y=1.5,
			DEFAULT_SPEED_X=3,
			DEFAULT_HEALTH=ProtagonistView.DEFAULT_BULLET_DAMAGE*2,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.1;
	
	public Shooting_Diagonal_DiveBomberView(Context context) {
		super(context);
		
		//override parent's default values
		this.setScoreValue(DEFAULT_SCORE);
		this.setSpeedX(DEFAULT_SPEED_X);
		this.setSpeedY(DEFAULT_SPEED_Y);
		this.heal(DEFAULT_HEALTH);
		this.setProbSpawnBeneficialObjectOnDeath(DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH);
		this.setLayoutParams(new RelativeLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.ship_dive_bomber_width),
				(int)context.getResources().getDimension(R.dimen.ship_dive_bomber_height)));
		this.setBackgroundResource(DEFAULT_BACKGROUND);
		
		init();
	}
	
	
	public Shooting_Diagonal_DiveBomberView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double probSpawnBeneficialObjecyUponDeath,
			int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, width, height, imageId);
		
		init();
	}
	
	private void init(){
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
