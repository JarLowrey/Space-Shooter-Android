package com.jtronlabs.to_the_moon;

import interfaces.Shooter;

import java.util.ArrayList;

import levels.Factory_ScriptedLevels;
import levels.LevelSystem;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bonuses.BonusView;
import bullets.BulletView;
import enemies.EnemyView;
import enemies.Shooting_ArrayMovingView;
import friendlies.FriendlyView;
import friendlies.ProtagonistView;

public class GameActivity extends Activity implements OnTouchListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
//	private TextView gameWindowOverlay;
	private static TextView ammoText;
	private static ProgressBar healthBar;
	public static ProtagonistView rocket;
	public static ImageView rocketExhaust;
//	private RelativeLayout btnBackground;
	private RelativeLayout gameScreen;

	public static ArrayList<FriendlyView> friendlies=new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies=new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses=new ArrayList<BonusView>();
	
	//MODEL
	private LevelSystem levelFactory;
	
	//MainGameLoop
    Handler gameHandler = new Handler();
   
    Runnable collisionDetectionRunnable = new Runnable() { 

        @Override
        public void run() {
        	if( ! Factory_ScriptedLevels.isLevelCompleted() && enemies.size() !=0){
	        	for(int k=friendlies.size()-1;k>=0;k--){
	        		FriendlyView friendly = friendlies.get(k);
	        		boolean isProtagonist = friendlies.get(k) == rocket;
	        		
	        		boolean friendlyIsAShooter = friendlies.get(k) instanceof Shooter;
	        		
		        	for(int i=enemies.size()-1;i>=0;i--){
		        		/*			COLLISION DETECTION			*/
		        		boolean enemyDies=false,friendlyDies=false;	        		
		        		EnemyView enemy = enemies.get(i);
		        		boolean enemyIsAShooter = enemy instanceof Shooter;
	
		        		//check enemy's bullets
		        		if(enemyIsAShooter){
			        		Shooter enemyShooter = (Shooter)enemy;
			        		
			        			ArrayList<BulletView> enemyBullets = enemyShooter.getMyBullets();
			        			
			        			for(int j=enemyBullets.size()-1;j>=0;j--){
			        				BulletView bullet = enemyBullets.get(j);
			        				if(friendly.collisionDetection(bullet)){//bullet collided with rocket
			        					//rocket is damaged
			                			friendlyDies = friendly.takeDamage(bullet.getDamage());
			                			if(friendlyDies && isProtagonist){gameOver();return;}
			                			else if(isProtagonist){healthBar.setProgress((int) friendly.getHealth());}
			                			
			                			
			                			bullet.removeGameObject();
			                		}
			        			}
			        			
		        		}
		        		
		    			//check if the enemy itself has collided with the  friendly
		        		if(enemy.getHealth()>0 && friendly.collisionDetection(enemy)){
		        			friendlyDies = friendly.takeDamage(enemy.getDamage());
		        			if(friendlyDies && isProtagonist){gameOver();return;}
		        			else if(isProtagonist){healthBar.setProgress((int) friendly.getHealth());}
		        			
		        			enemyDies=enemy.takeDamage(friendly.getDamage());
		        			if(enemyDies){
		        				LevelSystem.incrementScore(enemy.getScoreForKilling());
		        			}
		        		}
	
	//    				Log.d("lowrey","enemy);
		        		//check if friendly's bullets have hit the enemy
		        		if(enemy.getHealth()>0 && friendlyIsAShooter){
			        		Shooter friendlyShooter= (Shooter)friendlies.get(k);
			    			ArrayList<BulletView> friendlysBullets = friendlyShooter.getMyBullets();
			    			for(int j=friendlysBullets.size()-1;j>=0;j--){
			    				boolean stopCheckingIfFriendlysBulletsHitEnemy=false;
			    				BulletView bullet = friendlysBullets.get(j);
			    				
			    				if(bullet.collisionDetection(enemy)){//bullet collided with rocket
			    					//enemy is damaged
			            			enemyDies = enemy.takeDamage(bullet.getDamage());
			            			if(enemyDies){
			            				LevelSystem.incrementScore(enemy.getScoreForKilling());
			            			}
			            			
			            			bullet.removeGameObject();
			            			/*
			            			 * only one bullet can hit a specific enemy at once. 
			            			 * If that enemy were to die, then checking to see if the other bullets hit 
			            			 * him wastes resources and may cause issues.
			            			 */
			            			stopCheckingIfFriendlysBulletsHitEnemy=true;
			            		}
			    				if(stopCheckingIfFriendlysBulletsHitEnemy){
			            			break;
			    				}
			    			}
		        		}
		        	}
		        	//enemies loop is over
		
		
		        	if(friendlyIsAShooter){
		        		Shooter friendlyShooter= (Shooter)friendlies.get(k);
			        	for(int i=bonuses.size()-1;i>=0;i--){
			        		BonusView beneficialCastedView = (BonusView)bonuses.get(i);
			        		if(friendly.collisionDetection(beneficialCastedView)){
			        			if(isProtagonist){
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
			        			}else if(friendlyIsAShooter){
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
				        		}else{
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
			        			}
			        		}
			        	}
		        	}
	        	}
				/*			DO OTHER STUFF 		*/
	            gameHandler.postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
        	}else{
        		openStore();
        	}
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
//		gameWindowOverlay=(TextView)findViewById(R.id.game_background);
//		btnBackground=(RelativeLayout)findViewById(R.id.btn_background);
		ammoText = (TextView)findViewById(R.id.ammo);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) ProtagonistView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		
		//set up rocket
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		rocket = (ProtagonistView)findViewById(R.id.rocket_game);
		friendlies.add(rocket);
		
		//set up the game
		levelFactory = new LevelSystem(this,gameScreen);
		
		//start the game
		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() {
		    	gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	levelFactory.nextLevel();
		    } 
		});
		
	}
