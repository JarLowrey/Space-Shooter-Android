package parents;

import interfaces.Gravity;
import android.content.Context;

/**
 * A ProjectileView with a constant downwards force. This force is removed when the instance reaches its lowest threshold. 
 * The downward force may be different from the upward speed.
 * 
 * @author JAMES LOWREY
 *
 */
public class Moving_GravityView extends MovingView implements Gravity{

	
	public Moving_GravityView(Context context,float movingSpeedY,float movingSpeedX,int width,int height,int imageId){
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);
	}
    
	@Override
	public void restartThreads(){
		super.restartThreads();
	}

	@Override
	public void setThreshold(int newLowestPositionThreshold) {
		//nothing
	}

	@Override
	public int getThreshold() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void removeGameObject() {
		this.deaultCleanupOnRemoval();//needs to be the last thing called for handler to remove all callbacks
	}

	@Override
	public boolean hasReachedGravityThreshold() {
		return false;
	}
}
