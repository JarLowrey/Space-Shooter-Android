package com.jtronlabs.to_the_moon;

import enemies.EnemyView;
import enemies.Shooting_ArrayMovingView;
import friendlies.FriendlyView;
import friendlies.ProtagonistView;
import interfaces.GameActivityInterface;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import levels.LevelSystem;
import parents.MovingView;
import parents.Moving_ProjectileView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnTouchListener, GameActivityInterface{

	public static int offscreenBottom;
	
	private Button btnMoveLeft,btnMoveRight,btnMoveUp,btnMoveDown,btnShoot;
	private ImageButton	btnIncBulletDmg,btnIncBulletVerticalSpeed,
	btnIncBulletFreq,btnIncScoreWeight,btnNewGun,btnHeal,btnPurchaseFriend,btnNextLevel;
	private TextView resourceCount;
	private ProgressBar healthBar;
	public ProtagonistView protagonist;
	public ImageView rocketExhaust;
	private RelativeLayout gameLayout,storeLayout;

	//MODEL
	private LevelSystem levelCreator;
	
	private final int UPGRADE_BULLET_DAMAGE=0,UPGRADE_BULLET_SPEED=1,UPGRADE_BULLET_FREQ=3,
			UPGRADE_GUN=4,UPGRADE_FRIEND=5,UPGRADE_SCORE_MULTIPLIER=6,UPGRADE_HEAL=7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//set up Gameplay Views and listeners and layouts
		btnMoveLeft= (Button)findViewById(R.id.btn_move_left); 
		btnMoveRight= (Button)findViewById(R.id.btn_move_right);
		btnMoveUp = (Button)findViewById(R.id.btn_move_up);
		btnMoveDown = (Button)findViewById(R.id.btn_move_down);
		btnShoot = (Button)findViewById(R.id.btn_shoot);
		btnMoveUp.setOnTouchListener(this);
		btnMoveDown.setOnTouchListener(this);
		btnMoveLeft.setOnTouchListener(this);
		btnMoveRight.setOnTouchListener(this); 
		btnShoot.setOnTouchListener(this);
		gameLayout=(RelativeLayout)findViewById(R.id.gameplay_layout);
		healthBar=(ProgressBar)findViewById(R.id.health_bar);
		healthBar.setMax((int) ProtagonistView.DEFAULT_HEALTH);
		healthBar.setProgress(healthBar.getMax());
		rocketExhaust = (ImageView)findViewById(R.id.rocket_exhaust);
		
		//set up Store View and listeners
		resourceCount = (TextView)findViewById(R.id.resource_count);
		storeLayout = (RelativeLayout)findViewById(R.id.store_layout);
		btnIncBulletDmg= (ImageButton)findViewById(R.id.btn_inc_bullet_dmg); 
		btnIncBulletVerticalSpeed= (ImageButton)findViewById(R.id.btn_inc_bullet_speed); 
		btnIncBulletFreq= (ImageButton)findViewById(R.id.btn_inc_bullet_freq); 
		btnIncScoreWeight= (ImageButton)findViewById(R.id.btn_inc_score_weight); 
		btnNewGun= (ImageButton)findViewById(R.id.btn_new_gun); 
		btnHeal= (ImageButton)findViewById(R.id.btn_heal); 
		btnPurchaseFriend= (ImageButton)findViewById(R.id.btn_purchase_friend); 
		btnNextLevel= (ImageButton)findViewById(R.id.start_next_level); 
		
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
		
		//set up protagonist
		protagonist = new ProtagonistView(GameActivity.this,GameActivity.this);
		int protagonistPosition = (int) (offscreenBottom - protagonist.getLayoutParams().height * 1.5);// * 1.5 is for some botttom margin
		protagonist.setY( protagonistPosition );
		
		//set up the game
		levelCreator = new LevelSystem(this); 
		levelCreator.startNextLevel();
		
		/*
		ViewTreeObserver vto = gameLayout.getViewTreeObserver(); //Use a listener to perform actions after layouts have been loaded
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
		    @Override 
		    public void onGlobalLayout() {
		    	gameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			
		    } 
		});
		*/
	}
	/**
	 * Pause game. Eventually, state will need to be saved to database, as after on pause any
	 * variables are liable for Android garbage collection!
	 */
	@Override
    public void onPause() {
        super.onPause();

		for(int i=LevelSystem.enemies.size()-1;i>=0;i--){
			LevelSystem.enemies.get(i).removeGameObject();
		}
		for(int i=LevelSystem.bonuses.size()-1;i>=0;i--){ 
			LevelSystem.bonuses.get(i).removeGameObject();
		}
		levelCreator.pauseLevel();
    }
	
	@Override
	public void onResume(){
		super.onResume(); 
		
        for(FriendlyView friendly : LevelSystem.friendlies){
        	friendly.restartThreads();
        }
        
        if(levelCreator.isLevelPaused()){levelCreator.resumeLevel();}
	}
	
	public void gameOver(){
		Log.d("lowrey","num enemies spawned="+EnemyView.numSpawn+" died="+EnemyView.numRemoved);
		levelCreator.pauseLevel();
		healthBar.setProgress(0);
		
//		levelFactory.stopSpawning();
		for(int i=LevelSystem.enemies.size()-1;i>=0;i--){
			LevelSystem.enemies.get(i).removeGameObject();//this cleans up the threads and removes the Views from the Activity
		}
		
		//clean up variables
		LevelSystem.enemies=new ArrayList<EnemyView>();
		Shooting_ArrayMovingView.resetSimpleShooterArray();

		finish();
		Toast.makeText(this, "you suck...", Toast.LENGTH_LONG).show();
	}
	
	public void beatGame(){
		Toast.makeText(this, "winner winner chicken dinner", Toast.LENGTH_LONG).show();
		finish();
	}
	
	public void openStore(){
		storeLayout.setVisibility(View.VISIBLE);
		gameLayout.setVisibility(View.GONE);
		resourceCount.setText(""+NumberFormat.getNumberInstance(Locale.US).format(levelCreator.getScore()));
	}
	
	private void closeStoreAndStartNextLevel(){
		storeLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.VISIBLE);
		levelCreator.startNextLevel();
	}

	private boolean canBeginShooting=true,beginShootingRunnablePosted=false;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			switch(v.getId()){
				case R.id.btn_move_left:
					protagonist.beginMoving(Moving_ProjectileView.LEFT);
					break;  
				case R.id.btn_move_right:
					protagonist.beginMoving(Moving_ProjectileView.RIGHT);
					break;
				case R.id.btn_move_up:
					protagonist.beginMoving(Moving_ProjectileView.UP);
					break;
				case R.id.btn_move_down:
					protagonist.beginMoving(Moving_ProjectileView.DOWN);
					break;
				case R.id.btn_shoot:
						
					if(canBeginShooting){
						protagonist.startShooting();
						//force a delay after finishing shooting before user can shoot again. This is to try and fix an infinite shooting bug
					}	
					canBeginShooting=false;
					break;
			}
			break;
		case MotionEvent.ACTION_UP:
