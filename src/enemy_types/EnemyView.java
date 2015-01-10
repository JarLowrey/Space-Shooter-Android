package enemy_types;

import parents.Projectile_GravityView;
import android.content.Context;
import android.view.ViewGroup;
import bonuses.BonusView;

import com.jtronlabs.to_the_moon.GameActivity;

public class EnemyView extends Projectile_GravityView{
	
	//TODO Enemies that dodge (move away) from nearby friendly's bullets
	
	private int score;
	private double probSpawnBeneficialObject;
	
	public EnemyView(Context context,int scoreForKilling,double projectileSpeedY,
			double projectileSpeedX, 
			double projectileDamage,double projectileHealth,double probSpawnBeneficialObjectUponDeath) {
		super( context, projectileSpeedY, projectileSpeedX, 
				 projectileDamage, projectileHealth);
		
		score=scoreForKilling;
		probSpawnBeneficialObject= probSpawnBeneficialObjectUponDeath;
		GameActivity.enemies.add(this);
	}
	
	@Override
	public void removeGameObject(){
		if(!this.isRemoved()){
			if(Math.random()<probSpawnBeneficialObject){
				final float xAvg = (2 * this.getX()+this.getWidth())/2;
				final float yAvg = (2 * this.getY()+this.getHeight())/2;
				BonusView bene = BonusView.getRandomBonusView(this.getContext(),xAvg,yAvg);
				ViewGroup parent = (ViewGroup)this.getParent();
				if(parent!=null){parent.addView(bene,1);}
			}
		}
//		if(this.myGun!=null && this.myGun.myBullets.size()==0){
//			GameActivity.enemies.remove(this);			
//		}else if(this.myGun==null){
//			GameActivity.enemies.remove(this);			
//		}
		super.removeGameObject();
	}
	

	public void setProbSpawnBeneficialObjectOnDeath(double prob){
		probSpawnBeneficialObject=prob;
	}
	public double getProbSpawnBeneficialObjectOnDeath(){
		return probSpawnBeneficialObject;
	}
	public int getScoreForKilling(){
		return score;
	}
	public void setScoreValue(int newScore){
		score=newScore;
	}
}
