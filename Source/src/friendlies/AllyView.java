package friendlies;

import guns.Gun_SingleShotStraight;
import helpers.KillableRunnable;
import parents.Moving_ProjectileView;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RelativeLayout;
import bullets.Bullet_Basic;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class AllyView extends Friendly_ShooterView{
	
	public static int MAX_ALLY_LEVEL = 7, 
			DEFAULT_BULLET_FREQ = 1250;
	
	ProtagonistView trackMe;
	
	/**
	 * First 4 levels upgrade health. Next 4 levels upgrade gun damage.
	 * @param context
	 * @param viewToTrack
	 * @param levelOfAlly
	 */
	public AllyView(RelativeLayout layout,ProtagonistView viewToTrack, int levelOfAlly) {
		super(layout,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				allyHealth(layout.getContext()),
				allyPictureAndDimensions(layout.getContext())[1], 
				allyPictureAndDimensions(layout.getContext())[2],
				allyPictureAndDimensions(layout.getContext())[0]);
		
		//set this runnable to track the protagonist
		trackMe = viewToTrack;
		this.setX(trackMe.getX() - this.getLayoutParams().width);
		this.setY(trackMe.getY());
		
		reassignMoveRunnable( new KillableRunnable(){
			@Override
			public void doWork() {
				final double pixelDistanceDelta = 3.5 * MainActivity.getScreenDens();
				
				//Ally tracks X midpoint of Protagonist
				final float allyMidPointX = (AllyView.this.getX()+AllyView.this.getWidth() + AllyView.this.getX())/2;
				final float protagonistMidPointX = (trackMe.getX()+trackMe.getWidth() + trackMe.getX())/2;
				
				if( allyMidPointX < protagonistMidPointX - pixelDistanceDelta){
					AllyView.this.setSpeedX(Math.abs(DEFAULT_SPEED_X));//move right
				}else if ( allyMidPointX > protagonistMidPointX + pixelDistanceDelta) {
					AllyView.this.setSpeedX( - Math.abs(DEFAULT_SPEED_X));//move left
				}else{
					AllyView.this.setSpeedX( 0 );//dont move
				}
				
				//Ally tracks top of Protagonist. Botttom of Ally desires to be at top of protagonist
				final float allyBottom = AllyView.this.getY()+AllyView.this.getHeight();
				final float protagonistTop = trackMe.getY();
				
				if( allyBottom < protagonistTop - pixelDistanceDelta){
					AllyView.this.setSpeedY(Math.abs(DEFAULT_SPEED_Y));//move up
				}else if ( allyBottom > protagonistTop + pixelDistanceDelta) {
					AllyView.this.setSpeedY( - Math.abs(DEFAULT_SPEED_Y));//move down
				}else{
					AllyView.this.setSpeedY( 0 );//dont move
				}
				
				move();
				postDelayed(this,Moving_ProjectileView.HOW_OFTEN_TO_MOVE);
			}
		});

		//apply gun upgrades
		final int allyLvl = allyLevel(getContext());
		final int bulletSize = (int) ( (allyLvl >= 5) ? getContext().getResources().getDimension(R.dimen.bullet_round_med_length) :
			getContext().getResources().getDimension(R.dimen.bullet_round_small_length));// level 6+ use med bullet, and 6- use small bullet
		addGun(new Gun_SingleShotStraight( getMyLayout(), this,
				new Bullet_Basic(
						bulletSize, 
						bulletSize, 
						R.drawable.bullet_laser_round_green),
				DEFAULT_BULLET_FREQ, 
				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
				( ProtagonistView.DEFAULT_BULLET_DAMAGE / 2 ) * Math.max(1, (allyLvl-3) ) , //scale dmg 4 times, starting at level 4. Before that just use default damage
				50)
			);
		startShooting();
		
		//remove the ally from foreground and add to background - behind the protagonist
		addToBackground(this);
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
	
	private static int allyHealth(Context ctx){
		return Math.min( ( ProtagonistView.DEFAULT_HEALTH/4 ) * (allyLevel(ctx)+1), ProtagonistView.DEFAULT_HEALTH );
	}
	
	private static int[] allyPictureAndDimensions(Context context){
		int[] retVal = new int[3];
//		switch( allyLevel(context) ){
//		case FIRST_LEVEL:
//			retVal[0] = R.drawable.ship_ally_0;
//			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_width);
//			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_height);
//			break;
//		case SECOND_LEVEL:
//			retVal[0] = R.drawable.ship_ally_1;
//			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_1_game_width);
//			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_1_game_height);
//			break;
//		default://maximum level
//			retVal[0] = R.drawable.ship_ally_2;
//			retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_2_game_width);
//			retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_2_game_height);
//			break;
//		}
		
		//always have the ally be ship_0 (for now)
		retVal[0] = R.drawable.ship_ally_0;
		retVal[1] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_width);
		retVal[2] = (int)context.getResources().getDimension(R.dimen.ship_ally_0_game_height);
		
		return retVal;
	}
	
}
