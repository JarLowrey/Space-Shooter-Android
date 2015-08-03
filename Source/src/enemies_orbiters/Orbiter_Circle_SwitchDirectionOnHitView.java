package enemies_orbiters;

import interfaces.MovingViewInterface;
import android.widget.RelativeLayout;


public class Orbiter_Circle_SwitchDirectionOnHitView extends Orbiter_CircleView implements MovingViewInterface {
	

	public Orbiter_Circle_SwitchDirectionOnHitView(RelativeLayout layout,int level,int score,float speedY,int collisionDamage, 
			int health,float probSpawnBeneficialObjecyUponDeath,
			int orbitPixelX,int orbitPixelY,int width,int height,int imageId,int circleRadius,int angularVelocityInDegrees) {
		super( layout, level,score, speedY, collisionDamage, 
				 health, probSpawnBeneficialObjecyUponDeath,
				 orbitPixelX, orbitPixelY, width, height, imageId,circleRadius, angularVelocityInDegrees);
	}
	
	@Override
	public boolean takeDamage(int amountOfDamage){
		this.setAngularVelocity(this.getAngularVelocity() * -1);
		return super.takeDamage(amountOfDamage);
	}
}
