package friendlies;

import guns.Gun;
import android.content.Context;

import com.jtronlabs.to_the_moon.R;

public class AllyOneView extends Friendly_ShooterView{
	
	ProtagonistView trackMe;
	
	public AllyOneView(Context context,ProtagonistView viewToTrack, int levelOfAlly) {
		super(context,DEFAULT_SPEED_Y,DEFAULT_SPEED_X,DEFAULT_COLLISION_DAMAGE,
				DEFAULT_HEALTH, (int)context.getResources().getDimension(R.dimen.ship_protagonist_game_width), 
				(int)context.getResources().getDimension(R.dimen.ship_protagonist_game_height),R.drawable.ship_protagonist);

		trackMe = viewToTrack;
		
		//apply upgrades
		createGunSet(DEFAULT_BULLET_FREQ,(int) (DEFAULT_BULLET_DAMAGE+BULLET_DAMAGE_WEIGHT*levelOfAlly),levelOfAlly);
		startShooting();
	} 

	@Override
	public void startShooting() {
		isShooting=true;
		for(Gun gun: myGuns){
			gun.startShootingImmediately();
		}
	}
	
	@Override
	public void removeGameObject(){
		stopShooting();
		super.removeGameObject();
		
	}
}
