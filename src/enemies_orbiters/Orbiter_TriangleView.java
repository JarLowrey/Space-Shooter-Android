package enemies_orbiters;

import interfaces.MovingViewInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;


public class Orbiter_TriangleView extends Shooting_OrbiterView implements MovingViewInterface {

	public final static int DEFAULT_ORBIT_LENGTH=25,
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_triangle;
	
	private int currentSideOfTriangle, orbitDist;
	
	public Orbiter_TriangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength,int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath, width, height, imageId);

		currentSideOfTriangle=0; 
		orbitDist=orbitLength;

		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold((int) (orbitY-(orbitDist*this.getMagnitudeOfSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist * (2/3.0));
		

		orbitingRunnable = new Runnable(){
			@Override
			public void run() {
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
						
						ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,Orbiter_TriangleView.this);
			}
		};
		
	}
	

	public Orbiter_TriangleView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int orbitLength, int orbitPixelX, int orbitPixelY,int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health, probSpawnBeneficialObjecyUponDeath, orbitPixelX, orbitPixelY, width, height, imageId);
		
		currentSideOfTriangle=0;
		orbitDist=orbitLength;

		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold((int) (orbitY+(orbitDist*this.getMagnitudeOfSpeedY() ) / 2 ));
		howManyTimesMoved=(int) (orbitDist /3.0);
	}
}
