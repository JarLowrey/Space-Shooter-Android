package backgroundViews;

import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies_non_shooters.Gravity_MeteorView;
import parents.MovingView;

public class StarView extends SpecialEffectView{
	
	public static final int DEFAULT_BACKGROUND_ID = R.drawable.star;
	public static final float DEFAULT_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y / 5);//Density Pixels per millisecond
	
	private long timeSinceLastRandomReset,
			starLifespan;
	
	public StarView(RelativeLayout layout) {
		super(MovingView.randomXPosition(),MovingView.randomYPosition(),
				layout,
				(float) (Math.random() * DEFAULT_SPEED_Y) + DEFAULT_SPEED_Y,
				0,
				(int) layout.getContext().getResources().getDimension(R.dimen.star_len),
				(int) layout.getContext().getResources().getDimension(R.dimen.star_len),
				DEFAULT_BACKGROUND_ID);

		initStar();
	}

	private void initStar(){
		addToBackground(this);

		starLifespan = (int) (Math.random() * 4000) + 3000;
	}
	public void unRemoveStarView(RelativeLayout layout){
		super.unRemoveSpecialEffectView(MovingView.randomXPosition(),MovingView.randomYPosition(),
				layout,
				(float) (Math.random() * DEFAULT_SPEED_Y) + DEFAULT_SPEED_Y,
				0,
				(int) layout.getContext().getResources().getDimension(R.dimen.star_len),
				(int) layout.getContext().getResources().getDimension(R.dimen.star_len),
				DEFAULT_BACKGROUND_ID);

		initStar();
	}

	@Override
	public void movePhysicalPosition(long deltaTime){//do not remove if it passes the bounds of the screen (super.movePhysicalPosition() does this)
		timeSinceLastRandomReset +=deltaTime;
		if(timeSinceLastRandomReset >= starLifespan){
			setX(MovingView.randomXPosition());
			setY(MovingView.randomYPosition());
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
		//do nothing - constant speed
	}

}
