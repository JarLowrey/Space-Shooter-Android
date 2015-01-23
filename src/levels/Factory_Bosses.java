package levels;

import interfaces.GameActivityInterface;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;
import bullets.Bullet_Tracking_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_non_shooters.Gravity_MeteorView;
import enemies_non_shooters.Meteor_SidewaysView;
import enemies_orbiters.Orbiter_HorizontalLineView;
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
public class Factory_Bosses{
	protected int currentLevel;
	protected Context ctx;
	
	//GET methods
	public  int getLevel(){
		return currentLevel;
	}

	public GameActivityInterface getInteractivityInterface(){
		return (GameActivityInterface)ctx;
	}

	public Factory_Bosses(Context context){
		ctx=context;
	} 

	final  Runnable spawnGiantMeteor = new Runnable(){
		@Override
		public void run() {
			Gravity_MeteorView enemy = new Meteor_SidewaysView(ctx);
			
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
	
	final Runnable boss1 = new Runnable(){
		@Override
		public void run() {
			Orbiter_HorizontalLineView enemy = new Orbiter_HorizontalLineView(ctx,1000,
					Orbiter_HorizontalLineView.DEFAULT_SPEED_Y,Orbiter_HorizontalLineView.DEFAULT_SPEED_X,
					Orbiter_HorizontalLineView.DEFAULT_COLLISION_DAMAGE,ProtagonistView.DEFAULT_BULLET_DAMAGE*10,50,
					Orbiter_HorizontalLineView.DEFAULT_ORBIT_Y,
					(int) ctx.getResources().getDimension(R.dimen.boss1_width),
					(int) ctx.getResources().getDimension(R.dimen.boss1_height),
					R.drawable.ship_enemy_boss1);
			
			enemy.removeAllGuns();
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_Missile(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, (int) (Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE*1.5),50) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,5) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,95) );
			
			enemy.startShooting();
			
		}
	};
	
	final Runnable boss1_1 = new Runnable(){
		@Override
		public void run() {
			Orbiter_HorizontalLineView enemy = new Orbiter_HorizontalLineView(ctx,1000,
					Orbiter_HorizontalLineView.DEFAULT_SPEED_Y,Orbiter_HorizontalLineView.DEFAULT_SPEED_X,
					Orbiter_HorizontalLineView.DEFAULT_COLLISION_DAMAGE,ProtagonistView.DEFAULT_BULLET_DAMAGE*10,50,
					(int)(Math.random() * Orbiter_HorizontalLineView.DEFAULT_ORBIT_Y),//CHANGE ORBIT Y LOCATION 
					(int) ctx.getResources().getDimension(R.dimen.boss1_width),
					(int) ctx.getResources().getDimension(R.dimen.boss1_height),
					R.drawable.ship_enemy_boss1);
			
			enemy.removeAllGuns();
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_Missile(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, (int) (Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE*1.5),50) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,5) );
			enemy.addGun(new Gun_SingleShotStraight(ctx, enemy, new Bullet_Basic_LaserShort(),
					2000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,95) );
			
			enemy.startShooting();
			
		}
	};
	
	final Runnable boss2 = new Runnable(){
		@Override
		public void run() {
			Orbiter_HorizontalLineView enemy = new Orbiter_HorizontalLineView(ctx,1500,
					Orbiter_HorizontalLineView.DEFAULT_SPEED_Y,Orbiter_HorizontalLineView.DEFAULT_SPEED_X,
					Orbiter_HorizontalLineView.DEFAULT_COLLISION_DAMAGE,400,50,
					Orbiter_HorizontalLineView.DEFAULT_ORBIT_Y,
					(int) ctx.getResources().getDimension(R.dimen.boss2_width),
					(int) ctx.getResources().getDimension(R.dimen.boss2_height),
					R.drawable.ship_enemy_boss2);
			
			enemy.removeAllGuns();
			enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,5));
			enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,50));
			enemy.addGun(new Gun_TrackingSingle(ctx,getInteractivityInterface().getProtagonist(), enemy, new Bullet_Basic_LaserShort(),
					1000, Orbiter_HorizontalLineView.DEFAULT_BULLET_SPEED_Y, Orbiter_HorizontalLineView.DEFAULT_BULLET_DAMAGE,95));
			
			enemy.startShooting();
		}
	};
	
	final Runnable boss3 = new Runnable(){
		@Override
		public void run() {
			Orbiter_RectangleView enemy = new Orbiter_RectangleView(ctx,5000,
					Orbiter_RectangleView.DEFAULT_SPEED_Y,Orbiter_RectangleView.DEFAULT_SPEED_X,
					Orbiter_RectangleView.DEFAULT_COLLISION_DAMAGE,400,50,
					Orbiter_RectangleView.DEFAULT_ORBIT_LENGTH,
					Orbiter_RectangleView.DEFAULT_ORBIT_X,
					Orbiter_RectangleView.DEFAULT_ORBIT_Y,
					(int) ctx.getResources().getDimension(R.dimen.boss2_width),
					(int) ctx.getResources().getDimension(R.dimen.boss2_height),
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
