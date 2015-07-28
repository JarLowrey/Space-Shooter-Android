package parents;

import interfaces.Projectile;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;
import android.graphics.Color;

import com.jtronlabs.to_the_moon.R;

/**
 * ImageView object to be placed on screen with gameplay properties such as health, score, speeds, and more
 * @author JAMES LOWREY
 *
 */
public abstract class Moving_ProjectileView extends MovingView implements Projectile{

	final protected static double XXSMALL_SCALING=1.01,SMALL_SCALING=1.2, MED_SCALING=1.5, LARGE_SCALING=2;
	
	float speedX;
	int damage, health,maxHealth;
	
	public Moving_ProjectileView(Context context,float movingSpeedY,float movingSpeedX,int projectileDamage,
			int projectileHealth,int width,int height,int imageId) {
		super(context, movingSpeedY, movingSpeedX, width, height, imageId);	

		damage=projectileDamage;
		health=projectileHealth;
		maxHealth=projectileHealth;//set at beginning of life, or overwrite on heal()
	}

	/**
	 * 
	 * Subtract @param/amountOfDamage from this View's health. Flash the View's background red to indicate damage taken. Remove View from game if it dies
	 * @param amountOfDamage-how much the view's health should be subtracted
	 * @return True if thi dies
	 */
	public boolean takeDamage(int amountOfDamage){		
		boolean dies= false;
		health-=amountOfDamage;
		
		if(health<=0){
			removeGameObject();
			dies = true;
		}else{
			//set the background behind this view, and then remove it after howLongBackgroundIsApplied milliseconds
			this.setBackgroundResource(R.drawable.view_damaged);
			final int howLongBackgroundIsApplied=80;
			KillableRunnable removeDmg = new KillableRunnable(){
				@Override
				public void doWork() {Moving_ProjectileView.this.setBackgroundColor(Color.TRANSPARENT);}
			};
			ConditionalHandler.postIfAlive(removeDmg,howLongBackgroundIsApplied,this);			
//			createExplosion();
		}
		
		return dies;
	}
	
	public void heal(int howMuchHealed){
		health+=Math.abs(howMuchHealed);
		if(health>maxHealth){
			maxHealth=health;
		}
	}
	//SET METHODS
	public void setDamage(int newDamage){
		damage=newDamage;
	}
	public void setHealth(int healthValue){
		if(healthValue>maxHealth){
			maxHealth = healthValue;
		}
		this.health=healthValue;
	}
	
	
	//GET METHODS
	public int getHealth(){
		return health;
	}
	public int getMaxHealth(){
		return maxHealth;
	}
	public int getDamage(){
		return damage;
	}

	@Override
	public void restartThreads() {
		// do nothing for this class. Override in a child class if there are threads added
	}
	@Override
	public void removeGameObject() {
		this.deaultCleanupOnRemoval();//needs to be the last thing called for handler to remove all callbacks
	}
	
	/**
	 * Helper method to easily scale values
	 * @param defaultValue
	 * @param difficultyLvl
	 * @param scalingFactor
	 * @return
	 */
	protected static double scaledValue(double defaultValue, int difficultyLvl, double scalingFactor){
		if(difficultyLvl == 0){
			return defaultValue;
		}else{
			return defaultValue * ( difficultyLvl * scalingFactor );
		}
	}
}
