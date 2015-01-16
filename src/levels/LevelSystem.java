package levels;

import interfaces.GameActivityInterface;

import java.util.ArrayList;

import support.ConditionalHandler;
import android.content.Context;
import android.util.Log;
import background_objects.BackgroundView;
import background_objects.Bird;
import background_objects.Clouds;
import background_objects.Sun;
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
	public static ArrayList<BackgroundView> backgroundViews=new ArrayList<BackgroundView>();
	public static ArrayList<BulletView> friendlyBullets=new ArrayList<BulletView>();
	public static ArrayList<BulletView> enemyBullets=new ArrayList<BulletView>();
	public static ArrayList<FriendlyView> friendlies=new ArrayList<FriendlyView>();
	public static ArrayList<EnemyView> enemies=new ArrayList<EnemyView>();
	public static ArrayList<BonusView> bonuses=new ArrayList<BonusView>();
	
	/**
	 * 
	 * @param context MUST IMPLEMENT InteractiveGameInterface.java
	 */
	public LevelSystem(Context context) {
		super(context);
		
		gameDetector = new CollisionDetector(this);
	}

	public void newGame(){
		Log.d("lowrey","new game");
		score=0;
		currentLevel=GAME_NOT_BEGUN;//= -1. 
		startNextLevel();
	}
	
	public GameActivityInterface getInteractivityInterface(){
		return (GameActivityInterface)ctx;
	}
	
	/**
	 * 
	 * @return True if user has passed the last level
	 */
	public boolean startNextLevel(){
		for(int i=backgroundViews.size()-1;i>=0;i--){
			backgroundViews.get(i).removeGameObject();
		}
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
	
	public ConditionalHandler getConditionalHandler(){
		return this.conditionalHandler;
	}
	
	private final int[] backgroundColors={R.color.blue,R.color.dark_blue};
	
	private void createBackgroundEffects(){
		if(currentLevel<2){
			new Sun(ctx);

			for(int i=0;i<12;i++){
				Clouds a = new Clouds(ctx);
				Clouds b = new Clouds(ctx);
				a.setY((float) (MainActivity.getHeightPixels()*Math.random()));
				b.setY((float) (MainActivity.getHeightPixels()*Math.random()));
			}
			
			this.conditionalHandler.postIfLevelResumed(clouds);
		} 
		if(currentLevel>=backgroundColors.length){
			this.getInteractivityInterface().changeGameBackground(R.color.black);			
		}else{
			this.getInteractivityInterface().changeGameBackground(backgroundColors[currentLevel]);			
		}
	}
	
	Runnable clouds = new Runnable(){
		@Override
		public void run() {
			if(Math.random()<.5){
				new Bird(ctx);
			}
			if(Math.random()<.5){
				new Clouds(ctx);
			}
			new Bird(ctx);
			new Clouds(ctx);
			
			conditionalHandler.postIfLevelResumed(this, 4000);
		}
	};
	
}
