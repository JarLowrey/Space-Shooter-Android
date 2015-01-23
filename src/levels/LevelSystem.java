package levels;

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

public class LevelSystem extends Factory_Waves{

	CollisionDetector gameDetector;
	

	private int score;
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

	public void incrementLevel(){
		currentLevel++;
		currentWave=0;
	}
	
	public void resumeLevel(){		
		levelPaused=false;
		
		createBackgroundEffects();
		
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current wave will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer, and subtract 
		 * the delay from the currrentWave for the next wave's post
		 */
		
		Log.d("lowrey","resuemd, wave="+currentWave+ " level="+currentLevel);
		
		for(int i=currentWave;i<levels[currentLevel].length;i++){
			conditionalHandler.postIfLevelResumed(levels[currentLevel][i], i * DEFAULT_WAVE_DURATION 
					- currentWave * DEFAULT_WAVE_DURATION);
		}
		
		gameDetector.startDetecting();
	}
	
	public void pauseLevel(){
		levelPaused=true;
		
		conditionalHandler.stopSpawning();
		
		//clean up - kill Views & associated threads, stop all spawning & background threads
		for(int i=backgroundViews.size()-1;i>=0;i--){ 
			backgroundViews.get(i).removeGameObject();
		}
		for(int i=friendlyBullets.size()-1;i>=0;i--){ 
			friendlyBullets.get(i).removeGameObject();
		}	
		for(int i=enemyBullets.size()-1;i>=0;i--){ 
			enemyBullets.get(i).removeGameObject();
		}	
		for(int i=friendlies.size()-1;i>=0;i--){ 
			friendlies.get(i).removeGameObject();
		}	
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeGameObject();
		}
		for(int i=bonuses.size()-1;i>=0;i--){ 
			bonuses.get(i).removeGameObject();
		}
	}
	
	public void endLevel(){
		levelPaused=true;
		conditionalHandler.stopSpawning();
		
		setWave(0);
		incrementLevel();
		
		//clean up - kill Views & associated threads, stop all spawning & background threads--do not kill friendlies
		for(int i=backgroundViews.size()-1;i>=0;i--){ 
			backgroundViews.get(i).removeGameObject();
		}
		for(int i=friendlyBullets.size()-1;i>=0;i--){ 
			friendlyBullets.get(i).removeGameObject();
		}	
		for(int i=enemyBullets.size()-1;i>=0;i--){ 
			enemyBullets.get(i).removeGameObject();
		}	
		for(int i=enemies.size()-1;i>=0;i--){
			enemies.get(i).removeGameObject();
		}
		for(int i=bonuses.size()-1;i>=0;i--){ 
			bonuses.get(i).removeGameObject();
		}		
	}
	
	public int getMaxLevel(){
		return levels.length-1;
	}
	
	//GET/SET LEVEL STATE
	public int getWaveNumber(){
		return this.currentWave;
	}
	public void setWave(int wave){
		this.currentWave=wave;
	}
	public void setLevel(int level){
		this.currentLevel=level;
	}
	//SCORE
	public void setScore(int scoreValue){
		score=scoreValue;
	}
	public int getScore(){
		return score;
	}
	
	public ConditionalHandler getConditionalHandler(){
		return this.conditionalHandler;
	}
	
	
	//Background Animations and effects
	private final int[] backgroundColors={R.color.blue,R.color.dark_blue,R.color.very_dark_blue};
	
	private void createBackgroundEffects(){
		if(currentLevel<3){
			new Sun(ctx);

			for(int i=0;i< 12/ ( currentLevel+1 ) ;i++){
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
			
			conditionalHandler.postIfLevelResumed(this, 4000*(currentLevel+1));
		}
	};
	
}
