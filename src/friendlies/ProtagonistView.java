package friendlies;

import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.GameActivityInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import android.content.Context;
import android.view.View;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class ProtagonistView extends Friendly_ShooterView{
	
	public static final int HOW_OFTEN_TO_MOVE_ROCKET=50;
	
	GameActivityInterface myGame;
	private boolean isMoving;
	
	public ProtagonistView(Context context,GameActivityInterface interactWithGame) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),R.drawable.ship_protagonist);

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
	
	
	private static final long EXHAUST_VISIBLE_TIME=500,
			HOW_OFTEN_TO_MOVE_EXHAUST=HOW_OFTEN_TO_MOVE_ROCKET / 2;
	private static final double EXHAUST_FREQ=5000;
	 
	private int count = 0;
    
    Runnable exhaustRunnable = new Runnable(){
    	 @Override
         public void run() {
				GameActivityInterface screen = (GameActivityInterface)ctx;
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
	
//	private boolean canBeginShooting=true;
	
//	@Override
//	public void startShooting(){
//
////		if(canBeginShooting){
//			super.startShooting();
////		}	
////		canBeginShooting=false;
//	}
//	

	@Override
	public void startShooting() {
		isShooting=true;
		for(Gun gun: myGuns){
			gun.startShootingImmediately();
		}
	}
	
//	@Override 
//	public void stopShooting(){
//		super.stopShooting();
//
//		Runnable canShootAgainRunnable = new Runnable(){//force a delay until user can begin shooting again
//			@Override
//			public void run() {	canBeginShooting=true;	}	};
//			
//		postDelayed(canShootAgainRunnable,(long)getShootingDelay() * 2);
//	}
	
	@Override 
	public boolean takeDamage(double howMuchDamage){
		boolean isDead = super.takeDamage(howMuchDamage);
//		if(isDead){//not working, but functional if put in the collision detection
//			myGame.gameOver();
//		}
		myGame.setHealthBar();
		return isDead;
	}
	
	@Override
	public void restartThreads(){
		this.post(exhaustRunnable);
		super.restartThreads();
	}
	
	/**
	 * Do not allow the rocket to move off screen or past bounds
	 */
	@Override
	public boolean moveDirection(int direction){
		float x =this.getX();
		float y =this.getY();
		
		switch(direction){
		case Moving_ProjectileView.RIGHT:
			x+=this.getSpeedX();
			if((x+this.getWidth())<=MainActivity.getWidthPixels()){this.setX(x);}
			break;
		case Moving_ProjectileView.LEFT:
			x-=this.getSpeedX();
			if(x>=0){this.setX(x);}			
			break;
		case Moving_ProjectileView.UP:
			y-=this.getSpeedY();
			if(y > MainActivity.getHeightPixels()/2){this.setY(y);}			
			break;
		case Moving_ProjectileView.DOWN:
			y+=this.getSpeedY();
			if(y < ( GameActivity.offscreenBottom - this.getHeight() ) ){this.setY(y);}			
			break;		
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
	
//	@Override
//	public void removeGameObject() {
//		//no additional cleanup currently needed
//		super.removeGameObject();
//	}
	
}
