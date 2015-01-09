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
	public float getX();	
	public int getWidth();	
	public float getY();	
	public int getHeight();
	public boolean post(Runnable run);
	public boolean postDelayed(Runnable run,long millisecondDelay);
	public boolean removeCallbacks(Runnable run);
}
