package interfaces;

import android.widget.ImageView;
import parents.MovingView;
import friendlies.ProtagonistView;

public interface GameActivityInterface {

	public void openStore();
	public void gameOver();
	public void beatGame();
	public void setHealthBar();
	public void setScore(int score);
	public void incrementScore(int score);
	public ProtagonistView getProtagonist();
	
	public void changeGameBackground(int newBackgroundId);
	public void addToForeground(MovingView view);
	public void addToBackground(MovingView view);
	public ImageView getExhaust();
}