//			v.performClick();//why is this needed?
			switch(v.getId()){
				case R.id.btn_move_left:
						protagonist.stopMoving();
					break; 
				case R.id.btn_move_right:
						protagonist.stopMoving();
					break;
				case R.id.btn_move_up:
					protagonist.stopMoving();
					break; 
				case R.id.btn_move_down:
					protagonist.stopMoving();
					break;
				case R.id.btn_shoot:					
					protagonist.stopShooting();	
					

					Runnable canShootAgainRunnable = new Runnable(){//only one of these should ever be posted at a time
						@Override
						public void run() {	canBeginShooting=true;beginShootingRunnablePosted=false;	}	};
					
					if( ! beginShootingRunnablePosted){
						protagonist.postDelayed(canShootAgainRunnable,(long)ProtagonistView.DEFAULT_BULLET_FREQ);
						beginShootingRunnablePosted=true;
					}
					
					break;
				case R.id.btn_heal:
					confirmUpgradeDialog(UPGRADE_HEAL);
					break;
				case R.id.btn_inc_bullet_dmg:
					confirmUpgradeDialog(UPGRADE_BULLET_DAMAGE);					
					break;
				case R.id.btn_inc_bullet_freq:
					confirmUpgradeDialog(UPGRADE_BULLET_FREQ);
					break;
				case R.id.btn_inc_bullet_speed:
					confirmUpgradeDialog(UPGRADE_BULLET_SPEED);					
					break;
				case R.id.btn_inc_score_weight:
					confirmUpgradeDialog(UPGRADE_SCORE_MULTIPLIER);
					break;
				case R.id.btn_new_gun:
					confirmUpgradeDialog(UPGRADE_GUN);					
					break;
				case R.id.btn_purchase_friend:
					confirmUpgradeDialog(UPGRADE_FRIEND);
					break;
				case R.id.start_next_level: 
					if(levelCreator.getLevel()==1 && protagonist.getGunLevel()==0){
						Toast.makeText(getApplicationContext(),"It's not safe! Repair ship blasters first", Toast.LENGTH_LONG).show();
					}else{
							new AlertDialog.Builder(this)
					    .setTitle("Continue")
					    .setMessage("Venture out into the void")
					    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
					     })
					    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) {
					        	dialog.cancel();
					        	closeStoreAndStartNextLevel();
					        }
					     })
