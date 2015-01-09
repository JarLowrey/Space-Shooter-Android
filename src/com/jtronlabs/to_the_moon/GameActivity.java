package com.jtronlabs.to_the_moon;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jtronlabs.bonuses.BonusView;
import com.jtronlabs.specific_view_types.RocketView;
import com.jtronlabs.specific_view_types.Shooting_ArrayMovingView;
import com.jtronlabs.to_the_moon.bullets.Projectile_BulletView;
import com.jtronlabs.to_the_moon.misc.EnemyFactory;
import com.jtronlabs.to_the_moon.misc.GameObjectInterface;
import com.jtronlabs.to_the_moon.misc.Levels;
import com.jtronlabs.to_the_moon.views.Gravity_ShootingView;
import com.jtronlabs.to_the_moon.views.ProjectileView;

public class GameActivity extends Activity implements OnTouchListener{

	//VIEWS
	private Button btnLeft, btnRight,btnMiddle;
	private TextView text_score,gameWindowOverlay;
	private static TextView ammoText;
	private static ProgressBar healthBar;
	public static RocketView rocket;
	private RelativeLayout btnBackground;
	private RelativeLayout gameScreen;

	public static ArrayList<GameObjectInterface> friendlies=new ArrayList<GameObjectInterface>();
	public static ArrayList<GameObjectInterface> enemies=new ArrayList<GameObjectInterface>();
	public static ArrayList<GameObjectInterface> bonuses=new ArrayList<GameObjectInterface>();
	
	//MODEL
	private Levels levelInfo;
	private EnemyFactory enemyFactory;
	
	//MainGameLoop
    Handler gameHandler = new Handler();
    Runnable mainGameLoopRunnable = new Runnable() {

        @Override
        public void run() {
        	for(int k=friendlies.size()-1;k>=0;k--){
        		Gravity_ShootingView shooterCastedFriendly = (Gravity_ShootingView)friendlies.get(k);
        		boolean isProtagonist = friendlies.get(k) == rocket;
        		boolean isAShooter = friendlies.get(k) instanceof Gravity_ShootingView;
        		
	        	for(int i=enemies.size()-1;i>=0;i--){
	        		/*			COLLISION DETECTION			*/
	        		boolean enemyDies=false,friendlyDies=false;	        		
	        		ProjectileView projectileCastedEnemy = (ProjectileView)enemies.get(i);
	        		
	        		//if enemy has shoot, check if its bullets have hit the friendly
	        		if(enemies.get(i) instanceof Gravity_ShootingView){
	        			ArrayList<Projectile_BulletView> enemyBullets = ((Gravity_ShootingView) enemies.get(i)).myGun.myBullets;
	        			
	        			for(int j=enemyBullets.size()-1;j>=0;j--){
	        				Projectile_BulletView bullet = enemyBullets.get(j);
	        				if(shooterCastedFriendly.collisionDetection(bullet)){//bullet collided with rocket
	        					//rocket is damaged
	                			friendlyDies = shooterCastedFriendly.takeDamage(bullet.getDamage());
	                			if(friendlyDies && isProtagonist){gameOver();return;}
	                			else if(friendlyDies){shooterCastedFriendly.removeView(true);}
	                			else{healthBar.setProgress((int) shooterCastedFriendly.getHealth());}
	                			
	                			
	                			bullet.removeView(true);
	                		}
	        			}
	        		}
	        		
	    			//check if the enemy itself has collided with the  friendly
	        		if(projectileCastedEnemy.getHealth()>0 && shooterCastedFriendly.collisionDetection(projectileCastedEnemy)){
	        			friendlyDies = shooterCastedFriendly.takeDamage(projectileCastedEnemy.getDamage());
	        			if(friendlyDies && isProtagonist){gameOver();return;}
            			else if(friendlyDies){shooterCastedFriendly.removeView(true);}
	        			else{healthBar.setProgress((int) shooterCastedFriendly.getHealth());}
	        			
	        			enemyDies=projectileCastedEnemy.takeDamage(shooterCastedFriendly.getDamage());
	        			if(enemyDies){
	        				levelInfo.incrementScore(projectileCastedEnemy.getScoreForKilling());
	        				text_score.setText(""+levelInfo.getScore());
	        			}
	        		}
	        		
	        		//check if friendly's bullets have hit the enemy
	        		if(projectileCastedEnemy.getHealth()>0 && isAShooter){
		    			ArrayList<Projectile_BulletView> friendlysBullets = shooterCastedFriendly.myGun.myBullets;
		    			for(int j=friendlysBullets.size()-1;j>=0;j--){
		    				boolean stopCheckingIfFriendlysBulletsHitEnemy=false;
		    				Projectile_BulletView bullet = friendlysBullets.get(j);
		    				
		    				if(bullet.collisionDetection(projectileCastedEnemy)){//bullet collided with rocket
		    					//enemy is damaged
		            			enemyDies = projectileCastedEnemy.takeDamage(bullet.getDamage());
		            			if(enemyDies){
		            				levelInfo.incrementScore(projectileCastedEnemy.getScoreForKilling());
		            				text_score.setText(""+levelInfo.getScore());
		            			}
		            			
		            			bullet.removeView(true);
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
	
	
	        	for(int i=bonuses.size()-1;i>=0;i--){
	        		BonusView beneficialCastedView = (BonusView)bonuses.get(i);
	        		if(shooterCastedFriendly.collisionDetection(beneficialCastedView)){
	        			if(isProtagonist){
		        			beneficialCastedView.applyBenefit((RocketView)friendlies.get(k));
		        			beneficialCastedView.removeView(false);
	        			}else if(isAShooter){
		        			beneficialCastedView.applyBenefit((Gravity_ShootingView)friendlies.get(k));
		        			beneficialCastedView.removeView(false);
		        		}else{
		        			beneficialCastedView.applyBenefit(shooterCastedFriendly);
		        			beneficialCastedView.removeView(false);
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
		btnBackground=(RelativeLayout)findViewById(R.id.btn_background);
		text_score=(TextView)findViewById(R.id.score_textview);
		ammoText = (TextView)findViewById(R.id.ammo);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) RocketView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		
		//set up rocket
		rocket = (RocketView)findViewById(R.id.rocket_game);
		friendlies.add(rocket);
//		rocket.threshold=heightPixels/8;
		ViewTreeObserver vto = gameScreen.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() { 
		        gameScreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        //have a little portion of the rocket poking out above the bottom
				rocket.lowestPositionThreshold= (int) (btnBackground.getY()-GameActivity.this.getResources().getDimension(R.dimen.activity_margin_small));
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
        
        for(int i=0;i<enemies.size();i++){
        	((ProjectileView)enemies.get(i)).removeCallbacks(null);
        }
        ((ProjectileView)rocket).removeCallbacks(null);
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
			enemies.get(i).removeView(false);//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<GameObjectInterface>();
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		Shooting_ArrayMovingView.stopMovingAllShooters();
		
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
					rocket.myGun.startShooting();	
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
					rocket.myGun.stopShooting();	
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
