package levels;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public class Factory_LevelsScripted extends Factory_LevelWaves{

	public static int NEW_LEVEL=-1;
	
	
	
	public Factory_LevelsScripted(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
	}

	
	/*
	 * 
	private void changeGameBackgroundImage(final int idToChangeTo){
		Animation fade_out=AnimationUtils.loadAnimation(this,R.anim.fade_out);
		final Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in);
		fade_out.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				gameWindowOverlay.setBackgroundResource(idToChangeTo);
				gameWindowOverlay.startAnimation(fade_in);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {}

			@Override
			public void onAnimationStart(Animation arg0) {}
			
		});
		gameWindowOverlay.startAnimation(fade_out);
	} 
	*/
}
