package com.jtronlabs.enemy_types_non_shooters;

import android.content.Context;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.enemy_types.EnemyView;
import com.jtronlabs.to_the_moon.MainActivity;
import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.parents.MovingView;

public class Meteor_SidewaysMovingView extends EnemyView{
	
	public final static int DEFAULT_SCORE=5,DEFAULT_BACKGROUND=R.drawable.meteor;
	public final static double DEFAULT_SPEED_Y=7,DEFAULT_SPEED_X=10,
			DEFAULT_COLLISION_DAMAGE=12, 
			DEFAULT_HEALTH=5,
			DEFAULT_SPAWN_BENEFICIAL_OBJECT_ON_DEATH=.01;
	
	Runnable moveSidewaysRunnable = new Runnable(){
		@Override
		public void run() {
			Meteor_SidewaysMovingView.this.moveDirection(MovingView.SIDEWAYS);
			Meteor_SidewaysMovingView.this.postDelayed(this,MovingView.HOW_OFTEN_TO_MOVE);
		}
	};
	
	public Meteor_SidewaysMovingView(Context context,int score,double speedY,double speedX,
			double collisionDamage,double health,double probSpawnBeneficialObjectOnDeath,boolean moveLeft) {
		super(context,score,speedY,speedX,collisionDamage,
				health,probSpawnBeneficialObjectOnDeath);
				
		if(moveLeft){
			this.setSpeedX(this.getSpeedX()* -1);
		}
		//set image background
		this.setImageResource(R.drawable.meteor);
		
		//set image width,length
		int len=(int)this.getResources().getDimension(R.dimen.meteor_length);
		this.setLayoutParams(new LayoutParams(len,len));
		
		//set initial position of View
		float xRand = (float) ((MainActivity.getWidthPixels()-len)*Math.random());
		this.setX(xRand);
		this.setY(0);
		
	}
}
