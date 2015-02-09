package levels;

import java.util.ArrayList;

import support.ConditionalHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import background_objects.BackgroundView;
import background_objects.Bird;
import background_objects.Clouds;
import background_objects.Sun;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class LevelSystem extends Factory_Waves{

	CollisionDetector gameDetector;
	

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
	
	public void resumeLevel(){		
		levelPaused=false;
		
		createBackgroundEffects();
		
		/*
		 * Waves are a series of runnables. each runnable increments progress in level, and each new level resets that progress.
		 * If handler has runnables canceled before level finishes, then current wave will not change.
		 * thus, when restarting a level simply find which runnable to call by using that current progress integer, and subtract 
		 * the delay from the currrentWave for the next wave's post
		 */
		
		for(int i=getWave();i<levels[getLevel()].length;i++){
			conditionalHandler.postIfLevelResumed(levels[getLevel()][i], 
					i * DEFAULT_WAVE_DURATION 
					- getWave() * DEFAULT_WAVE_DURATION);
		}
		
		gameDetector.startDetecting();
	}
	
	public void pauseLevel(){		
		levelPaused=true;
		
		gameDetector.stopDetecting();
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

		gameDetector.stopDetecting();
		conditionalHandler.stopSpawning();
		
		//set new level
		incrementLevel();
		setWave(0);
		
		//save level and protagonist attributes
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		editor.putInt(GameActivity.STATE_RESOURCES, this.getResourceCount());
		editor.putInt(GameActivity.STATE_LEVEL, this.getLevel());
		editor.putInt(GameActivity.STATE_WAVE, this.getWave());
		editor.putInt(GameActivity.STATE_HEALTH,this.getInteractivityInterface().getProtagonist().getHealth());
		
		editor.commit();
		
		//clean up - kill Views & associated threads, stop all spawning & background threads--do not kill friendlies
		for(int i=backgroundViews.size()-1;i>=0;i--){ 
			backgroundViews.get(i).removeGameObject();
		}
		for(int i=friendlyBullets.size()-1;i>=0;i--){ 
			friendlyBullets.get(i).removeGameObject();
		}	
//		//level cannot end with enemies/bonuses alive, so don't worry about these
//		for(int i=enemyBullets.size()-1;i>=0;i--){ 
//			enemyBullets.get(i).removeGameObject();
//		}	
//		for(int i=enemies.size()-1;i>=0;i--){
//			enemies.get(i).removeGameObject();
//		}
//		for(int i=bonuses.size()-1;i>=0;i--){ 
//			bonuses.get(i).removeGameObject();
//		}		
	}
	
	public int getMaxLevel(){
		return levels.length-1;
	}
	
	public ConditionalHandler getConditionalHandler(){
		return this.conditionalHandler;
	}
	
	
	//Background Animations and effects
	private final int[] backgroundColors={R.color.blue,R.color.dark_blue,R.color.very_dark_blue};
	
	private void createBackgroundEffects(){
		if(getLevel()<3){
			new Sun(ctx);

			for(int i=0;i< 12/ ( getLevel()+1 ) ;i++){
				Clouds a = new Clouds(ctx);
				Clouds b = new Clouds(ctx);
				a.setY((float) (MainActivity.getHeightPixels()*Math.random()));
				b.setY((float) (MainActivity.getHeightPixels()*Math.random()));
			}
			
			this.conditionalHandler.postIfLevelResumed(clouds);
		} 
		if(getLevel()>=backgroundColors.length){
			this.getInteractivityInterface().changeGameBackground(R.color.black);			
		}else{
			this.getInteractivityInterface().changeGameBackground(backgroundColors[getLevel()]);			
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
			
			conditionalHandler.postIfLevelResumed(this, 4000*(getLevel()+1));
		}
	};
	
}
