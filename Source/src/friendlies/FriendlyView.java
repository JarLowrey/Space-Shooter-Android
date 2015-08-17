package friendlies;

import helpers.MediaController;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;

import enemies_non_shooters.Gravity_MeteorView;

public abstract class FriendlyView extends Moving_ProjectileView{

	public final static float DEFAULT_SPEED_Y=Gravity_MeteorView.DEFAULT_SPEED_Y * 10,//Density Pixels per millisecond
			DEFAULT_SPEED_X=DEFAULT_SPEED_Y;
	
	public FriendlyView(RelativeLayout layout,float projectileSpeedY
			,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super( layout, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth, width, height, imageId);
		
		GameLoop.friendlies.add(this);
	}
	

	@Override
	public void removeGameObject() {
		GameLoop.friendlies.remove(this);
		defaultCleanupOnRemoval();//needs to be called last for all pending callbacks to 'this' to be removed
	}
 
	@Override
	public boolean takeDamage(int howMuchDamage){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_FRIENDLY_HIT);
		return super.takeDamage(howMuchDamage);
	}
}
