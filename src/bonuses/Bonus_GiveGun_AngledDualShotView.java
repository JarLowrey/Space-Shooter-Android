package bonuses;

import guns.Gun;
import guns.Gun_AngledDualShot;
import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet_LaserShort;

import com.jtronlabs.to_the_moon.R;

public class Bonus_GiveGun_AngledDualShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_GiveGun_AngledDualShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	

		this.setImageResource(R.drawable.dual_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		Gun gun = new Gun_AngledDualShot(ctx, theBenefitter, new Bullet_LaserShort());
		theBenefitter.giveNewGun(gun);
	}
}
