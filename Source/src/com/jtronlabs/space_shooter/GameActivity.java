package com.jtronlabs.space_shooter;

import friendlies.AllyView;
import friendlies.ProtagonistView;
import helpers.GameAlertDialogBuilder;
import helpers.GameTextView;
import helpers.KillableRunnable;
import helpers.MediaController;
import helpers.StoreUpgradeHandler;
import interfaces.GameActivityInterface;

import java.util.ArrayList;
import java.util.List;

import levels.AttributesOfLevels;
import levels.LevelSystem;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import backgroundViews.ParticleBackgroundAnimation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class GameActivity extends Activity implements OnTouchListener, GameActivityInterface{

	private static int offscreenBottom;

	private boolean canBeginShooting=true,beginShootingRunnablePosted=false;
	
	public static final String GAME_STATE_PREFS = "GameStatePrefs",
			STATE_HEALTH="health",
			STATE_RESOURCES="resources",
			STATE_TOTAL_RESOURCES="totalResources",
			STATE_GUN_CONFIG="gunConfig",
			STATE_BULLET_FREQ_LEVEL="bulletFreqLevel",
			STATE_BULLET_DAMAGE_LEVEL="bulletDamageLevel",
			STATE_DEFENCE_LEVEL="defenceLevel",
			STATE_RESOURCE_MULTIPLIER_LEVEL="resourceMultLevel",
			STATE_FRIEND_LEVEL="friendLevel",
			STATE_LEVEL="level";
//			STATE_WAVE="wave";
	
//	private Button btnMoveLeft,btnMoveRight,btnMoveUp,btnMoveDown;
	private ImageButton btnMove,btnShoot;
	private ImageButton	btnIncBulletDmg,btnIncBulletVerticalSpeed,
	btnIncBulletFreq,btnIncScoreWeight,btnNewGun,btnHeal,btnPurchaseFriend,btnNextLevel;
	private GameTextView resourceCount,levelCount,scoreInGame;
	private ProgressBar healthBar,healthBarStore;
	public ProtagonistView protagonist;
	private AllyView ally;
	public ImageView rocketExhaust;
	private RelativeLayout gameLayout,storeLayout;
	private AdView adView;
	
	private boolean isGameOver ;
	private int scoreAtGameOver,levelAtGameOver;
   
	//MODEL     
	private LevelSystem levelCreator;    
	private ParticleBackgroundAnimation stars_creator_game,stars_creator_store;
	
	@Override       
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//set up Gameplay Views and listeners and layouts
//		btnMoveLeft= (Button)findViewById(R.id.btn_move_left); 
//		btnMoveRight= (Button)findViewById(R.id.btn_move_right);
//		btnMoveUp = (Button)findViewById(R.id.btn_move_up);
//		btnMoveDown = (Button)findViewById(R.id.btn_move_down);
	    btnMove = (ImageButton)findViewById(R.id.btn_move);
		btnShoot = (ImageButton)findViewById(R.id.btn_shoot);
//		btnMoveUp.setOnTouchListener(this);
//		btnMoveDown.setOnTouchListener(this);
//		btnMoveLeft.setOnTouchListener(this);
//		btnMoveRight.setOnTouchListener(this); 
		btnMove.setOnTouchListener(this);
		btnShoot.setOnTouchListener(this);
		scoreInGame = (GameTextView)findViewById(R.id.score_in_game);
		gameLayout=(RelativeLayout)findViewById(R.id.gameplay_layout);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBarStore = (ProgressBar)findViewById(R.id.health_bar_store);//technically this is in store
		setHealthBars();
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		
		//set up Store View and listeners
		resourceCount = (GameTextView)findViewById(R.id.resource_count);
		levelCount = (GameTextView)findViewById(R.id.level_count);
		storeLayout = (RelativeLayout)findViewById(R.id.store_layout);
		btnIncBulletDmg= (ImageButton)findViewById(R.id.btn_inc_bullet_dmg); 
		btnIncBulletVerticalSpeed= (ImageButton)findViewById(R.id.btn_inc_defence);  
		btnIncBulletFreq= (ImageButton)findViewById(R.id.btn_inc_bullet_freq); 
		btnIncScoreWeight= (ImageButton)findViewById(R.id.btn_inc_score_weight); 
		btnNewGun= (ImageButton)findViewById(R.id.btn_new_gun); 
		btnHeal= (ImageButton)findViewById(R.id.btn_heal); 
		btnPurchaseFriend= (ImageButton)findViewById(R.id.btn_purchase_friend); 
		btnNextLevel= (ImageButton)findViewById(R.id.start_next_level); 
		
		//Set up misc views and listeners
		ImageButton accept = (ImageButton)findViewById(R.id.gameOverAccept);
		accept.setOnTouchListener(this);
		ImageButton share = (ImageButton)findViewById(R.id.gameOverShare);
		share.setOnTouchListener(this);
		
		btnIncBulletDmg.setOnTouchListener(this); 
		btnIncBulletVerticalSpeed.setOnTouchListener(this); 
		btnIncBulletFreq.setOnTouchListener(this); 
		btnIncScoreWeight.setOnTouchListener(this); 
		btnHeal.setOnTouchListener(this); 
		btnPurchaseFriend.setOnTouchListener(this); 
		btnNextLevel.setOnTouchListener(this); 
		btnNewGun.setOnTouchListener(this); 
		
		//set up control panel
		RelativeLayout controlPanel = (RelativeLayout)findViewById(R.id.control_panel);
		offscreenBottom = (int) MainActivity.getHeightPixels() - controlPanel.getLayoutParams().height ;
		
		//set up the game
		levelCreator = new LevelSystem(gameLayout);
		isGameOver = false;
		
		//onResume() is called after onCreate, so needed setup is done there
		stars_creator_game = new ParticleBackgroundAnimation(gameLayout,ParticleBackgroundAnimation.DEFAULT_NUM_STARS);
		stars_creator_store = new ParticleBackgroundAnimation(storeLayout,
				(int) (1.5*ParticleBackgroundAnimation.DEFAULT_NUM_STARS));
		createAdViewInStore();
	}
	
	public static int getBottomScreen(){
		return offscreenBottom;
	} 
	/**
	 * Pause game. Eventually, state will need to be saved to database, as after on pause any
	 * variables are liable for Android garbage collection!
	 */
	@Override
    public void onPause() {
        super.onPause();
        stars_creator_game.stopSpawningStars();
        stars_creator_store.stopSpawningStars();

        
        scoreInGame.setVisibility(View.GONE);
        
        KillableRunnable.killAll();
		levelCreator.pauseLevel();
		
		//protagonist attributes saved when he is removeGameObject() in pauseLevel
		//store upgrades are saved straight to persistent storage when bought (so does not need to be saved here).
		//level attributes are saved in the levelSystem
		//gameover() resets variables in method
		
		MediaController.stopLoopingSound();
		MediaController.stopNonLoopingSound();
		
		adView.pause();
		Log.d("lowrey","num enemies on pause = "+levelCreator.enemies.size());
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }
	
	@Override
	public void onResume(){
		super.onResume();
		
        scoreInGame.setVisibility(View.VISIBLE);
		
		//allow shooting to begin after resuming from any pause
		canBeginShooting=true;
		beginShootingRunnablePosted=false;
		
		//I have NO IDEA why, but enemies can exist onResume(). These enemies are gone onPause(), and all debugging has failed thus 
		//far. Simple workaround is to remove these misplaced enemies
		for(int i=levelCreator.enemies.size()-1;i>=0;i--){
			levelCreator.enemies.get(i).removeGameObject();
		}
				
		levelCreator.loadScoreAndLevel();//need to reload variables first thing

		scoreInGame.setText(MainActivity.formatInt(levelCreator.getResourceCount() ) );
		adView.resume();
		
		//don't open the store up on the initial level
		if(isGameOver){
			/*
			 * do nothing. Game over layout should be up. If game is left open on GameOverLayout the onDestroy() method may be
			 * called, thus resetting everything and leading to onCreate being called again. isGameOver will be set to false and
			 * this if statement will be skipped.
			*/
		}else if(levelCreator.getLevel()==0){
			createNewProtagonistView();
			stars_creator_game.startSpawningStars();
			levelCreator.resumeLevel(this);
		}else{
			openStore();
		}
	}
	
	public void gameOver(){
		SharedPreferences gameState = getSharedPreferences(GAME_STATE_PREFS, 0);
		
		final int score = gameState.getInt(STATE_TOTAL_RESOURCES, 0)  + levelCreator.scoreGainedThisLevel(); 
		final int lvl = levelCreator.getLevel() - 1;
		final String title = "GAME OVER";
		String msg = this.getResources().getString(R.string.good_job);;

		//set images and msg
		ImageView medal = (ImageView)findViewById(R.id.medal_image);
		if(levelCreator.getLevel() < AttributesOfLevels.LEVELS_LOW  ){
			msg = this.getResources().getString(R.string.need_to_improve);
			medal.setImageResource(R.drawable.medal_bronze);
		}else if(levelCreator.getLevel() < AttributesOfLevels.LEVELS_HIGH ){
			medal.setImageResource(R.drawable.medal_silver);			
		}else{
			medal.setImageResource(R.drawable.medal_gold);
		}
		
		SharedPreferences gameMeta = getSharedPreferences(MainActivity.GAME_META_DATA_PREFS,0);
		SharedPreferences.Editor editor = gameMeta.edit();
		if(score > gameMeta.getInt(MainActivity.HIGHEST_SCORE, 0) ){
			editor.putInt(MainActivity.HIGHEST_SCORE, score);
			msg = this.getResources().getString(R.string.set_new_pb);
		}
		if(lvl > gameMeta.getInt(MainActivity.MAX_LEVEL, 0)){
			editor.putInt(MainActivity.MAX_LEVEL, lvl);			
			msg =  this.getResources().getString(R.string.set_new_pb);
		}
		editor.commit();
		
		if( levelCreator.getLevel() < AttributesOfLevels.LEVELS_HIGH){			
			MediaController.playSoundClip(this, R.raw.jingle_lose, false);
		}else{
			MediaController.playSoundClip(this, R.raw.jingle_win, false);
		}
		
		//set the variables used for the sharing message
		scoreAtGameOver=score;
		levelAtGameOver=lvl;
		
		healthBar.setProgress(0);	
		
		isGameOver = true;
		MediaController.stopLoopingSound();
		
		KillableRunnable.killAll(); 
		levelCreator.pauseLevel(); 
		gameOverAndResetSavedVariables();
		
		//set text
		RelativeLayout gameOverLayout = (RelativeLayout)findViewById(R.id.gameOverWindow);
		GameTextView messsageView = (GameTextView)findViewById(R.id.gameOverMessage);
		messsageView.setText(msg);
		GameTextView titleView = (GameTextView)findViewById(R.id.gameOverTitle);
		titleView.setText(title);
		GameTextView daysPassedView = (GameTextView)findViewById(R.id.num_days_passed);
		daysPassedView.setText(MainActivity.formatInt(lvl) );
		GameTextView finalScoreView = (GameTextView)findViewById(R.id.total_score);
		finalScoreView.setText(MainActivity.formatInt(score) );
		
		//show adView
		storeLayout.removeView(adView);
		gameOverLayout.addView(adView);
		
		gameOverLayout.setVisibility(View.VISIBLE);
		storeLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.GONE);
	}
	
	/**
	 * All persistent variables set to default values
	 */
	private void gameOverAndResetSavedVariables(){
		SharedPreferences gameState = getSharedPreferences(GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(STATE_HEALTH,ProtagonistView.DEFAULT_HEALTH);	//protagonist properties
		
		editor.putInt(STATE_TOTAL_RESOURCES, 0);
		editor.putInt(STATE_RESOURCES, 0);//store upgrades	
		editor.putInt(STATE_DEFENCE_LEVEL, 0);
		editor.putInt(STATE_GUN_CONFIG, -1);
		editor.putInt(STATE_BULLET_DAMAGE_LEVEL, 0);
		editor.putInt(STATE_BULLET_FREQ_LEVEL, 0);
		editor.putInt(STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
		editor.putInt(STATE_FRIEND_LEVEL, 0);
		
		editor.putInt(STATE_LEVEL, 0);		//reset level properties
		
		editor.commit();
	} 
	
	public void openStore(){
		final int lvl = levelCreator.getLevel();
		
		gameLayout.setVisibility(View.GONE);
		storeLayout.setVisibility(View.VISIBLE);
		
		stars_creator_game.stopSpawningStars();
		stars_creator_store.startSpawningStars();
		
		MediaController.stopLoopingSound();
		MediaController.playSoundClip(this, R.raw.background_store, true);

		resourceCount.setText(MainActivity.formatInt(levelCreator.getResourceCount()));
		levelCount.setText("Days In Space : "+ MainActivity.formatInt(lvl) );
		setHealthBars( );
		
		if(lvl % 15 == 0 && lvl !=0){
			//TODO display interstitial ad
		}
	}
	
	private void closeStoreAndResumeLevel(){	
		//adjust views
		storeLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.VISIBLE);
		scoreInGame.setText(MainActivity.formatInt( levelCreator.getResourceCount()) );
		stars_creator_store.stopSpawningStars();
		stars_creator_game.startSpawningStars();

		createNewProtagonistView();
		canBeginShooting = true;
		beginShootingRunnablePosted=false;
		
		levelCreator.resumeLevel(this);
		
		//create ally if needed		
		SharedPreferences gameState = getSharedPreferences(GAME_STATE_PREFS, 0);
		int friend_lvl = gameState.getInt(STATE_FRIEND_LEVEL,0);
		if( friend_lvl > 0 ){

			//if ally already exists, remove it and refresh it (so has full health)
			if(ally != null){
				ally.removeGameObject();
				ally = null;
			}
			
			ally = new AllyView(gameLayout, protagonist, friend_lvl);
		}
	}
	
	/**
	 * 
	 * @param left
	 * @param top
	 * @param right 
	 * @param bottom
	 * @param xTouch
	 * @param yTouch
	 * @return 1=top left, 2=top mid, 3=top right, 4=mid left, 5=mid mid, 6=mid right, 7=bottom left, 8=bottom mid, 9=bottom right
	 */
	private float[] percentXYAwayFromMidPoint(float left, float top,float right,float bottom,float xTouch,float yTouch){
		final float midX = (right-left)/2;
		final float midY = (bottom-top)/2;
		float xPosInPercent = (xTouch-midX)/(right-left);
		float yPosInPercent = (yTouch-midY)/(bottom-top);
		xPosInPercent = (Math.abs(xPosInPercent)>1) ? xPosInPercent/(Math.abs(xPosInPercent)) : xPosInPercent ;//maximum value is +/- 1
		yPosInPercent = (Math.abs(yPosInPercent)>1) ? yPosInPercent/(Math.abs(yPosInPercent)) : yPosInPercent ;
		final float[] percents = {xPosInPercent,yPosInPercent};
//		Log.d("lowrey","x= "+xPosInPercent+" y= "+yPosInPercent);
		return percents;
	} 
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			if(v.getId() == R.id.btn_move){
					final float[] percentXY = percentXYAwayFromMidPoint(v.getX(), v.getY(), v.getX()+v.getWidth(), v.getY()+v.getHeight(),event.getX(),event.getY());
					protagonist.beginMoving(percentXY[0],percentXY[1]);
			}
		}else if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(v.getId() == R.id.btn_move){
				final float[] percentXY_2 = percentXYAwayFromMidPoint(v.getX(), v.getY(), v.getX()+v.getWidth(), v.getY()+v.getHeight(),event.getX(),event.getY());
				protagonist.beginMoving(percentXY_2[0],percentXY_2[1]);
			}else if(v.getId() == R.id.btn_shoot){
					if(canBeginShooting){
						protagonist.startShooting();
						//force a delay after finishing shooting before user can shoot again. This is to try and fix an infinite shooting bug
					}	
					canBeginShooting=false;
			}
		}else if(event.getAction() == MotionEvent.ACTION_UP){
//			v.performClick();//why is this needed?
			if(v.getId() == R.id.btn_move){
				protagonist.stopMoving();
			}else if(v.getId() == R.id.btn_shoot){		
				protagonist.stopShooting();	
				
				if( ! beginShootingRunnablePosted){
					protagonist.postDelayed(new KillableRunnable(){
						@Override 
						public void doWork() {	
							canBeginShooting=true;beginShootingRunnablePosted=false;	
						}	
					},(long)ProtagonistView.getShootingDelay(this) );
					beginShootingRunnablePosted=true;
				}
			}else if(v.getId() == R.id.btn_heal){
				StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_HEAL, this, levelCreator); 
			}else if(v.getId() == R.id.btn_inc_bullet_dmg){
				StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_BULLET_DAMAGE, this, levelCreator);	
			}else if(v.getId() == R.id.btn_inc_bullet_freq){
					StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_BULLET_FREQ, this, levelCreator);
			}else if(v.getId() == R.id.btn_inc_defence){
					StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_DEFENCE, this, levelCreator);
			}else if(v.getId() == R.id.btn_inc_score_weight){
					StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_SCORE_MULTIPLIER, this, levelCreator);
			}else if(v.getId() == R.id.btn_new_gun){
					StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_GUN, this, levelCreator);	
			}else if(v.getId() == R.id.btn_purchase_friend){
					StoreUpgradeHandler.confirmUpgradeDialog(StoreUpgradeHandler.UPGRADE_FRIEND, this, levelCreator);
			}else if(v.getId() == R.id.start_next_level){
					if(StoreUpgradeHandler.getGunLevel(this) < 0){
						Toast.makeText(getApplicationContext(),"It's not safe! Repair ship blasters first", Toast.LENGTH_LONG).show();
					}else{
							new GameAlertDialogBuilder(this)
					    .setTitle("Continue")
					    .setMessage("Venture out into the void")
					    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
					     })
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					        	dialog.cancel();
					        	closeStoreAndResumeLevel();
					        }
					     })
