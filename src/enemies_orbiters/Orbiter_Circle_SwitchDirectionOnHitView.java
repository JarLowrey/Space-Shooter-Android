package enemies_orbiters;

import interfaces.GameObjectInterface;
import android.content.Context;


public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements GameObjectInterface {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(Context context,int score,double speedY, double speedX,double collisionDamage, 
			double health,
			float heightView,float widthView,double probSpawnBeneficialObjecyUponDeath,
			int circleRadius,int angularVelocityInDegrees) {
		super( context, score, speedY,  speedX, collisionDamage, 
				 health,
				 heightView, widthView, probSpawnBeneficialObjecyUponDeath,
				 circleRadius, angularVelocityInDegrees);
	}
	
	@Override
	public boolean takeDamage(double amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
