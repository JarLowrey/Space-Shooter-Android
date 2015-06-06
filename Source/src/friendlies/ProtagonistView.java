package friendlies;

import guns.Gun;
import interfaces.GameActivityInterface;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class ProtagonistView extends Friendly_ShooterView{
	
	public static final int UPGRADE_BULLET_DAMAGE=0,UPGRADE_DEFENCE=1,UPGRADE_BULLET_FREQ=3,
			UPGRADE_GUN=4,UPGRADE_FRIEND=5,UPGRADE_SCORE_MULTIPLIER=6,UPGRADE_HEAL=7;
	
	SharedPreferences gameState;
	GameActivityInterface myGame;
	
	public ProtagonistView(Context context,GameActivityInterface interactWithGame) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),
				R.drawable.ship_protagonist);

		this.setX(  MainActivity.getWidthPixels()/2 - context.getResources().getDimension(R.dimen.ship_protagonist_game_width)/2 );//middle of screen

		gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		myGame=interactWithGame;
		
		this.post(exhaustRunnable());

		//apply upgrades
		final float freq = getShootingDelay();
		final int dmg = (int) (DEFAULT_BULLET_DAMAGE + getBulletDamageLevel() * BULLET_DAMAGE_WEIGHT);
		createGunSet(freq,dmg,getGunLevel());
		applyDefenceUpgradeToProtagonist();
		this.killMoveRunnable();
//		addGun(new Gun_AngledDualShot(context, this, new Bullet_Basic_LaserLong(), 
//				freq, Friendly_ShooterView.DEFAULT_BULLET_SPEED_Y, dmg, 50
//				));
	}
	
	
	private static final long EXHAUST_VISIBLE_TIME=500;
	private static final double EXHAUST_FREQ=5000;
	 
	private int count = 0;
    
	private KillableRunnable exhaustRunnable(){
		final long howOftenExhaustMoves = (ProtagonistView.HOW_OFTEN_TO_MOVE/2);
		return new KillableRunnable(){
	    	 @Override
	         public void doWork() {
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
	
						ConditionalHandler.postIfAlive(this,howOftenExhaustMoves,ProtagonistView.this);//repost this runnable so the exhaust will reposition quickly
					
					}else{
						count=0;
						screen.getExhaust().setVisibility(View.GONE);					
						ConditionalHandler.postIfAlive(this,(long) (EXHAUST_FREQ+ 2 * EXHAUST_FREQ*Math.random()),ProtagonistView.this);//repost this to a random time in the future
					}
	         }
	    };
	}
	
	@Override
	public void heal(int howMuchHealed){
		super.heal(howMuchHealed);
		myGame.setHealthBar(this.getMaxHealth(),this.getHealth());
	}
	@Override
	public void setHealth(int healthValue){
		super.setHealth(healthValue);
		myGame.setHealthBar(this.getMaxHealth(),this.getHealth());
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
		myGame.setHealthBar(this.getMaxHealth(),this.getHealth());
		
		if(isDead){
			final long vibratePat[] = {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
			createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1,vibratePat);
			createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1);
			createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1);
		}else{
	        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
	        vibrator.vibrate(100);
		}
		return isDead;
	}
	
	@Override
	public void restartThreads(){
		this.post(exhaustRunnable());
		super.restartThreads();
	}
	
	public void beginMoving(final float percentXAwayFromMidPointOfMovementButton,final float percentYAwayFromMidPointOfMovementButton){

		setSpeedX(ProtagonistView.DEFAULT_SPEED_X*percentXAwayFromMidPointOfMovementButton);
		setSpeedY(ProtagonistView.DEFAULT_SPEED_Y*percentYAwayFromMidPointOfMovementButton);
		
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				move(0,
						(int) MainActivity.getWidthPixels() - ProtagonistView.this.getWidth(),
						(int)(.4 * MainActivity.getHeightPixels()),
						(int)(GameActivity.getBottomScreen() - ProtagonistView.this.getHeight()));
				ConditionalHandler.postIfAlive(this,ProtagonistView.HOW_OFTEN_TO_MOVE,ProtagonistView.this);
			}
		});
	}
	public void stopMoving(){
		this.killMoveRunnable();
	}
	
	public void applyUpgrade(final int whichUpgrade){
		SharedPreferences gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		switch(whichUpgrade){
		case UPGRADE_BULLET_DAMAGE:
			final int gunDmg = gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, gunDmg+1);
			break;
		case UPGRADE_DEFENCE:
			final int defence = gameState.getInt(GameActivity.STATE_DEFENCE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_DEFENCE_LEVEL, defence+1);
			applyDefenceUpgradeToProtagonist();
			break;
		case UPGRADE_BULLET_FREQ:
			final int gunFreq = gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_FREQ_LEVEL, gunFreq+1);
			break;
		case UPGRADE_GUN:
			final int gunSet = gameState.getInt(GameActivity.STATE_GUN_CONFIG, -1);
			editor.putInt(GameActivity.STATE_GUN_CONFIG, gunSet+1);
			break;
		case UPGRADE_FRIEND:
			final int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0);
			editor.putInt(GameActivity.STATE_FRIEND_LEVEL, friendLvl+1);
			break;
		case UPGRADE_SCORE_MULTIPLIER:
			final int resourceLvl = gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
			editor.putInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, resourceLvl+1);
			break;
		case UPGRADE_HEAL:
			this.setHealth(getMaxHealth());
			editor.putInt(GameActivity.STATE_HEALTH, getMaxHealth());
			break;
		}
		
		editor.commit();

		final float freq = getShootingDelay();
		final int dmg = (int) (DEFAULT_BULLET_DAMAGE + getBulletDamageLevel() * BULLET_DAMAGE_WEIGHT);
		
		createGunSet(freq,dmg,getGunLevel());
	}

	public float getShootingDelay(){
		return DEFAULT_BULLET_FREQ - getBulletBulletFreqLevel() * BULLET_FREQ_WEIGHT;
	}
	public int getGunLevel(){
		return gameState.getInt(GameActivity.STATE_GUN_CONFIG,-1);
	}
	public int getBulletDamageLevel(){
		return gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
	}
	public int getDefenceLevel(){
		return gameState.getInt(GameActivity.STATE_DEFENCE_LEVEL, 0);
	}
	public int getBulletBulletFreqLevel(){
		return gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
	}
	public void applyDefenceUpgradeToProtagonist(){
		this.setHealth(DEFAULT_HEALTH+ (DEFAULT_HEALTH/10) *getDefenceLevel() );
	}
	
	@Override
	public void removeGameObject(){
		super.removeGameObject();
		
		if(this.getHealth()>0){//if not dead, then game is paused. Save needed Attributes
//			SharedPreferences gameState = getContext().getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
//			SharedPreferences.Editor editor = gameState.edit();
//			
//			
//			
//			editor.commit();
		}
	}
}
