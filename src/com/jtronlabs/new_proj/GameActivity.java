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

import com.jtronlabs.views.GameObject;
import com.jtronlabs.views.ProjectileView;
import com.jtronlabs.views.RocketView;
import com.jtronlabs.views.ShootingView;
import com.jtronlabs.views.SideToSideMovingShooter;

public class GameActivity extends Activity implements OnClickListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView btn_taps_text,gameWindowOverlay;
	private RocketView rocket;
	private ImageView rocket_exhaust;
	private RelativeLayout btnBackground;
	private RelativeLayout gameScreen;
	
	
	public static ArrayList<GameObject> enemies=new ArrayList<GameObject>();
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//TIMER (by using threads via handler/runner)
    Handler gameHandler = new Handler();
    Runnable mainGameLoopRunnable = new Runnable() {

        @Override
        public void run() {
        	for(int i=enemies.size()-1;i>=0;i--){
        		/*			COLLISION DETECTION			*/
        		boolean enemyDies=false,protagonistDies=false;
        		ProjectileView projectileCastedEnemy = (ProjectileView)enemies.get(i);
        		
        		//if enemy can shoot, check if its bullets have hit the protagonist
        		if(enemies.get(i) instanceof ShootingView){
        			ArrayList<ProjectileView> enemyBullets = ((ShootingView) enemies.get(i)).myBullets;
        			
        			for(int j=enemyBullets.size()-1;j>=0;j--){
        				ProjectileView bullet = enemyBullets.get(j);
        				if(rocket.collisionDetection(bullet)){//bullet collided with rocket
        					//rocket is damaged
                			protagonistDies = rocket.takeDamage(bullet.getDamage());
                			if(protagonistDies){gameOver();return;}
                			//remove the bullet from the game
                			enemyBullets.remove(j);
                			bullet.cleanUpThreads();
                			bullet.removeView(true);
                		}
        			}
        		}
        		
    			//check if the enemy itself has collided with the  protagonist
        		if(rocket.collisionDetection(projectileCastedEnemy)){
        			protagonistDies = rocket.takeDamage(projectileCastedEnemy.getDamage());
        			if(protagonistDies){gameOver();return;}
        			
        			enemies.get(i).removeView(false);
        		}
        		
        		//check if protagonist's bullets have hit the enemy
    			ArrayList<ProjectileView> protagonistBullets = rocket.myBullets;
    			for(int j=protagonistBullets.size()-1;j>=0;j--){
    				boolean stopCheckingIfProtagonistBulletsHitEnemy=false;
    				ProjectileView bullet = protagonistBullets.get(j);
    				
    				if(bullet.collisionDetection(projectileCastedEnemy)){//bullet collided with rocket
    					//enemy is damaged
            			enemyDies = projectileCastedEnemy.takeDamage(bullet.getDamage());
            			//remove the bullet from the game
            			protagonistBullets.remove(j);
            			bullet.cleanUpThreads();
            			bullet.removeView(true);
            			/*
            			 * only one bullet can hit a specific enemy at once. 
            			 * If that enemy were to die, then checking to see if the other bullets hit 
            			 * him will cause a NullPointerException. Also, breaking here saves resources.
            			 */
            			stopCheckingIfProtagonistBulletsHitEnemy=true;
            		}
    				if(stopCheckingIfProtagonistBulletsHitEnemy){
            			break;
    				}
    			}    			
        	}

			/*			DO OTHER STUFF 		*/
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
				rocket.lowestPositionOnScreen=btnBackground.getY()
						-rocket.getHeight()/4;
		    } 
		});
		
		//set up the game
		levelInfo = new Levels();
		enemyFactory = new EnemyFactory(this,gameScreen);
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
        for(int i=0;i<enemies.size();i++){
        	enemies.get(i).cleanUpThreads();
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
        for(int i=0;i<enemies.size();i++){
        	enemies.get(i).restartThreads();
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
	
	private void gameOver(){
		//clean up all threads
		gameHandler.removeCallbacks(mainGameLoopRunnable);
		enemyFactory.cleanUpThreads();
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeView(false);//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<GameObject>();
		SideToSideMovingShooter.allSideToSideShooters = new ArrayList<SideToSideMovingShooter>();
	}
}
