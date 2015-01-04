package com.jtronlabs.new_proj;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jtronlabs.views.GravityView;
import com.jtronlabs.views.ProjectileView;
import com.jtronlabs.views.RocketView;

public class GameActivity extends Activity implements OnClickListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView btn_taps_text,gameWindowOverlay;
	private RocketView rocket;
	private ImageView rocket_exhaust;
	private RelativeLayout btnBackground;

	public static RelativeLayout gameScreen;
	public static ArrayList<ProjectileView> dangerousObjects=new ArrayList<ProjectileView>();;
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//TIMER (by using threads via handler/runner)
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
        	

            timerHandler.postDelayed(this, 51);
        }
    };
	
	//SCREEN VARIABLES
	private float widthPixels,heightPixels;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
		
		//set up Views and OnClickListeners and layouts
		btnLeft= (Button)findViewById(R.id.btnLeft); 
		btnLeft.setOnClickListener(this);
		btnRight= (Button)findViewById(R.id.btnRight);
		btnRight.setOnClickListener(this);
		btnMiddle = (Button)findViewById(R.id.btnMiddle);
		btnMiddle.setOnClickListener(this);
		rocket_exhaust = (ImageView)findViewById(R.id.rocket_exhaust); 
		gameScreen=(RelativeLayout)findViewById(R.id.gameScreen);
		gameWindowOverlay=(TextView)findViewById(R.id.game_background);
		btnBackground=(RelativeLayout)findViewById(R.id.btn_background);
		btn_taps_text=(TextView)findViewById(R.id.num_btn_taps_textview);
		
		//set up rocket
		rocket = (RocketView)findViewById(R.id.rocket_game);
		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		        gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
				rocket.threshold=btnBackground.getY();
		    } 
		});
		rocket.threshold=btnBackground.getY();
		
		//set up the game
		levelInfo = new Levels();
		enemyFactory = new EnemyFactory(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnLeft:
				if(levelInfo.rightBtnWasTappedPreviously){
					//update variables
					levelInfo.rightBtnWasTappedPreviously=false;
					levelInfo.incrementNumBtnTaps();
					
					//update screen
					btn_taps_text.setText(""+levelInfo.numBtnTaps());//flash fire behind the rocket
					if(levelInfo.numBtnTaps()%20==0){			
						rocket.runRocketExhaust(rocket_exhaust);
					}
					rocket.moveRocketUp();
				}else{
					rocket.moveRocketToSide(true);
				}
				break; 
			case R.id.btnRight:
				if(!levelInfo.rightBtnWasTappedPreviously){
					//update variables
					levelInfo.rightBtnWasTappedPreviously=true;
					levelInfo.incrementNumBtnTaps();
					
					//update Screen
					btn_taps_text.setText(""+levelInfo.numBtnTaps());//flash fire behind the rocket
					if(levelInfo.numBtnTaps()%20==0){			
						rocket.runRocketExhaust(rocket_exhaust);
					}  
					rocket.moveRocketUp();
				}else{
					rocket.moveRocketToSide(false);
				}
				break;
			case R.id.btnMiddle:
				rocket.spawnBullet();
				break;
		}
		
		int id = levelInfo.incrementLevel();
		if(id>0){
			changeGameBackgroundImage(id);
		}
	}
	
	@Override
    public void onPause() {
        super.onPause();
        
        //clean up handlers
//        dangerousSpaceJunkHandler.removeCallbacks(createDangerousSpaceJunkRunnable);
        for(int i=0;i<dangerousObjects.size();i++){
        	dangerousObjects.get(i).cleanUpThreads();
        }
        enemyFactory.cleanUpThreads();
//        timerHandler.removeCallbacks(timerRunnable);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
		//restart handlers. NOTE:: Since onResume() is called with onCreate, this also is responsible for starting the handlers at the beginning of the game
//		dangerousSpaceJunkHandler.postDelayed(createDangerousSpaceJunkRunnable,newSpaceJunkInterval-levelInfo.getLevel()*50);
        for(int i=0;i<dangerousObjects.size();i++){
        	dangerousObjects.get(i).restartThreads();
        }
        enemyFactory.restartThreads();
//	    timerHandler.post(timerRunnable);
	}
	
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
}
