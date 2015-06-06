package support;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

import com.jtronlabs.to_the_moon.MainActivity;

public class MediaController {
	
	/**
	 * Should be called whenever vibration is needed to ensure user's preference is respected
	 * @param ctx
	 * @param vibrationPattern
	 */
	public static void vibrate(Context ctx, long[] vibrationPattern){
		SharedPreferences gameState = ctx.getSharedPreferences(MainActivity.GAME_SETTING_PREFS, 0);
		final boolean vibrateIsOn = gameState.getBoolean(MainActivity.VIBRATE_PREF, true);
		if(vibrateIsOn){
	        Vibrator vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
	        vibrator.vibrate(vibrationPattern, -1);
		}
	}

	public static void vibrate(Context ctx, long vibrateLength){
		SharedPreferences gameState = ctx.getSharedPreferences(MainActivity.GAME_SETTING_PREFS, 0);
		final boolean vibrateIsOn = gameState.getBoolean(MainActivity.VIBRATE_PREF, true);
		if(vibrateIsOn){
	        Vibrator vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
	        vibrator.vibrate(vibrateLength);
		}
	}

	/**
	 * Should be called whenever sound is needed to ensure user's preference is respected
	 * @param ctx
	 * @param soundId
	 */
	public static void playSound(Context ctx, int soundId){
		SharedPreferences gameState = ctx.getSharedPreferences(MainActivity.GAME_SETTING_PREFS, 0);
		final boolean soundIsOn = gameState.getBoolean(MainActivity.SOUND_PREF, true);
		if(soundIsOn){

			//copy from previous project
		}
	}
}
