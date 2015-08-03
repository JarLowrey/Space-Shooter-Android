package enemies;

import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.GameActivityInterface;
import levels.AttributesOfLevels;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;
import bullets.Bullet_Tracking;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies_orbiters.Orbiter_RectangleView;
import friendlies.ProtagonistView;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
import guns.Gun_TrackingGattling;

public class HorizontalMovement_FinalBoss extends Shooting_HorizontalMovementView{

	private boolean isInvisible;
	public static int DEFAULT_SPEED_X = 10;
		
	public HorizontalMovement_FinalBoss(RelativeLayout layout,int level) {
		super(layout,level,
				50000,
				DEFAULT_SPEED_Y,
				Integer.MAX_VALUE,
				ProtagonistView.DEFAULT_BULLET_DAMAGE*530,
				0,
				(int)layout.getContext().getResources().getDimension(R.dimen.boss5_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.boss5_height),
				R.drawable.ship_enemy_boss5);

		this.setThreshold((int) MainActivity.getHeightPixels()/4);
		
		//default Gun loadout
		removeAllGuns();
		
		//Laser volleys
			//8sec volley
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					8000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					0));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					8000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					25));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					8000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					50));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					8000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					75));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					8000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					100));

			//17sec volley
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					17000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					0));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					17000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					25));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					17000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					50));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					17000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					75));
			addGun(new Gun_SingleShotStraight(getMyLayout(), this,
					new Bullet_Basic(
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
							(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
							R.drawable.bullet_laser_round_red),
					17000, 
					Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
					Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
					100));
		
		//Gattling lasers
		this.addGun(new Gun_TrackingGattling(getMyLayout(),( (GameActivityInterface)getContext() ).getProtagonist(), this,
				new Bullet_Tracking(( (GameActivityInterface)getContext() ).getProtagonist(), this,
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red),
				6000, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
				50,
				4 ));
		this.addGun(new Gun_TrackingGattling(getMyLayout(),( (GameActivityInterface)getContext() ).getProtagonist(), this,
				new Bullet_Basic(
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red),
				6000, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE,
				70,
				4 ));
		
		//angled
		this.addGun(new Gun_AngledDualShot(getMyLayout(),this,
				new Bullet_Basic(
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_width), 
						(int)getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
						R.drawable.bullet_laser_round_red),
				1000, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
				(int) (Orbiter_RectangleView.DEFAULT_BULLET_DAMAGE * 1.5),
				50));
		
		//missiles
		this.addGun(new Gun_SingleShotStraight(getMyLayout(),this,
				new Bullet_Tracking( ( (GameActivityInterface)getContext() ).getProtagonist(), this, 
						(int)getContext().getResources().getDimension(R.dimen.bullet_missile_one_width), 
						(int)getContext().getResources().getDimension(R.dimen.bullet_missile_one_height), 
						R.drawable.bullet_missile_one),
				3000, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y/2, 
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
	

	public static int getSpawningProbabilityWeight(int level) {
		//start at 1/100 giant meteor, increase a little every 30 levels until equal to 1/90 giant meteor
		int probabilityWeight = 0;
		if(level > AttributesOfLevels.FIRST_LEVEL_BOSS5_APPEARS){
			probabilityWeight = (int) (AttributesOfLevels.STANDARD_PROB_WEIGHT/100.0 + 
					(level/30)*(AttributesOfLevels.STANDARD_PROB_WEIGHT/100.0) ) ;
			probabilityWeight = (int) Math.min(probabilityWeight, AttributesOfLevels.STANDARD_PROB_WEIGHT / 90.0);
		}
		return probabilityWeight;
	}

}
