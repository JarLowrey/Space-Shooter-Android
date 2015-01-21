package friendlies;

import parents.Moving_ProjectileView;
import levels.LevelSystem;
import android.content.Context;

public class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(Context context,double projectileSpeedY
			,double projectileSpeedX, 
			double projectileDamage,double projectileHealth,int width,int height,int imageId) {
		super( context, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth, width, height, imageId);
		
		LevelSystem.friendlies.add(this);
	}
	

	@Override
	public void removeGameObject() {
		LevelSystem.friendlies.remove(this);
		deaultCleanupOnRemoval(true);//needs to be called last for all pending callbacks to 'this' to be removed
	}

}
