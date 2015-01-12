package parents;

import interfaces.CollidableObjectWithHealthDamageEtc;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_orbiters.Shooting_OrbiterView;

/**
 * ImageView object to be placed on screen with gameplay properties such as health, score, speeds, and more
 * @author JAMES LOWREY
 *
 */
public class Moving_ProjectileView extends MovingView implements CollidableObjectWithHealthDamageEtc{

	double speedX, damage, health,maxHealth;
	
	public Moving_ProjectileView(Context context,double movingSpeedY,double movingSpeedX,double projectileDamage,
			double projectileHealth) {
		super(context, movingSpeedY, movingSpeedX);	

		initInstanceVars( projectileDamage, projectileHealth);
	}
	
	public Moving_ProjectileView(Context context,AttributeSet at,double movingSpeedY,double movingSpeedX,
			double projectileDamage,double projectileHealth) {
		super(context,at, movingSpeedY, movingSpeedX);	
		
		initInstanceVars(     projectileDamage, projectileHealth);
	}
	private void initInstanceVars(double projectileDamage,double projectileHealth){
		
		damage=projectileDamage;
		health=projectileHealth;
		maxHealth=projectileHealth;
		
	}

	/**
	 * 
	 * Subtract @param/amountOfDamage from this View's health. Flash the View's background red to indicate damage taken. Remove View from game if it dies
	 * @param amountOfDamage-how much the view's health should be subtracted
	 * @return True if thi dies
	 */
	public boolean takeDamage(double amountOfDamage){
		boolean dies= false;
		health-=amountOfDamage;
		
		if(health<=0){
			removeGameObject();
			dies = true;
		}else{
			//set the background behind this view, and then remove it after howLongBackgroundIsApplied milliseconds
			this.setBackgroundResource(R.drawable.view_damaged);
			final int howLongBackgroundIsApplied=80;
			Runnable removeDmg = new Runnable(){
				@Override
				public void run() {Moving_ProjectileView.this.setBackgroundColor(Color.TRANSPARENT);}
			};
			this.postDelayed(removeDmg, howLongBackgroundIsApplied);
			
//			createExplosion();
		}
		
		return dies;
	}
	
	public void heal(double howMuchHealed){
		health+=howMuchHealed;
		if(this == GameActivity.protagonist){
			GameActivity.setHealthBar();
		}
	}
	

	
	//SET METHODS
	public void setDamage(double newDamage){
		damage=newDamage;
	}
	
	//GET METHODS
	public double getHealth(){
		return health;
	}
	public double getMaxHealth(){
		return maxHealth;
	}
	public double getDamage(){
		return damage;
	}

	@Override
	public void restartThreads() {
		// do nothing for this class. Override in a child class if there are threads added
	}

	@Override
	public boolean isDead() {
		return isRemoved() || health < 0;
	}
}
