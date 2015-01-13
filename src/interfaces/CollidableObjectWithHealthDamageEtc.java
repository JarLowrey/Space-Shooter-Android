package interfaces;
 

public interface CollidableObjectWithHealthDamageEtc extends GameObjectInterface{

	public void heal(double howMuchHealed);
	public double getMaxHealth();
	public double getHealth();

	public boolean takeDamage(double amountOfDamage);
	public void setDamage(double newDamage);
	public double getDamage();
	
}
