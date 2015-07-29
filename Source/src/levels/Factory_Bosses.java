package levels;

import support.KillableRunnable;
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
	
	final KillableRunnable boss1(){
		return new KillableRunnable(){
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
	}
	
	final KillableRunnable boss2(){
		return new KillableRunnable(){
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
	}
	
	final KillableRunnable boss3(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
//				Orbiter_RectangleView enemy = new Orbiter_RectangleView(ctx,5000,
//						Orbiter_RectangleView.DEFAULT_SPEED_Y,
//						Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
//						ProtagonistView.DEFAULT_BULLET_DAMAGE*60,
//						100,
//						Orbiter_RectangleView.DEFAULT_ORBIT_LENGTH*5,
//						Orbiter_RectangleView.DEFAULT_ORBIT_X,
//						Orbiter_RectangleView.DEFAULT_ORBIT_Y,
//						(int) ctx.getResources().getDimension(R.dimen.boss3_width),
//						(int) ctx.getResources().getDimension(R.dimen.boss3_height),
//						R.drawable.ship_enemy_boss3);
				

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
	}
	
	final KillableRunnable boss4(){ 
		return new KillableRunnable(){
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
	}
	final KillableRunnable boss5(){ 
		return new KillableRunnable(){
			@Override
			public void doWork() {
				new HorizontalMovement_FinalBoss(ctx);
			}
		}; 
	}

	//giant meteors
	public final void spawnGiantMeteor(){
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
	
	public final void spawnGiantMeteorWave(final int numMeteors, final int millisecondsBetweenEachMeteor){
		spawningHandler.post(
			new KillableRunnable(){
				private int numSpawned=0;
				
				@Override
				public void doWork() {
					spawnGiantMeteor();
					
					numSpawned++;
					if(numSpawned<numMeteors){
						spawningHandler.postDelayed(this, millisecondsBetweenEachMeteor);
					}
				}
			});
	}
	
	final KillableRunnable meteorsGiantAndSideways(){ 
		return new KillableRunnable(){
			@Override
			public void doWork() {
				spawnGiantMeteorWave(2,WAVE_SPAWNER_WAIT/2);//spawn for entire wave
	//			spawnSidewaysMeteorsWave(10,DEFAULT_WAVE_DURATION/10);//spawn for entire wave
				
			}
		};
	}
	
	final KillableRunnable meteorsGiant(){
		return new KillableRunnable(){
			@Override
			public void doWork() {
				spawnGiantMeteorWave(4,WAVE_SPAWNER_WAIT/4);
			}
		};
	}
}
