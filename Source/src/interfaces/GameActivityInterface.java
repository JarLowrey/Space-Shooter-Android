package interfaces;

import android.widget.ImageView;
import friendlies.ProtagonistView;

public interface GameActivityInterface {

	public void openStore();
	public void gameOver();  
	public void setHealthBars();
	public void setScore(int score);
	public void incrementScore(int score); 
	public ProtagonistView getProtagonist();
	  
	public void resetResourcesGameTextView();
	public ImageView getExhaust();
	void setTimerText(long numMilliecondsLeft);
}
