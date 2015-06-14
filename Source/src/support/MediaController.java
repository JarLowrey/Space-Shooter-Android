package support;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
	
	
	private static MediaPlayer soundNonLoopingMediaPlayer,soundLoopingMediaPlayer;
    public static boolean donePlayingNonLoopingSoundClip=true,currentlyPlayingLoopingSound=false;

    public static void stopNonLoopingShortSound() {
        if (soundNonLoopingMediaPlayer != null) {
            soundNonLoopingMediaPlayer.release();
            soundNonLoopingMediaPlayer = null;
        }
    }

    public static void stopLoopingSound() {
        if (soundLoopingMediaPlayer != null) {
        	currentlyPlayingLoopingSound = false;
        	soundLoopingMediaPlayer.release();
        	soundLoopingMediaPlayer = null;
        }
    }

    public static void playSoundClip(Context c, int rid,boolean soundIsLooping) {
    	//source for non looping sounds http://stackoverflow.com/questions/18254870/play-a-sound-from-res-raw
    	
		SharedPreferences gameState = c.getSharedPreferences(MainActivity.GAME_SETTING_PREFS, 0);
		final boolean soundIsOn = gameState.getBoolean(MainActivity.SOUND_PREF, true);
		
		if(soundIsOn){
			if(soundIsLooping){
				stopLoopingSound();
				currentlyPlayingLoopingSound = true;
		        soundLoopingMediaPlayer = MediaPlayer.create(c, rid);
		        soundLoopingMediaPlayer.setLooping(true);
		        soundLoopingMediaPlayer.start();
			}else{
		    	stopNonLoopingShortSound();
		        donePlayingNonLoopingSoundClip=false;
		
		        soundNonLoopingMediaPlayer = MediaPlayer.create(c, rid);
		        soundNonLoopingMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		            @Override
		            public void onCompletion(MediaPlayer mediaPlayer) {
		            	stopNonLoopingShortSound();
		                donePlayingNonLoopingSoundClip=true;
		            }
		        });
		
		        soundNonLoopingMediaPlayer.start();
			}
		}
    }
    
}