//					    .setIcon(android.R.drawable.ic_dialog_alert)
					     .show();
					}
					break;
			}
			break;
		}
		return false;//do not consume event, allow buttons to receive the touch as well
	}
	
	private void confirmUpgradeDialog(final int whichUpgrade){
		String msg="";
		int cost=0;
		boolean maxLevelItem=false;
		
		try{
			switch(whichUpgrade){
			case UPGRADE_BULLET_DAMAGE:
				cost = 	(int) (this.getResources().getInteger(R.integer.inc_bullet_damage_base_cost) * Math.pow((protagonist.getBulletDamageLevel()+1),2)) ;
				msg=this.getResources().getString(R.string.upgrade_bullet_damage);
				break;
			case UPGRADE_BULLET_SPEED:
				cost = (int) (this.getResources().getInteger(R.integer.inc_bullet_speed_base_cost) * Math.pow((protagonist.getBulletSpeedYLevel()+1),2)) ;
				msg=this.getResources().getString(R.string.upgrade_bullet_speed);
				break;
			case UPGRADE_BULLET_FREQ:
				cost = (int) (this.getResources().getInteger(R.integer.inc_bullet_frequency_base_cost) * Math.pow((protagonist.getBulletBulletFreqLevel()+1),2)) ;
				msg=this.getResources().getString(R.string.upgrade_bullet_frequency);
				break;
			case UPGRADE_GUN:
				cost = this.getResources().getIntArray(R.array.gun_upgrade_costs)[protagonist.getGunLevel()] ;
				msg = this.getResources().getStringArray(R.array.gun_descriptions)[protagonist.getGunLevel()];
				break;
			case UPGRADE_FRIEND:
				cost = this.getResources().getInteger(R.integer.friend_base_cost) ;
				msg=this.getResources().getString(R.string.upgrade_buy_friend);
				break;
			case UPGRADE_SCORE_MULTIPLIER:
				cost = this.getResources().getInteger(R.integer.score_multiplier_base_cost) ;
				msg=this.getResources().getString(R.string.upgrade_score_multiplier_create);
				break;
			case UPGRADE_HEAL:
				cost = 	this.getResources().getInteger(R.integer.heal_base_cost) * (this.levelCreator.getLevel()) ;
				msg=this.getResources().getString(R.string.upgrade_heal);
				break;
			}
			msg+="\n\n"+NumberFormat.getNumberInstance(Locale.US).format(cost);//add cost with commas
		}catch(IndexOutOfBoundsException e){//upgrade is past set bounds of the arrays.xml value
			msg="Maximum upgrade attained";
			maxLevelItem=true;
		}
		final int costCopy=cost;
		final boolean maxLevelItemCopy = maxLevelItem;
		
		new AlertDialog.Builder(this)
	    .setTitle("Upgrade")
	    .setMessage(msg)
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
	     })
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(!maxLevelItemCopy){ 
	        		applyUpgrade(whichUpgrade,costCopy); 
        		}else{
        			dialog.cancel();
        		}
	        }
	     })
//	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	private void applyUpgrade(final int whichUpgrade,int cost){
		boolean upgraded = levelCreator.getScore() >= cost;

		switch(whichUpgrade){
		case UPGRADE_BULLET_DAMAGE:
			if(upgraded){ protagonist.incrementBulletDamageLevel(); }
			break;
		case UPGRADE_BULLET_SPEED:
			if(upgraded){protagonist.incrementBulletSpeedYLevel();}
			break;
		case UPGRADE_BULLET_FREQ:
			if(upgraded){protagonist.incrementBulletFreqLevel();}
			break;
		case UPGRADE_GUN:
			if(upgraded){protagonist.upgradeGun();}			//NEED TO DEFINE GUN UPGRADE
			break;
		case UPGRADE_FRIEND:
//			if(upgraded){protagonist.incrementBulletFreqWeight();}		//NO FRIEND CAPABILITY YET
			break;
		case UPGRADE_SCORE_MULTIPLIER:
//			if(upgraded){protagonist.incrementBulletFreqWeight();}		//NO SCORE MULT YET
			break;
		case UPGRADE_HEAL:
			if(upgraded){ protagonist.heal(protagonist.getMaxHealth() - protagonist.getHealth()); }
			break;
		}
		
		if(upgraded){
			if(whichUpgrade==UPGRADE_GUN){btnShoot.setVisibility(View.VISIBLE);}//on first upgraded gun, set shoot to visible. this can be removed later but currently applies on every upgrade
			levelCreator.decrementScore(cost);
			resourceCount.setText(""+NumberFormat.getNumberInstance(Locale.US).format( levelCreator.getScore()));
			Toast.makeText(getApplicationContext(),"Purchased!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(),"Not enough resources", Toast.LENGTH_SHORT).show();
		}
	} 
	@Override
	public ProtagonistView getProtagonist() {
		return this.protagonist;
	}
	@Override
	public void setHealthBar(){
		healthBar.setProgress((int) protagonist.getHealth());
	}
	@Override
	public void changeGameBackground(int newBackgroundId) {
		this.gameLayout.setBackgroundResource(newBackgroundId);
	}
	@Override
	public void addToForeground(MovingView view) {
		gameLayout.addView(view,gameLayout.getChildCount()-2);		
	}
	@Override
	public void addToBackground(MovingView view) {
		//addToForeground is called in every instantiation of every MovingView. Thus addToBackground is non default,
		//and thus the view needs to be removed from its parent before it can be re-added
		gameLayout.removeView(view);
		gameLayout.addView(view,0);
	}
	@Override
	public ImageView getExhaust(){
		return this.rocketExhaust;
	}
	
//	@Override
//	public boolean onLongClick(View arg0) {
//		if(arg0.getId()==R.id.btn_shoot){
//			protagonist.startShooting();
//		}
//		return false;
//	}
}
