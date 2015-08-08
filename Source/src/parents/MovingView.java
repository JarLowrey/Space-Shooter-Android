package parents;

import helpers.KillableRunnable;
import helpers.MediaController;
import interfaces.MovingViewInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.MainActivity;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public abstract class MovingView extends ImageView implements MovingViewInterface{

	public static final float
		DEFAULT_SPEED_Y = (float) 0.122, //units = frame rate independent density pixels per milliseconds
		DEFAULT_SPEED_X = (float) (DEFAULT_SPEED_Y * .75); 
		
	private double speedX,speedY;
	private RelativeLayout myLayout;
	
	boolean isRemoved;
	
	public abstract void updateViewSpeed(long millisecondsSinceLastSpeedUpdate);
		
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
	
	public boolean RectToRectCollisionDetection(View two){
		float left1,right1,top1,bottom1;
		float left2,right2,top2,bottom2;
		
		//find the values of the x,y positions of the two views
		left1=getX();
		right1=getX()+getWidth();
		top1=getY();
		bottom1=getY()+getHeight();

		left2=two.getX();
		right2=two.getX()+two.getWidth();
		top2=two.getY();
		bottom2=two.getY()+two.getHeight();
		
		//Simple collision detection - determine if the two rectangular areas intersect
		//http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
		return !((bottom1 < top2) ||(top1 > bottom2) || (left1>right2) || (right1<left2));
	}
	
	/**
	 * Move the View on the screen according to is speedY or speedX.
	 * @param direction-whichDirection the View should move. Input needs to be ProjectileView.UP, ProjectileView.RIGHT,ProjectileView.DOWN, or ProjectileView.LEFT
	 * @return Always returns false, overwrite for different behavior
	 */
	public void move(long millisecondsSinceLastSpeedUpdate){
		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x = this.getX();
		float y = this.getY();
		y+=this.getSpeedY() * millisecondsSinceLastSpeedUpdate ;
		x+=this.getSpeedX() * millisecondsSinceLastSpeedUpdate ;
		
		//check that object is still within screen bounds
		if(y < -getHeight() || y > GameActivity.getBottomScreen() || x < -this.getWidth() || x > (MainActivity.getWidthPixels() + this.getWidth()) ){
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
	/**
	 * 
	 * @param newSpeed
	 * 		units = frame rate independent density pixels per milliseconds
	 */
	public void setSpeedY(float newSpeed){
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
	
	protected void createExplosion(int width,int height,int explosionImgId,long[] vibrationPattern){
		MediaController.playSoundEffect(getContext(), MediaController.SOUND_EXPLOSION1);
		
		if(vibrationPattern!=null){
			 //vibrate the phone  
			MediaController.vibrate(getContext(), vibrationPattern);
		}

		final ImageView exp = new ImageView(getContext());
		exp.setImageResource( explosionImgId );
		exp.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
		
		exp.setX(this.getX());
		exp.setY(this.getY());

		addToForeground(exp);
		
		exp.postDelayed(new KillableRunnable(){
			@Override
			public void doWork() {
				myLayout.removeView(exp);
			}
		},500);	
	}
	
	protected void createExplosion(int width,int height,int explosionId){
		createExplosion(width,height,explosionId,null);
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
