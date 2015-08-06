package enemies;

import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import levels.LevelSystem;
import parents.Projectile_GravityView;
import android.widget.RelativeLayout;
import bonuses.BonusView;

public abstract class EnemyView extends Projectile_GravityView{
	
	public static final int MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR = 3;
	public static int numSpawn=0,numRemoved=0,
			DELAY_BTW_SPAWN_IN_LOTS_OF_ENEMIES_WAVE = 500,
			DELAY_AFTER_SPAWN_IN_LOTS_OF_ENEMIES_WAVE = 8000;
	private int score;
	private double probSpawnBeneficialObject;
	
	public EnemyView(RelativeLayout layout,
			int level,
			int scoreForKilling,
			float projectileSpeedY,
			float projectileSpeedX, 
			int projectileDamage,
			int projectileHealth,
			float probSpawnBeneficialObjectUponDeath,
			int width,int height,int imageId) {
		super( layout,
				scaleSpeedY(level,projectileSpeedY),
				scaleSpeedX(level,projectileSpeedX),
				scaleCollisionDamage(level,projectileDamage),
				scaleHealth(level,projectileHealth),
				width, height, imageId);
		
		this.setY( - this.getHeight() /2 );//start all enemies 3/4 way offscreen
		
		numSpawn++;
		score = scaleScore(level, scoreForKilling);
		probSpawnBeneficialObject= scaleProbabilitySpawnBeneficialObjectOnDeath(level,probSpawnBeneficialObjectUponDeath);
		LevelSystem.enemies.add(this);
	}
	
	/**
	 * To be called whenever an Enemyview implements removeGameObject()
	 * NEW BEHAVIOR  :: 
	 * Two cases when an enemy will be removed, fallen off side of screen or hase been killed
	 * If has been killed, check to spawn a random BonusView and increment level score by this.score value
	 * If has fallen off screen, increment level score by this.score/3. I did this to help at lower levels and to give a benefit for dodging
	 */
	@Override
	public void removeGameObject(){		        
		//add this enemy's score to the player's score
		if(this.getHealth()<=0){//died
			((GameActivityInterface)this.getContext()).incrementScore((int) (this.getScoreForKilling()));
			
			if(Math.random()<probSpawnBeneficialObject){//check for random bonus
				final float xAvg = (2 * this.getX()+this.getWidth())/2;
				final float yAvg = (2 * this.getY()+this.getHeight())/2;
				BonusView.displayRandomBonusView(getMyLayout(),xAvg,yAvg);
			}
		} 
		else {//fallen offscreen
			((GameActivityInterface)this.getContext()).incrementScore(this.getScoreForKilling()/3);
		}
		LevelSystem.enemies.remove(this);

		numRemoved++;
		super.removeGameObject();
	}
	
	public void setProbSpawnBeneficialObjectOnDeath(double prob){
		probSpawnBeneficialObject=prob;
	}
	public double getProbSpawnBeneficialObjectOnDeath(){
		return probSpawnBeneficialObject;
	}
	public int getScoreForKilling(){
		return score;
	}
	public void setScoreValue(int newScore){
		score=newScore;
	}
	

	//SCALING METHODS - every enemy automatically scales base stats, can be overwritten in the individual enemy's class
	protected static int scaleHealth(int level,int defaultHealth){
		int value = 0;
		
		if(level < AttributesOfLevels.LEVELS_LOW) {
			value = defaultHealth;
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (defaultHealth * MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR/2.1 );			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (defaultHealth * MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR/1.3 );			
		}else{
			value = (int) (defaultHealth * MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR * 1.3 );			
		}
		
		return value;
	}
	
	public static int scaleScore(int level,int defaultScore){
		int value = 0;

		if(level < AttributesOfLevels.LEVELS_BEGINNER) {
			value = defaultScore;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			value = (int) (defaultScore  * 1.1);
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (defaultScore * 1.25 );			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (defaultScore * 1.55 );			
		}else{
			value = (int) (defaultScore * 1.85 );			
		}
		
		return value;
	}
	
	protected static float scaleSpeedY(int level, float defaultSpeedY){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_MED){
			value = defaultSpeedY;
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (defaultSpeedY * 1.05);		
		}else{
			value = (float) (defaultSpeedY * 1.1);	
		}
		 
		return value;
	}
	
	protected static float scaleSpeedX(int level,float defaultSpeedX){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_MED){
			value = defaultSpeedX;
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (defaultSpeedX * 1.05);		
		}else{
			value = (float) (defaultSpeedX * 1.1);	
		}
		
		return value;
	}
	

	protected static int scaleCollisionDamage(int level,int defaultCollisionDamage){
		int value = 0;
		
		if(level < AttributesOfLevels.LEVELS_LOW){
			value = defaultCollisionDamage;
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (int) (defaultCollisionDamage * 1.4);			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (int) (defaultCollisionDamage * 2.1);			
		}else{
			value = (int) (defaultCollisionDamage * 3);			
		}
		
		return value;
	}
	

	protected static float scaleProbabilitySpawnBeneficialObjectOnDeath(int level, float defaultProbSpawnBeneficialObjectOnDeath){
		float value = 0;
		
		if(level < AttributesOfLevels.LEVELS_BEGINNER){
			value = defaultProbSpawnBeneficialObjectOnDeath;
		}else if(level < AttributesOfLevels.LEVELS_LOW) {
			value = (float) (defaultProbSpawnBeneficialObjectOnDeath * .95 );
		}else if(level < AttributesOfLevels.LEVELS_MED){
			value = (float) (defaultProbSpawnBeneficialObjectOnDeath * .85);			
		}else if(level < AttributesOfLevels.LEVELS_HIGH){
			value = (float) (defaultProbSpawnBeneficialObjectOnDeath * .7);			
		}else{
			value = (float) (defaultProbSpawnBeneficialObjectOnDeath * .5);			
		}
		
		return value;
	}
}
