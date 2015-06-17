package support;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

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
	
	
	private static MediaPlayer soundNonLoopingMediaPlayer,//reference to MP is kept, sound can be stopped and known if playing
		soundLoopingMediaPlayer;
    public static boolean donePlayingNonLoopingSoundClip=true,
    		currentlyPlayingLoopingSound=false;

    public static void stopNonLoopingSound() {
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
		    	stopNonLoopingSound();
		        donePlayingNonLoopingSoundClip=false;
		 
		        soundNonLoopingMediaPlayer = MediaPlayer.create(c, rid);
		        soundNonLoopingMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		            @Override
		            public void onCompletion(MediaPlayer mediaPlayer) {
		            	stopNonLoopingSound();
		                donePlayingNonLoopingSoundClip=true;
		            }
		        });
		
		        soundNonLoopingMediaPlayer.start();
			}
		}
    } 
    
    private static SoundPool soundEffects;//sound pool should be used for effects that are called a lot, like shooting
    public static int SOUND_BONUS,SOUND_COINS,SOUND_EXPLOSION1,SOUND_FRIENDLY_HIT,SOUND_LASER_SHOOT2,
    	SOUND_ROCKET_LAUNCH;
    
    /**
     * Play a common sound effect
     * @param c		the context
     * @param soundEffect	given int from this class used to determine sound effect id
     */ 
    public static void playSoundEffect(Context c, int soundEffect){
    	if(soundEffects == null){
    		soundEffects = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
    		SOUND_BONUS = soundEffects.load(c, R.raw.bonus, 1);
    		SOUND_COINS = soundEffects.load(c, R.raw.coins, 1);
    		SOUND_EXPLOSION1 = soundEffects.load(c, R.raw.explosion1, 1);
    		SOUND_FRIENDLY_HIT = soundEffects.load(c, R.raw.friendly_hit, 1);
    		SOUND_LASER_SHOOT2 = soundEffects.load(c, R.raw.laser_shoot2, 1);
    		SOUND_ROCKET_LAUNCH = soundEffects.load(c, R.raw.rocket_launch,1);
    	}

		SharedPreferences gameState = c.getSharedPreferences(MainActivity.GAME_SETTING_PREFS, 0);
		final boolean soundIsOn = gameState.getBoolean(MainActivity.SOUND_PREF, true);
		
		if(soundIsOn){
			soundEffects.play(soundEffect,1,1, 1,0,1);
		}
		
		if(soundEffect != SOUND_BONUS && soundEffect != SOUND_COINS && soundEffect != SOUND_EXPLOSION1 && soundEffect != SOUND_FRIENDLY_HIT && soundEffect!=SOUND_LASER_SHOOT2){
			Log.d("lowrey","Sound Effect not found!!!");
		}
    }
    
}
