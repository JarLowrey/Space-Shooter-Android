package interfaces;

import parents.MovingView;
import friendlies.ProtagonistView;

public interface InteractiveGameInterface {

	public void openStore();
	public void gameOver();
	public void beatGame();
	public void setHealthBar();
	public ProtagonistView getProtagonist();
	public void addForegroundObjectToScreen(MovingView view);
	public void addBackgroundObjectToScreen(MovingView view);	
	public void changeBackground(int newBackgroundId);
	
}
