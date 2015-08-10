package com.jtronlabs.space_shooter;

import helpers.GameAlertDialogBuilder;
import helpers.GameTextView;
import helpers.MediaController;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import backgroundViews.ParticleBackgroundAnimation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity implements OnClickListener{

	public static final String GAME_SETTING_PREFS = "GameSettingPrefs",
			VIBRATE_PREF="vibrateOn",
			SOUND_PREF="soundOn";
	public static final String GAME_META_DATA_PREFS = "GameMetaData",
			HIGHEST_SCORE = "highestScore",
			MAX_LEVEL = "maxLevel";
	private static float screenDens,widthPixels,heightPixels;
	private ImageButton vibrate, sound, credits;
	private AdView adView;     
	    
	private ParticleBackgroundAnimation stars_creator;
	           
	@Override 
	protected void onCreate(Bundle savedInstanceState) {       
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main); 
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		 
	    //set up settings buttons
	    ImageButton settings = (ImageButton)findViewById(R.id.settings_btn); 
	    settings.setOnClickListener(this);
	    vibrate = (ImageButton)findViewById(R.id.toggle_vibration);
	    vibrate.setOnClickListener(this); 
	    sound = (ImageButton)findViewById(R.id.toggle_sound);  
	    sound.setOnClickListener(this);
	    credits = (ImageButton)findViewById(R.id.show_credits);
	    credits.setOnClickListener(this);
	    
	    TextView title = (TextView)findViewById(R.id.title);
	    Typeface spaceAgeFont = Typeface.createFromAsset(this.getAssets(), GameTextView.SPACE_AGE);
	    title.setTypeface(spaceAgeFont);
	    title.setTextSize(140);					//yScale = 140dp
	    title.setTextScaleX((float) (90.0/150));//xScale = 90dp
	    
		updateColorOfSettingsButtons(); 

		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
	    
		//set up buttons
		ImageButton playBtn = (ImageButton)findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);
	    
		createAdView();
		
		stars_creator = new ParticleBackgroundAnimation((RelativeLayout)findViewById(R.id.activity_main),
				(int) (1.5*ParticleBackgroundAnimation.DEFAULT_NUM_STARS));
	}
	
	private void createAdView(){		
		//Create and plate the Banner Ad in activity_main.xml
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-1314947069846070/4608411941");
		adView.setAdSize(AdSize.BANNER);
 
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
			    RelativeLayout.LayoutParams.WRAP_CONTENT, 
			    RelativeLayout.LayoutParams.WRAP_CONTENT); 
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adView.setLayoutParams(params);
		
		RelativeLayout background = (RelativeLayout)findViewById(R.id.activity_main);
		background.addView(adView);
		
		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice("FFAA2B4ECD49CBF2A0AB7F9D447410D7")
			.build(); 
 
		// Load ad request into Adview
		adView.loadAd(adRequest);	
	}
	
	 
	@Override
	public void onPause(){
		super.onPause();

		GameLoop.instance().stopLevelAndLoop();
		
		stars_creator.stopSpawningStars();
		
		adView.pause();
		MediaController.stopLoopingSound();
	}

	@Override
	public void onResume(){ 
		super.onResume();
		
		GameLoop.instance().startLevelAndLoop(this, null);

		stars_creator.startSpawningStars();
		
		//set GameTextViews here, as the text may have updated since onCreate 
		//(user plays game, gets new record, and goes back to mainActivity)
		SharedPreferences gameMeta = getSharedPreferences(MainActivity.GAME_META_DATA_PREFS,0);
		GameTextView score = (GameTextView)findViewById(R.id.high_score);
		score.setText( formatInt( gameMeta.getInt(HIGHEST_SCORE, 0)) );
		GameTextView days = (GameTextView)findViewById(R.id.max_day);
		days.setText(formatInt(gameMeta.getInt(MAX_LEVEL, 0)) );
		
		adView.resume();
		MediaController.playSoundClip(this, R.raw.background_intro, true);
	}

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy(); 
    }
	 
	@Override
	public void onClick(View v) {
		SharedPreferences gameSettings = getSharedPreferences(GAME_SETTING_PREFS, 0);
		SharedPreferences.Editor editor = gameSettings.edit();

		if(v.getId() == R.id.playBtn){
				Intent nextIntent;

				SharedPreferences gameState = getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
				final int currLevel = gameState.getInt(GameActivity.STATE_LEVEL, 0);
				
				nextIntent= new Intent(this, GameActivity.class);
				MediaController.stopLoopingSound();
				
				//startNext intent
				startActivity(nextIntent);
		}else if(v.getId() == R.id.settings_btn){
				RelativeLayout settingsWrap = (RelativeLayout)findViewById(R.id.other_settings_buttons_wrap);
				if(View.GONE == settingsWrap.getVisibility() ){
					settingsWrap.setVisibility(View.VISIBLE);
				}else{
					settingsWrap.setVisibility(View.GONE);					
				}
		}else if(v.getId() == R.id.toggle_sound){
				boolean soundEdit = gameSettings.getBoolean(SOUND_PREF, true);
				editor.putBoolean(SOUND_PREF, !soundEdit);
				editor.commit();
				
    			if(soundEdit){
        			Toast.makeText(getApplicationContext(),"Sound is turned off", Toast.LENGTH_SHORT).show();
    				MediaController.stopLoopingSound();
    			}else{
        			Toast.makeText(getApplicationContext(),"Sound is turned on", Toast.LENGTH_SHORT).show();
    				MediaController.playSoundClip(this, R.raw.background_intro, true);
    			}
		}else if(v.getId() == R.id.toggle_vibration){
				boolean vibrateEdit = gameSettings.getBoolean(VIBRATE_PREF, true);
				editor.putBoolean(VIBRATE_PREF, !vibrateEdit);
				
				String vibrateState = (!vibrateEdit) ? "on" : "off" ;
    			Toast.makeText(getApplicationContext(),"Vibration is turned "+vibrateState, Toast.LENGTH_SHORT).show();
		}else if(v.getId() == R.id.show_credits){
				new GameAlertDialogBuilder(this)
				    .setTitle("Credits")
				    .setMessage(this.getResources().getString(R.string.credits))
				    .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
				     })
				     .show();
		}   
		editor.commit();

		updateColorOfSettingsButtons();
	}

	private void updateColorOfSettingsButtons(){
		SharedPreferences gameState = getSharedPreferences(GAME_SETTING_PREFS, 0);
		boolean vibrateState = gameState.getBoolean(VIBRATE_PREF, true);
		boolean soundState = gameState.getBoolean(SOUND_PREF, true);
		
		if(vibrateState){
			vibrate.setBackgroundResource(R.drawable.btn_green);
		}else{
			vibrate.setBackgroundResource(R.drawable.btn_red);
		}
		 
		if(soundState){
			sound.setBackgroundResource(R.drawable.btn_green); 
		}else{  
			sound.setBackgroundResource(R.drawable.btn_red);
		}
	} 
	public static float getScreenDens(){
		return screenDens;
	}
	public static float getWidthPixels(){
		return widthPixels;
	} 
	public static float getHeightPixels(){
		return heightPixels;
	}
	
	public static String formatInt(int anUnformattedInteger){
		return ""+NumberFormat.getNumberInstance(Locale.US).format( anUnformattedInteger );
	}
}
