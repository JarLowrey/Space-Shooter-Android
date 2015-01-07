package com.jtronlabs.to_the_moon.misc;

import android.content.Context;
import android.widget.RelativeLayout;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;

public class Projectile_BeneficialView extends ProjectileView implements GameObjectInterface{

	public final static int SET_TRI_SHOT=0,SET_DUAL_SHOT=1,HEAL=2,UPGRADE_GUN=3;
	private final static float SPEED_Y=2;
	private int whichBenefitDoIHave;
	
	public Projectile_BeneficialView(Context context,int whichBenefit,float positionX,float positionY) {
		super(context,0,SPEED_Y,SPEED_Y,0,0,1);	
		
		whichBenefitDoIHave=whichBenefit;
		
		this.init(context, positionX, positionY);
	}
	//no benefit passed, must choose one
	public Projectile_BeneficialView(Context context,float positionX,float positionY) {
		super(context,0,SPEED_Y,SPEED_Y,0,0,1);	
		
		// create a random benefit
		final double rand = Math.random();
		if(rand<.6){
			whichBenefitDoIHave=HEAL;			
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
			theBenefitter.heal(theBenefitter.getMaxHealth()/4);
			break;
		case UPGRADE_GUN:
			theBenefitter.upgradeGun();
		}
	}
	public void applyBenefit(ProjectileView theBenefitter){
		switch(whichBenefitDoIHave){
		default:
			theBenefitter.heal(theBenefitter.getMaxHealth()/4);
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
		//tri shot is at 30 degrees. Thus, find needed X Speed
		final double bulletXSpeed = theBenefitter.getSpeedY() * Math.tan(Math.toRadians(30));
		if(theBenefitter.isTriShot()){
			theBenefitter.setAmmo(theBenefitter.getAmmo()+15);
		}else{
			theBenefitter.setAmmo(30);
			theBenefitter.setTriShot(bulletXSpeed);			
		}
	}
	private void setDualShot(Gravity_ShootingView theBenefitter){
		//dual shot is at 30 degrees. Thus, find needed X Speed
		final double bulletXSpeed = theBenefitter.getSpeedY() * Math.tan(Math.toRadians(30));
		if(theBenefitter.isDualShot()){
			theBenefitter.setAmmo(theBenefitter.getAmmo()+20);
		}else{
			theBenefitter.setAmmo(40);
			theBenefitter.setDualShot(bulletXSpeed);			
		}
	}
	
	public int removeView(boolean showExplosion){
		if(GameActivity.beneficials.contains(this)){
			GameActivity.beneficials.remove(this);			
		}
		return super.removeView(false);
	}
}
