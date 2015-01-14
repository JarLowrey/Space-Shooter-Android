package bonuses;

import parents.Moving_GravityView;
import interfaces.Shooter;
import levels.LevelSystem;
import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.R;

public abstract class BonusView extends Moving_GravityView {

	public final static int DEFAULT_SPEED_Y=4;
	Context ctx;
	
	
	public BonusView(Context context,float positionX,float positionY) {
		super(context,DEFAULT_SPEED_Y,0);	
		

		ctx = context;
		
		//set width and height, x and y
		final int len=(int)context.getResources().getDimension(R.dimen.bonus_background_len);
		this.setLayoutParams(new RelativeLayout.LayoutParams(len,len));
		this.setX(positionX);
		this.setY(positionY);
		
		//set background
		this.setBackgroundResource(R.drawable.white_center_red_outline);

		//add to collision detector
		LevelSystem.bonuses.add(this);
	}
	public abstract void applyBenefit(Shooter theBenefitter);
	
	@Override
	public void removeGameObject(){		
		super.removeGameObject();
		LevelSystem.bonuses.remove(this);
	}
	
	public static BonusView getRandomBonusView(Context context,float positionX,float positionY){
		BonusView bonus = new Bonus_HealView(context,positionX,positionY);
		double rand = Math.random();
		
		if(rand<.5){
			bonus = new Bonus_ScoreView(context,positionX,positionY);
		}
		
		return bonus;
	}
}
