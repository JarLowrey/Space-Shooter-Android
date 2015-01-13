package levels;

import interfaces.InteractiveGameInterface;
import android.content.Context;
import enemies.Shooting_ArrayMovingView;

public class Factory_LevelWaves extends Factory_Waves{

	public int DEFAULT_WAVE_DURATION=5000;
	
	protected int currentProgressInLevel;
	
	public Factory_LevelWaves(Context context,InteractiveGameInterface gameScreen){
		super( context, gameScreen);
	}
	
	private int getCurrentLevelLength(){
		return levels[currentLevel].length*DEFAULT_WAVE_DURATION;
	}
	
	
	/**
	 * define common waves here. for example, 2 meteor showers, one on each eadge of screen that force user to go into middle of screen.
	 * this class should contain runnables, not functions, so that the level factory can post delay them
	 */
	
	protected final  Runnable doNothing = new Runnable(){
		@Override
		public void run() {}};

	protected final Runnable levelWavesOver = new Runnable(){
		@Override
		public void run() {	
			levelWavesCompleted=true;
		}
	};
	
	//regular meteors
	final Runnable meteorSidewaysOnePerSecondForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave( getCurrentLevelLength() /1500 ,1000);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorsStraightOnePerSecondForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( getCurrentLevelLength() /1500 ,1000);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorSidewaysThisWave = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentProgressInLevel++;
		}
	};	
	
	//meteor showers
	final Runnable meteorShowerLong = new Runnable(){//lasts 2 waves
		@Override
		public void run() {
			spawnMeteorShower( (DEFAULT_WAVE_DURATION * 2 )/1000,1000,true);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorShowersThatForceUserToMiddle = new Runnable(){//this does not last a whole wave, which is fine.
		@Override
		public void run() {
				spawnMeteorShower(4,400,true);
				spawnMeteorShower(4,400,false);
				currentProgressInLevel++;
		}
	};
	final Runnable meteorShowersThatForceUserToRight = new Runnable(){
		@Override
		public void run() {
				spawnMeteorShower(9,DEFAULT_WAVE_DURATION/9,true);
				currentProgressInLevel++;
		}
	};
	final Runnable meteorShowersThatForceUserToLeft = new Runnable(){
		@Override
		public void run() {
				spawnMeteorShower(9,DEFAULT_WAVE_DURATION/9,false);
				currentProgressInLevel++;
		}
	};

	//giant meteors
	final Runnable meteorsGiantAndSideways = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(2,DEFAULT_WAVE_DURATION/2);//spawn for entire wave
			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
			currentProgressInLevel++;
		}
	};
	final Runnable meteorsOnlyGiants = new Runnable(){
		@Override
		public void run() {
			spawnGiantMeteorWave(4,DEFAULT_WAVE_DURATION/4);
			currentProgressInLevel++;
		}
	};
	
	//array shooters
	final Runnable refreshArrayShooters = new Runnable(){
		@Override
		public void run() {
			int temp=Shooting_ArrayMovingView.allSimpleShooters.size();
			
			for(int i=temp;i<Shooting_ArrayMovingView.getMaxNumShips();i++){
				spawnOneShooting_MovingArrayView();
			}
			currentProgressInLevel++;
		}
	};
	
	//dive bombers	
	final Runnable diveBomberOnePerSecond = new Runnable(){
		@Override
		public void run() {
			spawnDiveBomberWave(5,DEFAULT_WAVE_DURATION/5);//spawn for entire wave
			currentProgressInLevel++;
		}
	};
	
	//levels defined in terms of the previous waves
	final Runnable[] level1 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToRight,
			doNothing,
			meteorShowersThatForceUserToLeft,
			meteorsGiantAndSideways,
			meteorsGiantAndSideways,
			meteorShowerLong,
			meteorsOnlyGiants,
			meteorsOnlyGiants,
			levelWavesOver};
	
	final  Runnable[] level2 =level1;
	
	final  Runnable[] level3 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			levelWavesOver};
	
	final  Runnable[] level4 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorSidewaysThisWave,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			doNothing,
			refreshArrayShooters,
			doNothing,
			doNothing,
			diveBomberOnePerSecond,
			diveBomberOnePerSecond,
			levelWavesOver};
	
	final  Runnable[] level5 = {meteorSidewaysOnePerSecondForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			doNothing,
			diveBomberOnePerSecond,
			doNothing,
			diveBomberOnePerSecond,
			boss1,
			levelWavesOver};
	
	final Runnable levels[][] ={level1,level2,level3,level4,level5};
	
}
