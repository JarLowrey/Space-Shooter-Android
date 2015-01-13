package friendlies;

import levels.LevelSystem;
import abstract_parents.Moving_ProjectileView;
import android.content.Context;

public class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(Context context,double projectileSpeedY
			,double projectileSpeedX, 
			double projectileDamage,double projectileHealth) {
		super( context, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		LevelSystem.friendlies.add(this);
	}
	

	@Override
	public void removeGameObject() {
		deaultCleanupOnRemoval();//needs to be called last for all pending callbacks to 'this' to be removed
	}
}
