package enemies_orbiters;

import interfaces.MovingViewInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;


public class Orbiter_TriangleView extends Shooting_OrbiterView implements MovingViewInterface {

	public final static int DEFAULT_ORBIT_LENGTH=25,
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_triangle;
	
	private int currentSideOfTriangle, orbitDist;
	
	public Orbiter_TriangleView(Context context) {
		super(context, DEFAULT_SCORE, 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_triangular_width), 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_triangular_height), 
				DEFAULT_BACKGROUND);

		orbitDist=DEFAULT_ORBIT_LENGTH;

		init();
	}
	

	public Orbiter_TriangleView(Context context,int score,float speedY, float speedX,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitLength, int orbitPixelX, int orbitPixelY,int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health, probSpawnBeneficialObjecyUponDeath, 
				orbitPixelX, orbitPixelY, width, height, imageId);
		
		orbitDist=orbitLength;

		init();
	}
	
	private void init(){

		currentSideOfTriangle=0;
		
		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold((int) (orbitY-(orbitDist*Math.abs(this.getSpeedY()) ) / 2 ));
		howManyTimesMoved=(int) (orbitDist * (2/3.0));
		

		orbitingRunnable = new KillableRunnable(){
			@Override
			public void doWork() {
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
}
