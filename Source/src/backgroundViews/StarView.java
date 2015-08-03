package backgroundViews;

import helpers.KillableRunnable;
import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class StarView extends MovingView{
	
	public static final int DEFAULT_BACKGROUND_ID = R.drawable.star;
	public static final float DEFAULT_SPEED_Y = 5;
	
	private int numTimesStarCanMove;
	
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
		final int numTimesStarCanMoveCopy = numTimesStarCanMove;
		reassignMoveRunnable(new KillableRunnable(){
			private int numTimesMoved = 0;
			@Override
			public void doWork() {
				move();

//				alpha *= .95;
//				setAlpha(alpha);
				
				numTimesMoved++;
				if(numTimesMoved > numTimesStarCanMoveCopy){
					setRandomLocation();
					numTimesMoved=0;
				}
				StarView.this.postDelayed(this, HOW_OFTEN_TO_MOVE);
			}			
		}); 
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
	public void move(){//do not remove if it passes the bounds of the screen (super.move() does this)
		float x = this.getX();
		float y = this.getY();
		
		y+=this.getSpeedY();
		x+=this.getSpeedX();
		
		this.setY(y);
		this.setX(x);
	}

}
