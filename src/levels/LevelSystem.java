package levels;

import interfaces.InteractiveGameInterface;

import java.util.ArrayList;

import parents.Moving_GravityView;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class LevelSystem extends Factory_LevelWaves{

	public static final int GAME_NOT_BEGUN=-1;
	
	CollisionDetector gameDetector;
	

	private static int score;
	public static ArrayList<BulletView> friendlyBullets=new ArrayList<BulletView>();
	public static ArrayList<BulletView> enemyBullets=new ArrayList<BulletView>();
	public static ArrayList<FriendlyView> friendlies=new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies=new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses=new ArrayList<BonusView>();
	
	
	public LevelSystem(Context context,InteractiveGameInterface gameScreen) {
		super(context,gameScreen);
		
		gameDetector = new CollisionDetector(this);
	}

	public void newGame(){
		Log.d("lowrey","new game");
		score=0;
		currentLevel=GAME_NOT_BEGUN;//= -1. 
		startNextLevel();
	}
	
	public InteractiveGameInterface getInteractivityInterface(){
		return gameInteractivityInterface;
	}
	
	/**
	 * 
	 * @return True if user has passed the last level
	 */
	public boolean startNextLevel(){
		currentLevel++;
		if(currentLevel==levels.length){
			return true;
		}else{
			levelWavesCompleted=false;
			levelPaused=false;
			currentProgressInLevel=0;
			

			createBackgroundEffects();
			
			/*
			 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
			 * If handler has runnables canceled before level finishes, then current progress will not change.
			 * thus, when restarting a level simply find which runnable to call by using that current progress integer
			 */
			
			
			
			for(int i=currentProgressInLevel;i<levels[currentLevel].length;i++){
				conditionalHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION);
			}
			gameDetector.startDetecting();
			return false;
		}
	}
	public void resumeLevel(){
		levelPaused=false;
		
		createBackgroundEffects();
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current progress will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer
		 */
		
		for(int i=currentProgressInLevel;i<level1.length;i++){
			conditionalHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION);
		}
		
		gameDetector.startDetecting();
	}
	
	public void pauseLevel(){
		levelPaused=true;
		gameDetector.stopDetecting();
	}
	
	public int highestLevel(){
		return levels.length;
	}
	
	//GET/SET LEVEL STATE
	public void notifyLevelWavesCompleted(){
		levelWavesCompleted=true;
	}
	
	//SCORE
	public static void incrementScore(int howMuchToIncrementScore){
		score+=Math.abs(howMuchToIncrementScore);
	}
	public static void decrementScore(int howMuchToDecrementScore){
		score-=Math.abs(howMuchToDecrementScore);
	}
	public static int getScore(){
		return score;
	}
	
	private void createBackgroundEffects(){
		switch( currentLevel ){
		case 0:
			this.gameInteractivityInterface.changeBackground(R.color.sky_blue);
			this.conditionalHandler.postIfLevelResumed(clouds);
			break;
		}
	}
	
	Runnable clouds = new Runnable(){
		@Override
		public void run() {
			if(Math.random() < 0.5){
				Moving_GravityView cloud = new Moving_GravityView(ctx,1,0);
				cloud.setImageResource(R.drawable.cloud_img2);
				int height = (int) ctx.getResources().getDimension(R.dimen.cloud_height);
				int width = (int) ctx.getResources().getDimension(R.dimen.cloud_width);
				cloud.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
				cloud.setX( (float) (( MainActivity.getWidthPixels()-width ) *Math.random()));//random X position
				gameInteractivityInterface.addBackgroundObjectToScreen(cloud);
			}
			
			Moving_GravityView cloud = new Moving_GravityView(ctx,1,0);
			cloud.setImageResource(R.drawable.cloud_img1);
			int height = (int) ctx.getResources().getDimension(R.dimen.cloud_height);
			int width = (int) ctx.getResources().getDimension(R.dimen.cloud_width);
			cloud.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
			cloud.setX( (float) (( MainActivity.getWidthPixels()-cloud.getLayoutParams().width ) *Math.random()));//random X position
			gameInteractivityInterface.addBackgroundObjectToScreen(cloud);
			
			conditionalHandler.postIfLevelResumed(this, 5000);
		}
	};
	
}
