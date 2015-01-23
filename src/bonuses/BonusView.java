package bonuses;

import interfaces.Shooter;
import levels.LevelSystem;
import parents.Moving_GravityView;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public abstract class BonusView extends Moving_GravityView {

	public final static int DEFAULT_SPEED_Y=4;
	
	public BonusView(Context context,float positionX,float positionY) {
		super(context,DEFAULT_SPEED_Y,0,(int) context.getResources().getDimension(R.dimen.bonus_background_len),
				(int)context.getResources().getDimension(R.dimen.bonus_background_len),0);	//children classes will set image resource
		
		//set background and position
		this.setBackgroundResource(R.drawable.white_center_red_outline);
		this.setX(positionX);
		this.setY(positionY);
		
		//add to collision detector
		LevelSystem.bonuses.add(this);
	}
	public abstract void applyBenefit(Shooter theBenefitter);
	
	@Override
	public void removeGameObject(){		
		LevelSystem.bonuses.remove(this);
		super.removeGameObject();
	}
	
	public static void displayRandomBonusView(Context context,float positionX,float positionY){
		double rand = Math.random();
		
		if(rand<.5){
			new Bonus_ScoreView(context,positionX,positionY);
		}else{
			new Bonus_HealView(context,positionX,positionY);
		}
	}
}
