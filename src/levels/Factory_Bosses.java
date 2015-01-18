package levels;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_non_shooters.Gravity_MeteorView;
import enemies_orbiters.Orbiter_HorizontalLine;
import friendlies.ProtagonistView;
import guns.Gun_ShootTowardsTargetSingleShot;
import guns.Gun_SingleShotStraight;

/**
 * Create a default enemy using EnemyFactory class, then overwrite position, speed, damage size, background, guns, bullets, etc To make a boss
 * @author JAMES LOWREY
 *
 */
public class Factory_Bosses extends Factory_GenericEnemies{
	
	public Factory_Bosses(Context context){
		super(context);
	} 

	final  Runnable spawnGiantMeteor = new Runnable(){
		@Override
		public void run() {
			Gravity_MeteorView enemy = spawnSidewaysMeteor();
			
			//change width and height. set X and Y positions
			final int width = (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
			final int height= (int)ctx.getResources().getDimension(R.dimen.meteor_giant_length);
			
			enemy.setLayoutParams(new LayoutParams(width,height));
			enemy.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));//with non default size, set new position
			
			//make more powerful
			enemy.setDamage( ProtagonistView.DEFAULT_HEALTH/6 );
			enemy.heal(ProtagonistView.DEFAULT_BULLET_DAMAGE*3.5);
			enemy.setScoreValue(100);
		}
	};
	
	final Runnable boss1 = new Runnable(){
		@Override
		public void run() {
			Orbiter_HorizontalLine enemy = spawnHorizontalLineOrbiter();
			
			enemy.removeAllGuns();
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_Missile(),
					2000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE*2,0) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,0) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,0) );
			
			enemy.startShooting();
			
			enemy.setImageResource(R.drawable.ship_enemy_boss1);
			
			enemy.heal(400);
			enemy.setScoreValue(700);
		}
	};
	
	final Runnable boss2 = new Runnable(){
		@Override
		public void run() {
			Orbiter_HorizontalLine enemy = spawnHorizontalLineOrbiter();
			
			enemy.removeAllGuns();
			enemy.addGun(new Gun_ShootTowardsTargetSingleShot(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,5));
			enemy.addGun(new Gun_ShootTowardsTargetSingleShot(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,50));
			enemy.addGun(new Gun_ShootTowardsTargetSingleShot(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLine.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLine.DEFAULT_COLLISION_DAMAGE,95));
			
//			enemy.stopShooting();
			enemy.startShooting();
			
			enemy.setImageResource(R.drawable.ship_enemy_boss2);
			
			enemy.heal(500);
			enemy.setScoreValue(1500);
		}
	};
}
