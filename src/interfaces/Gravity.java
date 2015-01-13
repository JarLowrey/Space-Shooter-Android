package interfaces;

/**
 * A ProjectileView with a constant downwards force. 
 * This force is removed when the instance reaches its lowest threshold. 
 * All enemies and bonuses will have this force active
 * 
 * @author JAMES LOWREY
 *
 */
public interface Gravity {

	public static final int NO_THRESHOLD=Integer.MAX_VALUE;
	
	public void startGravity();
	
	public void stopGravity();
	
	public void setThreshold (int newLowestPositionThreshold);
	
	public int getThreshold();
}
