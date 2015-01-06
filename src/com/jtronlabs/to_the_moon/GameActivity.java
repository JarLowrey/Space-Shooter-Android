package com.jtronlabs.to_the_moon;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jtronlabs.to_the_moon.bullet_views.BulletView;
import com.jtronlabs.to_the_moon.misc.EnemyFactory;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.misc.Levels;
import com.jtronlabs.to_the_moon.misc.ProjectileView;
import com.jtronlabs.to_the_moon.ship_views.Shooting_MovingArrayView;
import com.jtronlabs.to_the_moon.ship_views.RocketView;
import com.jtronlabs.to_the_moon.ship_views.Gravity_ShootingView;

public class GameActivity extends Activity implements OnTouchListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView text_score,gameWindowOverlay;
	private RocketView rocket;
//	private ImageView rocket_exhaust;
	private RelativeLayout btnBackground;
	private RelativeLayout gameScreen;
	private ProgressBar healthBar;
	
	public static ArrayList<GameObjectInterface> enemies=new ArrayList<GameObjectInterface>();
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//MainGameLoop
    Handler gameHandler = new Handler();
    Runnable mainGameLoopRunnable = new Runnable() {

        @Override
        public void run() {
        	for(int i=enemies.size()-1;i>=0;i--){
        		/*			COLLISION DETECTION			*/
        		boolean enemyDies=false,protagonistDies=false;
        		ProjectileView projectileCastedEnemy = (ProjectileView)enemies.get(i);
        		
        		//if enemy can shoot, check if its bullets have hit the protagonist
        		if(enemies.get(i) instanceof Gravity_ShootingView){
        			ArrayList<BulletView> enemyBullets = ((Gravity_ShootingView) enemies.get(i)).myBullets;
        			
        			for(int j=enemyBullets.size()-1;j>=0;j--){
        				BulletView bullet = enemyBullets.get(j);
        				if(rocket.collisionDetection(bullet)){//bullet collided with rocket
        					//rocket is damaged
                			protagonistDies = rocket.takeDamage(bullet.getDamage());
                			if(protagonistDies){gameOver();return;}
                			else{healthBar.setProgress((int) rocket.getHealth());}
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
        			else{healthBar.setProgress((int) rocket.getHealth());}
        			
        			enemyDies=projectileCastedEnemy.takeDamage(rocket.getDamage());
        			if(enemyDies){
        				levelInfo.incrementScore(projectileCastedEnemy.getScoreForKilling());
        				text_score.setText(""+levelInfo.getScore());
        			}
        		}
        		
        		//check if protagonist's bullets have hit the enemy
    			ArrayList<BulletView> protagonistBullets = rocket.myBullets;
    			for(int j=protagonistBullets.size()-1;j>=0;j--){
    				boolean stopCheckingIfProtagonistBulletsHitEnemy=false;
    				BulletView bullet = protagonistBullets.get(j);
    				
    				if(bullet.collisionDetection(projectileCastedEnemy)){//bullet collided with rocket
    					//enemy is damaged
            			enemyDies = projectileCastedEnemy.takeDamage(bullet.getDamage());
            			if(enemyDies){
            				levelInfo.incrementScore(projectileCastedEnemy.getScoreForKilling());
            				text_score.setText(""+levelInfo.getScore());
            			}
            			
            			//remove the bullet from the game
            			protagonistBullets.remove(j);
            			bullet.cleanUpThreads();
            			bullet.removeView(true);
            			/*
            			 * only one bullet can hit a specific enemy at once. 
            			 * If that enemy were to die, then checking to see if the other bullets hit 
            			 * him wastes resources and may cause issues.
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
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		//set up Views and OnClickListeners and layouts
		btnLeft= (Button)findViewById(R.id.btnLeft); 
		btnLeft.setOnTouchListener(this);
		btnRight= (Button)findViewById(R.id.btnRight);
		btnRight.setOnTouchListener(this);
		btnMiddle = (Button)findViewById(R.id.btnMiddle);
		btnMiddle.setOnTouchListener(this);
//		rocket_exhaust = (ImageView)findViewById(R.id.rocket_exhaust); 
		gameScreen=(RelativeLayout)findViewById(R.id.gameScreen);
		gameWindowOverlay=(TextView)findViewById(R.id.game_background);
		btnBackground=(RelativeLayout)findViewById(R.id.btn_background);
		text_score=(TextView)findViewById(R.id.score_textview);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) RocketView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		
		//set up rocket
		rocket = (RocketView)findViewById(R.id.rocket_game);
//		rocket.threshold=heightPixels/8;
		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		        gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        //have a little portion of the rocket poking out above the bottom
				rocket.lowestPositionThreshold=btnBackground.getY();
		    } 
		});
		
		//set up the game
		levelInfo = new Levels();
		enemyFactory = new EnemyFactory(this,gameScreen);
	}

//	@Override
//	public void onClick(View v) {
//		switch(v.getId()){
//			case R.id.btnLeft:
//				if(levelInfo.rightBtnWasTappedPreviously){
//					//update variables
//					levelInfo.rightBtnWasTappedPreviously=false;
//					levelInfo.incrementNumBtnTaps();
//					
//					//update screen
//					btn_taps_text.setText(""+levelInfo.numBtnTaps());//flash fire behind the rocket
//					if(levelInfo.numBtnTaps()%20==0){			
//						rocket.runRocketExhaust(rocket_exhaust);
//					}
//					
//					//pause gravity and move the rocket
//					rocket.stopGravity();
//					rocket.move(ProjectileView.UP);
//					rocket.startGravity();
//				}else{
//					rocket.move(ProjectileView.LEFT);
//				}
//				break; 
//			case R.id.btnRight:
//				if(!levelInfo.rightBtnWasTappedPreviously){
//					//update variables
//					levelInfo.rightBtnWasTappedPreviously=true;
//					levelInfo.incrementNumBtnTaps();
//					
//					//update Screen
//					btn_taps_text.setText(""+levelInfo.numBtnTaps());//flash fire behind the rocket
//					if(levelInfo.numBtnTaps()%20==0){			
//						rocket.runRocketExhaust(rocket_exhaust);
//					}  
//					
//					//pause gravity and move the rocket
//					rocket.stopGravity();
//					rocket.move(ProjectileView.UP);
//					rocket.startGravity();
//				}else{
//					rocket.move(ProjectileView.RIGHT);
//				}
//				break;
//			case R.id.btnMiddle:
//				rocket.spawnLevelOneCenteredBullet();
//				break;
//		}
//		
//		int id = levelInfo.incrementLevel();
//		if(id>0){
//			changeGameBackgroundImage(id);
//		}
//	}
	
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
		healthBar.setProgress(0);
		
		//remove OnClickListeners
		rocket.stopMoving();
		btnLeft.setOnTouchListener(null);
		btnRight.setOnTouchListener(null);
		btnMiddle.setOnTouchListener(null);
		
		//clean up all threads
		gameHandler.removeCallbacks(mainGameLoopRunnable);
		enemyFactory.cleanUpThreads();
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeView(false);//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<GameObjectInterface>();
		Shooting_MovingArrayView.resetSimpleShooterArray();
		Shooting_MovingArrayView.beginMovingAllShootersInASquare();
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			switch(v.getId()){
				case R.id.btnLeft:
					rocket.stopMoving();
					rocket.beginMoving(ProjectileView.LEFT);
					break; 
				case R.id.btnRight:
					rocket.stopMoving();
					rocket.beginMoving(ProjectileView.RIGHT);
					break;
				case R.id.btnMiddle:
					rocket.startShooting();	
					break;
			}
		}else if(event.getAction()==MotionEvent.ACTION_UP){
			switch(v.getId()){
				case R.id.btnLeft:
						rocket.stopMoving();
					break; 
				case R.id.btnRight:
						rocket.stopMoving();
					break;
				case R.id.btnMiddle:
					rocket.stopShooting();	
					break;
			}
		}
		return false;
	}
}
