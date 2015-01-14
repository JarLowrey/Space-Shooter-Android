package friendlies;

import parents.Moving_ProjectileView;
import guns.Gun;
import guns.Gun_AngledDualShot;
import guns.Gun_StraightSingleShot;
import interfaces.InteractiveGameInterface;
import support.ConditionalHandler;
import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;
import bullets.Bullet_Basic_LaserShort;
import bullets.Bullet_Basic_Missile;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class ProtagonistView extends Friendly_ShooterView{
	
	private final int HOW_OFTEN_TO_MOVE_ROCKET=50;
	private int directionMoving=Moving_ProjectileView.LEFT;
	
	InteractiveGameInterface myGame;
	
	public ProtagonistView(Context context,InteractiveGameInterface interactWithGame) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEEDX,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH);

		this.stopShooting();
		
		//set image background
		this.setImageResource(R.drawable.ship_protagonist);
		
		//set image width,length and X position. Let Y Position be set in GameActivity
		int height=(int)this.getResources().getDimension(R.dimen.protagonist_game_height);
		int width=(int)this.getResources().getDimension(R.dimen.protagonist_game_width);
		this.setLayoutParams(new LayoutParams(width,height));
		
		this.setX(MainActivity.getWidthPixels()/2-width/2);//middle of screen
		
		myGame=interactWithGame;
		
		//debugging purposes only, will be deleted when user buys first gun
		Gun gun1 = new Gun_AngledDualShot(ctx, this, new Bullet_Basic_LaserShort(),
				DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
		Gun gun2 = new Gun_StraightSingleShot(ctx, this, new Bullet_Basic_Missile(),
				DEFAULT_BULLET_FREQ,DEFAULT_BULLET_DAMAGE,DEFAULT_BULLET_SPEED_Y);
		this.addGun(gun2);
		this.addGun(gun1);
	}
	
	
//	private static final long EXHAUST_VISIBLE_TIME=500,EXHAUST_MOVE_FREQ=20;
//	private static final double EXHAUST_FREQ=15000;
//	 
//	private int count = 0;
//    
//    Runnable exhaustRunnable = new Runnable(){
//    	 @Override
//         public void run() {
//			//ensure view is not removed before running
//				if(count*EXHAUST_MOVE_FREQ<EXHAUST_VISIBLE_TIME){
//					GameActivity.rocketExhaust.setVisibility(View.VISIBLE);					
//		    		 //position the exhaust
//					final float y=ProtagonistView.this.getY()+ProtagonistView.this.getHeight(); //set the fire's Y pos to behind rocket
//					final float averageRocketsX= (2 * ProtagonistView.this.getX()+ProtagonistView.this.getWidth() )/2;//find average of rocket's left and right x pos
//					final float x = averageRocketsX-GameActivity.rocketExhaust.getWidth()/2;//fire's new X pos should set the middle of fire to middle of rocket
//					GameActivity.rocketExhaust.setY(y);
//					GameActivity.rocketExhaust.setX(x);
//					count++;
//
//					ConditionalHandler.postIfAlive(this,EXHAUST_MOVE_FREQ,ProtagonistView.this);//repost this runnable so the exhaust will reposition quickly
//				
//				}else{
//					count=0;
//					GameActivity.rocketExhaust.setVisibility(View.INVISIBLE);					
//					ConditionalHandler.postIfAlive(this,EXHAUST_FREQ+ 2 * EXHAUST_FREQ*Math.random(),ProtagonistView.this);//repost this runnable so the exhaust will reposition quickly
//				}
//         }
//    };
    
	private Runnable moveRunnable = new Runnable(){
		@Override
		public void run() {
				ProtagonistView.this.moveDirection(directionMoving);
				ConditionalHandler.postIfAlive(moveRunnable,HOW_OFTEN_TO_MOVE_ROCKET,ProtagonistView.this);
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
	
	public void beginMoving(int direction){
		directionMoving = direction;
		this.post(moveRunnable);
	}
	public void stopMoving(){
		this.removeCallbacks(moveRunnable);
	}

	@Override
	public void removeGameObject() {
		//no additional cleanup currently needed
		super.removeGameObject();
	}
	
}
