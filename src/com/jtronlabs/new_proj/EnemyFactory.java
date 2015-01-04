package com.jtronlabs.new_proj;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jtronlabs.views.GravityView;
import com.jtronlabs.views.ProjectileView;

public class EnemyFactory{
	
    private final int[] spaceJunkDrawableIds = {R.drawable.ufo,R.drawable.satellite,R.drawable.meteor};
    private float screenDens,widthPixels,heightPixels;    
	private Context ctx;
	private Levels levelInfo = new Levels();
	
    //
    public int meteorInterval=5000;
    
    Handler enemySpawnHandler = new Handler();
    Runnable meteorRunnable = new Runnable(){
    	@Override
        public void run() {
//    		double rand = Math.random()*100;
//    		switch(levelInfo.getLevel()){
//    		case 1:
//    			if(rand<)
//    			break;
//    		case 2:
//    			
//    			break;
//    		case 3:
//    			
//    			break;
//    		case 4:
//    			
//    			break;
//    		case 5:
//    			
//    			break;
//    		}
    		GravityView new_junk_img = createNewSpaceJunk(1,1,1,1);
    		GameActivity.gameScreen.addView(new_junk_img,1);
    		GameActivity.dangerousObjects.add(new_junk_img);
    		//for level 1, meteors spawn between meteorInterval and meteorInteval+1 seconds. Each level increase, decreases the time by 1000/sqrt(lvl)
    		enemySpawnHandler.postDelayed(this, (long) (meteorInterval/(Math.sqrt(levelInfo.getLevel()))+Math.random()*1000));
    	}
	};
	
	public EnemyFactory(Context context){
		ctx=context;
		
		//find screen density and width/height of screen in pixels
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
	    screenDens = displayMetrics.density;
	    widthPixels = displayMetrics.widthPixels;
	    heightPixels = displayMetrics.heightPixels;
	    
	    enemySpawnHandler.post(meteorRunnable);
	}
	
	public void cleanUpThreads(){
		enemySpawnHandler.removeCallbacks(meteorRunnable);
	}
	public void restartThreads(){
		enemySpawnHandler.post(meteorRunnable);
	}

	public GravityView createNewSpaceJunk(double speedY,double speedX, double damage, double health){
		//create a new space junk ImageView and modify force of gravity by screenDens
		GravityView new_junk_img = new GravityView(ctx,speedY,speedX,damage,health);
		
		//set image background
		new_junk_img.setImageResource(R.drawable.meteor);
		
		//add image to layout
		int len=(int)ctx.getResources().getDimension(R.dimen.space_junk_length);
		new_junk_img.setLayoutParams(new LayoutParams(len,len));
		
		//position View
		float xRand = (float) ((widthPixels-len)*Math.random());
		new_junk_img.setX(xRand);
		new_junk_img.setY(-len/2);//slightly off top of screen
		
		return new_junk_img;
	}
	
}
