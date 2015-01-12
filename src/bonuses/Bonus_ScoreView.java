package bonuses;

import levels.LevelSystem;
import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_ScoreView extends BonusView implements GameObjectInterface{
	
	public Bonus_ScoreView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.resources);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		LevelSystem.incrementScore(500);
	}
}
