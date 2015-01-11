package enemies_orbiters;

import interfaces.GameObjectInterface;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;


public class Orbiter_RectangleView extends Shooting_OrbiterView implements GameObjectInterface {

	public static final int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/3),
			DEFAULT_ORBIT_X=(int) (MainActivity.getWidthPixels()/2);
	
	public final static int DEFAULT_ORBIT_LENGTH=25,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_rectangle;
	
	private int currentSideOfRectangle,orbitDist;

	private Runnable moveInARectangleRunnable = new Runnable() {
		@Override
		public void run() {
    		//ensure view is not removed before running
			if( ! Orbiter_RectangleView.this.isRemoved()){
				switch (currentSideOfRectangle) {
				case 0:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.RIGHT);
					break;
				case 1:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.DOWN);
					break;
				case 2:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.LEFT);
					break;
				case 3:
					Orbiter_RectangleView.this.moveDirection(Moving_ProjectileView.UP);
					break;
				}
				
				//change side
				if (howManyTimesMoved % orbitDist == 0) {
					currentSideOfRectangle = (currentSideOfRectangle + 1) % 4;
				}
				howManyTimesMoved++;
				
				Orbiter_RectangleView.this.postDelayed(this,
						Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	
	public Orbiter_RectangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, 
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength) {
		super(context, score,speedY, speedX,
				collisionDamage, health,heightView,widthView,
				 probSpawnBeneficialObjecyUponDeath);

		//set image background, width, and height and orbit location
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_rectangular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_rectangular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setBackgroundResource(DEFAULT_BACKGROUND);

		currentSideOfRectangle=0;
		orbitDist=orbitLength;
	
		//default to begin orbit at top of rectangle, 3/4 of way through (thus top middle, moving right)
		this.setThreshold((int) (orbitY-(orbitDist*this.getMagnitudeOfSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist *3.0/4);
	}
	

	public Orbiter_RectangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, 
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength,int orbitPixelX,int orbitPixelY) {
		super(context, score,speedY, speedX,
				collisionDamage, health,heightView,widthView,
				 probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY);
		

		//set image background, width, and height and orbit location
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_rectangular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_rectangular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setBackgroundResource(DEFAULT_BACKGROUND);

		currentSideOfRectangle=0;
		orbitDist=orbitLength;
	
		//default to begin orbit at top of rectangle, 3/4 of way through (thus top middle, moving right)
		this.setThreshold((int) (orbitY+(orbitDist*this.getMagnitudeOfSpeedY() ) ));
		howManyTimesMoved=(int) (orbitDist *3.0/4);
	}
	
	public void beginOrbit(){
		this.post(moveInARectangleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInARectangleRunnable);
	}

}
