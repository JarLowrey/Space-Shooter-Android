package com.jtronlabs.space_shooter;

import helpers.KillableRunnable;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameIntroActivity extends Activity implements OnClickListener{
	 
	private TextView text;
	private Button next;
	private final String[] introText={"Strange things are happening on the moon.",
			"Tides are out of alignment, meteors plummet from above.",
			"Strange, strange things are happening on the moon.",
			"Soldier, you are the last hope.\n\nSteer your vessel to the heavens above and solve this.",
			"For humanity.\nFor your family.\nFor the ladies.",
			"You are equipped with the latest and greatest ship.",
			"God speed..."};
	 
	private int posInArray=0,posInCurrentString=0,
			newCharInterval=50;
	private boolean allCharsDisplayed=false;
	   
	//Handler and runnable to display one extra character every newCharInterval
    Handler introHandler = new Handler();
    KillableRunnable showNewCharsRunnable = new KillableRunnable() {
		@Override
		public void doWork() {
			posInCurrentString++;
			String currentStringInArray = introText[posInArray];
			
			//if position in current string is less than length, display another char from the string and repost runnable. Otherwise, flag all chars displayed and do not repost
			if(posInCurrentString<=currentStringInArray.length()){
				String displayedString = currentStringInArray.substring(0,posInCurrentString);
				text.setText(displayedString);
				
				introHandler.postDelayed(this, newCharInterval);
			}else{
				if(posInArray==(introText.length-1)){
					next.setText("Begin");
				}
				allCharsDisplayed=true;
				introHandler.removeCallbacks(showNewCharsRunnable);
			}
		}
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_intro);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		text = (TextView)findViewById(R.id.intro_text);
		next = (Button)findViewById(R.id.intro_btn_next);
		next.setOnClickListener(this);
		
		introHandler.post(showNewCharsRunnable);
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.intro_btn_next){
			if(allCharsDisplayed){
				if(posInArray==(introText.length-1)){//start game
					Log.d("lowrey","wtf");
					finish();
					Intent i= new Intent(this, GameActivity.class);
					startActivity(i);
				}else{//increment position in array and post displayNewChars runnable
					posInArray++;
					allCharsDisplayed=false;
					posInCurrentString=0;
					
					introHandler.post(showNewCharsRunnable);
				}
			}else{//display all characters
				if(posInArray==(introText.length-1)){//start game					 
					next.setText("Begin");
				}
				introHandler.removeCallbacks(showNewCharsRunnable);
				text.setText(introText[posInArray]);
				allCharsDisplayed=true;
			}
		}
		
	}
}
