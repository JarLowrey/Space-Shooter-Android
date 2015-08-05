package friendlies;

import helpers.MediaController;
import levels.LevelSystem;
import parents.Moving_ProjectileView;
import android.widget.RelativeLayout;

public class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(RelativeLayout layout,float projectileSpeedY
			,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super( layout, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth, width, height, imageId);
		
		LevelSystem.friendlies.add(this);
	}
	

	@Override
	public void removeGameObject() {
		LevelSystem.friendlies.remove(this);
		defaultCleanupOnRemoval();//needs to be called last for all pending callbacks to 'this' to be removed
	}
 
	@Override
	public boolean takeDamage(int howMuchDamage){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_FRIENDLY_HIT);
		return super.takeDamage(howMuchDamage);
	}
}
