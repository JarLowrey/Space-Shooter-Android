package enemies_orbiters;

import interfaces.GameObjectInterface;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;


public class Orbiter_HorizontalLine extends Shooting_OrbiterView implements GameObjectInterface {

	public final static int DEFAULT_ORBIT_LOCATION=(int) (MainActivity.getHeightPixels()/4),
			DEFAULT_ANGLE = 30,
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_orbiter_horizontal_line;

	public final static int DEFAULT_SCORE=100,
			DEFAULT_BULLET_FREQ_INTERVAL=1000;
	public final static double DEFAULT_SPEED_Y=5,
			DEFAULT_SPEED_X=5,
			DEFAULT_COLLISION_DAMAGE=20, 
			DEFAULT_HEALTH=300,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.08;
	public final static double DEFAULT_BULLET_SPEED_Y=15,
			DEFAULT_BULLET_DAMAGE=30;
	
	private Runnable moveInALineRunnable = new Runnable(){
		@Override
		public void run() {
    		//ensure view is not removed before running
			if( ! Orbiter_HorizontalLine.this.isRemoved()){
				Orbiter_HorizontalLine.this.moveDirection(SIDEWAYS);
				
				
				final float leftPos = Orbiter_HorizontalLine.this.getX();
				final float rightPos = Orbiter_HorizontalLine.this.getX()+Orbiter_HorizontalLine.this.getHeight();
				if(leftPos<0 || rightPos > MainActivity.getWidthPixels()){
					Orbiter_HorizontalLine.this.setSpeedX(Orbiter_HorizontalLine.this.getSpeedX() * -1);//reverse side direction
				}
					
				Orbiter_HorizontalLine.this.postDelayed(this,
							Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		}
	};
	
	public Orbiter_HorizontalLine(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath) {
		super(context, score,speedY, speedX,
				collisionDamage, health,0,0,//these are 0 as they do not apply for a horizontal line orbiter
				 probSpawnBeneficialObjecyUponDeath);

		//set image background, width, and height
		final int height_int=(int)context.getResources().getDimension(R.dimen.orbit_horizontal_height);
		int width_int = (int)context.getResources().getDimension(R.dimen.orbit_horizontal_width);
		this.setLayoutParams(new RelativeLayout.LayoutParams(width_int,height_int));
		 
		this.setImageResource(DEFAULT_BACKGROUND);
		//default to begin orbit at top of triangle, 1/3 of way through (thus top = moving left. it is not a perfect orbit, but good enough)
		this.setThreshold(DEFAULT_ORBIT_LOCATION);
		this.setX(MainActivity.getWidthPixels()/2);
	}
	
	public void beginOrbit(){
		this.post(moveInALineRunnable);	
	}
	public void endOrbit(){
		this.removeCallbacks(moveInALineRunnable);
	}

}