//					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					}
			}else if(v.getId() == R.id.gameOverAccept){
					finish();
			}else if(v.getId() == R.id.gameOverShare){
					final String[] sharingTargets = {"face","twit","whats","chat","sms","mms","plus","email","gmail"};
					  
					List<Intent> targetedShareIntents = new ArrayList<Intent>();
					for(int i=0;i<sharingTargets.length;i++){
						Intent thisShareIntent = getShareIntent(this,sharingTargets[i]);
						if(thisShareIntent != null){targetedShareIntents.add(thisShareIntent);}
					}
					Intent chooser = Intent.createChooser(targetedShareIntents.remove(0), "Share via");
					chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
					this.startActivity(chooser);
			}
		}
		return false;//do not consume event, allow buttons to receive the touch as well
	}
	
	private Intent getShareIntent(Context ctx,String type){
		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		// gets the list of intents that can be loaded.
		List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(share, 0);
//		System.out.println("resinfo: " + resInfo);
		if (!resInfo.isEmpty()){
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(type) ||
						info.activityInfo.name.toLowerCase().contains(type) ) {
					if(!type.equals("mms")){
						share.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.app_name) );
					}
					String msg = "Check out \""+this.getResources().getString(R.string.app_name)+"\" on the Google Play Store. I made it to day "+levelAtGameOver+
							" and I got "+scoreAtGameOver+" points! ";
					
					share.putExtra(Intent.EXTRA_TEXT, msg);
					found = true;
					share.setPackage(info.activityInfo.packageName);
					break;
				}
			}
			if (!found)
				return null;
			return share;
			}
		return null;
		}
	
	
	@Override
	public ProtagonistView getProtagonist() {
		return this.protagonist;
	}
	@Override
	public void setHealthBars(){
		final int max = ProtagonistView.getProtagonistMaxHealth(this);
		final int progress = (protagonist == null || protagonist.isRemoved()) ? ProtagonistView.getProtagonistCurrentHealth(this) 
				: protagonist.getHealth();
		
		healthBar.setMax(max);
		healthBar.setProgress(progress);
		healthBarStore.setMax(max);
		healthBarStore.setProgress(progress);
	}
