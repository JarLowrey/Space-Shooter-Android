package enemies_orbiters;

import interfaces.MovingViewInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;


public class Orbiter_HorizontalLineView extends Shooting_OrbiterView implements MovingViewInterface {

	public final static int DEFAULT_ORBIT_Y=(int) (MainActivity.getHeightPixels()/4),
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_horizontal_line;

	public final static int DEFAULT_SCORE=100,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=300,
			DEFAULT_BULLET_FREQ_INTERVAL=1000;
	public final static float DEFAULT_SPEED_Y=5,
			DEFAULT_SPEED_X=5,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .08;

	public Orbiter_HorizontalLineView(Context context) {
		super(context, DEFAULT_SCORE, 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_horizontal_width), 
				(int)context.getResources().getDimension(R.dimen.ship_orbit_horizontal_height), 
				DEFAULT_BACKGROUND);
		init(DEFAULT_ORBIT_Y);
	}
	
	public Orbiter_HorizontalLineView(Context context,int score,float speedY, float speedX,int collisionDamage, 
			int health, float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelY,int width,int height,int imageId) {
		super(context, score,speedY, speedX,
				collisionDamage, health,
				 probSpawnBeneficialObjecyUponDeath,(int)MainActivity.getWidthPixels()/2,
				 orbitPixelY, width, height, imageId);
		
		init(orbitPixelY);
	}
	
	private void init(int orbitY){

		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold(orbitY);
		this.setX(MainActivity.getWidthPixels()/2);
		
		orbitingRunnable = new Runnable(){
			@Override
			public void run() {
					Orbiter_HorizontalLineView.this.moveDirection(SIDEWAYS);
					
					
					final float leftPos = Orbiter_HorizontalLineView.this.getX();
					final float rightPos = Orbiter_HorizontalLineView.this.getX()+Orbiter_HorizontalLineView.this.getHeight();
					if(leftPos<0 || rightPos > MainActivity.getWidthPixels()){
						Orbiter_HorizontalLineView.this.setSpeedX(Orbiter_HorizontalLineView.this.getSpeedX() * -1);//reverse side direction
					}
						
					ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE, Orbiter_HorizontalLineView.this);
			}
		};
	}
}
