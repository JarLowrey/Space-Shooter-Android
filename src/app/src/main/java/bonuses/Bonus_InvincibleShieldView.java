package bonuses;
 
import interfaces.MovingViewInterface;
import android.widget.RelativeLayout;

public class Bonus_InvincibleShieldView extends BonusView implements MovingViewInterface{
	
	public Bonus_InvincibleShieldView(RelativeLayout layout,float positionX,float positionY) {
		super(layout,positionX,positionY);	

//		this.setImageResource(R.drawable.XXXXXXXXX);
	}
	
	public void applyBenefit(){
		//TODO add a shield image around the protagonist
		//TODO neglect the next X shots of damage that hit the player
	}
}
