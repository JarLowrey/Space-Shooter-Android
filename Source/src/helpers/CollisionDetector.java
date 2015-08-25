package helpers;

import interfaces.Shooter;
import android.view.View;
import bonuses.BonusView;
import bullets.BulletView;
import bullets.Bullet_HasDurationView;

import com.jtronlabs.space_shooter.GameLoop;

import enemies.EnemyView;
import friendlies.FriendlyView;

public class CollisionDetector {

    public static void detectCollisions(){
		detectAnyFriendlyHasCollidedWithAnyEnemy();
		detectAnyFriendlyHasHitAnyEnemyBullet();
		detectAnyFriendlyHasHitAnyBonus();
		detectAnyEnemyHasHitAnyFriendlyBullets();
    }
	
	//it is possible for enemy to be removed after beginning a for(enemies) loop, but before calling enemies.get(i)
	//in that case, just catch the error and don't worry about it, it was already processed
    
    private static void detectAnyFriendlyHasCollidedWithAnyEnemy(){		
    	for(int k=GameLoop.friendlies.size()-1;k>=0;k--){
    		FriendlyView friendly = GameLoop.friendlies.get(k);
    		
	    	for(int i=GameLoop.enemies.size()-1;i>=0;i--){
//	    		try{
		    		EnemyView enemy = GameLoop.enemies.get(i);
		    		if(RectToRectCollisionDetection(friendly,enemy)){
		    			
		    			friendly.takeDamage(enemy.getDamage());
		    			enemy.takeDamage(friendly.getDamage());
		    		}
//	    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
	    	}
    	}
    }
    
    private static void detectAnyFriendlyHasHitAnyEnemyBullet(){
    	for(int k=GameLoop.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = GameLoop.friendlies.get(k);
			
			for(int j=GameLoop.enemyBullets.size()-1;j>=0;j--){
//				try{
					BulletView bullet = GameLoop.enemyBullets.get(j);
					if( RectToRectCollisionDetection(friendly,bullet)){
	
		    			friendly.takeDamage(bullet.getDamage());
		    			if (! (bullet instanceof Bullet_HasDurationView) ){bullet.removeGameObject();}
		    		}
//	    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
			}
    	}
    }

    private static void detectAnyFriendlyHasHitAnyBonus(){
    	for(int k=GameLoop.friendlies.size()-1;k>=0;k--){
			FriendlyView friendly = GameLoop.friendlies.get(k);
			
			//check if friendly has hit a bonus
			if(friendly instanceof Shooter){
		    	for(int i=GameLoop.bonuses.size()-1;i>=0;i--){
//		    		try{
			    		BonusView bonus = GameLoop.bonuses.get(i);
			    		if( /*! bonus.isRemoved() &&*/ RectToRectCollisionDetection(friendly,bonus)){//game object could be removed in previous loop iteration
			    			bonus.applyBenefit();
			    			bonus.removeGameObject();
			    		}
//		    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
		    	}
			}
    	}
    }
    
    private static void detectAnyEnemyHasHitAnyFriendlyBullets(){
    	for(int i=GameLoop.enemies.size()-1;i>=0;i--){
    		EnemyView enemy = GameLoop.enemies.get(i);
    		
	    		for(int j=GameLoop.friendlyBullets.size()-1;j>=0;j--){
//	    			try{
						BulletView bullet = GameLoop.friendlyBullets.get(j);
						if( RectToRectCollisionDetection(bullet,enemy)){
							
		        			enemy.takeDamage(bullet.getDamage());
		        			if (! (bullet instanceof Bullet_HasDurationView) ){bullet.removeGameObject();}
		        		}
//		    		}catch(Exception e){Log.d("lowrey",e.getMessage());}
	        	}
    	}
    }
    

	private static boolean RectToRectCollisionDetection(View one,View two){
		float left1,right1,top1,bottom1;
		float left2,right2,top2,bottom2;
		
		//find the values of the x,y positions of the two views
		left1=one.getX();
		right1=one.getX()+one.getWidth();
		top1=one.getY();
		bottom1=one.getY()+one.getHeight();

		left2=two.getX();
		right2=two.getX()+two.getWidth();
		top2=two.getY();
		bottom2=two.getY()+two.getHeight();
		
		//Simple collision detection - determine if the two rectangular areas intersect
		//http://devmag.org.za/2009/04/13/basic-collision-detection-in-2d-part-1/
		return !((bottom1 < top2) ||(top1 > bottom2) || (left1>right2) || (right1<left2));
	}
 
}
