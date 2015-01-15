package enemies_orbiters;

import interfaces.MovingObject;
import android.content.Context;


public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements MovingObject {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees,int width,int height,int imageId) {
		super( context, score, speedY,  speedX, collisionDamage, 
				 health, probSpawnBeneficialObjecyUponDeath,
				 circleRadius, angularVelocityInDegrees, width, height, imageId);
	}
	
	@Override
	public boolean takeDamage(double amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
