package levels;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemy_types.Shooting_ArrayMovingView;
import enemy_types.Shooting_DiagonalMovingView;
import enemy_types.Shooting_Diagonal_DiveBomberView;
import enemy_types_non_shooters.Gravity_MeteorView;
import enemy_types_orbiters.Orbiter_CircleView;
import enemy_types_orbiters.Orbiter_RectangleView;
import enemy_types_orbiters.Orbiter_TriangleView;
import enemy_types_orbiters.Shooting_OrbiterView;

/**
 * Factory in charge of create enemies with default values and adding that enemy to game layout
 * default values must be scaled as game progresses
 * 
 * @author JAMES LOWREY
 *
 */

public class EnemyFactory{
	
	public static final int LONG_INTERVAL=10,SHORT_INTERVAL=5;
	
	private Context ctx;
	private RelativeLayout gameLayout;
	private Levels levelInfo = new Levels();
    
	/* old runnables
    
private Runnable spawnOrbitersRunnable = new Runnable(){
		@Override
		public void run() {
//			Shooting_OrbiterView circle = EnemyFactory.this.spawnCirclingOrbitingView();
			Shooting_OrbiterView triangle = EnemyFactory.this.spawnTriangularOrbitingView();
			Shooting_OrbiterView rectangle = EnemyFactory.this.spawnRectangleOrbitingView();
			
//    		gameLayout.addView(circle,1);
    		gameLayout.addView(triangle,1);
    		gameLayout.addView(rectangle,1);
    		
//    		GameActivity.enemies.add(circle);
    		GameActivity.enemies.add(triangle);
    		GameActivity.enemies.add(rectangle);
			
			enemySpawnHandler.postDelayed(this, calculateOrbiterSpawnInterval());
		}
    };
    
    private Runnable meteorSpawningRunnable = new Runnable(){
    	@Override
        public void run() {
    		Gravity_MeteorView meteor = spawnMeteorView();
    		
    		gameLayout.addView(meteor,1);
    		GameActivity.enemies.add(meteor);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, calculateMeteorSpawnInterval());
    	}
	};
	
	Runnable spawnShooterArrayRunnable = new Runnable(){
    	@Override
        public void run() {
    		final int numShootersAlive = Shooting_ArrayMovingView.allSimpleShooters.size();
    		final int maxNumShooters = Shooting_ArrayMovingView.getMaxNumShips();
//    		final int numShootersAliveCutoff = 4;
    		
    		if(levelInfo.getDifficulty()>0 && numShootersAlive<maxNumShooters){
    			spawnAllSimpleShooters();
//    			//if num shooters< .33 of the max, there is a 33% chance to respawn all shooters
//    			if(numShootersAlive<(maxNumShooters/numShootersAliveCutoff) && Math.random()<0.33 ){
//    				spawnAllSimpleShooters();
//    			}
//    			//otherwise, respawn just 1 simple shooter
//    			else{
//	    			SimpleEnemyShooterArray shooter = new SimpleEnemyShooterArray(ctx);
//	    			gameLayout.addView(shooter,1);
//	        		GameActivity.enemies.add(shooter);
//    			}
			}
    		 
    		enemySpawnHandler.postDelayed(this, calculateMovingSideToSideShooterSpawnInterval());
    	}
	};
	
	Runnable spawnDiagonalShooterRunnable = new Runnable(){
		@Override
		public void run() {
			
			Shooting_DiagonalMovingView shooter = EnemyFactory.this.spawnFullScreenDiagonalAttacker();
			
			gameLayout.addView(shooter,1);
    		GameActivity.enemies.add(shooter);
			
			Shooting_DiagonalMovingView shooter2 = EnemyFactory.this.spawnDiveBomber();
			
			gameLayout.addView(shooter2,1);
    		GameActivity.enemies.add(shooter2);
    		
			enemySpawnHandler.postDelayed(this, calculatDiagonalShooterSpawnInterval());
		}
	};
	*/
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		
		gameLayout= gameScreen;
		
