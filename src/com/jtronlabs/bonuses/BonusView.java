package com.jtronlabs.bonuses;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.Projectile_GravityView;

public abstract class BonusView extends Projectile_GravityView implements GameObjectInterface{

	public final static int SET_TRI_SHOT=0,SET_DUAL_SHOT=1,HEAL=2,UPGRADE_GUN=3;
	public final static float DEFAULT_SPEED_Y=3;
	
	public BonusView(Context context,float positionX,float positionY) {
		super(context,0,DEFAULT_SPEED_Y,DEFAULT_SPEED_Y,0,0,1,0);	
		
		
		this.init(context, positionX, positionY);
	}
	private void init(Context context,float positionX,float positionY){
		//set width and height, x and y
		final int len=(int)context.getResources().getDimension(R.dimen.benefical_object_len);
		this.setLayoutParams(new RelativeLayout.LayoutParams(len,len));
		this.setX(positionX);
		this.setY(positionY);
		
		//set background
		this.setBackgroundResource(R.drawable.white_center_red_outline);

		//add to collision detector
		GameActivity.bonuses.add(this);
		
	}
	
	public abstract void applyBenefit(Gravity_ShootingView theBenefitter);
	
	public int removeView(boolean showExplosion){
		if(GameActivity.bonuses.contains(this)){
			GameActivity.bonuses.remove(this);			
		}
		return super.removeView(false);
	}
	
	public static BonusView getRandomBonusView(Context context,float positionX,float positionY){
		BonusView bonus;
		double rand = Math.random();
		
		if(rand<.3){
			bonus = new Bonus_HealView(context,positionX,positionY);
		}else if (rand<.4){
			bonus = new Bonus_Gun_IncBulletDamageView(context,positionX,positionY);			
		}else if (rand<.7){
			bonus = new Bonus_Gun_IncBulletFreqView(context,positionX,positionY);			
		}else if (rand<.8){
			bonus = new Bonus_Gun_IncBulletSpeedView(context,positionX,positionY);			
		}else if(rand<.95){
			bonus = new Bonus_SpecialGun_AngledDualShotView(context,positionX,positionY);
		}else{
			bonus = new Bonus_SpecialGun_AngledTriShotView(context,positionX,positionY);
		}
		
//testing
		bonus = new Bonus_Bullet_Missile(context,positionX,positionY);	
		
		return bonus;
	}
}
