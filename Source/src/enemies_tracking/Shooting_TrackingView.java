package enemies_tracking;

import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;

public class Shooting_TrackingView extends Enemy_ShooterView{

	public static final float
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .02,
			DEFAULT_BULLET_FREQ=10000;
	
	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_SCORE=100,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE  * 2.5),
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker;
	
	private Moving_ProjectileView viewToTrack;
	
	public Shooting_TrackingView(Context context,Moving_ProjectileView trackMe,final int difficulty) {
		super(context, DEFAULT_SCORE, 
				DEFAULT_SPEED_Y, DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, 
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)context.getResources().getDimension(R.dimen.ship_tracker_width),
				(int)context.getResources().getDimension(R.dimen.ship_tracker_height), 
				DEFAULT_BACKGROUND);

		//set up the enemy to track the given MovingView
		viewToTrack=trackMe;
		this.setX((float) (MainActivity.getWidthPixels()*Math.random()));
		
		
		if(difficulty>0){//the tracking view accelerates as it moves down the screen
			reassignMoveRunnable( new KillableRunnable(){
				@Override
				public void doWork() {
					Shooting_TrackingView.this.setSpeedX(getTrackingSpeedX());
					Shooting_TrackingView.this.setSpeedY(
							(float) (Shooting_TrackingView.this.getSpeedY()+0.2*difficulty*MainActivity.getScreenDens()));
					
					move();				
					ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,Shooting_TrackingView.this);
				}
			});
		}else{//tracking view maintains constant downward speed
			reassignMoveRunnable( new KillableRunnable(){
				@Override
				public void doWork() {
					Shooting_TrackingView.this.setSpeedX(getTrackingSpeedX());
					
					move();				
					ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,Shooting_TrackingView.this);
				}
			});
		}
	}
	
	protected float getTrackingSpeedX(){
		final int trackPoint =(int) ( viewToTrack.getX()*2 + viewToTrack.getWidth() )/2;
		final int myPos = (int)( Shooting_TrackingView.this.getX()*2 + Shooting_TrackingView.this.getWidth() )/2;
		final int diff = trackPoint - myPos;
		float speedX = DEFAULT_SPEED_X;
		
		//set X direction
		final float pixelDistanceDelta = 5 * MainActivity.getScreenDens();
		if(Math.abs(diff) > pixelDistanceDelta){
			speedX  *= diff/Math.abs(diff);//multiply by sign of diff 
		}else{
			speedX = 0;
		}
		return speedX;	
	}
	
	@Override 
	public void restartThreads(){
		super.restartThreads();
	}

	@Override
	public float getShootingFreq() {
		return DEFAULT_BULLET_FREQ;
	}

	@Override
	public void reachedGravityPosition() {
		removeGameObject();
	}

}
