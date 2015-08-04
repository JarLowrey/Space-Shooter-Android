package bullets;

import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.Shooter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Bullet_HasDurationView extends BulletView
{
	public static final float DEFAULT_SPEED_Y = 20;
	
	private static final int HOW_MANY_TIMES_FASTER_THIS_MOVES_THAN_DEFAULT = 8;
	private static final long INTERVAL = (long)HOW_OFTEN_TO_MOVE / HOW_MANY_TIMES_FASTER_THIS_MOVES_THAN_DEFAULT;
 
	private long myLifeSpanInMilliseconds;
	private KillableRunnable maintainBullet;
	
	
	public Bullet_HasDurationView(RelativeLayout layout, Shooter shooter,
			float bulletSpeedY, int bulletDamage, int width, int height,
			int imageId, long lifeSpanInMilliseconds) {
		super(layout, shooter, bulletSpeedY, bulletDamage, width, height, imageId);
		
		myLifeSpanInMilliseconds = lifeSpanInMilliseconds;
		
		this.reassignMoveRunnable(null);//bullet does not move, instead it grows
		
		maintainBullet = new KillableRunnable(){
			private long currentLife=0;
			
			@Override
			public void doWork() {
				currentLife += INTERVAL;
				
				//grow bullet
				
				//set alpha if close to dying
				if(currentLife > myLifeSpanInMilliseconds * .9){
					Bullet_HasDurationView.this.setAlpha(
							(float)(Bullet_HasDurationView.this.getAlpha() * .5) );
				}
				
				//position bullet on its shooter as the shooter moves
				final float shooterMidX = ( theOneWhoShotMe.getX() * 2 + theOneWhoShotMe.getWidth() ) / 2;
				final float shooterMidY = ( theOneWhoShotMe.getY() * 2 + theOneWhoShotMe.getHeight() ) / 2;
				Bullet_HasDurationView.this.setX(shooterMidX);
				Bullet_HasDurationView.this.setY(shooterMidY);
				

				RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
				final float height = Bullet_HasDurationView.this.getHeight();
//				final float yPos = Bullet_HasDurationView.this.getY();
				//The commented out portion is no longer needed, as the height can extend the bounds of the screen
//				if( (yPos + height) >= MainActivity.getHeightPixels() ){ 
//					//end of bullet is approximately at end of screen. Shrink the bullet
//					params.height = (int) (height - getSpeedY() );
//					hasReachedBottomOfScreen = true;
//				}else if (!hasReachedBottomOfScreen){  
					//end of bullet is still growing. Increase its size.
					params.height = (int) (height + getSpeedY());
//				}
				Bullet_HasDurationView.this.setLayoutParams(getLayoutParams());
				
				//remove bullet if its life has run out, continue otherwise
				if(currentLife < myLifeSpanInMilliseconds){
					ConditionalHandler.postIfAlive(this, INTERVAL,theOneWhoShotMe);
				}else{
					Bullet_HasDurationView.this.removeGameObject();
				}
			}
		};
		
		this.post(maintainBullet);
	}
	
	@Override 
	public void removeGameObject(){
		maintainBullet.kill();
				
		super.removeGameObject();
	}

}
