package com.jtronlabs.specific_view_types;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.guns.Gun_Special;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledDualShot;
import com.jtronlabs.to_the_moon.guns.Gun_Special_AngledTriShot;
import com.jtronlabs.to_the_moon.guns.Gun_Upgradeable;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;
import com.jtronlabs.to_the_moon.views.Projectile_GravityView;

public class Projectile_BeneficialView extends Projectile_GravityView implements GameObjectInterface{

	public final static int SET_TRI_SHOT=0,SET_DUAL_SHOT=1,HEAL=2,UPGRADE_GUN=3;
	public final static float DEFAULT_SPEED_Y=3;
	private int whichBenefitDoIHave;
	
	public Projectile_BeneficialView(Context context,int whichBenefit,float positionX,float positionY) {
		super(context,0,DEFAULT_SPEED_Y,DEFAULT_SPEED_Y,0,0,1,0);	
		
		whichBenefitDoIHave=whichBenefit;
		
		this.init(context, positionX, positionY);
	}
	//no benefit passed, must choose one
	public Projectile_BeneficialView(Context context,float positionX,float positionY) {
		super(context,0,DEFAULT_SPEED_Y,DEFAULT_SPEED_Y,0,0,1,0);	
		
		// create a random benefit
		final double rand = Math.random();
		if(rand<.6){
			whichBenefitDoIHave=HEAL;
			whichBenefitDoIHave=UPGRADE_GUN;
		}else if(rand<.9){
			whichBenefitDoIHave=SET_DUAL_SHOT;			
		}else{
			whichBenefitDoIHave=SET_TRI_SHOT;
		}

		this.init(context, positionX, positionY);
	}
	private void init(Context context,float positionX,float positionY){
		//set width and height, x and y
		final int len=(int)context.getResources().getDimension(R.dimen.benefical_object_len);
		this.setLayoutParams(new RelativeLayout.LayoutParams(len,len));
		this.setX(positionX);
		this.setY(positionY);
		
		//set image and background
		this.setBackgroundResource(R.drawable.white_center_red_outline);
		setImage(whichBenefitDoIHave);

		//add to collision detector
		GameActivity.beneficials.add(this);
		
	}
	
	public void applyBenefit(Gravity_ShootingView theBenefitter){
		switch(whichBenefitDoIHave){
		case SET_TRI_SHOT:
			setTriShot(theBenefitter);
			break;
		case SET_DUAL_SHOT:
			setDualShot(theBenefitter);
			break;
		case HEAL:
			heal(theBenefitter);
			break;
		case UPGRADE_GUN:
			theBenefitter.upgradeOrDowngradeGun(true);
		}
	}
	public void applyBenefit(ProjectileView theBenefitter){
		switch(whichBenefitDoIHave){
		case HEAL:
			heal(theBenefitter);
			break;
		default:
			heal(theBenefitter);
		}
	}
	private void setImage(int whichBenefit){
		switch(whichBenefit){
		case SET_TRI_SHOT:
			this.setImageResource(R.drawable.tri_bullet); 
			break;
		case SET_DUAL_SHOT:
			this.setImageResource(R.drawable.dual_bullet);
			break;
		case HEAL:
			this.setImageResource(R.drawable.heal);
			break;
		case UPGRADE_GUN:
			this.setImageResource(R.drawable.upgrade_bullet);
		}
	}
	
	private void setTriShot(Gravity_ShootingView theBenefitter){
		if(theBenefitter.myGun instanceof Gun_Special_AngledTriShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.myGun;
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+7);
		}else{
			Gun_Special newGun = new Gun_Special_AngledTriShot(ctx,theBenefitter.myGun.getMostRecentUpgradeableGun(),
					theBenefitter);

			theBenefitter.giveSpecialGun(newGun, 25);
		}
	}
	
	private void setDualShot(Gravity_ShootingView theBenefitter){
		if(theBenefitter.myGun instanceof Gun_Special_AngledDualShot){
			Gun_Special benefittersSpecialGun = (Gun_Special) theBenefitter.myGun;
			benefittersSpecialGun.setAmmo(benefittersSpecialGun.getAmmo()+12);
		}else{
			Gun_Special newGun = new Gun_Special_AngledDualShot(ctx,theBenefitter.myGun.getMostRecentUpgradeableGun(),
					theBenefitter);
			
			theBenefitter.giveSpecialGun(newGun, 30);
		}
	}
	
	/**
	 * Do not heal past max health
	 * @param theBenefitter ProjectileView to heal
	 */
	private void heal(ProjectileView theBenefitter){
		final double amtToHeal = theBenefitter.getMaxHealth()/5;
		if(theBenefitter.getHealth()+amtToHeal<theBenefitter.getMaxHealth()){
			theBenefitter.heal(amtToHeal);
		}else{
			theBenefitter.heal(theBenefitter.getMaxHealth()-theBenefitter.getHealth());
		}
	}
	
	public int removeView(boolean showExplosion){
		if(GameActivity.beneficials.contains(this)){
			GameActivity.beneficials.remove(this);			
		}
		return super.removeView(false);
	}
}
