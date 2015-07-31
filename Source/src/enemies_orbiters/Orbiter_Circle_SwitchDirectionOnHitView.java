package enemies_orbiters;

import interfaces.MovingViewInterface;
import android.content.Context;


public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements MovingViewInterface {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(Context context,int level,int score,float speedY,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,int width,int height,int imageId,int circleRadius,int angularVelocityInDegrees) {
		super( context, level,score, speedY, collisionDamage, 
				 health, probSpawnBeneficialObjecyUponDeath,
				 orbitPixelX, orbitPixelY, width, height, imageId,circleRadius, angularVelocityInDegrees);
	}
	
	@Override
	public boolean takeDamage(int amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
