package com.jtronlabs.to_the_moon;

import interfaces.Shooter;
import levels.LevelSystem;
import parents.MovingView;
import android.os.Handler;
import bonuses.BonusView;
import bullets.BulletView;
import enemies.EnemyView;
import friendlies.FriendlyView;

public class CollisionDetector {
	
	static Handler gameHandler = new Handler();
	   
    static Runnable collisionDetectionRunnable = new Runnable() { 

        @Override
        public void run() {
        	if( ! LevelSystem.isLevelCompleted() || GameActivity.enemies.size() !=0){
        		boolean enemyDies=false,friendlyDies=false;	
        		//run collision detection on every friendly
	        	for(int k=GameActivity.friendlies.size()-1;k>=0;k--){
	        		FriendlyView friendly = GameActivity.friendlies.get(k);
	        		boolean isProtagonist = GameActivity.friendlies.get(k) == GameActivity.protagonist;
	        		boolean friendlyIsAShooter = GameActivity.friendlies.get(k) instanceof Shooter;

		        	//check if enemy and friendly have collided
		        	for(int i=GameActivity.enemies.size()-1;i>=0;i--){
		        		EnemyView enemy = GameActivity.enemies.get(i);
		        		
		        		if(enemy.getHealth()>0 && friendly.collisionDetection(enemy)){
		        			friendlyDies = friendly.takeDamage(enemy.getDamage());
		        			if(friendlyDies && isProtagonist){GameActivity.gameOver();return;}
		        			else if(isProtagonist){GameActivity.setHealthBar();}
		        			
		        			enemyDies=enemy.takeDamage(friendly.getDamage());
		        			if(enemyDies){
		        				LevelSystem.incrementScore(enemy.getScoreForKilling());
		        			}
		        		}
		        		
		        	}

		        	//check if friendly has hit a bonus
		        	if(friendlyIsAShooter){
		        		Shooter friendlyShooter= (Shooter)GameActivity.friendlies.get(k);
			        	for(int i=GameActivity.bonuses.size()-1;i>=0;i--){
			        		BonusView beneficialCastedView = (BonusView)GameActivity.bonuses.get(i);
			        		if(friendly.collisionDetection(beneficialCastedView)){
			        			if(isProtagonist){
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
			        			}else if(friendlyIsAShooter){
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
				        		}else{
				        			beneficialCastedView.applyBenefit(friendlyShooter);
				        			beneficialCastedView.removeView(false);
			        			}
			        		}
			        	}
		        	}
		        
	        		//check if enemy bullets have hit friendly
	    			for(int j=GameActivity.enemyBullets.size()-1;j>=0;j--){
	    				BulletView bullet = GameActivity.enemyBullets.get(j);
	    				if(friendly.collisionDetection(bullet)){//bullet collided with rocket
	    					//rocket is damaged
	            			friendlyDies = friendly.takeDamage(bullet.getDamage());
	            			if(friendlyDies && isProtagonist){GameActivity.gameOver();return;}
	            			else if(isProtagonist){GameActivity.setHealthBar();}
	            			
	            			
	            			bullet.removeGameObject();
	            		}
	    			}
	        	}
    			
	        	//check if friendly bullets have hit enemy. This should not be done in the above loop over the friendlies, as it does not relate
    			for(int j=GameActivity.friendlyBullets.size()-1;j>=0;j--){
		        	for(int i=GameActivity.enemies.size()-1;i>=0;i--){
		        		
		        		EnemyView enemy = GameActivity.enemies.get(i);
	    				BulletView bullet = GameActivity.friendlyBullets.get(j);
	    				boolean stopCheckingIfFriendlysBulletsHitEnemy=false;
	    				
	    				if(bullet.collisionDetection(enemy)){
	    					//enemy is damaged
	            			enemyDies = enemy.takeDamage(bullet.getDamage());
	            			if(enemyDies){
	            				LevelSystem.incrementScore(enemy.getScoreForKilling());
	            			}
	            			
	            			bullet.removeGameObject();
	            			/*
	            			 * only one bullet can hit a specific enemy at once. 
	            			 * If that enemy were to die, then checking to see if the other bullets hit 
	            			 * him wastes resources and would cause extra bullets to be removed from game
	            			 */
	            			stopCheckingIfFriendlysBulletsHitEnemy=true;
	            		}
	    				if(stopCheckingIfFriendlysBulletsHitEnemy){
	            			break;
	    				}
		        	}
    			}
				/*			DO OTHER STUFF ?		*/
	            gameHandler.postDelayed(this, MovingView.HOW_OFTEN_TO_MOVE);
        	}else{
        		GameActivity.openStore();
        	}
        }
    };
    
    public static void startDetecting(){
    	gameHandler.post(collisionDetectionRunnable);
    }
    public static void stopDetecting(){
    	gameHandler.removeCallbacks(collisionDetectionRunnable);
    }
}
