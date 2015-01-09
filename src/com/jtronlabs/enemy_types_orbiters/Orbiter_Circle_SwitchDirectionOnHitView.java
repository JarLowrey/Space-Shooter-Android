package com.jtronlabs.enemy_types_orbiters;

import android.content.Context;

import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;

public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements GameObjectInterface {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health, double bulletFreq,
			float heightView,float widthView,double bulletDamage,double bulletVerticalSpeed,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees) {
		super( context, score, speedY,  speedX, collisionDamage, 
				 health,  bulletFreq,
				 heightView, widthView, bulletDamage, bulletVerticalSpeed, probSpawnBeneficialObjecyUponDeath,
				 circleRadius, angularVelocityInDegrees);
	}
	
	@Override
	public boolean takeDamage(double amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
