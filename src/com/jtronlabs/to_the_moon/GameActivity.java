package com.jtronlabs.to_the_moon;

import java.util.ArrayList;

import levels.CollisionDetector;
import levels.LevelSystem;
import parents.Moving_ProjectileView;
import android.app.Activity;
import android.os.Bundle;
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
import enemies.EnemyView;
import enemies.Shooting_ArrayMovingView;
import friendlies.FriendlyView;
import friendlies.ProtagonistView;

public class GameActivity extends Activity implements OnTouchListener{

	//VIEWS
	private static Button btnMoveLeft,btnMoveRight,btnShoot,
		btnIncBulletDmg,btnIncBulletVerticalSpeed,btnIncBulletFreq,btnIncScoreWeight,btnNewGun,btnHeal,btnPurchaseFriend,btnNextLevel;
//	private TextView gameWindowOverlay;
	private static TextView ammoText;
	private static ProgressBar healthBar;
	public static ProtagonistView protagonist;
	public static ImageView rocketExhaust;
//	private RelativeLayout btnBackground;
	private static RelativeLayout gameLayout,storeLayout;

	public static ArrayList<FriendlyView> friendlies=new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies=new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses=new ArrayList<BonusView>();
	
	//MODEL
	private static LevelSystem levelFactory;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		//set up Gameplay Views and listeners and layouts
		btnMoveLeft= (Button)findViewById(R.id.btn_move_left); 
		btnMoveRight= (Button)findViewById(R.id.btn_move_right);
		btnShoot = (Button)findViewById(R.id.btn_shoot);
		btnMoveLeft.setOnTouchListener(this);
		btnMoveRight.setOnTouchListener(this); 
		btnShoot.setOnTouchListener(this);
//		rocket_exhaust = (ImageView)findViewById(R.id.rocket_exhaust); 
		gameLayout=(RelativeLayout)findViewById(R.id.gameplay_layout);
		ammoText = (TextView)findViewById(R.id.ammo);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) ProtagonistView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		protagonist = (ProtagonistView)findViewById(R.id.rocket_game);
		friendlies.add(protagonist);
		
		//set up Store View and listeners
		storeLayout = (RelativeLayout)findViewById(R.id.store_layout);
		btnIncBulletDmg= (Button)findViewById(R.id.btn_inc_bullet_dmg); 
		btnIncBulletVerticalSpeed= (Button)findViewById(R.id.btn_inc_bullet_speed); 
		btnIncBulletFreq= (Button)findViewById(R.id.btn_inc_bullet_freq); 
		btnIncScoreWeight= (Button)findViewById(R.id.btn_inc_score_weight); 
		btnNewGun= (Button)findViewById(R.id.btn_new_gun); 
		btnHeal= (Button)findViewById(R.id.btn_heal); 
		btnPurchaseFriend= (Button)findViewById(R.id.btn_purchase_friend); 
		btnNextLevel= (Button)findViewById(R.id.start_next_level); 
		
		btnIncBulletDmg.setOnTouchListener(this); 
		btnIncBulletVerticalSpeed.setOnTouchListener(this); 
		btnIncBulletFreq.setOnTouchListener(this); 
		btnIncScoreWeight.setOnTouchListener(this); 
		btnHeal.setOnTouchListener(this); 
		btnPurchaseFriend.setOnTouchListener(this); 
		btnNextLevel.setOnTouchListener(this); 
		
		//set up the game
		levelFactory = new LevelSystem(this,gameLayout);
		
		//start the game
		ViewTreeObserver vto = gameLayout.getViewTreeObserver(); //Use a listener to find position of btnBackground afte Views have been drawn. This pos is used as rocket's gravity threshold
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() {
		    	gameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	levelFactory.nextLevel();
		    	CollisionDetector.startDetecting();
		    } 
		});
		
	}
	@Override
    public void onPause() {
        super.onPause();
        
        for(int i=0;i<enemies.size();i++){
        	((Moving_ProjectileView)enemies.get(i)).removeCallbacks(null);
        }
        protagonist.removeCallbacks(null);
//        levelFactory.stopSpawning();
    	CollisionDetector.stopDetecting();
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
        for(int i=0;i<enemies.size();i++){
        	enemies.get(i).restartThreads();
//        	enemies.get(i).refreshDrawableState();
        }
    	CollisionDetector.startDetecting();
        protagonist.restartThreads();
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
	
	public static void gameOver(){
		levelFactory.stopLevelSpawning();
		healthBar.setProgress(0);
		ammoText.setVisibility(View.GONE);
		
		//remove OnClickListeners
		btnMoveLeft.setOnTouchListener(null);
		btnMoveRight.setOnTouchListener(null);
		btnShoot.setOnTouchListener(null);
		
//		levelFactory.stopSpawning();
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeGameObject();//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up static variables
		enemies=new ArrayList<EnemyView>();
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		
	}
	
	public static void openStore(){
		storeLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.VISIBLE);
	}
	
	private static void closeStore(){
		storeLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.VISIBLE);
		levelFactory.nextLevel();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			switch(v.getId()){
				case R.id.btn_move_left:
					protagonist.stopMoving();
					protagonist.beginMoving(Moving_ProjectileView.LEFT);
					break; 
				case R.id.btn_move_right:
					protagonist.stopMoving();
					protagonist.beginMoving(Moving_ProjectileView.RIGHT);
					break;
				case R.id.btn_shoot:
					protagonist.startShooting();	
					break;
			}
		}else if(event.getAction()==MotionEvent.ACTION_UP){
			switch(v.getId()){
				case R.id.btn_move_left:
						protagonist.stopMoving();
					break; 
				case R.id.btn_move_right:
						protagonist.stopMoving();
					break;
				case R.id.btn_shoot:
					protagonist.stopShooting();	
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
		healthBar.setProgress((int) protagonist.getHealth());
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
