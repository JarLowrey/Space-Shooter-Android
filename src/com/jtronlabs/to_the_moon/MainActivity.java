package com.jtronlabs.to_the_moon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import custom_support_classes.DrawTextView;

 //http://stackoverflow.com/questions/15842901/set-animated-gif-as-background-android


public class MainActivity extends Activity implements OnClickListener{

	private static float screenDens,widthPixels,heightPixels;
	
	AnimationDrawable animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		

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
		Animation earthSpin=AnimationUtils.loadAnimation(this,R.anim.spin_earth);
		ImageView earth= (ImageView)findViewById(R.id.earth);
		earth.startAnimation(earthSpin);
		
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
//	    animation = (AnimationDrawable) imgView.getBackground();
//	    animation.start();
		
	}
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch(v.getId()){
			case R.id.playBtn: 
				i= new Intent(this, GameActivity.class);
				startActivity(i);
//				i= new Intent(this, IntroActivity.class);
//				startActivity(i);
				break;
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
