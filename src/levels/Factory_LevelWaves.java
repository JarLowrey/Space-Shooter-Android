package levels;

import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;

public class Factory_LevelWaves extends Factory_Waves{

	public int DEFAULT_WAVE_DURATION=5000;
	
	protected int currentProgressInLevel;
	
	public Factory_LevelWaves(Context context){
		super( context );
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
	final Runnable meteorSidewaysForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnSidewaysMeteorsWave( getCurrentLevelLength() /2000 ,2000);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorsStraightForWholeLevel = new Runnable(){
		@Override
		public void run() {
			spawnStraightFallingMeteorsAtRandomXPositionsWave( getCurrentLevelLength() /2000 ,2000);
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
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors/=2;
			numMeteors-=2;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			
			spawnMeteorShower(numMeteors,400,true);
			spawnMeteorShower(numMeteors,400,false);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorShowersThatForceUserToRight = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,true);
			currentProgressInLevel++;
		}
	};
	final Runnable meteorShowersThatForceUserToLeft = new Runnable(){
		@Override
		public void run() {
			int numMeteors = (int) (MainActivity.getWidthPixels()/ctx.getResources().getDimension(R.dimen.meteor_length));
			numMeteors-=4;
			spawnMeteorShower(numMeteors,DEFAULT_WAVE_DURATION/numMeteors,false);
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
				new Shooting_ArrayMovingView(ctx);
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
	
	
	//circular orbiters
	
	final Runnable circlesThreeOrbiters = new Runnable(){
		@Override
		public void run() {
			
		}		
	};
	//levels defined in terms of 5second  waves
	final Runnable[] level1 = {meteorSidewaysForWholeLevel,
			meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToLeft,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			levelWavesOver
		};
	
	final  Runnable[] level2 ={meteorSidewaysForWholeLevel,
			meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToRight,
			doNothing,
			meteorShowersThatForceUserToLeft,
			meteorsGiantAndSideways,
			meteorsGiantAndSideways,
			meteorShowerLong,
			meteorsOnlyGiants,
			meteorsOnlyGiants,
			levelWavesOver
		};
	
	final  Runnable[] level3 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			meteorShowersThatForceUserToMiddle,
			diveBomberOnePerSecond,
			diveBomberOnePerSecond,
			diveBomberOnePerSecond,
			levelWavesOver
		};
	
	final  Runnable[] level4 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToMiddle,
			refreshArrayShooters,
			doNothing,
			doNothing,
			diveBomberOnePerSecond,
			diveBomberOnePerSecond,
			doNothing,
			doNothing,
			doNothing,
			levelWavesOver
		};
	
	final  Runnable[] level5 = {meteorSidewaysForWholeLevel,
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
			levelWavesOver
		};
	
	final  Runnable[] level6 = {meteorSidewaysForWholeLevel,
			meteorShowersThatForceUserToRight,
			meteorShowersThatForceUserToLeft,
			refreshArrayShooters,
			doNothing,
			diveBomberOnePerSecond,
			doNothing,
			diveBomberOnePerSecond,
			boss1_missileAndDualLaser,
			levelWavesOver
		};
	
	final Runnable[] level7 = {
			boss3,
			levelWavesOver
		};
	
	final Runnable levels[][] ={level7};
	
}
