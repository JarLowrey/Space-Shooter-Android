package backgroundViews;

import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies_non_shooters.Gravity_MeteorView;

public class StarView extends SpecialEffectView{
	
	public static final int DEFAULT_BACKGROUND_ID = R.drawable.star;
	public static final float DEFAULT_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y / 3);//Density Pixels per millisecond
	
	private long timeSinceLastRandomReset,
			starLifespan;
	
	public StarView(RelativeLayout layout) {
		super(layout, 
				(float) (Math.random() * DEFAULT_SPEED_Y) + DEFAULT_SPEED_Y, 
				0, 
				(int)layout.getContext().getResources().getDimension(R.dimen.star_len),
				(int)layout.getContext().getResources().getDimension(R.dimen.star_len), 
				DEFAULT_BACKGROUND_ID);
		
		setRandomLocation();

		addToBackground(this);

		starLifespan = (int) (Math.random() * 4000) + 3000;
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
	public void move(long deltaTime){//do not remove if it passes the bounds of the screen (super.move() does this)
		timeSinceLastRandomReset +=deltaTime;
		if(timeSinceLastRandomReset >= starLifespan){
			setRandomLocation();
			timeSinceLastRandomReset=0;
		}
		
		float x = this.getX();
		float y = this.getY();
		
		y+=this.getSpeedY() * deltaTime;//speed in dp/millisec
		x+=this.getSpeedX() * deltaTime;
		
		this.setY(y); 
		this.setX(x);
	}

	@Override
	public void restartThreads() {
		// do nothing
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		//do nothing - constant speed until random location
	}

}
