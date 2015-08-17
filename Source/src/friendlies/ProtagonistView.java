package friendlies;

import guns.Gun;
import helpers.KillableRunnable;
import helpers.MediaController;
import helpers.StoreUpgradeHandler;
import interfaces.GameActivityInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RelativeLayout;
import backgroundViews.ExplosionView;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

import enemies.EnemyView;
import enemies_non_shooters.Gravity_MeteorView;

public class ProtagonistView extends Friendly_ShooterView{

	public static final float BULLET_SPEED_MULTIPLIER = (float) 1.15,
			DEFAULT_SPEED_Y=(float) (Gravity_MeteorView.DEFAULT_SPEED_Y * 4.5),//Density Pixels per millisecond
			DEFAULT_SPEED_X=DEFAULT_SPEED_Y;
	public static final int 
			BULLET_FREQ_UPGRADE_WEIGHT=15,
			DEFAULT_HEALTH=10000,
			DEFAULT_BULLET_DAMAGE = DEFAULT_HEALTH,
			BULLET_DAMAGE_UPGRADE_WEIGHT = (int) (DEFAULT_BULLET_DAMAGE * .25),
			DEFAULT_BULLET_FREQ = 350; //All enemy health is defined with respect to Protagonist's default bullet damage
	public final static int MIN_SHOOTING_FREQ = 150;
	
	GameActivityInterface myGame;
	private double percentDistanceTouchFromMidpointButtonX = 0,
			percentDistanceTouchFromMidpointButtonY = 0;
	
	private KillableRunnable exhaustRunnable;
	
	public ProtagonistView(RelativeLayout layout,GameActivityInterface interactWithGame) {
		super(layout,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_protagonist_game_height),
				R.drawable.ship_protagonist);

		//set Vars
		this.setX(  MainActivity.getWidthPixels()/2 - 
				getContext().getResources().getDimension(R.dimen.ship_protagonist_game_width)/2 );//middle of screen
		myGame=interactWithGame;

		//apply upgrades
		StoreUpgradeHandler.createProtagonistGunSet(this);
		this.setHealth( getProtagonistMaxHealth(getContext()) );
		
