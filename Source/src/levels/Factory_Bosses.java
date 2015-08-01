package levels;

import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;
import guns.Gun_TrackingSingle;
import helpers.KillableRunnable;
import helpers.SpawnableWave;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Shooting_HorizontalMovementView;
import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_RectangleView;
import friendlies.ProtagonistView;

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
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,getLevel(),
						900,//score
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 43,
						50,//probability of good drop on death
						(int) ctx.getResources().getDimension(R.dimen.boss1_width),
						(int) ctx.getResources().getDimension(R.dimen.boss1_height),
						R.drawable.ship_enemy_boss1);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic( 
						(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_height), 
						R.drawable.bullet_missile_one),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, (int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE*1.5),50) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
						R.drawable.bullet_laser_rectangular_red),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
						R.drawable.bullet_laser_rectangular_red),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95) );
				
				enemy.startShooting();
			}
		};
		
		return new SpawnableWave(r,5000 - (getLevel()/5) * 1000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss1(getLevel()) );
	}
	
	final SpawnableWave boss2(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,
						2500,
						getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 64,
						75,
						(int) ctx.getResources().getDimension(R.dimen.boss2_width),
						(int) ctx.getResources().getDimension(R.dimen.boss2_height),
						R.drawable.ship_enemy_boss2);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_rectangular_red),
						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y,
						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
						5));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_rectangular_red),
						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
						50));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_rectangular_red),
						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y,
						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
						95));
				
				enemy.startShooting();
			}
		};
		
		return new SpawnableWave(r,15000 - (getLevel()/5) * 3000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss2(getLevel()) );
	}
	
	final SpawnableWave boss3(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,
						5000,getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 128,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss3_width),
						(int) ctx.getResources().getDimension(R.dimen.boss3_height),
						R.drawable.ship_enemy_boss3);
				 
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_rectangular_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						10,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_rectangular_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						90,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_height), 
								R.drawable.bullet_missile_one),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						50));
				
				enemy.startShooting();	
			}
		};
		
		return new SpawnableWave(r,20000 - (getLevel()/5) * 4000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss3(getLevel()) );
	}
	
	final SpawnableWave boss4(){ 
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,20000,getLevel(),
						Orbiter_RectangleView.DEFAULT_SPEED_Y,
						Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 192,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss4_width),
						(int) ctx.getResources().getDimension(R.dimen.boss4_height),
						R.drawable.ship_enemy_boss4);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_long_height), 
								R.drawable.bullet_laser_rectangular_red),
						750, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						50));
				
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_rectangular_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						5,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_rectangular_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						95,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_height), 
								R.drawable.bullet_missile_one),
						4000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						20));
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.bullet_missile_one_height), 
								R.drawable.bullet_missile_one),
						4000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						80));
				
				enemy.startShooting();	
			}
		};  
		
		return new SpawnableWave(r,25000 - (getLevel()%5) * 5000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss4(getLevel()) );
	}

	public final SpawnableWave spawnGiantMeteor(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Gravity_MeteorView enemy = new Meteor_SidewaysView(ctx,getLevel() );
				
				//change width and height. set X and Y positions
				final int width = (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
				final int height= (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
				
				enemy.setLayoutParams(new LayoutParams(width,height));
				enemy.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));//with non default size, set new position
				
				//make more powerful
				enemy.setDamage( ProtagonistView.DEFAULT_HEALTH/6 );
				enemy.setHealth((int) (ProtagonistView.DEFAULT_BULLET_DAMAGE * 7.2));
				enemy.setScoreValue(100);
			}
		};
		
		return new SpawnableWave(r,2000,Gravity_MeteorView.getSpawningProbabilityWeightOfGiantMeteors(getLevel()));
	}
	
}
