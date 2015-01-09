package com.jtronlabs.to_the_moon;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jtronlabs.bonuses.BonusView;
import com.jtronlabs.enemy_types.EnemyView;
import com.jtronlabs.enemy_types.Shooting_ArrayMovingView;
import com.jtronlabs.friendly_types.FriendlyView;
import com.jtronlabs.friendly_types.RocketView;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon.game_state.EnemyFactory;
import com.jtronlabs.to_the_moon.game_state.Levels;
import com.jtronlabs.to_the_moon.parents.Moving_ProjectileView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class GameActivity extends Activity implements OnTouchListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView text_score,gameWindowOverlay;
	private static TextView ammoText;
	private static ProgressBar healthBar;
	public static RocketView rocket;
	public static ImageView rocketExhaust;
//	private RelativeLayout btnBackground;
	private RelativeLayout gameScreen;

	public static ArrayList<FriendlyView> friendlies=new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies=new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses=new ArrayList<BonusView>();
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//MainGameLoop
    Handler gameHandler = new Handler();
    Runnable mainGameLoopRunnable = new Runnable() {

        @Override
        public void run() {
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
//        				Log.d("lowrey","enemyShooter");
	        			if(enemyShooter.getGun() != null){
//	        				Log.d("lowrey","enemyGunNotNull");
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
	        		}
	        		
	    			//check if the enemy itself has collided with the  friendly
	        		if(enemy.getHealth()>0 && friendly.collisionDetection(enemy)){
	        			friendlyDies = friendly.takeDamage(enemy.getDamage());
	        			if(friendlyDies && isProtagonist){gameOver();return;}
	        			else if(isProtagonist){healthBar.setProgress((int) friendly.getHealth());}
	        			
	        			enemyDies=enemy.takeDamage(friendly.getDamage());
	        			if(enemyDies){
	        				levelInfo.incrementScore(enemy.getScoreForKilling());
	        				text_score.setText(""+levelInfo.getScore());
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
		            				levelInfo.incrementScore(enemy.getScoreForKilling());
		            				text_score.setText(""+levelInfo.getScore());
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
//		btnBackground=(RelativeLayout)findViewById(R.id.btn_background);
		text_score=(TextView)findViewById(R.id.score_textview);
		ammoText = (TextView)findViewById(R.id.ammo);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) RocketView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		
		//set up rocket
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		rocket = (RocketView)findViewById(R.id.rocket_game);
		friendlies.add(rocket);
//		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//		    @Override 
//		    public void onGlobalLayout() { 
//		        gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//		        //have a little portion of the rocket poking out above the bottom
//				rocket.setThreshold((int) (btnBackground.getY()-GameActivity.this.getResources().getDimension(R.dimen.activity_margin_small)));
//		    } 
//		});
		
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
        
        for(int i=0;i<enemies.size();i++){
        	((Moving_ProjectileView)enemies.get(i)).removeCallbacks(null);
        }
        ((Moving_ProjectileView)rocket).removeCallbacks(null);
        enemyFactory.stopSpawning();
        gameHandler.removeCallbacks(mainGameLoopRunnable);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
        for(int i=0;i<enemies.size();i++){
        	enemies.get(i).restartThreads();
        }
        rocket.restartThreads();
        enemyFactory.beginSpawning();
        gameHandler.post(mainGameLoopRunnable);
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
		ammoText.setVisibility(View.GONE);
		
		//remove OnClickListeners
		btnLeft.setOnTouchListener(null);
		btnRight.setOnTouchListener(null);
		btnMiddle.setOnTouchListener(null);
		
		//clean up all threads
		gameHandler.removeCallbacks(mainGameLoopRunnable);
		enemyFactory.stopSpawning();
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeGameObject();//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<EnemyView>();
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		Shooting_ArrayMovingView.stopMovingAllShooters();
		
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
