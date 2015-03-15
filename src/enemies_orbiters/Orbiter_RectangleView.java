package enemies_orbiters;

import interfaces.MovingViewInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;


public class Orbiter_RectangleView extends Shooting_OrbiterView implements MovingViewInterface {

	public static final int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/3),
			DEFAULT_ORBIT_X=(int) (MainActivity.getWidthPixels()/2);
	
	public final static int DEFAULT_ORBIT_LENGTH=25,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_rectangle;
	
	private int currentSideOfRectangle,orbitDist;

	public Orbiter_RectangleView(Context context) {
		super(context, DEFAULT_SCORE, 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_rectangular_width), 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_rectangular_height), 
				DEFAULT_BACKGROUND);

		orbitDist=DEFAULT_ORBIT_LENGTH;
		
		init();
	}
	

	public Orbiter_RectangleView(Context context,int score,float speedY,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitLength,int orbitPixelX,int orbitPixelY,int width,int height,int imageId) {
		super(context, score,speedY,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY, width, height, imageId);
		
		orbitDist=orbitLength;
		
		init();
	}
	
	private void init(){

		currentSideOfRectangle=0;
		
		//default to begin orbit at top of rectangle, 3/4 of way through (thus top middle, moving right)
		this.setThreshold((int) (orbitY-(orbitDist*Math.abs(this.getSpeedY()) ) / 2 ));
		howManyTimesMoved=(int) (orbitDist *3.0/4);
	}


	@Override
	protected void reachedGravityPosition() {
		
		reassignMoveRunnable( new KillableRunnable() {
			@Override
			public void doWork() {					
					//change side
					if (howManyTimesMoved % orbitDist == 0) {
						currentSideOfRectangle = (currentSideOfRectangle + 1) % 4;
						
						switch (currentSideOfRectangle) {
							case 0:
								Orbiter_RectangleView.this.setSpeedY(0);
								Orbiter_RectangleView.this.setSpeedX(DEFAULT_SPEED_X);
								break;
							case 1:
								Orbiter_RectangleView.this.setSpeedX(0);
								Orbiter_RectangleView.this.setSpeedY( - DEFAULT_SPEED_Y);
								break;
							case 2:
								Orbiter_RectangleView.this.setSpeedY(0);
								Orbiter_RectangleView.this.setSpeedX( - DEFAULT_SPEED_X);
								break;
							case 3:
								Orbiter_RectangleView.this.setSpeedX(0);
								Orbiter_RectangleView.this.setSpeedY(DEFAULT_SPEED_Y);
								break;
						}
					}
					howManyTimesMoved++;
					
					move();
					ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,Orbiter_RectangleView.this);
			}
		});
	}
}
