package levels;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;
import bullets.Bullet_Tracking_LaserShort;
import bullets.Bullet_Tracking_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
import enemies.HorizontalMovement_FinalBoss;
import enemies.Shooting_HorizontalMovementView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_RectangleView;
import friendlies.ProtagonistView;
import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;
import guns.Gun_TrackingSingle;
import helpers.KillableRunnable;
import helpers.SpawnableWave;

/**
 * Create a default enemy using EnemyFactory class, then overwrite position, speed, damage size, background, guns, bullets, etc To make a boss
 * @author JAMES LOWREY
 *
 */
public abstract class Factory_Bosses extends Factory_ScriptedWaves
{
	
	public Factory_Bosses(Context context){
		super(context);
	} 
	
	final SpawnableWave boss1(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,
						1000,//score
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*20,
						50,//probability of good drop on death
						(int) ctx.getResources().getDimension(R.dimen.boss1_width),
						(int) ctx.getResources().getDimension(R.dimen.boss1_height),
						R.drawable.ship_enemy_boss1);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_Missile(),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, (int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE*1.5),50) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95) );
				
				enemy.startShooting();
			}
		};

		int probabilityWeight = 0;
		if(difficulty() > 1){
			probabilityWeight = 10 + difficulty() * 6;
			probabilityWeight = Math.max(probabilityWeight, 30);
		}

		
		return new SpawnableWave(r,5000 - difficulty() * 1000,probabilityWeight);
	}
	
	final SpawnableWave boss2(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,1500,
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*30,
						75,
						(int) ctx.getResources().getDimension(R.dimen.boss2_width),
						(int) ctx.getResources().getDimension(R.dimen.boss2_height),
						R.drawable.ship_enemy_boss2);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,50));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95));
				
				enemy.startShooting();
			}
		};
		
		int probabilityWeight = 0;
		if(difficulty() > 2){
			probabilityWeight = 10 + difficulty() * 2;
			probabilityWeight = Math.max(probabilityWeight, 20);
		}
		
		return new SpawnableWave(r,15000 - difficulty() * 3000,probabilityWeight);
	}
	
	final SpawnableWave boss3(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,5000,
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*60,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss3_width),
						(int) ctx.getResources().getDimension(R.dimen.boss3_height),
						R.drawable.ship_enemy_boss3);
				 
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic_LaserShort(),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						10,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic_LaserShort(),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						90,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking_Missile( getInteractivityInterface().getProtagonist(), enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						50));
				
				enemy.startShooting();	
			}
		};

		int probabilityWeight = 0;
		if(difficulty() > 3){
			probabilityWeight = 10 + difficulty() ;
			probabilityWeight = Math.max(probabilityWeight, 15);
		}
		
		return new SpawnableWave(r,20000 - difficulty() * 4000,probabilityWeight);
	}
	
	final SpawnableWave boss4(){ 
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,20000,
						Orbiter_RectangleView.DEFAULT_SPEED_Y,
						Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*90,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss4_width),
						(int) ctx.getResources().getDimension(R.dimen.boss4_height),
						R.drawable.ship_enemy_boss4);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy,
						new Bullet_Basic_LaserLong(),
						750, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						50));
				
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking_LaserShort(getInteractivityInterface().getProtagonist(), enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						5,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking_LaserShort(getInteractivityInterface().getProtagonist(), enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						95,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking_Missile( getInteractivityInterface().getProtagonist(), enemy),
						4000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						20));
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking_Missile( getInteractivityInterface().getProtagonist(), enemy),
						4000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						80));
				
				enemy.startShooting();	
			}
		};  
		
		int probabilityWeight = 0;
		if(difficulty() > 4){
			probabilityWeight = 5 + difficulty() ;
			probabilityWeight = Math.max(probabilityWeight, 10);
		}
		
		return new SpawnableWave(r,25000 - difficulty() * 5000,probabilityWeight);
	}
	final SpawnableWave boss5(){ 
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				new HorizontalMovement_FinalBoss(ctx);
			}
		}; 
		
		int probabilityWeight = 0;
		
		return new SpawnableWave(r,100000000,probabilityWeight);
	}

	public final SpawnableWave spawnGiantMeteor(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Gravity_MeteorView enemy = new Meteor_SidewaysView(ctx,difficulty() );
				
				//change width and height. set X and Y positions
				final int width = (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
				final int height= (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
				
				enemy.setLayoutParams(new LayoutParams(width,height));
				enemy.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));//with non default size, set new position
				
				//make more powerful
				enemy.setDamage( ProtagonistView.DEFAULT_HEALTH/6 );
				enemy.heal((int) (ProtagonistView.DEFAULT_BULLET_DAMAGE*3.5));
				enemy.setScoreValue(100);
			}
		};
		
		int probabilityWeight = 5;
		if(difficulty() > 0){
			probabilityWeight = 20 ;
		}
		
		return new SpawnableWave(r,2000,probabilityWeight);
	}
	
}
