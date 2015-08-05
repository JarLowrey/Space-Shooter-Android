package levels;

import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;
import guns.Gun_TrackingSingle;
import helpers.KillableRunnable;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic;
import bullets.Bullet_Duration;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

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
	
	public Factory_Bosses(RelativeLayout layout){
		super(layout);
	} 
	
	final SpawnableWave boss1(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(gameScreen,getLevel(),
						900,//score
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 43,
						50,//probability of good drop on death
						(int) gameScreen.getResources().getDimension(R.dimen.boss1_width),
						(int) gameScreen.getResources().getDimension(R.dimen.boss1_height),
						R.drawable.ship_enemy_boss1);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(gameScreen, enemy, new Bullet_Basic( 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_height), 
						R.drawable.bullet_missile_one),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, (int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE*1.5),50) );
				enemy.addGun(new Gun_SingleShotStraight(gameScreen, enemy, new Bullet_Basic(
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
						R.drawable.bullet_laser_round_red),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,5) );
				enemy.addGun(new Gun_SingleShotStraight(gameScreen, enemy, new Bullet_Basic(
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
						R.drawable.bullet_laser_round_red),
						2000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE,95) );
				
				enemy.startShooting();
			}
		};
		
		return new SpawnableWave(r,3000 - (getLevel()/5) * 1000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss1(getLevel()) );
	}
	
	final SpawnableWave boss2(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(gameScreen,
						3000,
						getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 50,
						75,
						(int) gameScreen.getResources().getDimension(R.dimen.boss2_width),
						(int) gameScreen.getResources().getDimension(R.dimen.boss2_height),
						R.drawable.ship_enemy_boss2);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingSingle(gameScreen,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_mid_fat_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red),
						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y,
						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
						20));
//				enemy.addGun(new Gun_TrackingSingle(gameScreen,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
//						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_width), 
//						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_height), 
//						R.drawable.bullet_laser_round_red),
//						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
//						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
//						50));
				enemy.addGun(new Gun_TrackingSingle(gameScreen,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic(
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_mid_fat_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red),
						1000, Bullet_Interface.DEFAULT_BULLET_SPEED_Y,
						(int) (Shooting_HorizontalMovementView.DEFAULT_BULLET_DAMAGE * 0.8),
						80));
				
				enemy.startShooting();
			}
		};
		
		return new SpawnableWave(r,10000,
				Shooting_HorizontalMovementView.getSpawningProbabilityWeightForBoss2(getLevel()) );
	}
	
	final SpawnableWave boss3(){
		KillableRunnable r = new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(gameScreen,
						5000,getLevel(),
						Shooting_HorizontalMovementView.DEFAULT_SPEED_Y,
						Shooting_HorizontalMovementView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 128,
						100,
						(int) gameScreen.getResources().getDimension(R.dimen.boss3_width),
						(int) gameScreen.getResources().getDimension(R.dimen.boss3_height),
						R.drawable.ship_enemy_boss3);
				 
				enemy.removeAllGuns();
				enemy.addGun(new Gun_TrackingGattling(gameScreen,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_round_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						10,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				enemy.addGun(new Gun_TrackingGattling(gameScreen,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Basic(
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_round_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						90,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS));
				
				enemy.addGun(new Gun_SingleShotStraight(gameScreen,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_height), 
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
				Shooting_HorizontalMovementView enemy = new Shooting_HorizontalMovementView(gameScreen,20000,getLevel(),
						Orbiter_RectangleView.DEFAULT_SPEED_Y,
						Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,
						ProtagonistView.DEFAULT_BULLET_DAMAGE * 192,
						100,
						(int) gameScreen.getResources().getDimension(R.dimen.boss4_width),
						(int) gameScreen.getResources().getDimension(R.dimen.boss4_height),
						R.drawable.ship_enemy_boss4);
				
				enemy.removeAllGuns();
				enemy.addGun(new Gun_SingleShotStraight(gameScreen, enemy, new Bullet_Duration(
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_xskinny_width), 
						(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red,
						Bullet_Duration.DEFAULT_BULLET_DURATION,
						50),
					5000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Bullet_Duration.DEFAULT_BULLET_DAMAGE,
					50));
//				enemy.addGun(new Gun_SingleShotStraight(gameScreen, enemy,
//						new Bullet_Basic(
//								(int)gameScreen.getResources().getDimension(R.dimen.bullet_mid_fat_width), 
//								(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_long_height), 
//								R.drawable.bullet_laser_round_red),
//						750, 
//						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
//						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
//						50));
				
				enemy.addGun(new Gun_TrackingGattling(gameScreen,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_round_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						5,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				enemy.addGun(new Gun_TrackingGattling(gameScreen,getInteractivityInterface().getProtagonist(), enemy,
						new Bullet_Tracking(getInteractivityInterface().getProtagonist(), enemy,
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_skinny_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_rec_short_height), 
								R.drawable.bullet_laser_round_red),
						5000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
						Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
						95,
						Gun_TrackingGattling.DEFAULT_NUM_GATTLING_SHOTS / 2));
				
				enemy.addGun(new Gun_SingleShotStraight(gameScreen,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_height), 
								R.drawable.bullet_missile_one),
						4000, 
						Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
						(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
						20));
				enemy.addGun(new Gun_SingleShotStraight(gameScreen,enemy,
						new Bullet_Tracking( getInteractivityInterface().getProtagonist(), enemy, 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_width), 
								(int)gameScreen.getResources().getDimension(R.dimen.bullet_missile_one_height), 
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
				Gravity_MeteorView enemy = new Meteor_SidewaysView(gameScreen,getLevel() );
				
				//change width and height. set X and Y positions
				final int width = (int)gameScreen.getResources().getDimension(R.dimen.meteor_giant_length);
				final int height= (int)gameScreen.getResources().getDimension(R.dimen.meteor_giant_length);
				
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
