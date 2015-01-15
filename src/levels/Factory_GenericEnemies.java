package levels;

import guns.Gun;
import guns.Gun_StraightDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.GameView;
import android.content.Context;
import bullets.Bullet_Basic_LaserShort;

import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_Diagonal_DiveBomberView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_HorizontalLine;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_orbiters.Shooting_OrbiterView;

/**
 * Factory in charge of create enemies with default values and adding that enemy to game layout
 * default values must be scaled as game progresses
 * 
 * @author JAMES LOWREY
 *
 */

public class Factory_GenericEnemies{
	
	public final int BULLET_FREQ_LONG_INTERVAL=10,BULLET_FREQ_SHORT_INTERVAL=5;

	protected int currentLevel=0;
	protected Context ctx;
//	protected InteractiveGameInterface gameInteractivityInterface;
	
	
	public Factory_GenericEnemies(Context context){
		ctx=context;
	} 

	//GET methods
	public  int getLevel(){
		return currentLevel+1;
	}
	
	//DIAGONAL ORBITERS
	protected final  Shooting_DiagonalMovingView spawnFullScreenDiagonalAttacker(){
		final int score=Shooting_DiagonalMovingView.DEFAULT_SCORE;
		final double speedY=Shooting_DiagonalMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_DiagonalMovingView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_DiagonalMovingView.DEFAULT_COLLISION_DAMAGE,
				health=Shooting_DiagonalMovingView.DEFAULT_HEALTH,
				spawnBeneficialObject= Shooting_DiagonalMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Shooting_DiagonalMovingView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Shooting_DiagonalMovingView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Shooting_DiagonalMovingView enemy = new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
				collisionDamage,health,spawnBeneficialObject,
				(int)ctx.getResources().getDimension(R.dimen.ship_diagonal_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_diagonal_width),
				Shooting_DiagonalMovingView.DEFAULT_BACKGROUND);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
		
	}

	protected final Shooting_Diagonal_DiveBomberView spawnDiveBomber(){
		final int score=Shooting_Diagonal_DiveBomberView.DEFAULT_SCORE;
		final double speedY=Shooting_Diagonal_DiveBomberView.DEFAULT_SPEED_Y,
				speedX=Shooting_Diagonal_DiveBomberView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_Diagonal_DiveBomberView.DEFAULT_COLLISION_DAMAGE,
				health=Shooting_Diagonal_DiveBomberView.DEFAULT_HEALTH,
				spawnBeneficialObject= Shooting_Diagonal_DiveBomberView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Shooting_Diagonal_DiveBomberView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Shooting_Diagonal_DiveBomberView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Shooting_Diagonal_DiveBomberView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Shooting_Diagonal_DiveBomberView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Shooting_Diagonal_DiveBomberView enemy =  new Shooting_Diagonal_DiveBomberView(ctx,score,speedY,speedX,
				collisionDamage,health,spawnBeneficialObject,
				(int)ctx.getResources().getDimension(R.dimen.ship_dive_bomber_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_dive_bomber_height),
				Shooting_Diagonal_DiveBomberView.DEFAULT_BACKGROUND);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	//HORIZONTAL ORBITER
	protected final  Orbiter_HorizontalLine spawnHorizontalLineOrbiter(){
		final int score=Orbiter_HorizontalLine.DEFAULT_SCORE;
		final double speedY=Orbiter_HorizontalLine.DEFAULT_SPEED_Y,
				speedX=Orbiter_HorizontalLine.DEFAULT_SPEED_X, 
				collisionDamage=Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,
				health=Orbiter_HorizontalLine.DEFAULT_HEALTH,
				spawnBeneficialObject= Orbiter_HorizontalLine.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Orbiter_HorizontalLine.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Orbiter_HorizontalLine.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Orbiter_HorizontalLine.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Orbiter_HorizontalLine enemy =  new Orbiter_HorizontalLine(ctx,score,speedY,speedX,
				collisionDamage,health,spawnBeneficialObject,
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_horizontal_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_horizontal_height),
				Orbiter_HorizontalLine.DEFAULT_BACKGROUND);


		Gun defaultGun = new Gun_StraightDualShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	//CIRCLE ORBITERS
	/**
	 * use default radius, angular velocity. Pass in pixel x,y desired to orbit
	 * @param orbitPixelX
	 * @param orbitPixelY
	 * @return
	 */
	protected final Shooting_OrbiterView spawnCircularOrbitingView(int radius,int angularVelocity){

		final int orbitRadius=radius;

		final int score=Orbiter_CircleView.DEFAULT_SCORE,
				angVel=angularVelocity;
		final double speedY=Orbiter_CircleView.DEFAULT_SPEED_Y,
				speedX=Orbiter_CircleView.DEFAULT_SPEED_X, 
				collisionDamage=Orbiter_CircleView.DEFAULT_COLLISION_DAMAGE,
				health=Orbiter_CircleView.DEFAULT_HEALTH,
				spawnBeneficialObject= Orbiter_CircleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Orbiter_CircleView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Orbiter_CircleView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Orbiter_CircleView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Orbiter_CircleView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		
		Shooting_OrbiterView enemy =  new Orbiter_CircleView(ctx,score,speedY,speedX,
				collisionDamage,health,
				spawnBeneficialObject,orbitRadius,angVel,
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_circular_height),
				Orbiter_CircleView.DEFAULT_BACKGROUND);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	/**
	 * use default radius, angular velocity, and orbit location
	 * @return
	 */
	protected final Shooting_OrbiterView spawnCircularOrbitingView(){
		return spawnCircularOrbitingView(Orbiter_CircleView.DEFAULT_CIRCLE_RADIUS,Orbiter_CircleView.DEFAULT_ANGULAR_VELOCITY);
	}
	
	//TRIANGLE ORBITERS

	/**
	 * use default triangle angle
	 * @param orbitPixelX
	 * @param orbitPixelY
	 * @return
	 */
	protected final Shooting_OrbiterView spawnTriangularOrbitingView(int angle){
		final int orbitLength=Orbiter_TriangleView.DEFAULT_ORBIT_LENGTH;
	
		final int score=Orbiter_TriangleView.DEFAULT_SCORE;
		final double speedY=Orbiter_TriangleView.DEFAULT_SPEED_Y,
				speedX= Math.tan(Math.toRadians(angle))*speedY, 
				collisionDamage=Orbiter_TriangleView.DEFAULT_COLLISION_DAMAGE,
				health=Orbiter_TriangleView.DEFAULT_HEALTH,
				spawnBeneficialObject= Orbiter_TriangleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Orbiter_TriangleView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Orbiter_TriangleView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Orbiter_TriangleView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Orbiter_TriangleView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Shooting_OrbiterView enemy =  new Orbiter_TriangleView(ctx,score,speedY,speedX,
				collisionDamage,health,
				spawnBeneficialObject,orbitLength,
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_triangular_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_triangular_height),
				Orbiter_TriangleView.DEFAULT_BACKGROUND);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	/**
	 * use default triangle angle and orbiting location
	 * @return
	 */
	protected final Shooting_OrbiterView spawnTriangularOrbitingView(){
		return spawnTriangularOrbitingView(Orbiter_TriangleView.DEFAULT_ANGLE);
	}

	//RECTANGLE ORBITERS
	protected final  Shooting_OrbiterView spawnRectanglularOrbitingView(){
		final int orbitLength=Orbiter_RectangleView.DEFAULT_ORBIT_LENGTH;
	
		final int score=Orbiter_RectangleView.DEFAULT_SCORE;
		final double speedY=Orbiter_RectangleView.DEFAULT_SPEED_Y,
				speedX= Orbiter_RectangleView.DEFAULT_SPEED_X, 
				collisionDamage=Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
				health=Orbiter_RectangleView.DEFAULT_HEALTH,
				spawnBeneficialObject= Orbiter_RectangleView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Orbiter_RectangleView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_SHORT_INTERVAL * Orbiter_RectangleView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Shooting_OrbiterView enemy =  new Orbiter_RectangleView(ctx,score,speedY,speedX,
				collisionDamage,health,
				spawnBeneficialObject,orbitLength,
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_rectangular_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_orbit_rectangular_height),
				Orbiter_RectangleView.DEFAULT_BACKGROUND);

		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	//Shooter ARRAY
	protected final Shooting_ArrayMovingView spawnOneShooting_MovingArrayView(){
		final int score=Shooting_ArrayMovingView.DEFAULT_SCORE;
		final double speedY=Shooting_ArrayMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_ArrayMovingView.DEFAULT_SPEEDX, 
				collisionDamage=Shooting_ArrayMovingView.DEFAULT_COLLISION_DAMAGE,
				health=Shooting_ArrayMovingView.DEFAULT_HEALTH,
				bulletDmg=Shooting_ArrayMovingView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Shooting_ArrayMovingView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * BULLET_FREQ_LONG_INTERVAL * Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL),
				spawnBeneficialFreq=Shooting_ArrayMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH;
		
		
		Shooting_ArrayMovingView enemy = new Shooting_ArrayMovingView(ctx,score,speedY,
				speedX,collisionDamage,health,spawnBeneficialFreq,
				(int)ctx.getResources().getDimension(R.dimen.ship_array_shooter_width),
				(int)ctx.getResources().getDimension(R.dimen.ship_array_shooter_height),
				Shooting_ArrayMovingView.DEFAULT_BACKGROUND);

		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
		
	}
	
	//METEORS
	protected final Gravity_MeteorView spawnMeteor(){

		final int score=Gravity_MeteorView.DEFAULT_SCORE;
				
		final double speedY=Gravity_MeteorView.DEFAULT_SPEED_Y,
				collisionDamage=Gravity_MeteorView.DEFAULT_COLLISION_DAMAGE,
				health=Gravity_MeteorView.DEFAULT_HEALTH,
				spawnBeneficialObject= Gravity_MeteorView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH;
		
		Gravity_MeteorView enemy = new Gravity_MeteorView(ctx,score,speedY,collisionDamage,health,spawnBeneficialObject,
				(int)ctx.getResources().getDimension(R.dimen.meteor_length),
				(int)ctx.getResources().getDimension(R.dimen.meteor_length),
				Gravity_MeteorView.DEFAULT_BACKGROUND);

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}
	
	protected final Meteor_SidewaysView spawnSidewaysMeteor(){
		final int score=Meteor_SidewaysView.DEFAULT_SCORE;
				
		final double speedY=Meteor_SidewaysView.DEFAULT_SPEED_Y,
				collisionDamage=Meteor_SidewaysView.DEFAULT_COLLISION_DAMAGE,
				health=Meteor_SidewaysView.DEFAULT_HEALTH,
				spawnBeneficialObject= Meteor_SidewaysView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				speedX=Meteor_SidewaysView.DEFAULT_SPEED_X;
		
		Meteor_SidewaysView enemy = new Meteor_SidewaysView(ctx,score,speedY,speedX,collisionDamage,health,spawnBeneficialObject,
				(int)ctx.getResources().getDimension(R.dimen.meteor_length),
				(int)ctx.getResources().getDimension(R.dimen.meteor_length),
				Gravity_MeteorView.DEFAULT_BACKGROUND);

//		gameInteractivityInterface.addToForeground(enemy);
		
		return enemy;
	}

}
