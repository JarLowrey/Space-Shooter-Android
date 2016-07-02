package bonuses;

import parents.MovingView;
import android.widget.RelativeLayout;

import com.jtronlabs.space_shooter.GameLoop;
import com.jtronlabs.space_shooter.R;

import enemies_non_shooters.Gravity_MeteorView;

public abstract class BonusView extends MovingView {

	public static final float
		DEFAULT_SPEED_Y = (float) (Gravity_MeteorView.DEFAULT_SPEED_Y * .9);
			
	public BonusView(RelativeLayout layout,float positionX,float positionY) {
		super(positionX,positionY,layout,
				DEFAULT_SPEED_Y,
				0,
				(int) layout.getContext().getResources().getDimension(R.dimen.bonus_background_len),
				(int)layout.getContext().getResources().getDimension(R.dimen.bonus_background_len)
				,0);	//children classes will set image resource
		
		//set background and position
		this.setBackgroundResource(R.drawable.white_center_red_outline);
		
		GameLoop.bonuses.add(this);
	}
	
	/**
	 * Bonus has been picked up by protagonist or his ally. Apply benefit to protagonist.
	 */
	public abstract void applyBenefit();

	public static void displayRandomBonusView(RelativeLayout layout,float positionX,float positionY){
		double rand = Math.random();
		
		if(rand<.5){
			new Bonus_ScoreView(layout,positionX,positionY);
		}else{
			new Bonus_HealView(layout,positionX,positionY);
		}
	}


	@Override
	public void restartThreads() {
		//do nothing
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		// do nothing - constant speed

	}
}
