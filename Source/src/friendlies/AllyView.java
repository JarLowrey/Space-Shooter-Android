package friendlies;

import guns.Gun_SingleShotStraight;
import interfaces.GameActivityInterface;
import parents.Moving_ProjectileView;
import support.ConditionalHandler;
import support.KillableRunnable;
import android.content.Context;
import bullets.Bullet_Basic_LaserLong;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class AllyView extends Friendly_ShooterView{
	
	ProtagonistView trackMe;
	
	public AllyView(Context context,ProtagonistView viewToTrack, int levelOfAlly) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),R.drawable.ship_protagonist);
		
		//set this runnable to track the protagonist
		trackMe = viewToTrack;
		this.setX(trackMe.getX() - this.getLayoutParams().width);
		this.setY(trackMe.getY());
		
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				final float pixelDistanceDelta = 7 * MainActivity.getScreenDens();
				
				//keep it simple. Always move the ally on the left side of protagonist
				if( (AllyView.this.getX()+AllyView.this.getWidth() ) < trackMe.getX()){//right side of ally is left of left side of protagonist
					AllyView.this.setSpeedX(Math.abs(DEFAULT_SPEED_X));
//				}else if( AllyOneView.this.getX() > ( trackMe.getWidth() + trackMe.getX() ) ){//left side of ally is right of right side of protagonist
//					AllyOneView.this.setSpeedX( - Math.abs(DEFAULT_SPEED_X));
//				}else{
//					AllyOneView.this.setSpeedX(0);					
//				}
				}else if ( (AllyView.this.getX()+AllyView.this.getWidth() ) >
						trackMe.getX()+ pixelDistanceDelta ) {
					AllyView.this.setSpeedX( - Math.abs(DEFAULT_SPEED_X));
				}else{
					AllyView.this.setSpeedX( 0 );
				}
				
				//move the ally up/down to stay on same vertical plane
				float yDiff = AllyView.this.getY() - trackMe.getY();
				if( Math.abs( yDiff ) > pixelDistanceDelta ){
					AllyView.this.setSpeedY(DEFAULT_SPEED_Y * -1 * ( yDiff / Math.abs(yDiff) ) );//move in the direction to close the gap bewtween ships
				}else{
					AllyView.this.setSpeedY(0);
				}
				
				move();
				ConditionalHandler.postIfAlive(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE,AllyView.this);
			}
		});

		//apply upgrades
		//createGunSet(DEFAULT_BULLET_FREQ,(int) (DEFAULT_BULLET_DAMAGE+BULLET_DAMAGE_WEIGHT*levelOfAlly),levelOfAlly);
		addGun(new Gun_SingleShotStraight(getContext(), this,
				new Bullet_Basic_LaserLong(),
				1000, 
				DEFAULT_BULLET_SPEED_Y, 
				DEFAULT_BULLET_DAMAGE,
				50)
			);
		startShooting();
		
		//remove this guy from foreground and add to background - behind the protagonist

		((GameActivityInterface)context).removeView(this);
		((GameActivityInterface)context).addToBackground(this);
	} 
	
}
