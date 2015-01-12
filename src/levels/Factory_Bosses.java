package levels;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;

import enemies_non_shooters.Gravity_MeteorView;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * Create a default enemy using EnemyFactory class, then overwrite position, speed, damage size, background, guns, bullets, etc To make a boss
 * @author JAMES LOWREY
 *
 */
public class Factory_Bosses extends Factory_Enemies{
	
	public Factory_Bosses(Context context,RelativeLayout gameScreen){
		super(context,gameScreen);
	} 

	protected Gravity_MeteorView spawnGiantMeteor(){
		Gravity_MeteorView giant = spawnSidewaysMeteor();
		
		//change width and height. set X and Y positions
		final int width = (int)ctx.getResources().getDimension(R.dimen.giant_meteor_length);
		final int height= (int)ctx.getResources().getDimension(R.dimen.giant_meteor_length);
		
		giant.setLayoutParams(new LayoutParams(width,height));
		giant.setX((float) ((MainActivity.getWidthPixels()-width)*Math.random()));//with non default size, set new position
		
		//set damage and health to 200, score to 20
		giant.setDamage(150);
		giant.heal(100);
		giant.setScoreValue(100);
		
		return giant;
	}
}
