package enemies;

import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.GameActivityInterface;
import android.content.Context;
import bullets.Bullet_Basic_LaserLong;
import bullets.Bullet_Tracking_LaserShort;
import bullets.Bullet_Tracking_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_orbiters.Orbiter_RectangleView;
import friendlies.ProtagonistView;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;

public class HorizontalMovement_FinalBoss extends Shooting_HorizontalMovementView{

	private boolean isInvisible;
	public static int DEFAULT_SPEED_X = 10;
		
	public HorizontalMovement_FinalBoss(Context context) {
		super(context,
				50000,
				DEFAULT_SPEED_Y,
				ProtagonistView.DEFAULT_HEALTH*1000,
				ProtagonistView.DEFAULT_BULLET_DAMAGE*250,
				0,
				(int)context.getResources().getDimension(R.dimen.boss5_width),
				(int)context.getResources().getDimension(R.dimen.boss5_height),
				R.drawable.ship_enemy_boss5);

		this.setThreshold((int) MainActivity.getHeightPixels()/4);
		
		//default Gun loadout
		removeAllGuns();
		
		//Laser volleys
			//8sec volley
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					8000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					0));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					8000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					25));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					8000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					50));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					8000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					75));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					8000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					100));

			//17sec volley
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					17000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					0));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					17000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					25));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					17000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					50));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					17000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					75));
			addGun(new Gun_SingleShotStraight(getContext(), this,
					new Bullet_Basic_LaserLong(),
					17000, 
					Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					100));
		
		//Gattling lasers
		this.addGun(new Gun_TrackingGattling(getContext(),( (GameActivityInterface)getContext() ).getProtagonist(), this,
				new Bullet_Tracking_LaserShort(( (GameActivityInterface)getContext() ).getProtagonist(), this),
				6000, 
				Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
				Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
				50,
				4 ));
		this.addGun(new Gun_TrackingGattling(getContext(),( (GameActivityInterface)getContext() ).getProtagonist(), this,
				new Bullet_Tracking_LaserShort(( (GameActivityInterface)getContext() ).getProtagonist(), this),
				6000, 
				Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y, 
				Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
				70,
				4 ));
		
		//angled
		this.addGun(new Gun_AngledDualShot(getContext(),this,
				new Bullet_Basic_LaserLong(),
				1000, 
				Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
				(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
				50));
		
		//missiles
		this.addGun(new Gun_SingleShotStraight(getContext(),this,
				new Bullet_Tracking_Missile( ( (GameActivityInterface)getContext() ).getProtagonist(), this),
				3000, 
				Orbiter_RectangleView.DEFAULT_BULLET_SPEED_Y/2, 
				(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
				30));
	}
	
	@Override
	public boolean takeDamage(int amountOfDamage){
		boolean isKilled = super.takeDamage(amountOfDamage);
		
		//at certain health values, change guns/speed/abilites, spawn enemies, ... ?
		
		return isKilled;
	}
	
	@Override
	protected void reachedGravityPosition() {
		super.reachedGravityPosition();

		this.startShooting();
		isInvisible = false;
		
		this.post(new KillableRunnable(){
			@Override
			public void doWork() {
				if(isInvisible){
					HorizontalMovement_FinalBoss.this.setImageResource(R.drawable.ship_enemy_boss5);
					
					isInvisible = false;
					ConditionalHandler.postIfAlive(this,(long)( 5000+Math.random()*2000 ), HorizontalMovement_FinalBoss.this);
				}else{
					HorizontalMovement_FinalBoss.this.setImageResource( 0 );
					
					isInvisible = true;
					ConditionalHandler.postIfAlive(this,(long)( 2000+Math.random() * 2000 ), HorizontalMovement_FinalBoss.this);
				}
			}
		});
	}

}
