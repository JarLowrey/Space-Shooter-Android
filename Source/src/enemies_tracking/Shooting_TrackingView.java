package enemies_tracking;

import interfaces.GameActivityInterface;
import guns.Gun;
import guns.Gun_SingleShotStraight;
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import levels.AttributesOfLevels;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
import enemies.Shooting_DiagonalMovingView;
import friendlies.ProtagonistView;

public class Shooting_TrackingView extends Enemy_ShooterView{

	public static final float
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .02,
			DEFAULT_BULLET_FREQ=850,
			DEFAULT_SPEED_X = 16;
	
	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/50,
			DEFAULT_SCORE=100,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE  * 5),
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker;
	
	private Moving_ProjectileView viewToTrack;
	
	public Shooting_TrackingView(RelativeLayout layout,int level) {
		super(layout, level,
				DEFAULT_SCORE,
				getDefaultSpeedY(level), 
				DEFAULT_SPEED_X,
				DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, 
				DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH, 
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_tracker_width),
				(int)layout.getContext().getResources().getDimension(R.dimen.ship_tracker_height), 
				DEFAULT_BACKGROUND);

		//set up the enemy to track the given MovingView
		viewToTrack = ((GameActivityInterface)getContext()).getProtagonist();
		this.setX((float) (MainActivity.getWidthPixels()*Math.random()));
		
		//tracking view maintains constant downward speed
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				Shooting_TrackingView.this.setSpeedX(getTrackingSpeedX());
				
				move();				
				ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,Shooting_TrackingView.this);
			}
		});
		
		
		if(Math.random()< 0.5 && level>AttributesOfLevels.LEVELS_LOW){//the tracking view accelerates as it moves down the screen
			reassignMoveRunnable( new KillableRunnable(){
				@Override
				public void doWork() {
					Shooting_TrackingView.this.setSpeedX(getTrackingSpeedX());
					Shooting_TrackingView.this.setSpeedY(
							(float) (Shooting_TrackingView.this.getSpeedY()+0.3*MainActivity.getScreenDens()));
					
					move();				
					ConditionalHandler.postIfAlive(this,HOW_OFTEN_TO_MOVE,Shooting_TrackingView.this);
				}
			});
		}
		   

		//add guns
		final float bulletFreq = (float) (DEFAULT_BULLET_FREQ + 2 * DEFAULT_BULLET_FREQ * Math.random());
		Gun defaultGun = new Gun_SingleShotStraight(getMyLayout(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
				(int)getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
				R.drawable.bullet_laser_round_red),
				bulletFreq, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				DEFAULT_BULLET_DAMAGE,50);
		this.addGun(defaultGun);
		this.startShooting();
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
	public void reachedGravityPosition() {
		removeGameObject();
	}
	
	private static float getDefaultSpeedY(int level){
		if(level< AttributesOfLevels.LEVELS_MED){
			return DEFAULT_SPEED_Y;
		}else{
			return (float) (DEFAULT_SPEED_Y * 1.1);
		}
	}

	public static int getSpawningProbabilityWeight(int level) {
		int probabilityWeight = 0;
		if( level > AttributesOfLevels.LEVELS_BEGINNER ){
			if(level < AttributesOfLevels.LEVELS_MED){
				probabilityWeight = (int) (Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level) * .78);
			}else{
				probabilityWeight = Shooting_DiagonalMovingView.getSpawningProbabilityWeight(level);				
			}
		}
		return probabilityWeight;
	}
}
