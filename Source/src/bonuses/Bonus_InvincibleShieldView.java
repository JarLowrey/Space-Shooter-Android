package bonuses;
 
import interfaces.MovingViewInterface;
import interfaces.Shooter;
import android.content.Context;

public class Bonus_InvincibleShieldView extends BonusView implements MovingViewInterface{
	
	public Bonus_InvincibleShieldView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

//		this.setImageResource(R.drawable.XXXXXXXXX);
	}
	
	public void applyBenefit(){
		//TODO add a shield image around the protagonist
		//TODO neglect the next X shots of damage that hit the player
	}
}
