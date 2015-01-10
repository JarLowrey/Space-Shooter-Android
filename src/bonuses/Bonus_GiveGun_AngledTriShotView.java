package bonuses;

import guns.Gun;
import guns.Gun_AngledTriShot;
import interfaces.GameObjectInterface;
import interfaces.Shooter;
import android.content.Context;
import bullets.Bullet_LaserShort;

import com.jtronlabs.to_the_moon.R;

public class Bonus_GiveGun_AngledTriShotView extends BonusView implements GameObjectInterface{
	
	public Bonus_GiveGun_AngledTriShotView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		this.setImageResource(R.drawable.tri_bullet);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		Gun gun = new Gun_AngledTriShot(ctx, theBenefitter, new Bullet_LaserShort());
		theBenefitter.giveNewGun(gun);
	}
}
