package com.jtronlabs.to_the_moon;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;



public class AppIntroActivity extends Activity{
     
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_app_intro);
		
		new CountDownTimer(800,200){
	        @Override 
	        public void onTick(long millisUntilFinished){
	        } 

	        @Override
	        public void onFinish(){
	        	Intent i= new Intent(AppIntroActivity.this, MainActivity.class);
	    		startActivity(i);
	    		AppIntroActivity.this.finish();
	        }
	   }.start();
	}	
}