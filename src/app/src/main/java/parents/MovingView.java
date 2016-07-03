package parents;

import interfaces.GameActivityInterface;
import interfaces.MovingViewInterface;
import android.view.View;
import android.view.ViewGroup;
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
	private float xPosition,yPosition;
	private int myHeight,myWidth;
	private RelativeLayout myLayout;
	
	boolean isRemoved;
	
	public abstract void updateViewSpeed(long deltaTime);
		
	public MovingView(float xInitialPosition,float yInitialPosition,RelativeLayout layout,float movingSpeedY,float movingSpeedX,int width,int height,int imageId) {
		super(layout.getContext());
		init(xInitialPosition,yInitialPosition,layout,movingSpeedY,movingSpeedX,width,height,imageId);
	}

	public void unRemove(float xInitialPosition,float yInitialPosition,RelativeLayout layout,float movingSpeedY,float movingSpeedX,int width,int height,int imageId){
		init(xInitialPosition,yInitialPosition,layout,movingSpeedY,movingSpeedX,width,height,imageId);
	}

	private void init(float xInitialPosition,float yInitialPosition,RelativeLayout layout,float movingSpeedY,float movingSpeedX,int width,int height,int imageId){
		myLayout = layout;

		this.setSize(width,height);
		this.setImageResource(imageId);

		//remove from old layout, add to foreground of new layout
		ViewGroup currentParent = (ViewGroup)this.getParent();
		if(currentParent!=null){ currentParent.removeView(this); }
		addToForeground(this);

		setSpeedY(movingSpeedY);
		setSpeedX(movingSpeedX);

		isRemoved=false;

		//required to prevent View from flashing in the top left corner: (0,0) coordinates
		setX(xInitialPosition);
		setY(yInitialPosition);
		super.setX(xInitialPosition);
		super.setY(yInitialPosition);
	}

	@Override
	public float getX(){
		return xPosition;
	}
	@Override
	public float getY(){
		return yPosition;
	}
	@Override
	public void setX(float newX){
		xPosition = newX;
	}
	@Override
	public void setY(float newY){
		yPosition = newY;
	}
	public void renderPosition(){
		super.setX(xPosition);
		super.setY(yPosition);
	}
	public void setSize(int width,int height){
		this.setLayoutParams( new RelativeLayout.LayoutParams(width,height) );
		myHeight = height;
		myWidth = width;
	}

	protected static float getRandomXPosInMiddle(float width){
		return (float) ((MainActivity.getWidthPixels() - 2 * width) * Math.random() + width / 2);
	}

	public static float randomXPosition(){
		return (float) (Math.random() * MainActivity.getWidthPixels());
	}
	public static float randomYPosition(){
		return  (float) (Math.random() * MainActivity.getHeightPixels() );
	}
	
	/**
	 * Move the View on the screen according to is speedY or speedX.
	 */
	public void movePhysicalPosition(long deltaTime){
		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x = this.getX();
		float y = this.getY();
		y+=this.getSpeedY() * deltaTime ;
		x+=this.getSpeedX() * deltaTime ;
		
		//check that object is still within screen bounds		
		final float bottomScreen = (int) MainActivity.getHeightPixels() - getContext().getResources().getDimension(R.dimen.control_panel_height) ;

		if( y < -2*myHeight || y > bottomScreen || x < -myWidth || x > (MainActivity.getWidthPixels() + myWidth) ){
			setViewToBeRemovedOnNextRendering();
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
	public void removeGameObject(){
		if(isRemoved){
			myLayout.removeView(this);
		}
	}

	public void setViewToBeRemovedOnNextRendering(){//to be called when collision detection results in a dead object, or when an object moves off the screen
		isRemoved = true;
		this.removeCallbacks(null);
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

	
	public float getMidX(){
		return (this.getX() * 2 + this.getWidth() )/2;
	}

	public float getMidY(){
		return (this.getY() * 2+ this.getHeight() )/2;
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
