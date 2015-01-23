package parents;

import interfaces.Gravity;
import support.ConditionalHandler;
import android.content.Context;

/**
 * A ProjectileView with a constant downwards force. This force is removed when the instance reaches its lowest threshold. 
 * The downward force may be different from the upward speed.
 * 
 * @author JAMES LOWREY
 *
 */
public class Moving_GravityView extends MovingView implements Gravity{
	
	private int gravityThreshold;
	private boolean atThreshold;
	
	public Moving_GravityView(Context context,float movingSpeedY,float movingSpeedX,int width,int height,int imageId){
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);

		atThreshold=false;
		gravityThreshold=Gravity.NO_THRESHOLD;
		ConditionalHandler.postIfAlive(gravityRunnable,this);
	}

    //GRAVITY RUNNABLE
    Runnable gravityRunnable = new Runnable(){
    	@Override
        public void run() {
        		atThreshold=moveDirection(Moving_ProjectileView.DOWN);
        		
        		//if View is at lowest threshold stop reposting runnable
        		if(!atThreshold){
        			ConditionalHandler.postIfAlive(this, HOW_OFTEN_TO_MOVE/2,Moving_GravityView.this);
        		}
    	}
    };
    
    public boolean moveDirection(int direction){
    	boolean offScreen =  super.moveDirection(direction);
    	boolean atThreshold=false;
    	if(direction==DOWN){
    		float y=this.getY();
    		atThreshold = gravityThreshold!=NO_THRESHOLD && (y+getHeight())>gravityThreshold;
    	}
    	return offScreen || atThreshold;
    }
    
    
    
	public void stopGravity(){
		this.removeCallbacks(gravityRunnable);		
	}
	public void startGravity(){
		ConditionalHandler.postIfAlive(gravityRunnable,this);		
	}
	
	@Override
	public void restartThreads(){
		if( ! atThreshold){
			startGravity();
		}
	}

	@Override
	public void setThreshold(int newLowestPositionThreshold) {
		this.gravityThreshold = newLowestPositionThreshold;		
	}

	@Override
	public int getThreshold() {
		return gravityThreshold;
	}

	@Override
	public void removeGameObject() {
		this.deaultCleanupOnRemoval();//needs to be the last thing called for handler to remove all callbacks
	}
}
