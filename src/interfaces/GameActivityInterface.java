package interfaces;

import android.widget.ImageView;
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
	public void removeView(ImageView view);
	public void addToForeground(ImageView view);
	public void addToBackground(ImageView view);
	public ImageView getExhaust();
}
