package levels;

import enemies.Shooting_PauseAndMove;
import support.KillableRunnable;
import android.content.Context;

public class Levels extends Factory_Bosses{
	
	private int numLevels;
	private int[] wavesInEachLevel; 
	
	public Levels(Context context) {
		super(context);
		
		KillableRunnable[][] lvls = scriptedLevels();
		numLevels = lvls.length;
		wavesInEachLevel = new int[numLevels];
		for(int i=0;i<lvls.length;i++){
			wavesInEachLevel[i] = lvls[i].length;
		}
	}

	public int getMaxLevel() {
		return numLevels;
	}
	public int getCurrentLevelLengthMilliseconds(){
		return wavesInEachLevel[getLevel()]*DEFAULT_WAVE_DURATION;
	}
	public int getNumWavesInLevel(int level){
		if(level>=0 && level<numLevels){
			return wavesInEachLevel[level];			
		}else{
			return 0;		
		}
	}
	
	@Override
	public boolean areLevelWavesCompleted(){
		return getWave() == wavesInEachLevel[ getLevel() ]; 
	}
	
	public void startLevelSpawning(){
		final KillableRunnable[][] lvls = scriptedLevels();
		
		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				if (!areLevelWavesCompleted()) {
					if(getLevel()<lvls.length){
						KillableRunnable r = lvls[getLevel()][getWave()];
						spawningHandler.post( r );
					}else{
						
					}
					incrementWave();
					spawningHandler.postDelayed(this,DEFAULT_WAVE_DURATION);
				}
			}
			
		});
	}
	 

	private KillableRunnable[] level_1(){
		KillableRunnable[] r =
			{
//				new SpawnDefaultEnemyRunnable( ( 6*DEFAULT_WAVE_DURATION ) /2000 ,2000,Meteor_SidewaysView.class,spawningHandler,ctx),
//				new SpawnDefaultEnemyRunnable( ( 5*DEFAULT_WAVE_DURATION ) /2000 ,2000,Meteor_SidewaysView.class,spawningHandler,ctx),
				meteorSidewaysForWholeLevel(),			
				meteorSidewaysForWholeLevel(),
				meteorShowersThatForceUserToMiddle(),
				meteorShowersThatForceUserToLeft(),
				meteorShowersThatForceUserToRight(),
				meteorShowersThatForceUserToLeft()
			};
		return r;
	}
	private KillableRunnable[] level_2(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorSidewaysForWholeLevel(),
				meteorShowersThatForceUserToMiddle(),
				meteorShowersThatForceUserToRight(),
				doNothing(),
				meteorShowersThatForceUserToLeft(),
				meteorsGiantAndSideways(),
				meteorsGiantAndSideways(),
				meteorShowerLong(),
				meteorsGiant(),
				meteorsGiant()
			};
		return r;
	}
	private KillableRunnable[] level_3(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorShowersThatForceUserToMiddle(),
				meteorShowersThatForceUserToMiddle(),
				meteorShowersThatForceUserToMiddle(),
				diagonalFullScreen(),
				diagonalFullScreen(),
				diagonalColumns(),
				diagonalColumns()
			};
		return r;
	}
	private KillableRunnable[] level_4(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorShowersThatForceUserToMiddle(),
				refreshArrayShooters(),
				doNothing(),
				doNothing(),
				doNothing(),
			};
		return r;
	}
	private KillableRunnable[] level_5(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorShowersThatForceUserToMiddle(),
				refreshArrayShooters(),
				meteorShowersThatForceUserToMiddle(),
				doNothing(),
				doNothing(),
				doNothing(),
				diagonalFullScreen(),
				diagonalColumns()
			};
		return r;
	}
	private KillableRunnable[] level_6(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorSidewaysThisWave(),
				meteorShowersThatForceUserToMiddle(),
				refreshArrayShooters(),
				doNothing(),
				doNothing(),
				trackingEnemy(),
				trackingEnemy(),
				doNothing(),
				doNothing(),
				trackingEnemy(),
				doNothing(),
				doNothing(),
				trackingEnemy()
			};
		return r;
	}
	private KillableRunnable[] level_7(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),			
				meteorShowersThatForceUserToRight(),	
				meteorShowersThatForceUserToLeft(),
				refreshArrayShooters(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				diagonalColumns(),
				boss1(),
				doNothing(),
				doNothing(),
				trackingEnemy(),
				doNothing(),
				trackingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_8(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),					
				boss1(),
				meteorsGiantAndSideways(),
				meteorsGiantAndSideways(),
				meteorShowersThatForceUserToMiddle(),
				trackingEnemy(),
				trackingEnemy(),
				trackingEnemy(),
				boss1(),
				trackingEnemy(),
				trackingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_9(){
		KillableRunnable[] r =
			{
				meteorSidewaysForWholeLevel(),					
				boss1(),
				doNothing(),
				doNothing(),
				doNothing(),
				meteorShowersThatForceUserToMiddle(),
				trackingEnemy(),
				trackingEnemy(),
				doNothing(),
				doNothing(),
				meteorShowersThatForceUserToMiddle(),
				boss2()
			};
		return r;		
	}
	private KillableRunnable[] level_10(){
		KillableRunnable[] r =
			{

				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				doNothing(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				doNothing(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class)
			};
		return r;		
	}
	private KillableRunnable[] level_11(){
		KillableRunnable[] r =
			{
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				diagonalFullScreen(),
				trackingEnemy(),
				doNothing(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				diagonalFullScreen(),
				diagonalColumns(),
				trackingEnemy(),
				trackingEnemy(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				trackingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_12(){
		KillableRunnable[] r =
			{
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				diagonalFullScreen(),
				trackingEnemy(),
				doNothing(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				diagonalFullScreen(),
				diagonalColumns(),
				trackingEnemy(),
				trackingEnemy(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				trackingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_13(){
		KillableRunnable[] r =
			{
				refreshArrayShooters(),
				trackingEnemy(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				trackingEnemy(),
				trackingEnemy(),
				doNothing(),
				doNothing(), 
				boss3(),
				doNothing(),
				doNothing(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				doNothing()
			};
		return r;		
	}
	private KillableRunnable[] level_14(){
		KillableRunnable[] r =
			{
				meteorsGiantAndSideways(), 
				meteorsGiantAndSideways(),
				meteorsGiantAndSideways(),
				doNothing(),
				boss4(),
				doNothing(),
				doNothing(),
				rect(),
				rect(),
				trackingEnemy(),
				doNothing(),
				trackingEnemy(),
				doNothing(),
				trackingEnemy(),
				doNothing(),
				trackingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_15(){
		KillableRunnable[] r =
			{
				refreshArrayShooters(),
				doNothing(),
				doNothing(),
				trackingEnemy(),
				boss1(),
				boss1(),
				diagonalFullScreen(),
				diagonalFullScreen(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				boss1()
			};
		return r;		
	}
	private KillableRunnable[] level_16(){
		KillableRunnable[] r =
			{
				rect(),
				rect(),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				spawnEnemyWithDefaultConstructorParameters(5,DEFAULT_WAVE_DURATION/5,Shooting_PauseAndMove.class),
				doNothing(),
				doNothing(),
				doNothing(),
				tri(),
				tri(),
				doNothing(),
				doNothing(),
				rect(),
				doNothing(),
				circlesThreeOrbiters(),
				doNothing(),
				doNothing(),
				circlesTwoOrbiters(),
				doNothing(),
				doNothing(),
				circlesOneOrbiters(),
				tri()
			};
		return r;		
	}
	private KillableRunnable[] level_17(){
		KillableRunnable[] r =
			{
				boss1(),
				boss1(),
				doNothing(),
				doNothing(),
				boss2(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				boss3(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				boss4(),
				doNothing()	
			};
		return r;		
	}
	private KillableRunnable[] level_18(){
		KillableRunnable[] r =
			{
				boss5(),
				doNothing(),
				doNothing() 
			};
		return r;		
	}    
	
	public KillableRunnable[][] scriptedLevels(){
		KillableRunnable[][] r = {
   
				level_1(),
				level_2(),
				level_3(),
				level_4(),
				level_5(),
				level_6(),
				level_7(),
				level_8(),
				level_9(),
				level_10(),
				level_11(), 
				level_12(),
				level_13(), 
				level_14(),
				level_15(),
				level_16(),
				level_17(),
				level_18()
		};
		return r;
	}
	
	
}
