package levels;

import interfaces.Shooter;
import parents.MovingView;
import android.os.Handler;
import android.util.Log;
import bonuses.BonusView;
import bullets.BulletView;
import enemies.EnemyView;
import friendlies.FriendlyView;

public class CollisionDetector {
	
	private LevelSystem levelingSystem;
	
	public CollisionDetector(LevelSystem aLevelSystem){
		levelingSystem=aLevelSystem;
	}
	private Handler gameHandler = new Handler();
    private Runnable collisionDetectionRunnable = new Runnable() { 
        @Override
        public void run() {
        	Log.d("lowrey","enemiesNo="+LevelSystem.enemies.size() +" enemy bulletsNo=" +
        			LevelSystem.enemyBullets.size()+" waveNo="+levelingSystem.getWave() +" level="+levelingSystem.getLevel() );
        	
        	if( levelingSystem.getInteractivityInterface().getProtagonist().getHealth() > 0 &&
        			( ! levelingSystem.isLevelPaused() && ! levelingSystem.areLevelWavesCompleted() 
        			|| LevelSystem.enemies.size() !=0 || LevelSystem.enemyBullets.size() != 0  || LevelSystem.bonuses.size() != 0) ){
        		
        		detectAnyFriendlyHasCollidedWithAnyEnemy();
        		detectAnyFriendlyHasHitAnyEnemyBullet();
        		detectAnyFriendlyHasHitAnyBonus();
        		detectAnyEnemyHasHitAnyFriendlyBullets();
        		
	            gameHandler.postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
	            
        	}else{
    			levelingSystem.endLevel();
    			
        		if(levelingSystem.getInteractivityInterface().getProtagonist().getHealth() <= 0 ){
        			levelingSystem.getInteractivityInterface().gameOver();
        		}else if( levelingSystem.getLevel() > levelingSystem.getMaxLevel() ){
        			levelingSystem.getInteractivityInterface().beatGame();
        		}else{ 
        			levelingSystem.getInteractivityInterface().openStore();
        		}
        	}
        } 
    };

	//it is possible for enemy to be removed after beginning a for(enemies) loop, but before calling enemies.get(i)
	//in that case, just catch the error and don't worry about it, it was already processed
    
    private void detectAnyFriendlyHasCollidedWithAnyEnemy(){		
    	for(int k=LevelSystem.friendlies.size()-1;k>=0;k--){
    		FriendlyView friendly = LevelSystem.friendlies.get(k);
    		
	    	for(int i=LevelSystem.enemies.size()-1;i>=0;i--){
	    		try{
		    		EnemyView enemy = LevelSystem.enemies.get(i);
		    		if(friendly.collisionDetection(enemy)){
		    			
		    			friendly.takeDamage(enemy.getDamage());
		    			enemy.takeDamage(friendly.getDamage());
		    		}
	    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
	    	}
    	}
    }
    
    private void detectAnyFriendlyHasHitAnyEnemyBullet(){
    	for(int k=LevelSystem.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = LevelSystem.friendlies.get(k);
			
			for(int j=LevelSystem.enemyBullets.size()-1;j>=0;j--){
				try{
					BulletView bullet = LevelSystem.enemyBullets.get(j);
					if( friendly.collisionDetection(bullet)){
	
		    			friendly.takeDamage(bullet.getDamage());
		    			bullet.removeGameObject();
		    		}
	    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
			}
    	}
    }

    private void detectAnyFriendlyHasHitAnyBonus(){
    	for(int k=LevelSystem.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = LevelSystem.friendlies.get(k);
			
			//check if friendly has hit a bonus
			if(friendly instanceof Shooter){
				Shooter friendlyShooter= (Shooter)LevelSystem.friendlies.get(k);
		    	for(int i=LevelSystem.bonuses.size()-1;i>=0;i--){
		    		try{
			    		BonusView bonus = LevelSystem.bonuses.get(i);
			    		if( /*! bonus.isRemoved() &&*/ friendly.collisionDetection(bonus)){//game object could be removed in previous loop iteration
			    			bonus.applyBenefit(friendlyShooter);
			    			bonus.removeGameObject();
			    		}
		    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
		    	}
			}
    	}
    }
    
    private void detectAnyEnemyHasHitAnyFriendlyBullets(){
    	for(int i=LevelSystem.enemies.size()-1;i>=0;i--){
    		EnemyView enemy = LevelSystem.enemies.get(i);
    		
	    		for(int j=LevelSystem.friendlyBullets.size()-1;j>=0;j--){
	    			try{
						BulletView bullet = LevelSystem.friendlyBullets.get(j);
						if( bullet.collisionDetection(enemy)){
							
		        			enemy.takeDamage(bullet.getDamage());
		        			bullet.removeGameObject();
		        		}
		    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
	        	}
    	}
    }
    
    public void startDetecting(){
    	gameHandler.post(collisionDetectionRunnable);
    }
    public void stopDetecting(){
    	gameHandler.removeCallbacks(collisionDetectionRunnable);
    }
}
