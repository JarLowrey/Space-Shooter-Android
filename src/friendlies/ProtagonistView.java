package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.GameView;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;
import android.util.Log;
import android.view.View;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class ProtagonistView extends Friendly_ShooterView{
	
	public static final int HOW_OFTEN_TO_MOVE_ROCKET=50;
	
	GameView myGame;
	private boolean canShoot,isMoving;
	
	public ProtagonistView(Context context,GameView interactWithGame) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),R.drawable.ship_protagonist);

		canShoot=true;
		
		this.setX(  MainActivity.getWidthPixels()/2 - context.getResources().getDimension(R.dimen.ship_protagonist_game_width)/2 );//middle of screen
		
		myGame=interactWithGame;
		
		//debugging purposes only, will be overwritten when user buys first gun
		Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),
				DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
		Gun gun2 = new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_Missile(),
				DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
		this.addGun(gun2);
		this.addGun(gun1);
		this.post(exhaustRunnable);
	}
	
	
	private static final long EXHAUST_VISIBLE_TIME=500,HOW_OFTEN_TO_MOVE_EXHAUST=HOW_OFTEN_TO_MOVE_ROCKET / 2;
	private static final double EXHAUST_FREQ=5000;
	 
	private int count = 0;
    
    Runnable exhaustRunnable = new Runnable(){
    	 @Override
         public void run() {
				GameView screen = (GameView)ctx;
				if(count*HOW_OFTEN_TO_MOVE_ROCKET<EXHAUST_VISIBLE_TIME){
					screen.getExhaust().setVisibility(View.VISIBLE);					
		    		 //position the exhaust
					final float y=ProtagonistView.this.getY()+ProtagonistView.this.getHeight(); //set the fire's Y pos to behind rocket
					final float averageRocketsX= (2 * ProtagonistView.this.getX()+ProtagonistView.this.getWidth() )/2;//find average of rocket's left and right x pos
					final float x = averageRocketsX-screen.getExhaust().getLayoutParams().width /2;//fire's new X pos should set the middle of fire to middle of rocket
					screen.getExhaust().setY(y);
					screen.getExhaust().setX(x);
					count++;

					ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE_EXHAUST,ProtagonistView.this);//repost this runnable so the exhaust will reposition quickly
				
				}else{
					count=0;
					screen.getExhaust().setVisibility(View.GONE);					
					ConditionalHandler.postIfAlive(this,(long) (EXHAUST_FREQ+ 2 * EXHAUST_FREQ*Math.random()),ProtagonistView.this);//repost this to a random time in the future
				}
         }
    };
	
	@Override
	public void heal(double howMuchHealed){
		super.heal(howMuchHealed);
		myGame.setHealthBar();
	}
	
	@Override 
	public boolean takeDamage(double howMuchDamage){
		boolean isDead = super.takeDamage(howMuchDamage);
		if(isDead){
			myGame.gameOver();
		}
		myGame.setHealthBar();
		return isDead;
	}
	
	@Override
	public void restartThreads(){
		super.restartThreads();
		this.stopShooting();//super will start the gun shooting. For the protagonist, gun must not be shooting on restart
	}
	
	/**
	 * Do not allow the rocket to move off the sides of the screen
	 */
	@Override
	public boolean moveDirection(int direction){
		float x =this.getX();
		
		switch(direction){
		case Moving_ProjectileView.RIGHT:
			x+=this.getSpeedX();
			if((x+this.getWidth())<=MainActivity.getWidthPixels()){this.setX(x);}
			break;
		case Moving_ProjectileView.LEFT:
			x-=this.getSpeedX();
			if(x>=0){this.setX(x);}			
			break;
		default:
			return super.moveDirection(direction);			
		}
		return false;
	}
	
	public void beginMoving(final int direction){
		isMoving=true;
		ConditionalHandler.startMoving(this, direction);
	}
	public void stopMoving(){
		isMoving=false;
	}
	public boolean isMoving(){
		return isMoving;
	}
	
	private int numShootings=0;
public int getNumShooting(){
	return numShootings;
}
	@Override
	public void startShooting(){
		numShootings++;
		Log.d("lowrey","numShots"+numShootings);
//		Log.d("lowrey","guns avail?"+canShoot);
		if(canShoot && numShootings==1){
			super.startShooting();
			canShoot=false;
		}
	}
	@Override
	public void stopShooting(){
		super.stopShooting();

		Runnable delayedShot = new Runnable(){
			@Override
			public void run() { canShoot=true;	numShootings--; }};
			
		this.postDelayed(delayedShot, (long) (DEFAULT_BULLET_FREQ - this.bulletFreqLevel * BULLET_FREQ_WEIGHT));
	}
	
	public boolean canShoot(){
		return canShoot;
	}
	
//	@Override
//	public void removeGameObject() {
//		//no additional cleanup currently needed
//		super.removeGameObject();
//	}
	
}
