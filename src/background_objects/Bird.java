package background_objects;
import parents.MovingView;
import support.ConditionalHandler;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;

import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;
 
public class Bird extends BackgroundView{

	AnimationDrawable animation;
	
   	private Runnable moveSideways = new Runnable(){
   		
   		@Override
   		public void run() {
   			Bird.this.moveDirection(MovingView.SIDEWAYS);
   			ConditionalHandler.postIfAlive(this, MovingView.HOW_OFTEN_TO_MOVE,Bird.this);
   		}
   		
   	};
   	
       public Bird(Context context) {
		super(context, 3, -Math.random()*5-7, 
				(int) ( 30*MainActivity.getScreenDens() ), 
				(int) ( 30*MainActivity.getScreenDens() ), 
				0);

		//set position somewhere on the right side of the screen
		this.setX(MainActivity.getWidthPixels()-this.getLayoutParams().width);
		this.setY( (float) (( MainActivity.getHeightPixels()/2 ) *Math.random()));

		//reset image
		if(Math.random()<.5){
			this.setBackgroundResource(R.anim.bird_1);
		}else{
			this.setBackgroundResource(R.anim.bird_2);			
		} 
		//create background animation
		animation = (AnimationDrawable) this.getBackground();
	    animation.start();
	    

		ConditionalHandler.postIfAlive(moveSideways, this);
		
	}
       
       @Override
       public void removeGameObject(){
    	   animation.stop();
    	   super.removeGameObject();
       }
       
}