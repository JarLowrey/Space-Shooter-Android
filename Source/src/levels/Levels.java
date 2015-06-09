package levels;

import support.KillableRunnable;
import android.content.Context;

public class Levels extends Factory_Bosses{
	
	private int numLevels;
	private int[] wavesInEachLevel; 
	
	public Levels(Context context) {
		super(context);
		
		KillableRunnable[][] lvls = levels();
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
	
	/**
	 * DOES NOT WORK. When exit activity and re-enter quickly
	 */
	public void startLevelSpawning(){
		final KillableRunnable[][] lvls = levels();
		
		spawningHandler.post(new KillableRunnable() {
			@Override
			public void doWork() {
				if (!areLevelWavesCompleted()) {//check isKilled() to ensure Runnable cannot progress
					KillableRunnable r = lvls[getLevel()][getWave()];
					spawningHandler.post( r );
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
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				boss1(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy()
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
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
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
				pauseAndShoot(),
				pauseAndShoot(),
				pauseAndShoot(),
				doNothing(),
				pauseAndShoot(),
				pauseAndShoot(),
				pauseAndShoot(),
				doNothing(),
				pauseAndShoot(),
				pauseAndShoot()
			};
		return r;		
	}
	private KillableRunnable[] level_11(){
		KillableRunnable[] r =
			{
				pauseAndShoot(),
				pauseAndShoot(),
				diagonalFullScreen(),
				trackingAcceleratingEnemy(),
				doNothing(),
				pauseAndShoot(),
				diagonalFullScreen(),
				diagonalColumns(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				pauseAndShoot(),
				pauseAndShoot(),
				trackingAcceleratingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_12(){
		KillableRunnable[] r =
			{
				pauseAndShoot(),
				pauseAndShoot(),
				diagonalFullScreen(),
				trackingAcceleratingEnemy(),
				doNothing(),
				pauseAndShoot(),
				diagonalFullScreen(),
				diagonalColumns(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				pauseAndShoot(),
				pauseAndShoot(),
				trackingAcceleratingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_13(){
		KillableRunnable[] r =
			{
				refreshArrayShootersStaggered(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				trackingAcceleratingEnemy(),
				doNothing(),
				doNothing(),
				doNothing(),
				doNothing(),
				boss3(),
				doNothing(),
				doNothing(),
				pauseAndShoot(),
				rect(),
				doNothing(),
				pauseAndShoot()
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
				trackingAcceleratingEnemy(),
				doNothing(),
				trackingAcceleratingEnemy(),
				doNothing(),
				trackingAcceleratingEnemy(),
				doNothing(),
				trackingAcceleratingEnemy()
			};
		return r;		
	}
	private KillableRunnable[] level_15(){
		KillableRunnable[] r =
			{
				refreshArrayShootersStaggered(),
				doNothing(),
				doNothing(),
				trackingAcceleratingEnemy(),
				boss1(),
				boss1(),
				diagonalFullScreen(),
				diagonalFullScreen(),
				pauseAndShoot(),
				pauseAndShoot(),
				pauseAndShoot(),
				boss1()
			};
		return r;		
	}
	private KillableRunnable[] level_16(){
		KillableRunnable[] r =
			{
				rect(),
				rect(),
				pauseAndShoot(),
				pauseAndShoot(),
				doNothing(),
				doNothing(),
				doNothing(),
				pauseAndShoot(),
				tri(),
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
				circlesOneOrbiters()	
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
	
	public KillableRunnable[][] levels(){
		KillableRunnable[][] r = {

				level_1()
//				level_2(),
//				level_3(),
//				level_4(),
//				level_5(),
//				level_6(),
//				level_7(),
//				level_8(),
//				level_9(),
//				level_10(),
//				level_11(),
//				level_12(),
//				level_13(),
//				level_14(),
//				level_15(),
//				level_16(),
//				level_17(),
//				level_18()
		};
		return r;
	}
	
	
}
