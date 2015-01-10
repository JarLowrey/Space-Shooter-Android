package interfaces;

/**
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
