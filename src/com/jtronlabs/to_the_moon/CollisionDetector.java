package com.jtronlabs.to_the_moon;

import interfaces.Shooter;

import java.util.ArrayList;

import levels.LevelSystem;

import parents.MovingView;
import android.os.Handler;
import android.util.Log;
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
	        	for(int k=GameActivity.friendlies.size()-1;k>=0;k--){
	        		FriendlyView friendly = GameActivity.friendlies.get(k);
	        		boolean isProtagonist = GameActivity.friendlies.get(k) == GameActivity.protagonist;
	        		
	        		boolean friendlyIsAShooter = GameActivity.friendlies.get(k) instanceof Shooter;
	        		
		        	for(int i=GameActivity.enemies.size()-1;i>=0;i--){
		        		/*			COLLISION DETECTION			*/
		        		boolean enemyDies=false,friendlyDies=false;	        		
		        		EnemyView enemy = GameActivity.enemies.get(i);
		        		boolean enemyIsAShooter = enemy instanceof Shooter;
		        		
		        		
		        		//check enemy's bullets
		        		if(enemyIsAShooter){
			        		Shooter enemyShooter = (Shooter)enemy;
			        		
			        			ArrayList<BulletView> enemyBullets = enemyShooter.getMyBullets();
			        			
			        			if(enemyBullets.size()==0 && enemy.getHealth()<=0){enemy.removeGameObject();}//check if enemy is dead and all bullets are gone

			        			
			            		if(LevelSystem.isLevelCompleted()){Log.d("lowrey","numEnemies="+GameActivity.enemies.size()+
			            				"bullets="+enemyBullets.size());}
			            		
			            		
			        			for(int j=enemyBullets.size()-1;j>=0;j--){
			        				BulletView bullet = enemyBullets.get(j);
			        				if(friendly.collisionDetection(bullet)){//bullet collided with rocket
			        					//rocket is damaged
			                			friendlyDies = friendly.takeDamage(bullet.getDamage());
			                			if(friendlyDies && isProtagonist){GameActivity.gameOver();return;}
			                			else if(isProtagonist){GameActivity.setHealthBar();}
			                			
			                			
			                			bullet.removeGameObject();
			                		}
			        			}
			        			
		        		}
		        		
		    			//check if the enemy itself has collided with the  friendly
		        		if(enemy.getHealth()>0 && friendly.collisionDetection(enemy)){
		        			friendlyDies = friendly.takeDamage(enemy.getDamage());
		        			if(friendlyDies && isProtagonist){GameActivity.gameOver();return;}
		        			else if(isProtagonist){GameActivity.setHealthBar();}
		        			
		        			enemyDies=enemy.takeDamage(friendly.getDamage());
		        			if(enemyDies){
		        				LevelSystem.incrementScore(enemy.getScoreForKilling());
		        			}
		        		}
	
	//    				Log.d("lowrey","enemy);
		        		//check if friendly's bullets have hit the enemy
		        		if(enemy.getHealth()>0 && friendlyIsAShooter){
			        		Shooter friendlyShooter= (Shooter)GameActivity.friendlies.get(k);
			    			ArrayList<BulletView> friendlysBullets = friendlyShooter.getMyBullets();
			    			for(int j=friendlysBullets.size()-1;j>=0;j--){
			    				boolean stopCheckingIfFriendlysBulletsHitEnemy=false;
			    				BulletView bullet = friendlysBullets.get(j);
			    				
			    				if(bullet.collisionDetection(enemy)){//bullet collided with rocket
			    					//enemy is damaged
			            			enemyDies = enemy.takeDamage(bullet.getDamage());
			            			if(enemyDies){
			            				LevelSystem.incrementScore(enemy.getScoreForKilling());
			            			}
			            			
			            			bullet.removeGameObject();
			            			/*
			            			 * only one bullet can hit a specific enemy at once. 
			            			 * If that enemy were to die, then checking to see if the other bullets hit 
			            			 * him wastes resources and may cause issues.
			            			 */
			            			stopCheckingIfFriendlysBulletsHitEnemy=true;
			            		}
			    				if(stopCheckingIfFriendlysBulletsHitEnemy){
			            			break;
			    				}
			    			}
		        		}
		        	}
		        	//enemies loop is over
		
		
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
	        	}
				/*			DO OTHER STUFF 		*/
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
