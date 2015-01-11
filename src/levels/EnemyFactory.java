package levels;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic_LaserShort;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_ArrayMovingView;
import enemies.Shooting_DiagonalMovingView;
import enemies.Shooting_Diagonal_DiveBomberView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_CircleView;
import enemies_orbiters.Orbiter_RectangleView;
import enemies_orbiters.Orbiter_TriangleView;
import enemies_orbiters.Shooting_OrbiterView;
import guns.Gun;
import guns.Gun_StraightSingleShot;

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
	
	public EnemyFactory(Context context,RelativeLayout gameScreen){
		ctx=context;
		
		gameLayout= gameScreen;
	} 
	
	//DIAGONAL ORBITERS
	protected Shooting_DiagonalMovingView spawnFullScreenDiagonalAttacker(){
		final int score=Shooting_DiagonalMovingView.DEFAULT_SCORE;
		final double speedY=Shooting_DiagonalMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_DiagonalMovingView.DEFAULT_SPEED_X, 
				collisionDamage=Shooting_DiagonalMovingView.DEFAULT_COLLISION_DAMAGE,
				health=Shooting_DiagonalMovingView.DEFAULT_HEALTH,
				spawnBeneficialObject= Shooting_DiagonalMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Shooting_DiagonalMovingView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Shooting_DiagonalMovingView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * SHORT_INTERVAL * Shooting_DiagonalMovingView.DEFAULT_BULLET_FREQ_INTERVAL);
		
		Shooting_DiagonalMovingView enemy = new Shooting_DiagonalMovingView(ctx,score,speedY,speedX,
				collisionDamage,health,spawnBeneficialObject);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();
		
		gameLayout.addView(enemy,1);
		
		return enemy;
		
	}

	protected Shooting_Diagonal_DiveBomberView spawnDiveBomber(){
		final int score=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SCORE;
		final double speedY=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPEED_Y,
				speedX=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPEED_X, 
				collisionDamage=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_COLLISION_DAMAGE,
				health=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_HEALTH,
				spawnBeneficialObject= Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				bulletDmg=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_DAMAGE,
				bulletSpdY=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_SPEED_Y,
				bulletFreq=Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL+
				(Math.random() * SHORT_INTERVAL * Shooting_Diagonal_DiveBomberView.DEFAULT_DIVE_BOMBER_BULLET_FREQ_INTERVAL);
		
		Shooting_Diagonal_DiveBomberView diveBomber =  new Shooting_Diagonal_DiveBomberView(ctx,score,speedY,speedX,
				collisionDamage,health,spawnBeneficialObject);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, diveBomber, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		diveBomber.addGun(defaultGun);
		
		gameLayout.addView(diveBomber,1);
		
		return diveBomber;
	}

	//CIRCLE ORBITERS
	/**
	 * use default radius, angular velocity. Pass in pixel x,y desired to orbit
	 * @param orbitPixelX
	 * @param orbitPixelY
	 * @return
	 */
	protected Shooting_OrbiterView spawnCircularOrbitingView(int radius,int angularVelocity){

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
				(Math.random() * SHORT_INTERVAL * Orbiter_CircleView.DEFAULT_BULLET_FREQ_INTERVAL);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView enemy =  new Orbiter_CircleView(ctx,score,speedY,speedX,
				collisionDamage,health,height,width,
				spawnBeneficialObject,orbitRadius,angVel);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();
		
		gameLayout.addView(enemy,1);
		
		return enemy;
	}
	
	/**
	 * use default radius, angular velocity, and orbit location
	 * @return
	 */
	protected Shooting_OrbiterView spawnCircularOrbitingView(){
		return spawnCircularOrbitingView(Orbiter_CircleView.DEFAULT_CIRCLE_RADIUS,Orbiter_CircleView.DEFAULT_ANGULAR_VELOCITY);
	}
	
	//TRIANGLE ORBITERS

	/**
	 * use default triangle angle
	 * @param orbitPixelX
	 * @param orbitPixelY
	 * @return
	 */
	protected Shooting_OrbiterView spawnTriangularOrbitingView(int angle){
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
				(Math.random() * SHORT_INTERVAL * Orbiter_TriangleView.DEFAULT_BULLET_FREQ_INTERVAL);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView enemy =  new Orbiter_TriangleView(ctx,score,speedY,speedX,
				collisionDamage,health,height,width,
				spawnBeneficialObject,orbitLength);


		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();
		
		gameLayout.addView(enemy,1);
		
		return enemy;
	}
	
	/**
	 * use default triangle angle and orbiting location
	 * @return
	 */
	protected Shooting_OrbiterView spawnTriangularOrbitingView(){
		return spawnTriangularOrbitingView(Orbiter_TriangleView.DEFAULT_ANGLE);
	}

	//RECTANGLE ORBITERS
	protected Shooting_OrbiterView spawnRectanglularOrbitingView(){
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
				(Math.random() * SHORT_INTERVAL * Orbiter_RectangleView.DEFAULT_BULLET_FREQ_INTERVAL);
		final float height=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width=ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_OrbiterView enemy =  new Orbiter_RectangleView(ctx,score,speedY,speedX,
				collisionDamage,health,height,width,
				spawnBeneficialObject,orbitLength);

		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();
		
		gameLayout.addView(enemy,1);
		
		return enemy;
	}
	
	//Shooter ARRAY
	protected Shooting_ArrayMovingView spawnOneShooting_MovingArrayView(){
		final int score=Shooting_ArrayMovingView.DEFAULT_SCORE;
		final double speedY=Shooting_ArrayMovingView.DEFAULT_SPEED_Y,
				speedX=Shooting_ArrayMovingView.DEFAULT_SPEEDX, 
				collisionDamage=Shooting_ArrayMovingView.DEFAULT_COLLISION_DAMAGE,
				health=Shooting_ArrayMovingView.DEFAULT_HEALTH,
				bulletDmg=Shooting_ArrayMovingView.DEFAULT_BULLET_DAMAGE,
				bulletSpdY=Shooting_ArrayMovingView.DEFAULT_BULLET_SPEED_Y,
				bulletFreq=Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL+
				(Math.random() * LONG_INTERVAL * Shooting_ArrayMovingView.DEFAULT_BULLET_FREQ_INTERVAL),
				spawnBeneficialFreq=Shooting_ArrayMovingView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH;
		
		final float height = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_height),
				width = ctx.getResources().getDimension(R.dimen.simple_enemy_shooter_width);
		
		Shooting_ArrayMovingView enemy = new Shooting_ArrayMovingView(ctx,score,speedY,speedX,collisionDamage,health,
				height,width,spawnBeneficialFreq);

		Gun defaultGun = new Gun_StraightSingleShot(ctx, enemy, new Bullet_Basic_LaserShort(),
				bulletFreq, bulletSpdY, bulletDmg);
		enemy.addGun(defaultGun);
		enemy.startShooting();
		
		gameLayout.addView(enemy,1);
		
		return enemy;
		
	}
	
	//METEORS
	protected Gravity_MeteorView spawnMeteor(){

		final int score=Gravity_MeteorView.DEFAULT_SCORE;
				
		final double speedY=Gravity_MeteorView.DEFAULT_SPEED_Y,
				collisionDamage=Gravity_MeteorView.DEFAULT_COLLISION_DAMAGE,
				health=Gravity_MeteorView.DEFAULT_HEALTH,
				spawnBeneficialObject= Gravity_MeteorView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH;
		
		Gravity_MeteorView met = new Gravity_MeteorView(ctx,score,speedY,collisionDamage,health,spawnBeneficialObject);

		gameLayout.addView(met,1);
		
		return met;
	}
	
	protected Meteor_SidewaysView spawnSidewaysMeteor(){
		final int score=Meteor_SidewaysView.DEFAULT_SCORE;
				
		final double speedY=Meteor_SidewaysView.DEFAULT_SPEED_Y,
				collisionDamage=Meteor_SidewaysView.DEFAULT_COLLISION_DAMAGE,
				health=Meteor_SidewaysView.DEFAULT_HEALTH,
				spawnBeneficialObject= Meteor_SidewaysView.DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH,
				speedX=Meteor_SidewaysView.DEFAULT_SPEED_X;
		
		Meteor_SidewaysView met = new Meteor_SidewaysView(ctx,score,speedY,speedX,collisionDamage,health,spawnBeneficialObject);

		gameLayout.addView(met,1);
		return met;
	}
	
	protected Gravity_MeteorView spawnGiantMeteor(){
		Gravity_MeteorView giant = spawnSidewaysMeteor();
		
		//change width and height. set X and Y positions
		final int width = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_length);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_length);
		
		giant.setLayoutParams(new LayoutParams(width,height));
		giant.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));
		
		//set damage and health to 200, score to 20
		giant.setDamage(150);
		giant.heal(130-Gravity_MeteorView.DEFAULT_HEALTH);
		giant.setScoreValue(80);
		
		return giant;
	}


}
