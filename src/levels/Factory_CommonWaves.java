package levels;

import android.content.Context;
import android.widget.RelativeLayout;

public class Factory_CommonWaves extends Factory_Waves{

	public Factory_CommonWaves(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}
	
	/**
	 * define common waves here. for example, 2 meteor showers, one on each eadge of screen that force user to go into middle of screen.
	 * this class should contain runnables, not functions, so that the level factory can post delay them
	 */
}
