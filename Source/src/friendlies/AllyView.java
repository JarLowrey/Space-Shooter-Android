package friendlies;

import guns.Gun_SingleShotStraight;
import helpers.ConditionalHandler;
import helpers.KillableRunnable;
import interfaces.GameActivityInterface;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import bullets.Bullet_Basic;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

public class AllyView extends Friendly_ShooterView{
	
	ProtagonistView trackMe;
	
	private static final int FIRST_LEVEL = 1, SECOND_LEVEL = 2;
	
	public AllyView(Context context,ProtagonistView viewToTrack, int levelOfAlly) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				( ProtagonistView.DEFAULT_HEALTH/4 ) * allyLevel(context),
				allyPictureAndDimensions(context)[1], 
				allyPictureAndDimensions(context)[2],
				allyPictureAndDimensions(context)[0]);
		
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
		//higher level = gun shoots faster and hits harder
		addGun(new Gun_SingleShotStraight(getContext(), this,
				new Bullet_Basic(
						(int)getContext().getResources().getDimension(R.dimen.laser_long_width), 
						(int)getContext().getResources().getDimension(R.dimen.laser_long_height), 
						R.drawable.bullet_laser_rectangular_friendly),
				3000 / allyLevel(context), 
				DEFAULT_BULLET_SPEED_Y, 
				( DEFAULT_BULLET_DAMAGE / 2 ) * allyLevel(context) ,
				50)
			);
		startShooting();
		
		//remove this guy from foreground and add to background - behind the protagonist

		((GameActivityInterface)context).removeView(this);
		((GameActivityInterface)context).addToBackground(this);
	}
	 

	@Override 
	public boolean takeDamage(int howMuchDamage){ 
		boolean isDead = super.takeDamage(howMuchDamage);
		
		if(isDead){
			final long vibrationPattern[] = {0,100,100};
			createExplosion(this.getWidth(),this.getHeight(),R.drawable.explosion1,vibrationPattern);
		}
		
		return isDead;
	}
	
	private static int allyLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0);
	}
	
	private static int[] allyPictureAndDimensions(Context context){
		int[] retVal = new int[3];
		switch( allyLevel(context) ){
		case FIRST_LEVEL:
			retVal[0] = R.drawable.ship_ally_0;
			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_width);
			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_height);
			break;
		case SECOND_LEVEL:
			retVal[0] = R.drawable.ship_ally_1;
			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_1_game_width);
			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_1_game_height);
			break;
		default://maximum level
			retVal[0] = R.drawable.ship_ally_2;
			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_2_game_width);
			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_2_game_height);
			break;
		}
		Log.d("lowrey","ally lvl = "+allyLevel(context));
		return retVal;
	}
	
}
