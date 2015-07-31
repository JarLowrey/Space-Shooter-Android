package levels;

import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;
import guns.Gun_TrackingSingle;
import helpers.KillableRunnable;
import helpers.SpawnableWave;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic;
import bullets.Bullet_Tracking;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
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
						1000,//score
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*20,
						50,//probability of good drop on death
						(int) ctx.getResources().getDimension(R.dimen.boss1_width),
						(int) ctx.getResources().getDimension(R.dimen.boss1_height),
						R.drawable.ship_enemy_boss1);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic( 
						(int)ctx.getResources().getDimension(R.dimen.missile_one_width), 
						(int)ctx.getResources().getDimension(R.dimen.missile_one_height), 
						R.drawable.bullet_missile_one),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, (int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE*1.5),50) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
						R.drawable.bullet_laser_rectangular_enemy),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5) );
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
						R.drawable.bullet_laser_rectangular_enemy),
						2000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95) );
				
				enemy.startShooting();
			}
		};

		int probabilityWeight = 0;
		if( getLevel() > FIRST_LEVEL_BOSS1_APPEARS){
			//start at 1/10 giant meteor, increase a little every 10 levels until equal to 1/4 * giant meteor
			probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 10.0 + 
					(getLevel()/10) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/20.0);
			
			probabilityWeight = (int) Math.min(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 4.0);
		}

		
		return new SpawnableWave(r,5000 - (getLevel()/5) * 1000,probabilityWeight);
	}
	
	final SpawnableWave boss2(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,1500,getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*30,
						75,
						(int) ctx.getResources().getDimension(R.dimen.boss2_width),
						(int) ctx.getResources().getDimension(R.dimen.boss2_height),
						R.drawable.ship_enemy_boss2);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
						R.drawable.bullet_laser_rectangular_enemy),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
						R.drawable.bullet_laser_rectangular_enemy),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,50));
				enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
						(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
						R.drawable.bullet_laser_rectangular_enemy),
						1000, Enemy_ShooterView.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95));
				
				enemy.startShooting();
			}
		};
		
		int probabilityWeight = 0;
		if( getLevel() > FIRST_LEVEL_BOSS2_APPEARS){
			//start at 1/20 giant meteor, increase a little every 16 levels until equal to 1/9 * giant meteor
			probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 20.0 + 
					(getLevel()/16) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/35.0);
			
			probabilityWeight = (int) Math.min(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 9.0);
		}
		
		return new SpawnableWave(r,15000 - (getLevel()/5) * 3000,probabilityWeight);
	}
	
	final SpawnableWave boss3(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,5000,getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*60,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss3_width),
						(int) ctx.getResources().getDimension(R.dimen.boss3_height),
						R.drawable.ship_enemy_boss3);
				 
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
								R.drawable.bullet_laser_rectangular_enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						10,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
								R.drawable.bullet_laser_rectangular_enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						90,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_height), 
								R.drawable.bullet_missile_one),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						50));
				
				enemy.startShooting();	
			}
		};

		int probabilityWeight = 0;
		if( getLevel() > FIRST_LEVEL_BOSS3_APPEARS){
			//start at 1/30 giant meteor, increase a little every 20 levels until equal to 1/9 * giant meteor
			probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 30.0 + 
					(getLevel()/20) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/24.0);
			
			probabilityWeight = (int) Math.min(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 9.0);
		}
		
		return new SpawnableWave(r,20000 - (getLevel()/5) * 4000,probabilityWeight);
	}
	
	final SpawnableWave boss4(){ 
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(ctx,20000,getLevel(),
						Orbiter_RectangleView.DEFAULT_SPEED_Y,
						Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE*90,
						100,
						(int) ctx.getResources().getDimension(R.dimen.boss4_width),
						(int) ctx.getResources().getDimension(R.dimen.boss4_height),
						R.drawable.ship_enemy_boss4);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(ctx, enemy,
						new Bullet_Basic(
								(int)ctx.getResources().getDimension(R.dimen.laser_long_width), 
								(int)ctx.getResources().getDimension(R.dimen.laser_long_height), 
								R.drawable.bullet_laser_rectangular_enemy),
						750, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						50));
				
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
								R.drawable.bullet_laser_rectangular_enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						5,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				enemy.addGun(new Gun_TrackingGattling(ctx,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)ctx.getResources().getDimension(R.dimen.laser_short_width), 
								(int)ctx.getResources().getDimension(R.dimen.laser_short_height), 
								R.drawable.bullet_laser_rectangular_enemy),
						5000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						95,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_height), 
								R.drawable.bullet_missile_one),
						4000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						20));
				enemy.addGun(new Gun_SingleShotStraight(ctx,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_width), 
								(int)ctx.getResources().getDimension(R.dimen.missile_one_height), 
								R.drawable.bullet_missile_one),
						4000, 
						Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						80));
				
				enemy.startShooting();	
			}
		};  
		
		int probabilityWeight = 0;
		if( getLevel() > FIRST_LEVEL_BOSS4_APPEARS){
			//start at 1/50 giant meteor, increase a little every 25 levels until equal to 1/15 * giant meteor
			probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 50.0 + 
					(getLevel()/25) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/40.0);
			
			probabilityWeight = (int) Math.min(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR / 15.0);
		}
		
		return new SpawnableWave(r,25000 - (getLevel()%5) * 5000,probabilityWeight);
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
				enemy.heal((int) (ProtagonistView.DEFAULT_BULLET_DAMAGE*3.5));
				enemy.setScoreValue(100);
			}
		};
		
		return new SpawnableWave(r,2000,Gravity_MeteorView.getSpawningProbabilityWeightOfGiantMeteors(getLevel()));
	}
	
}
