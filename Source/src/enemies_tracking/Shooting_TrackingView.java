package enemies_tracking;

import guns.Gun;
import guns.Gun_SingleShotStraight;
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import levels.AttributesOfLevels;
import parents.Moving_ProjectileView;
import android.content.Context;
import bullets.Bullet_Basic;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies.Enemy_ShooterView;
import friendlies.ProtagonistView;

public class Shooting_TrackingView extends Enemy_ShooterView{

	public static final float
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=(float) .02,
			DEFAULT_BULLET_FREQ=1000;
	
	public static final int DEFAULT_COLLISION_DAMAGE=ProtagonistView.DEFAULT_HEALTH/10,
			DEFAULT_BULLET_DAMAGE= ProtagonistView.DEFAULT_HEALTH/30,
			DEFAULT_SCORE=100,
			DEFAULT_HEALTH=(int) (ProtagonistView.DEFAULT_BULLET_DAMAGE  * 2.5),
			DEFAULT_BACKGROUND=R.drawable.ship_enemy_tracker;
	
	private Moving_ProjectileView viewToTrack;
	
	public Shooting_TrackingView(Context context,Moving_ProjectileView trackMe,int level) {
		super(context, level,
				DEFAULT_SCORE, 
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
		Gun defaultGun = new Gun_SingleShotStraight(getContext(), this, new Bullet_Basic(
				(int)getContext().getResources().getDimension(R.dimen.laser_long_width), 
				(int)getContext().getResources().getDimension(R.dimen.laser_long_height), 
				R.drawable.laser_rectangular_enemy),
				bulletFreq, 
				DEFAULT_BULLET_SPEED_Y, 
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

	

	public static int getSpawningProbabilityWeight(int level) {
		//start at 1.5x giant meteor, increase a little every 20 levels until equal to 2x giant meteor		
		int probabilityWeight = 0;
		if( level > AttributesOfLevels.LEVELS_BEGINNER ){
			probabilityWeight = (int) (AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR * 1.5 + 
				(level/20) * AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR/3);
		
			probabilityWeight = Math.min(probabilityWeight, AttributesOfLevels.WEIGHT_PROBABILITY_GIANT_METEOR * 2);
		}
		return probabilityWeight;
	}
}
