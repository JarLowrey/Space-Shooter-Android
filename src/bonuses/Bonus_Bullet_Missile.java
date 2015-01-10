package bonuses;

import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_Bullet_Missile extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_Missile(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.missile_bonus);
	}
	
	public void applyBenefit(Shooter theBenefitter){
//		theBenefitter.setBulletType( new Bullet_Missile() );
	}
}
