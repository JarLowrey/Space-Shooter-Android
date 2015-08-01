package interfaces;

import android.widget.ImageView;
import friendlies.ProtagonistView;

public interface GameActivityInterface {

	public void openStore();
	public void lostGame();
	public void beatGame();
	public void setHealthBars();
	public void setScore(int score);
	public void incrementScore(int score); 
	public ProtagonistView getProtagonist();
	
	public void resetResourcesTextView();
	public void removeView(ImageView view);
	public void addToForeground(ImageView view);
	public void addToBackground(ImageView view);
	public ImageView getExhaust();
}
