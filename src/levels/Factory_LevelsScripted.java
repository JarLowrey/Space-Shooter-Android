package levels;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * 
 * @author JAMES LOWREY
 *
 */
public class Factory_LevelsScripted extends Factory_CommonWaves{

	public static int NEW_LEVEL=-1;
	
	protected static boolean levelCompleted,levelStarted;
	protected static int currentProgressInLevel;
	

	private final Runnable levelOverRunnable = new Runnable(){
		@Override
		public void run() {	
			levelCompleted=true;
			levelStarted=false;
			levelPaused=true;
			pauseLevel(); 
		}
	};
	

	final Runnable[] level1 = {meteorsOneStraightFallingEverySecondForWholeLevel,
			meteorsOneSidewaysFallingEverySecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToRight,
			meteorsGiantAndSideways,
			meteorsGiantAndSideways,
			meteorShowerLong,
			meteorsOnlyGiants,
			meteorsOnlyGiants,
			levelOverRunnable};

	final Runnable[] level2 =level1;
	
	final Runnable[] level3 = {meteorsOneSidewaysFallingEverySecondForWholeLevel,
			meteorsOneSidewaysFallingEverySecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			refreshArrayShooters,
			diveBombersEverySecondWave,
			diveBombersEverySecondWave,
			levelOverRunnable};
	
	final Runnable[] level4 = {meteorsOneSidewaysFallingEverySecondForWholeLevel,
			meteorSidewaysThisWave,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			diveBombersEverySecondWave,
			levelOverRunnable};
	
	final Runnable[] level5 = {meteorsOneSidewaysFallingEverySecondForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			diveBombersEverySecondWave,
			doNothing,
			diveBombersEverySecondWave,
			boss1,
			levelOverRunnable};
	
	final Runnable levels[][] ={level1,level2,level3,level4,level5};
	
	public Factory_LevelsScripted(Context context,RelativeLayout gameScreen){
		super( context, gameScreen);
		levelCompleted = false;
		levelPaused=false;
	}

	protected void initLevelVarsAtBeginningOfEveryLevel(){
		levelCompleted=false;
		levelStarted=true;
		levelPaused=false;
		currentProgressInLevel=0;
	}
	
	public void pauseLevel(){
		levelPaused=true;
		spawnHandler.removeCallbacks(null);
	}
	
	public static boolean isLevelCompleted(){
		return levelCompleted;
	}
	
	public static boolean hasLevelStarted(){
		return levelStarted;
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
