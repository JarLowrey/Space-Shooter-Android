package interfaces;
 

public interface Projectile extends MovingViewInterface{

	public void heal(int howMuchHealed);
	public int getMaxHealth();
	public int getHealth();

	public boolean takeDamage(int amountOfDamage);
	public void setDamage(int newDamage);
	public int getDamage();
	
}
