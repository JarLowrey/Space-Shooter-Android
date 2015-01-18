package enemies_orbiters;

import interfaces.MovingViewInterface;
import android.content.Context;


public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements MovingViewInterface {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,double probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,int width,int height,int imageId,int circleRadius,int angularVelocityInDegrees) {
		super( context, score, speedY,  speedX, collisionDamage, 
				 health, probSpawnBeneficialObjecyUponDeath,
				 orbitPixelX, orbitPixelY, width, height, imageId,circleRadius, angularVelocityInDegrees);
	}
	
	@Override
	public boolean takeDamage(double amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
