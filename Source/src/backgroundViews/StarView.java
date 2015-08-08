package backgroundViews;

import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class StarView extends MovingView{
	
	public static final int DEFAULT_BACKGROUND_ID = R.drawable.star;
	public static final float DEFAULT_SPEED_Y = (float) (MovingView.DEFAULT_SPEED_Y / 3);//Density Pixels per millisecond
	
	private int numTimesStarCanMove, numTimesMoved = 0;
	
//	private float alpha;//computationally expensive
	
	public StarView(RelativeLayout layout) {
		super(layout, 
				(float) (Math.random() * DEFAULT_SPEED_Y) + DEFAULT_SPEED_Y, 
				0, 
				(int)layout.getContext().getResources().getDimension(R.dimen.star_len),
				(int)layout.getContext().getResources().getDimension(R.dimen.star_len), 
				DEFAULT_BACKGROUND_ID);
		
//		alpha = (float) Math.random();
//		setAlpha(alpha);
		
		setRandomLocation();

		addToBackground(this);
		
		numTimesStarCanMove = (int) (Math.random() * 25) + 30;
	}
	
	/**
	 * Stars cannot go off the sides of the screen or beyond the control panel, else they will automatically be removed.
	 * Need to find a random, safe location
	 */
	private void setRandomLocation(){ 
		setX((float) (Math.random() * MainActivity.getWidthPixels()));
		/*
		 * GameActivity.offScreenBottom is not set when stars are created. Thus ignore the fact
		 * that stars cannot be seen behind the game's control panel and just let them move randomly on the entire screen.
		 */
		setY( (float) (Math.random() * MainActivity.getHeightPixels() ) );
	}

	@Override
	public void removeGameObject() {
		this.defaultCleanupOnRemoval();
	}
	
	@Override
	public void move(long millisecondsSinceLastSpeedUpdate){//do not remove if it passes the bounds of the screen (super.move() does this)

//		alpha *= .95;
//		setAlpha(alpha);
		
		numTimesMoved++;
		if(numTimesMoved > numTimesStarCanMove){
			setRandomLocation();
			numTimesMoved=0;
		}
		
		float x = this.getX();
		float y = this.getY();
		
		y+=this.getSpeedY() * millisecondsSinceLastSpeedUpdate;//speed in dp/millisec
		x+=this.getSpeedX() * millisecondsSinceLastSpeedUpdate;
		
		this.setY(y); 
		this.setX(x);
	}

	@Override
	public void restartThreads() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateViewSpeed(long millisecondsSinceLastSpeedUpdate) {
		//do nothing - constant speed until random location
	}

}
