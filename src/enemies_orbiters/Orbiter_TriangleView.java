package enemies_orbiters;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import parents.Moving_ProjectileView;
import interfaces.GameObjectInterface;
import android.content.Context;
import android.widget.RelativeLayout;


public class Orbiter_TriangleView extends Shooting_OrbiterView implements GameObjectInterface {

	public final static int DEFAULT_ORBIT_LENGTH=15,
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_triangle;
	
	private int currentSideOfTriangle, orbitDist;

	private Runnable moveInATriangleRunnable = new Runnable(){
		@Override
		public void run() {
    		//ensure view is not removed before running
			if( ! Orbiter_TriangleView.this.isRemoved()){
				//triangle is equilateral
					switch (currentSideOfTriangle) {
					case 0:
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.LEFT);
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.LEFT);
						break;
					case 1:
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.RIGHT);
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.DOWN);
						break;
					case 2:
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.RIGHT);
						Orbiter_TriangleView.this.moveDirection(Moving_ProjectileView.UP);
						break;
					}
					//change side
					if (howManyTimesMoved % orbitDist == 0) {
						currentSideOfTriangle = (currentSideOfTriangle + 1) % 3;
					}
					howManyTimesMoved++;
					
					Orbiter_TriangleView.this.postDelayed(this,
							Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	
	public Orbiter_TriangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength) {
		super(context, score,speedY, speedX,
				collisionDamage, health,heightView,widthView,
				 probSpawnBeneficialObjecyUponDeath);

		//set image background, width, and height
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_triangular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_triangular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setBackgroundResource(DEFAULT_BACKGROUND);
		
		orbitX=(int) (MainActivity.getWidthPixels()/2-width_int/2);
		orbitY=(int) (MainActivity.getHeightPixels()/2-height_int/2);
		
		currentSideOfTriangle=0;
		orbitDist=orbitLength;

		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold((int) (orbitY+(orbitDist*this.getSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist /3.0);
		this.setX(orbitX-width_int/2);
	}
	

	public Orbiter_TriangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength, int orbitPixelX, int orbitPixelY) {
		super(context, score,speedY, speedX,
				collisionDamage, health,heightView,widthView, probSpawnBeneficialObjecyUponDeath);

		//set image background, width, and height
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_triangular_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_triangular_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		
		this.setBackgroundResource(DEFAULT_BACKGROUND);
		
		orbitX=orbitPixelX;
		orbitY=orbitPixelY;
		
		currentSideOfTriangle=0;
		orbitDist=orbitLength;

		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold((int) (orbitY+(orbitDist*this.getSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist /3.0);
		this.setX(orbitX-width_int/2);
	}
	
	public void beginOrbit(){
		this.post(moveInATriangleRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInATriangleRunnable);
	}

}
