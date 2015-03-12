package friendlies;

import com.jtronlabs.to_the_moon.GameActivity;
import com.jtronlabs.to_the_moon.MainActivity;

import parents.Moving_ProjectileView;
import levels.LevelSystem;
import android.content.Context;

public class FriendlyView extends Moving_ProjectileView{
	
	public FriendlyView(Context context,float projectileSpeedY
			,float projectileSpeedX, 
			int projectileDamage,int projectileHealth,int width,int height,int imageId) {
		super( context, projectileSpeedY,projectileSpeedX, 
				 projectileDamage, projectileHealth, width, height, imageId);
		
		LevelSystem.friendlies.add(this);
	}
	

	@Override
	public void removeGameObject() {
		LevelSystem.friendlies.remove(this);
		deaultCleanupOnRemoval();//needs to be called last for all pending callbacks to 'this' to be removed
	}

	/**
	 * Do not allow the friendly to move off screen
	 */
	@Override
	public boolean moveDirection(int direction){
		if(direction!=UP && direction!=DOWN && direction!=LEFT && direction != RIGHT){
			throw new IllegalArgumentException("direction argument must be MovingView.UP, MovingView.RIGHT,MovingView.DOWN, or MovingView.LEFT");
		}
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
			if(y > 2 * MainActivity.getHeightPixels()/5){this.setY(y);}			
			break;
		case Moving_ProjectileView.DOWN:
			y+=this.getSpeedY();
			if(y < ( GameActivity.getBottomScreen() - this.getHeight() ) ){this.setY(y);}			
			break;		
		}
		return false;
	}
}
