package com.jtronlabs.to_the_moon_interfaces;


public interface CollidableObjectWithHealthDamageEtc{

	public void heal(double howMuchHealed);
	public double getMaxHealth();
	public double getHealth();

	public boolean takeDamage(double amountOfDamage);
	
	public boolean isDead();

	public void setDamage(double newDamage);
	public double getDamage();
	

	
	
	//default, built in, final, Android interface methods
	public int getLeft();	
	public int getRight();	
	public int getTop();	
	public int getBottom();
}
