package com.jtronlabs.to_the_moon;

import support.DrawTextView;
import support.MediaController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

 //http://stackoverflow.com/questions/15842901/set-animated-gif-as-background-android


public class MainActivity extends Activity implements OnClickListener{

	public static final String GAME_SETTING_PREFS = "GameSettingPrefs",
			INTRO_PREF="introOn",
			VIBRATE_PREF="vibrateOn",
			SOUND_PREF="soundOn";
	private static float screenDens,widthPixels,heightPixels;
	private ImageButton vibrate, sound, intro, credits;
	
//	AnimationDrawable animation;
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
	    intro = (ImageButton)findViewById(R.id.toggle_intro);
	    intro.setOnClickListener(this);
	    credits = (ImageButton)findViewById(R.id.show_credits);
	    credits.setOnClickListener(this);
	    
		updateColorOfSettingsButtons();

		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
	    
	    
//		//HACK to make stars appear as tiles in X direction only
//		BitmapDrawable TileMe = (BitmapDrawable)getResources().getDrawable(R.drawable.level4);
//		TileMe.setTileModeX(Shader.TileMode.REPEAT);
//		ImageView stars = (ImageView)findViewById(R.id.stars);
//		stars.setBackgroundDrawable(TileMe);
	    
		// moon spinning animation
		Animation moonSpin=AnimationUtils.loadAnimation(this,R.anim.spin_moon);
		ImageView moon= (ImageView)findViewById(R.id.moon);
		moon.startAnimation(moonSpin);
		// earth spinning animation 
//		Animation earthSpin=AnimationUtils.loadAnimation(this,R.anim.spin_earth);
//		ImageView earth= (ImageView)findViewById(R.id.earth);
//		earth.startAnimation(earthSpin);
		
		//set up buttons
		ImageButton playBtn = (ImageButton)findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);

		//draw title words
	    DrawTextView titlePanel = (DrawTextView)findViewById(R.id.title);
	    titlePanel.drawText("To The Moon!");
	    
	    //launch the rocket
		ImageView rocket= (ImageView)findViewById(R.id.rocket_main);
	    Animation rocketLaunch=AnimationUtils.loadAnimation(this,R.anim.rocket_launch_title_screen);
		rocket.startAnimation(rocketLaunch);

		//create background animation
//		ImageView imgView = (ImageView) findViewById(R.id.animating_image_view_main_menu);
//		AnimationDrawable animation = (AnimationDrawable) imgView.getBackground();
//	    animation.start();
	}
	
	@Override
	public void onPause(){
		super.onPause();

		MediaController.stopLoopingSound();
	}

	@Override
	public void onResume(){
		super.onResume();

		MediaController.playSoundClip(this, R.raw.background_intro, true);
	}
	 
	@Override
	public void onClick(View v) {
		SharedPreferences gameSettings = getSharedPreferences(GAME_SETTING_PREFS, 0);
		SharedPreferences.Editor editor = gameSettings.edit();

		switch(v.getId()){
			case R.id.playBtn: 
				boolean showIntro = gameSettings.getBoolean(INTRO_PREF, true);
				Intent nextIntent;

				SharedPreferences gameState = getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
				final int currLevel = gameState.getInt(GameActivity.STATE_LEVEL, 0);
				
				if(showIntro && currLevel==0){
					nextIntent= new Intent(this, IntroActivity.class);
				}else{
					nextIntent= new Intent(this, GameActivity.class);
					MediaController.stopLoopingSound();
				}
				
				//startNext intent
				startActivity(nextIntent);
				break; 
			case R.id.settings_btn:
				RelativeLayout settingsWrap = (RelativeLayout)findViewById(R.id.other_settings_buttons_wrap);
				if(View.GONE == settingsWrap.getVisibility() ){
					settingsWrap.setVisibility(View.VISIBLE);
				}else{
					settingsWrap.setVisibility(View.GONE);					
				}
				break;
			case R.id.toggle_intro:
				boolean showIntroEdit = gameSettings.getBoolean(INTRO_PREF, true);
				editor.putBoolean(INTRO_PREF, !showIntroEdit);
				
				String introState = (!showIntroEdit) ? "on" : "off" ;
    			Toast.makeText(getApplicationContext(),"Intro is turned "+introState, Toast.LENGTH_SHORT).show();
				break;
			case R.id.toggle_sound:
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
				break;
			case R.id.toggle_vibration:
				boolean vibrateEdit = gameSettings.getBoolean(VIBRATE_PREF, true);
				editor.putBoolean(VIBRATE_PREF, !vibrateEdit);
				
				String vibrateState = (!vibrateEdit) ? "on" : "off" ;
    			Toast.makeText(getApplicationContext(),"Vibration is turned "+vibrateState, Toast.LENGTH_SHORT).show();
				break;
			case R.id.show_credits:
				new AlertDialog.Builder(this)
				    .setTitle("Credits")
				    .setMessage(this.getResources().getString(R.string.credits))
				    .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
				     })
				     .show();
				break;
		}
		editor.commit();

		updateColorOfSettingsButtons();
	}

	private void updateColorOfSettingsButtons(){
		SharedPreferences gameState = getSharedPreferences(GAME_SETTING_PREFS, 0);
		boolean vibrateState = gameState.getBoolean(VIBRATE_PREF, true);
		boolean soundState = gameState.getBoolean(SOUND_PREF, true);
		boolean introState = gameState.getBoolean(INTRO_PREF, true);
		
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

		if(introState){
			intro.setBackgroundResource(R.drawable.btn_green);
		}else{
			intro.setBackgroundResource(R.drawable.btn_red);
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
}
