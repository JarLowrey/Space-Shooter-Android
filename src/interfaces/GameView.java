package interfaces;

import android.widget.ImageView;
import parents.MovingView;
import friendlies.ProtagonistView;

public interface GameView {

	public void openStore();
	public void gameOver();
	public void beatGame();
	public void setHealthBar();
	public ProtagonistView getProtagonist();
	
	public void changeGameBackground(int newBackgroundId);
	public void addToForeground(MovingView view);
	public void addToBackground(MovingView view);
	public ImageView getExhaust();
}
