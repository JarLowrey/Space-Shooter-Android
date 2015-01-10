package bonuses;

import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_Gun_IncBulletDamageView extends BonusView implements GameObjectInterface{
	
	public static double DAMAGE_INCREASE_RATIO=1.1;
	
	public Bonus_Gun_IncBulletDamageView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.bullet_damage);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletDamage(theBenefitter.getBulletDamage() * DAMAGE_INCREASE_RATIO );
	}
}
