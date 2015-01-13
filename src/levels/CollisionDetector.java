package levels;

import interfaces.Shooter;
import abstract_parents.MovingView;
import android.os.Handler;
import bonuses.BonusView;
import bullets.BulletView;

import com.jtronlabs.to_the_moon.GameActivity;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class CollisionDetector {
	
	private LevelSystem myLevelSystem;
	
	public CollisionDetector(LevelSystem aLevelSystem){
		myLevelSystem=aLevelSystem;
	}
	
	private Handler gameHandler = new Handler();
    private Runnable collisionDetectionRunnable = new Runnable() { 

        @Override
        public void run() {
        	if( ! myLevelSystem.isLevelPaused() && ! myLevelSystem.areLevelWavesCompleted() || GameActivity.enemies.size() !=0){
        		
        		boolean protagonistDiedFromCollision = detectAnyFriendlyHasCollidedWithAnyEnemy();
        		boolean protagonistDiedFromBullet = detectAnyFriendlyHasHitAnyEnemyBullet();
        		 
        		if(protagonistDiedFromCollision || protagonistDiedFromBullet){
        			GameActivity.gameOver(); 
        		}else{
	        		detectAnyFriendlyHasHitAnyBonus();
	        		detectAnyEnemyHasHitAnyFriendlyBullets();
	        		
		            gameHandler.postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
        		}
        	}else{
        		if(myLevelSystem.getLevel()==LevelSystem.MAX_NUMBER_LEVELS){
        			
        			GameActivity.beatGame();
        			
        		}else{
//	        		myLevelSystem.notifyLevelFinishedAndAllEnemiesAreDead();
	        		GameActivity.openStore();
        		}
        	}
        }
    };
    
    private static boolean detectAnyFriendlyHasCollidedWithAnyEnemy(){		
    	for(int k=GameActivity.friendlies.size()-1;k>=0;k--){
    		FriendlyView friendly = GameActivity.friendlies.get(k);
//    		if(!friendly.isRemoved()){//game object could be removed in previous loop iteration?
//    			
	    		boolean isProtagonist = friendly == GameActivity.protagonist;
	    		
		    	for(int i=GameActivity.enemies.size()-1;i>=0;i--){
		    		EnemyView enemy = GameActivity.enemies.get(i);
		    		if( /*!enemy.isRemoved() && */ friendly.collisionDetection(enemy)){
		    			//have the friendly take damage. if he is the protagonist then update health bar, 
						//and if he died then run gameover
		    			final boolean friendlyDies = friendly.takeDamage(enemy.getDamage());
		    			if(isProtagonist){
		    				if(friendlyDies){
		        				return true;
		    				}
		    				GameActivity.setHealthBar();
						}
		    			//enemy takes damage as well
		    			enemy.takeDamage(friendly.getDamage());
		    		}
		    	}
//	    	}
    	}
    	return false;
    }
    
    private static boolean detectAnyFriendlyHasHitAnyEnemyBullet(){
    	for(int k=GameActivity.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = GameActivity.friendlies.get(k);
//    		if(!friendly.isRemoved()){//game object could be removed in previous loop iteration
				boolean isProtagonist = friendly == GameActivity.protagonist;
				
				for(int j=GameActivity.enemyBullets.size()-1;j>=0;j--){
					BulletView bullet = GameActivity.enemyBullets.get(j);
					if(/*!bullet.isRemoved() && */ friendly.collisionDetection(bullet)){//game object could be removed in previous loop iteration
						//have the friendly take damage. if he is the protagonist then update health bar, 
						//and if he died then run gameover
		    			final boolean friendlyDies = friendly.takeDamage(bullet.getDamage());
		    			if(isProtagonist){
		    				if(friendlyDies){
		        				return true;
		    				}
		    				GameActivity.setHealthBar();
						}
		    			bullet.removeGameObject();
		    		}
				}
//    		}
    	}
		return false;
    }

    private static void detectAnyFriendlyHasHitAnyBonus(){
    	for(int k=GameActivity.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = GameActivity.friendlies.get(k);
			
			//check if friendly has hit a bonus
			if(friendly instanceof Shooter){
				Shooter friendlyShooter= (Shooter)GameActivity.friendlies.get(k);
		    	for(int i=GameActivity.bonuses.size()-1;i>=0;i--){
		    		
		    		BonusView bonus = GameActivity.bonuses.get(i);
		    		if( /*! bonus.isRemoved() &&*/ friendly.collisionDetection(bonus)){//game object could be removed in previous loop iteration
		    			bonus.applyBenefit(friendlyShooter);
		    			bonus.removeGameObject();
		    		}
		    	}
			}
    	}
    }
    
    private static void detectAnyEnemyHasHitAnyFriendlyBullets(){
    	for(int i=GameActivity.enemies.size()-1;i>=0;i--){
    		EnemyView enemy = GameActivity.enemies.get(i);
    		
//    		if(!enemy.isRemoved()){//game object could be removed in previous loop iteration
	    		for(int j=GameActivity.friendlyBullets.size()-1;j>=0;j--){
	    			
					BulletView bullet = GameActivity.friendlyBullets.get(j);
					if(/*!bullet.isRemoved() &&*/ bullet.collisionDetection(enemy)){//game object could be removed in previous loop iteration
	        			enemy.takeDamage(bullet.getDamage());
	        			bullet.removeGameObject();
	        		}
	        	}
//    		}
    	}
    }
    
    public void startDetecting(){
    	gameHandler.post(collisionDetectionRunnable);
    }
    public void stopDetecting(){
    	gameHandler.removeCallbacks(collisionDetectionRunnable);
    }
}
