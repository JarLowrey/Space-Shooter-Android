package com.jtronlabs.space_shooter;

import friendlies.AllyView;
import friendlies.ProtagonistView;
import helpers.GameAlertDialogBuilder;
import helpers.GameTextView;
import helpers.KillableRunnable;
import helpers.MediaController;
import helpers.StoreUpgradeHandler;
import interfaces.GameActivityInterface;

import java.text.DecimalFormat;
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
import android.widget.ScrollView;
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
			STATE_DEFENSE_LEVEL="defenseLevel",
			STATE_RESOURCE_MULTIPLIER_LEVEL="resourceMultLevel",
			STATE_FRIEND_LEVEL="friendLevel",
			STATE_LEVEL="level";
//			STATE_WAVE="wave";
	
//	private Button btnMoveLeft,btnMoveRight,btnMoveUp,btnMoveDown;
	private ImageButton btnMove,btnShoot,storeNextLevel;
	private RelativeLayout	storeIncBulletDmg,storeIncdefense,
		storeIncBulletFreq,storeIncScoreWeight,storeNewGun,storeRepair,storePurchaseFriend;
	private GameTextView resourceCount,
		levelCount,
		scoreInGame,
		progressInLevel;
	private ProgressBar healthBar,healthBarStore;
	public ProtagonistView protagonist;
	private AllyView ally;
	public ImageView rocketExhaust;
	private RelativeLayout gameLayout,storeLayout;
	private ScrollView storeScrollView;
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
		progressInLevel = (GameTextView)findViewById(R.id.progress_in_level);
		gameLayout=(RelativeLayout)findViewById(R.id.gameplay_layout);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBarStore = (ProgressBar)findViewById(R.id.health_bar_store);//technically this is in store
		setHealthBars();
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		
		//set up Store View and listeners
		resourceCount = (GameTextView)findViewById(R.id.resource_count);  
		levelCount = (GameTextView)findViewById(R.id.level_count);
		storeLayout = (RelativeLayout)findViewById(R.id.store_layout);
		storeScrollView = (ScrollView)findViewById(R.id.store_scrollview);
		
		storeIncBulletDmg= (RelativeLayout)findViewById(R.id.buy_inc_bullet_dmg_layout); 
		storeIncdefense = (RelativeLayout)findViewById(R.id.buy_inc_defense_layout); 
		storeIncBulletFreq= (RelativeLayout)findViewById(R.id.buy_inc_bullet_freq_layout); 
		storeIncScoreWeight= (RelativeLayout)findViewById(R.id.buy_inc_score_weight_layout); 
		storeNewGun= (RelativeLayout)findViewById(R.id.buy_new_gun_layout); 
		storeRepair= (RelativeLayout)findViewById(R.id.buy_repair_layout);      
		storePurchaseFriend= (RelativeLayout)findViewById(R.id.buy_inc_friend_layout); 
		
		storeNextLevel= (ImageButton)findViewById(R.id.start_next_level);   
		storeNextLevel.setOnTouchListener(this);
		
		//Set up misc views and listeners
		ImageButton accept = (ImageButton)findViewById(R.id.gameOverAccept);
		accept.setOnTouchListener(this);
		ImageButton share = (ImageButton)findViewById(R.id.gameOverShare);
		share.setOnTouchListener(this);
		
		storeIncBulletDmg.setOnTouchListener(this); 
		storeIncdefense.setOnTouchListener(this);
		storeIncBulletFreq.setOnTouchListener(this); 
		storeIncScoreWeight.setOnTouchListener(this); 
		storeNewGun.setOnTouchListener(this); 
		storeRepair.setOnTouchListener(this); 
		storePurchaseFriend.setOnTouchListener(this); 
		
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
	
	@Override
	public int getBottomScreen(){
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
        
//        KillableRunnable.killAll();
        GameLoop.instance().stopLevelAndLoop();
		
		//protagonist attributes saved when he is removeGameObject() in pauseLevel
		//store upgrades are saved straight to persistent storage when bought (so does not need to be saved here).
		//level attributes are saved in the levelSystem
		//gameover() resets variables in method
		
		MediaController.stopLoopingSound();
		MediaController.stopNonLoopingSound();
		
		adView.pause();
		Log.d("lowrey","num enemies on pause = "+GameLoop.enemies.size());
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
//		
//		//I have NO IDEA why, but enemies can exist onResume(). These enemies are gone onPause(), and all debugging has failed thus 
//		//far. Simple workaround is to remove these misplaced enemies
//		for(int i=GameLoop.enemies.size()-1;i>=0;i--){
//			GameLoop.enemies.get(i).removeGameObject();
//		}
				
		levelCreator.loadScoreAndLevel();//need to reload variables first thing

		scoreInGame.setText("$"+MainActivity.formatInt(levelCreator.getResourceCount() ) );
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
			GameLoop.instance().startLevelAndLoop(this,levelCreator);//start after creating protagonist
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
		GameLoop.instance().stopLevelAndLoop(); 
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
		storeScrollView.setVisibility(View.GONE);
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
		editor.putInt(STATE_DEFENSE_LEVEL, 0);
		editor.putInt(STATE_GUN_CONFIG, -1);
		editor.putInt(STATE_BULLET_DAMAGE_LEVEL, 0);
		editor.putInt(STATE_BULLET_FREQ_LEVEL, 0);
		editor.putInt(STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
		editor.putInt(STATE_FRIEND_LEVEL, 0);
		
		editor.putInt(STATE_LEVEL, 0);		//reset level properties
		
		editor.commit();
	} 
	
	public void openStore(){
		GameLoop.instance().stopLevelAndLoop();//stop game loop
		
		setStoreItemsTitles();
		setStoreItemsMessages();
		
		final int lvl = levelCreator.getLevel();
		
		gameLayout.setVisibility(View.GONE);
		storeScrollView.setVisibility(View.VISIBLE);
		
		//after restarting the game loop, create necessary Views
		stars_creator_game.stopSpawningStars();
		stars_creator_store.startSpawningStars();
		
		MediaController.stopLoopingSound();
		MediaController.playSoundClip(this, R.raw.background_store, true);

		resourceCount.setText("$"+MainActivity.formatInt(levelCreator.getResourceCount()));
		if( lvl == 1 ){
			levelCount.setText("1 Day In Space ");			
		}else{
			levelCount.setText(MainActivity.formatInt(lvl) + " Days "); 
		}
		setHealthBars( );
		
		if(lvl % 15 == 0 && lvl !=0){
			//TODO display interstitial ad
		}
		
		GameLoop.instance().startLevelAndLoop(this, null);//start passive game loop for moving background image stars
	}
	
	private void closeStoreAndResumeLevel(){			
		GameLoop.instance().stopLevelAndLoop();//stop passive game loop for moving background image stars

		//adjust views
		storeScrollView.setVisibility(View.GONE);
		gameLayout.setVisibility(View.VISIBLE);
		scoreInGame.setText("$"+MainActivity.formatInt( levelCreator.getResourceCount()) );

		canBeginShooting = true;
		beginShootingRunnablePosted=false;

		/* after restarting the game loop, create necessary Views */
		//create protagonist
		createNewProtagonistView();
		
		//create special effects
		stars_creator_store.stopSpawningStars();
		stars_creator_game.startSpawningStars();
		
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
		

		GameLoop.instance().startLevelAndLoop(this,levelCreator);//start real game loop after all views created
	}
	
	/**
	 * Returns percentage finger is away from midpoint of button in X and Y. 0 is at midpoint, 1 is at edge of button
	 * 
	 * @param left
	 * @param top
	 * @param right 
	 * @param bottom
	 * @param xTouch relative position within this view
	 * @param yTouch relative position within this view
	 * @return 1=top left, 2=top mid, 3=top right, 4=mid left, 5=mid mid, 6=mid right, 7=bottom left, 8=bottom mid, 9=bottom right
	 */
	private float[] percentXYAwayFromMidPoint(float left, float top,float right,float bottom,float xTouch,float yTouch){
		final float xRadius = (float) ((right-left)/2.0), 
					yRadius = (float) ((bottom-top)/2.0);
		//at top of view, yTouch is 0, and at left xTouch is 0. We want those to be -1 (-100%) so 
		//subtract the respective radius and divide by radius to get a percent value
		float xPosInPercent = (xTouch-xRadius)/xRadius;
		float yPosInPercent = (yTouch-yRadius)/yRadius;
		
//		Log.d("lowrey","xPos "+ xPosInPercent + " yPos "+ yPosInPercent);

		//make it easier to stay still or move in a straight line. The user's finger does not need to be exactly on 0 mark
		if(Math.abs(xPosInPercent) < 0.04){
			xPosInPercent = 0;
		}
		if(Math.abs(yPosInPercent) < 0.04){
			yPosInPercent = 0;
		}
			
		final float[] percents = {xPosInPercent,yPosInPercent};
				
		return percents;
	} 
	 
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			if(v.getId() == R.id.btn_move){
					final float[] percentXY = percentXYAwayFromMidPoint(v.getX(), v.getY(), 
							v.getX()+v.getWidth(), v.getY()+v.getHeight(),event.getX(),event.getY());
					protagonist.updatePercentDistanceFromMidpointOfMoveButton(percentXY[0],percentXY[1]);
			}
		}else if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(v.getId() == R.id.btn_move){
				final float[] percentXY_2 = percentXYAwayFromMidPoint(v.getX(), v.getY(), 
						v.getX()+v.getWidth(), v.getY()+v.getHeight(),event.getX(),event.getY());
				protagonist.updatePercentDistanceFromMidpointOfMoveButton(percentXY_2[0],percentXY_2[1]);
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
				protagonist.updatePercentDistanceFromMidpointOfMoveButton(0,0);//stop moving
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
			}else if(v.getId() == R.id.buy_repair_layout){ 
				StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_repair_layout, this, levelCreator); 
			}else if(v.getId() == R.id.buy_inc_bullet_dmg_layout){
				StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_inc_bullet_dmg_layout, this, levelCreator);	
			}else if(v.getId() == R.id.buy_inc_bullet_freq_layout){
					StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_inc_bullet_freq_layout, this, levelCreator);
			}else if(v.getId() == R.id.buy_inc_defense_layout){
					StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_inc_defense_layout, this, levelCreator);
			}else if(v.getId() == R.id.buy_inc_score_weight_layout){
					StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_inc_score_weight_layout, this, levelCreator);
			}else if(v.getId() == R.id.buy_new_gun_layout){
					StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_new_gun_layout, this, levelCreator);	
			}else if(v.getId() == R.id.buy_inc_friend_layout){
					StoreUpgradeHandler.confirmUpgradeDialog(R.id.buy_inc_friend_layout, this, levelCreator);
			}else if(v.getId() == R.id.start_next_level){
//					if(StoreUpgradeHandler.getGunLevel(this) < 0){
//						Toast.makeText(getApplicationContext(),"It's not safe! Repair ship blasters first", Toast.LENGTH_LONG).show();
//					}else{
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
					     .show();
//					}
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
		
		scoreInGame.setText("$"+MainActivity.formatInt(newScore));
	}
	
	@Override
	public void setTimerText(long numMilliecondsLeft){
		DecimalFormat df = new DecimalFormat("#.0"); 
		if(numMilliecondsLeft == 0){
			progressInLevel.setText("0");	
		}else{
			progressInLevel.setText(""+
					df.format(numMilliecondsLeft/1000.0));			
		}
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
		protagonist.setHealth(gameState.getInt(STATE_HEALTH, ProtagonistView.DEFAULT_HEALTH));
	}

	@Override
	public void resetResourcesGameTextView() {
		resourceCount.setText("$"+MainActivity.formatInt(levelCreator.getResourceCount() ) );		
	}
	
	@Override
	public void setStoreItemsTitles(){
		GameTextView repairTitle = (GameTextView)findViewById(R.id.buy_repair_title);
		GameTextView newGunTitle = (GameTextView)findViewById(R.id.buy_new_gun_title);
		GameTextView defenseTitle = (GameTextView)findViewById(R.id.buy_inc_defense_title);
		GameTextView bulletDmgTitle = (GameTextView)findViewById(R.id.buy_inc_bullet_dmg_title);
		GameTextView bulletFreqTitle = (GameTextView)findViewById(R.id.buy_inc_bullet_freq_title);
		GameTextView allyTitle = (GameTextView)findViewById(R.id.buy_inc_friend_title);
		GameTextView scoreWeightTitle = (GameTextView)findViewById(R.id.buy_inc_score_weight_title);
		
		repairTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_repair_layout));
		newGunTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_new_gun_layout));
		defenseTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_inc_defense_layout));
		bulletDmgTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_inc_bullet_dmg_layout));
		bulletFreqTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_inc_bullet_freq_layout));
		allyTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_inc_friend_layout));
		scoreWeightTitle.setText(StoreUpgradeHandler.getUpgradeTitle(this, R.id.buy_inc_score_weight_layout));
	}

	@Override
	public void setStoreItemsMessages(){
		GameTextView repairMsg = (GameTextView)findViewById(R.id.buy_repair_message);
		GameTextView newGunMsg = (GameTextView)findViewById(R.id.buy_new_gun_message);
		GameTextView defenseMsg = (GameTextView)findViewById(R.id.buy_inc_defense_message);
		GameTextView bulletDmgMsg = (GameTextView)findViewById(R.id.buy_inc_bullet_dmg_message);
		GameTextView bulletFreqMsg = (GameTextView)findViewById(R.id.buy_inc_bullet_freq_message);
		GameTextView allyMsg = (GameTextView)findViewById(R.id.buy_inc_friend_message);
		GameTextView scoreWeightMsg = (GameTextView)findViewById(R.id.buy_inc_score_weight_message);
		
		repairMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_repair_layout));
		newGunMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_new_gun_layout));
		defenseMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_inc_defense_layout));
		bulletDmgMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_inc_bullet_dmg_layout));
		bulletFreqMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_inc_bullet_freq_layout));
		allyMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_inc_friend_layout));
		scoreWeightMsg.setText(StoreUpgradeHandler.getUpgradeMessage(this, R.id.buy_inc_score_weight_layout));
	}
}
