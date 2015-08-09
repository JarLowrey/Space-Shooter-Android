package bullets;

import helpers.MediaController;
import interfaces.Shooter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Bullet_HasDurationView extends BulletView
{	 
	private long myLifeSpanInMilliseconds, currentLife;
	private int soundEffectStreamId;
	
	
	public Bullet_HasDurationView(RelativeLayout layout, Shooter shooter,
			float bulletSpeedY, int bulletDamage, int width, int height,
			int imageId, long lifeSpanInMilliseconds) {
		super(layout, shooter, bulletSpeedY, bulletDamage, width, height, imageId);
		
		myLifeSpanInMilliseconds = lifeSpanInMilliseconds;
		
		soundEffectStreamId = MediaController.playLoopingSoundEffect(getContext(), MediaController.SOUND_LASER_LOOPING);
		
		this.setSpeedX(0);
		this.setSpeedY(0);
	}
	
	@Override 
	public void removeGameObject(){		
		MediaController.stopLoopingSoundEffect(getContext(), soundEffectStreamId);
				
		super.removeGameObject();
	}

	@Override
	public void move(long deltaTime){
		currentLife += deltaTime;
		
		//grow bullet
		
		//computationally expensive and hard to see, not worth it
//		//set alpha if close to dying
//		if(currentLife > myLifeSpanInMilliseconds * .9){
//			Bullet_HasDurationView.this.setAlpha(
//					(float)(Bullet_HasDurationView.this.getAlpha() * .5) );
//		}
		
		//position bullet on its shooter as the shooter moves
		final float shooterMidY = ( theOneWhoShotMe.getY() * 2 + theOneWhoShotMe.getHeight() ) / 2;
		Bullet_HasDurationView.this.setY(shooterMidY);
		

		RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
		final float height = Bullet_HasDurationView.this.getHeight();
		
		params.height = (int) (height + getSpeedY() * deltaTime);//speed in dp/millisec

		Bullet_HasDurationView.this.setLayoutParams(getLayoutParams());
		
		//remove bullet if its life has run out
		if(currentLife >= myLifeSpanInMilliseconds){
			Bullet_HasDurationView.this.removeGameObject();
		}
	}
}