//	@Override
//	public void changeGameBackground(int newBackgroundId) {
//		this.gameLayout.setBackgroundResource(newBackgroundId);
//	}
	@Override
	public ImageView getExhaust(){
		return this.rocketExhaust;
	}

	@Override
	public void setScore(int score) {
		levelCreator.setResources(score);		
	}
	  
	@Override
	public void incrementScore(int amountToIncrementScore){
		//load resource multiplier 
		SharedPreferences gameState = this.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		final int resourceMultiplierLevel = gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0) ;

		final int newScore = levelCreator.getResourceCount() +(int) ( amountToIncrementScore * ( resourceMultiplierLevel * 0.2 + 1 ));
		
		levelCreator.setResources(newScore);
		
		scoreInGame.setText(MainActivity.formatInt(newScore));
	}
//
//	@Override
//	public void removeView(ImageView view) {
//		gameLayout.removeView(view);
//	}
//
//	@Override
//	public void addToForeground(ImageView view) {
//		gameLayout.addView(view,gameLayout.getChildCount()-2);
//	}
//
//	@Override
//	public void addToBackground(ImageView view) {
//		//addToForeground is called in every instantiation of every MovingView. Thus addToBackground is non default,
//		//and thus the view needs to be removed from its parent before it can be re-added
//		gameLayout.removeView(view);
//		gameLayout.addView(view,0);
//	}
	

	private void createAdViewInStore(){		
		//Create and plate the Banner Ad in activity_main.xml
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-1314947069846070/4608411941");
		adView.setAdSize(AdSize.BANNER);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
			    RelativeLayout.LayoutParams.WRAP_CONTENT, 
			    RelativeLayout.LayoutParams.WRAP_CONTENT); 
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adView.setLayoutParams(params);
		
		storeLayout.addView(adView);
		
		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice("FFAA2B4ECD49CBF2A0AB7F9D447410D7")
			.build(); 
 
		// Load ad request into Adview
		adView.loadAd(adRequest);	
	}
	
	private void createNewProtagonistView(){
		SharedPreferences gameState = getSharedPreferences(GAME_STATE_PREFS, 0);
		
		//create protagonist View & restore his state
		protagonist = new ProtagonistView(gameLayout,GameActivity.this);
		int protagonistPosition = (int) (offscreenBottom - protagonist.getLayoutParams().height * 1.5);// * 1.5 is for some botttom margin
		protagonist.setY( protagonistPosition );
		protagonist.setHealth(gameState.getInt(STATE_HEALTH, ProtagonistView.DEFAULT_HEALTH));
	}

	@Override
	public void resetResourcesGameTextView() {
		resourceCount.setText(MainActivity.formatInt( levelCreator.getResourceCount() ) );		
	}
	
}
