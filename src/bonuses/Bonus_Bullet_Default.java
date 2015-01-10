package bonuses;

import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class Bonus_Bullet_Default extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_Default(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		//I gave up on giving an xml drawable a width and height		
		this.setImageResource(R.drawable.laser_default_img);
	}
	
	public void applyBenefit(Shooter theBenefitter){
//		theBenefitter.setBulletType(new Bullet_LaserShort());
	}
}