		//prepare ship
		restartThreads();
	}
	
	
	private static final long EXHAUST_VISIBLE_TIME=500;
	private static final double EXHAUST_FREQ=5000;
	 
	private int count = 0;
    
	private KillableRunnable showExhaustRunnable(){
		final long howOftenExhaustMoves = (10);
		
		return new KillableRunnable(){
			private boolean hasPlayedSound = false;
			
	    	 @Override
	         public void doWork() {	  
	    		if( ! hasPlayedSound){ //need to check if already played to prevent playing every movement of exhaust
	    			MediaController.playSoundEffect(getContext(), MediaController.SOUND_ROCKET_LAUNCH);
	    			hasPlayedSound = true; 
	    		}
    		 	GameActivityInterface screen = (GameActivityInterface) getContext();
				if( ( count* howOftenExhaustMoves)  < EXHAUST_VISIBLE_TIME){
					screen.getExhaust().setVisibility(View.VISIBLE);					
		    		 //position the exhaust
					final float y=ProtagonistView.this.getY()+ProtagonistView.this.getHeight(); //set the fire's Y pos to behind rocket
					final float averageRocketsX= (2 * ProtagonistView.this.getX()+ProtagonistView.this.getWidth() )/2;//find average of rocket's left and right x pos
					final float x = averageRocketsX-screen.getExhaust().getLayoutParams().width /2;//fire's new X pos should set the middle of fire to middle of rocket
					screen.getExhaust().setY(y);
					screen.getExhaust().setX(x);
					count++; 

					postDelayed(this,howOftenExhaustMoves);//repost this runnable so the exhaust will reposition quickly
				
				}else{		    		
	    			hasPlayedSound = false;
					count=0;
					screen.getExhaust().setVisibility(View.GONE);					
					postDelayed(this,(long) (EXHAUST_FREQ+ 2 * EXHAUST_FREQ*Math.random()));//repost this to a random time in the future
				}
	         }
	    };
	}
	

	public static float getShootingDelay(Context ctx){
		final float freq = DEFAULT_BULLET_FREQ - StoreUpgradeHandler.getBulletBulletFreqLevel(ctx) * BULLET_FREQ_UPGRADE_WEIGHT;
		return Math.max(freq, MIN_SHOOTING_FREQ);
	}
	public static int getBulletDamage(Context ctx){
		final int dmg =  (int) (DEFAULT_BULLET_DAMAGE + BULLET_DAMAGE_UPGRADE_WEIGHT * StoreUpgradeHandler.getBulletDamageLevel(ctx) );
		return Math.min(dmg, DEFAULT_BULLET_DAMAGE * EnemyView.MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR);
	}
	public static int getProtagonistCurrentHealth(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_HEALTH, ProtagonistView.DEFAULT_HEALTH);
	}
	public static int getProtagonistMaxHealth(Context ctx){
		return ProtagonistView.DEFAULT_HEALTH + (ProtagonistView.DEFAULT_HEALTH/10) * StoreUpgradeHandler.getdefenseLevel(ctx);		
	}
	
	@Override
	public void heal(int howMuchHealed){
		super.heal(howMuchHealed);
		myGame.setHealthBars();
	}
	@Override
	public void setHealth(int healthValue){
		super.setHealth(healthValue);
		myGame.setHealthBars();
	}
	

	@Override
	public void startShooting() {
		isShooting=true;
		for(Gun gun: myGuns){
			gun.startShootingImmediately();
		}
	}
	
	@Override 
	public boolean takeDamage(int howMuchDamage){ 
		boolean isDead = super.takeDamage(howMuchDamage);
		myGame.setHealthBars();//must set health bars after taking damage
		
		if(isDead){
			final long vibratePat[] = {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
			new ExplosionView(this.getMyLayout(),this,
					R.drawable.explosion1,vibratePat);
			new ExplosionView(this.getMyLayout(),this,
					R.drawable.explosion1,null);
			new ExplosionView(this.getMyLayout(),this,
					R.drawable.explosion1,null);
		}else{
			MediaController.vibrate(getContext(), 130);
		}
		return isDead;
	}
	
	@Override
	public void restartThreads(){
		exhaustRunnable = showExhaustRunnable();
		this.post(exhaustRunnable);
		super.restartThreads();
	}
	
	@Override 
	public void removeGameObject(){ 
		super.removeGameObject();
		
		exhaustRunnable.kill();
	}

	public void updatePercentDistanceFromMidpointOfMoveButton(double xPercent,double yPercent){
		percentDistanceTouchFromMidpointButtonX = xPercent;
		percentDistanceTouchFromMidpointButtonY = yPercent;
	}

	@Override
	public void updateViewSpeed(long deltaTime) {
		//check for user finger position, if inside button then set movement speed and the GameLoop will take care of moving
		
		setSpeedX((float) (ProtagonistView.DEFAULT_SPEED_X*percentDistanceTouchFromMidpointButtonX));
		setSpeedY((float) (ProtagonistView.DEFAULT_SPEED_Y*percentDistanceTouchFromMidpointButtonY));	
	}
	
	@Override
	public void move(long deltaTime){
		final int boundLeft = 0;
		final int boundRight = (int) MainActivity.getWidthPixels() - ProtagonistView.this.getWidth();
		final int boundTop = (int)(.4 * MainActivity.getHeightPixels());
		final int boundBottom = (int)(GameActivity.getBottomScreen() - ProtagonistView.this.getHeight());
		
		//Move by setting this instances X or Y position to its current position plus its respective speed.
		float x = this.getX();
		float y = this.getY();
		y+=getSpeedY() * deltaTime ;
		x+=getSpeedX() * deltaTime ;
		
		//ensure ProtagonistView never moves out of screen bounds
		if(y>boundTop && y<boundBottom){this.setY(y);}
		if(x>boundLeft && x<boundRight){this.setX(x);}
	}

}
