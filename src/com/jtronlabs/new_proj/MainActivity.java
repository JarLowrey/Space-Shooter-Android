package com.jtronlabs.new_proj;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jtronlabs.views.DrawTextPanel;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//HACK to make stars appear as tiles in X direction only
		BitmapDrawable TileMe = (BitmapDrawable)getResources().getDrawable(R.drawable.level4);
		TileMe.setTileModeX(Shader.TileMode.REPEAT);
		ImageView stars = (ImageView)findViewById(R.id.stars);
		stars.setBackgroundDrawable(TileMe); 
		
		// moon spinning animation
		Animation moonSpin=AnimationUtils.loadAnimation(this,R.anim.spin_moon);
		ImageView moon= (ImageView)findViewById(R.id.moon);
		moon.startAnimation(moonSpin);
		// earth spinning animation 
		Animation earthSpin=AnimationUtils.loadAnimation(this,R.anim.spin_earth);
		ImageView earth= (ImageView)findViewById(R.id.earth);
		earth.startAnimation(earthSpin);
		
		//set up buttons
		RelativeLayout playBtn = (RelativeLayout)findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);

		//draw title words
	    DrawTextPanel titlePanel = (DrawTextPanel)findViewById(R.id.title);
	    titlePanel.drawText("To The Moon!");
	    
	    //launch the rocket
		ImageView rocket= (ImageView)findViewById(R.id.rocket_main);
	    Animation rocketLaunch=AnimationUtils.loadAnimation(this,R.anim.rocket_launch_title_screen);
		rocket.startAnimation(rocketLaunch);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch(v.getId()){
			case R.id.playBtn: 
				i= new Intent(this, GameActivity.class);
				startActivity(i);
				break;
		}
	}

}
