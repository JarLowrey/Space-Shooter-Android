package com.jtronlabs.new_proj;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

import com.jtronlabs.views.ProjectileView;
import com.jtronlabs.views.RocketView;
import com.jtronlabs.views.SideToSideMovingShooter;

public class GameActivity extends Activity implements OnClickListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView btn_taps_text,gameWindowOverlay;
	private RocketView rocket;
	private ImageView rocket_exhaust;
	private RelativeLayout btnBackground;

	public static RelativeLayout gameScreen;
	public static ArrayList<ProjectileView> dangerousObjects=new ArrayList<ProjectileView>();
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//TIMER (by using threads via handler/runner)
    Handler gameHandler = new Handler();
    Runnable mainGameLoopRunnable = new Runnable() {

        @Override
        public void run() {
        	for(int i=dangerousObjects.size()-1;i>=0;i--){
        		ProjectileView enemy = dangerousObjects.get(i);
        		if(rocket.collisionDetection(enemy)){
        			rocket.takeDamage(enemy.getDamage());
        			enemy.removeView(true);
        			dangerousObjects.remove(i);
        		}
        	}
            gameHandler.postDelayed(this, 50);
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
//		rocket.threshold=heightPixels/8;
		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		        gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        //have a little portion of the rocket poking out above the bottom
				rocket.threshold=btnBackground.getY()
						-rocket.getHeight()/4;
		    } 
		});
		
		//set up the game
		levelInfo = new Levels();
		enemyFactory = new EnemyFactory(this);
		resetGame();
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
					rocket.move(ProjectileView.UP,false);
				}else{
					rocket.move(ProjectileView.LEFT,false);
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
					rocket.move(ProjectileView.UP,false);
				}else{
					rocket.move(ProjectileView.RIGHT,false);
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
        rocket.cleanUpThreads();
        enemyFactory.cleanUpThreads();
        gameHandler.removeCallbacks(mainGameLoopRunnable);
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
        rocket.restartThreads();
        enemyFactory.restartThreads();
        gameHandler.post(mainGameLoopRunnable);
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
	
	/**
	 * Reset all static variables, so that the game can be run multiple times
	 */
	private void resetGame(){
		levelInfo.reset();
		gameScreen = (RelativeLayout)findViewById(R.id.gameScreen);

		for(ProjectileView a:dangerousObjects){//not sure if this looping is needed, but just double checking everything was removed from memory
			a.removeView(false);
		}
		dangerousObjects=new ArrayList<ProjectileView>();
		
		for(SideToSideMovingShooter a:SideToSideMovingShooter.allSideToSideShooters){
			a.removeView(false);
		}
		SideToSideMovingShooter.allSideToSideShooters = new ArrayList<SideToSideMovingShooter>();
	}
}