		Shooting_ArrayMovingView.resetSimpleShooterArray();
		Shooting_ArrayMovingView.startMovingAllShooters();
	} 
	
	/*old spawning methods
	public void stopSpawning(){
		enemySpawnHandler.removeCallbacks(null);
	}
	
	public void beginSpawning(){
		enemySpawnHandler.postDelayed(meteorSpawningRunnable,calculateMeteorSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnShooterArrayRunnable,calculateMovingSideToSideShooterSpawnInterval());
	    enemySpawnHandler.postDelayed(spawnDiagonalShooterRunnable,0);
	    enemySpawnHandler.postDelayed(spawnOrbitersRunnable,calculateOrbiterSpawnInterval());
	}  
	
	//GET INTERVAL SPAWN MONSTERS
	
	private long calculateMeteorSpawnInterval(){
		final int meteorInterval = 2000;
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*4000);
	}
	
	private long calculateOrbiterSpawnInterval(){
		final int meteorInterval = 9000;
		return (long) (meteorInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	
	private long calculateMovingSideToSideShooterSpawnInterval(){
		final int simpleArrayShooterInterval = 15000;
		return (long) (simpleArrayShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*3000);
	}
	
	private long calculatDiagonalShooterSpawnInterval(){
		final int diagonalShooterInterval=3000; 
		return (long) (diagonalShooterInterval/(Math.sqrt(levelInfo.getDifficulty()))+Math.random()*4000);
	}
	*/
	
	//SPAWN MONSTER METHODS
	
	protected Shooting_DiagonalMovingView spawnFullScreenDiagonalAttacker(){
		final int diff = levelInfo.getDifficulty();
		
		final int score=Shooting_DiagonalMovingView.DEFAULT_SCORE*diff;
		final double speedY=Shooting_DiagonalMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_DiagonalMovingView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_DiagonalMovingView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Shooting_DiagonalMovingView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Shooting_DiagonalMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Shooting_DiagonalMovingView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_DiagonalMovingView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * SHORT_INTERVAL * Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_DiagonalMovingView shooter = new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
				collisionDamage,health,height,width,spawnBeneficialObject,
				bulletFreq,bulletSpdY,bulletDmg);

		gameLayout.addView(shooter,1);
		
		return shooter;
		
	}

	protected Shooting_Diagonal_DiveBomberView spawnDiveBomber(){
		final int diff = levelInfo.getDifficulty();
		
		final int score=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SCORE*diff;
		final double speedY=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPEED_Y,
				speedX=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPEED_X, 
				collisionDamage=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_COLLISION_DAMAGE*diff,
				health=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_HEALTH*diff,
				spawnBeneficialObject= Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * SHORT_INTERVAL * Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		
		final float height=ctx.getResources().getDimension(R.dimen.dive_bomber1_height),
				width=ctx.getResources().getDimension(R.dimen.dive_bomber1_width);
		
		Shooting_Diagonal_DiveBomberView diveBomber =  new Shooting_Diagonal_DiveBomberView(ctx,score,speedY,speedX,
				collisionDamage,health,height,width,spawnBeneficialObject,bulletFreq,
				bulletSpdY,bulletDmg);

		gameLayout.addView(diveBomber,1);
		
		return diveBomber;
	}
	
	protected Shooting_OrbiterView spawnCirclingOrbitingView(int radius,int angularVelocity){
		final int diff = levelInfo.getDifficulty();

		final int orbitRadius=radius;

		final int score=Orbiter_CircleView.DEFAULT_SCORE*diff,
				angVel=angularVelocity;
		final double speedY=Orbiter_CircleView.DEFAULT_SPEED_Y,
				speedX=Orbiter_CircleView.DEFAULT_SPEED_X, 
				collisionDamage=Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Orbiter_CircleView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Orbiter_CircleView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Orbiter_CircleView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Orbiter_CircleView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * SHORT_INTERVAL * Orbiter_CircleView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView orbiter =  new Orbiter_CircleView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,
				spawnBeneficialObject,bulletSpdY,bulletDmg,orbitRadius,angVel);

		gameLayout.addView(orbiter,1);
		
		return orbiter;
	}
	
	protected Shooting_OrbiterView spawnCirclingOrbitingView(){
		return spawnCirclingOrbitingView(Orbiter_CircleView.DEFAULT_CIRCLE_RADIUS,Orbiter_CircleView.DEFAULT_ANGULAR_VELOCITY);
	}
	
	protected Shooting_OrbiterView spawnTriangularOrbitingView(int angle){
		final int diff = levelInfo.getDifficulty();

		final int orbitLength=Orbiter_TriangleView.DEFAULT_ORBIT_LENGTH;
	
		final int score=Orbiter_TriangleView.DEFAULT_SCORE*diff;
		final double speedY=Orbiter_TriangleView.DEFAULT_SPEED_Y,
				speedX= Math.tan(Math.toRadians(angle))*speedY, 
				collisionDamage=Orbiter_TriangleView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Orbiter_TriangleView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Orbiter_TriangleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Orbiter_TriangleView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Orbiter_TriangleView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Orbiter_TriangleView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * SHORT_INTERVAL * Orbiter_TriangleView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView orbiter =  new Orbiter_TriangleView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,
				spawnBeneficialObject,bulletSpdY,bulletDmg,orbitLength);

		gameLayout.addView(orbiter,1);
		
		return orbiter;
	}
	
	protected Shooting_OrbiterView spawnTriangularOrbitingView(){
		return spawnTriangularOrbitingView(Orbiter_TriangleView.DEFAULT_ANGLE);
	}

	protected Shooting_OrbiterView spawnRectangleOrbitingView(){
		final int diff = levelInfo.getDifficulty();

		final int orbitLength=Orbiter_RectangleView.DEFAULT_ORBIT_LENGTH;
	
		final int score=Orbiter_RectangleView.DEFAULT_SCORE*diff;
		final double speedY=Orbiter_RectangleView.DEFAULT_SPEED_Y,
				speedX= Orbiter_RectangleView.DEFAULT_SPEED_X, 
				collisionDamage=Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Orbiter_RectangleView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Orbiter_RectangleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff,
				bulletDmg=Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Orbiter_RectangleView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * SHORT_INTERVAL * Orbiter_RectangleView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView orbiter =  new Orbiter_RectangleView(ctx,score,speedY,speedX,
				collisionDamage,health,bulletFreq,height,width,
				spawnBeneficialObject,bulletSpdY,bulletDmg,orbitLength);

		gameLayout.addView(orbiter,1);
		
		return orbiter;
	}
	
	protected Shooting_ArrayMovingView spawnOneShooting_MovingArrayView(){
		final int diff = levelInfo.getDifficulty();

		final int score=Shooting_ArrayMovingView.DEFAULT_SCORE*diff;
		final double speedY=Shooting_ArrayMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_ArrayMovingView.DEFAULT_SPEEDX, 
				collisionDamage=Shooting_ArrayMovingView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Shooting_ArrayMovingView.DEFAULT_HEALTH*diff,
				bulletDmg=Shooting_ArrayMovingView.DEFAULT_BULLET_DAMAGE*diff,
				bulletSpdY=Shooting_ArrayMovingView.DEFAULT_BULLET_SPEED_Y*Math.sqrt(diff),
				bulletFreq=Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL/Math.sqrt(diff)+
				(Math.random() * LONG_INTERVAL * Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL)/Math.sqrt(diff),
				spawnBeneficialFreq=Shooting_ArrayMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff;
		
		final float height = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_ArrayMovingView shooter = new Shooting_ArrayMovingView(ctx,score,speedY,speedX,collisionDamage,health,
				height,width,spawnBeneficialFreq,bulletFreq,bulletDmg,bulletSpdY);

		gameLayout.addView(shooter,1);
		
		return shooter;
		
	}
	
	protected Gravity_MeteorView spawnMeteor(){
		final int diff = levelInfo.getDifficulty();

		final int score=Gravity_MeteorView.DEFAULT_SCORE*diff;
				
		final double speedY=Gravity_MeteorView.DEFAULT_SPEED_Y,
				speedX=Gravity_MeteorView.DEFAULT_SPEED_X, 
				collisionDamage=Gravity_MeteorView.DEFAULT_COLLISION_DAMAGE*diff,
				health=Gravity_MeteorView.DEFAULT_HEALTH*diff,
				spawnBeneficialObject= Gravity_MeteorView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH*diff;
		
		Gravity_MeteorView met = new Gravity_MeteorView(ctx,score,speedY,speedX,collisionDamage,health,spawnBeneficialObject);

		gameLayout.addView(met,1);
		
		return met;
	}
	
	protected Gravity_MeteorView spawnSidewaysMeteor(){
		Gravity_MeteorView met = spawnMeteor();
		
		double 	speedX=Gravity_MeteorView.DEFAULT_SPEED_X_MOVING_SIDEWAYS;
		if(Math.random()<0.5){speedX*=-1;}
		met.setSpeedX(speedX);

		gameLayout.addView(met,1);
		
		return met;
	}
	
	protected Gravity_MeteorView spawnGiantMeteor(){
		Gravity_MeteorView giant = spawnMeteor();
		
		//change width and height. set X and Y positions
		final int width = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_width);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_height);
		
		giant.setLayoutParams(new LayoutParams(width,height));
		giant.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));
		
		//set damage and health to 200, score to 20
		giant.setDamage(150);
		giant.heal(150-Gravity_MeteorView.DEFAULT_HEALTH);
		giant.setScoreValue(20);
		
		return giant;
	}


}