/*	previous onclick method
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
					
					//pause gravity and move the rocket
					rocket.stopGravity();
					rocket.move(ProjectileView.UP);
					rocket.startGravity();
				}else{
					rocket.move(ProjectileView.LEFT);
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
					
					//pause gravity and move the rocket
					rocket.stopGravity();
					rocket.move(ProjectileView.UP);
					rocket.startGravity();
				}else{
					rocket.move(ProjectileView.RIGHT);
				}
				break;
			case R.id.btnMiddle:
				rocket.spawnLevelOneCenteredBullet();
				break;
		}
		
		int id = levelInfo.incrementLevel();
		if(id>0){
			changeGameBackgroundImage(id);
		}
	}
	*/
	
	@Override
    public void onPause() {
        super.onPause();
        
        for(int i=0;i<enemies.size();i++){
        	((Moving_ProjectileView)enemies.get(i)).removeCallbacks(null);
        }
        rocket.removeCallbacks(null);
//        levelFactory.stopSpawning();
        gameHandler.removeCallbacks(collisionDetectionRunnable);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
        for(int i=0;i<enemies.size();i++){
        	enemies.get(i).restartThreads();
//        	enemies.get(i).refreshDrawableState();
        }
        rocket.restartThreads();
//        levelFactory.beginSpawning();
        gameHandler.post(collisionDetectionRunnable);
	}
	
	/*
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
	*/
	
	private void gameOver(){
		levelFactory.stopLevelSpawning();
		healthBar.setProgress(0);
		ammoText.setVisibility(View.GONE);
		
		//remove OnClickListeners
		btnLeft.setOnTouchListener(null);
		btnRight.setOnTouchListener(null);
		btnMiddle.setOnTouchListener(null);
		
		//clean up all threads
		gameHandler.removeCallbacks(collisionDetectionRunnable);
//		levelFactory.stopSpawning();
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeGameObject();//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<EnemyView>();
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		
	}
	
	private void openStore(){
		
	}
	
	private void closeStore(){
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			switch(v.getId()){
				case R.id.btnLeft:
					rocket.stopMoving();
					rocket.beginMoving(Moving_ProjectileView.LEFT);
					break; 
				case R.id.btnRight:
					rocket.stopMoving();
					rocket.beginMoving(Moving_ProjectileView.RIGHT);
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
				case R.id.btn_heal:
					Log.d("lowrey","healed");
					break;
				case R.id.btn_inc_bullet_dmg:
					break;
				case R.id.btn_inc_bullet_freq:
					break;
				case R.id.btn_inc_bullet_speed:
					break;
				case R.id.btn_inc_score_weight:
					break;
				case R.id.btn_new_gun:
					break;
				case R.id.btn_purchase_friend:
					break;
			}
		} 
		return false;
	}
	
	public static void setHealthBar(){
		healthBar.setProgress((int) rocket.getHealth());
	}
	
	public static void setAmmoText(int ammo){
		if(ammo<=0){
			ammoText.setVisibility(View.GONE);
		}
		ammoText.setText(ammo+"");
	}
	
	public static void showAmmoText(int ammo){
		ammoText.setVisibility(View.VISIBLE);
		setAmmoText(ammo);
	}
}
