package friendlies;

import helpers.MediaController;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;

public abstract class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(float xInitialPosition,float yInitialPosition,RelativeLayout layout,float projectileSpeedY
			,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super( xInitialPosition, yInitialPosition,layout, projectileSpeedY,projectileSpeedX,
				 projectileDamage, projectileHealth, width, height, imageId);
		
		GameLoop.friendlies.add(this);
	}

	@Override
	public boolean takeDamage(int howMuchDamage){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_FRIENDLY_HIT);
		return super.takeDamage(howMuchDamage);
	}
}
