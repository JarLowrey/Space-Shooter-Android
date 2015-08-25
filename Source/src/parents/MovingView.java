package parents;

import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class MovingView extends ImageView implements MovingViewInterface{
		
	private double speedX,speedY;
	private RelativeLayout myLayout;
	
	boolean isRemoved;
	
	public abstract void updateViewSpeed(long deltaTime);
		
	public MovingView(RelativeLayout layout,float movingSpeedY,float movingSpeedX,int width,int height,int imageId) {
		super(layout.getContext());

		myLayout = layout;
		
		this.setLayoutParams( new RelativeLayout.LayoutParams(width,height) );
		this.setImageResource(imageId);

		addToForeground(this);

		setSpeedY( movingSpeedY );
		setSpeedX( movingSpeedX );
		
//		Log.d("lowrey","on creation, speedY = "+speedY+" speedX = "+speedX);
		
		isRemoved=false;
	}
	
	
	/**
	 * Move the View on the screen according to is speedY or speedX.
	 * @param direction-whichDirection the View should move. Input needs to be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT
	 * @return Always returns false, overwrite for different behavior
	 */
	public void move(long deltaTime){
		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x = this.getX();
		float y = this.getY();
		y+=this.getSpeedY() * deltaTime ;
		x+=this.getSpeedX() * deltaTime ;
		
		//check that object is still within screen bounds		
		final float bottomScreen = (int) MainActivity.getHeightPixels() - getContext().getResources().getDimension(R.dimen.control_panel_height) ;

		if(y < -getHeight() || y > bottomScreen || x < -this.getWidth() || x > (MainActivity.getWidthPixels() + this.getWidth()) ){
			this.removeGameObject();
		}else{
			this.setY(y);
			this.setX(x);
		}
	}
	
	/**
	 * 
	 * @param newSpeed
	 * 		units = frame rate independent density pixels per milliseconds
	 */
	public void setSpeedX(double newSpeed){
		speedX = ( newSpeed*MainActivity.getScreenDens() ) / GameLoop.instance().targetFrameRate();
	}
	/**
	 * 
	 * @param newSpeed
	 * 		units = frame rate independent density pixels per milliseconds
	 */
	public double getSpeedX(){
		return ( speedX * GameLoop.instance().targetFrameRate()) / MainActivity.getScreenDens();
	}
	
	public double getFrameAndScreenDensDependentSpeedX(){
		return speedX;
	}
	public double getFrameAndScreenDensDependentSpeedY(){
		return speedY;
	}
	/**
	 * 
	 * @param newSpeed
	 * 		units = frame rate independent density pixels per milliseconds
	 */
	public void setSpeedY(double newSpeed){
		speedY = ( newSpeed*MainActivity.getScreenDens() ) / GameLoop.instance().targetFrameRate();
	}
	/**
	 * 
	 * @return 
	 * 		units = frame rate independent density pixels per milliseconds
	 */
	public double getSpeedY(){
		return ( GameLoop.instance().targetFrameRate() * speedY) / MainActivity.getScreenDens();
	}
	public boolean isRemoved(){
		return isRemoved;
	}
	public abstract void removeGameObject();
	 
	/**
	 * To be called on every implementation of removeGameObject();
	 */
	protected void defaultCleanupOnRemoval(){
		isRemoved=true;
		this.removeCallbacks(null);	
		myLayout.removeView(this);
	}
	
	protected void addToForeground(View view){
		myLayout.addView(view,myLayout.getChildCount()-2);
	}

	protected void addToBackground(View view){
		//addToForeground is called in every instantiation of every MovingView. Thus addToBackground is non default,
		//and thus the view needs to be removed from its parent before it can be re-added
		myLayout.removeView(view);
		myLayout.addView(view,0);
	}
	
	protected RelativeLayout getMyLayout(){
		return myLayout;
	}
	
	protected void setRandomXPos(){
		RelativeLayout.LayoutParams p = (LayoutParams) getLayoutParams();
		this.setX( (float) ((MainActivity.getWidthPixels()-2 * p.width) *Math.random() + p.width/2) );
		p = null;
	}
	
	public float getMidX(){
		return (this.getX() * 2 + this.getWidth() )/2;
	}

	public float getMidY(){
		return (this.getY() * 2 + this.getHeight() )/2;
	}
	
	@Override
	public boolean post(Runnable r){
		if( ! isRemoved){
			return super.post(r);
		}
		return true;
	}
	@Override
	public boolean postDelayed(Runnable r,long delay){
		if( ! isRemoved){
			return super.postDelayed(r,delay);
		}
		return true;
	}
}
