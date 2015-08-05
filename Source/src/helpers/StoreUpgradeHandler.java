package helpers;

import interfaces.GameActivityInterface;

import java.text.NumberFormat;
import java.util.Locale;

import levels.LevelSystem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.RelativeLayout;
import android.widget.Toast;
import bullets.Bullet_Basic;
import bullets.Bullet_Duration;
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.R;

import enemies.EnemyView;
import friendlies.AllyView;
import friendlies.ProtagonistView;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;

public class StoreUpgradeHandler {

	public static final int UPGRADE_BULLET_DAMAGE=0,UPGRADE_DEFENCE=1,UPGRADE_BULLET_FREQ=3,
			UPGRADE_GUN=4,UPGRADE_FRIEND=5,UPGRADE_SCORE_MULTIPLIER=6,UPGRADE_HEAL=7;

	public static void confirmUpgradeDialog(final int whichUpgrade, final Context ctx, final LevelSystem levelCreator){
		String msg="",title="";
		int cost=0;
		boolean maxLevelItem=false;
		final String maxMsg = "Maximum upgrade attained";

		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
	
		switch(whichUpgrade){
		case UPGRADE_BULLET_DAMAGE:
			title = "Damage";
			cost = 	(int) (ctx.getResources().getInteger(R.integer.inc_bullet_damage_base_cost) 
					* Math.pow((getBulletDamageLevel(ctx)+1),2)) ;
			msg = ctx.getResources().getString(R.string.upgrade_bullet_damage);
			final int damageScalingFactor = ProtagonistView.getBulletDamage(ctx) / ProtagonistView.DEFAULT_BULLET_DAMAGE;
			if( damageScalingFactor == EnemyView.MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR){
				msg = maxMsg;
				maxLevelItem = true;
			}
			break;
		case UPGRADE_DEFENCE:
			title = "Defence";
			cost = (int) (ctx.getResources().getInteger(R.integer.inc_defence_base_cost) 
					* Math.pow((getDefenceLevel(ctx)+1),2)) ;
			msg=ctx.getResources().getString(R.string.upgrade_defence);
			break;
		case UPGRADE_BULLET_FREQ:
			title = "Fire Rate";
			cost = (int) (ctx.getResources().getInteger(R.integer.inc_bullet_frequency_base_cost) 
					* Math.pow((getBulletBulletFreqLevel(ctx)+1),2)) ;
			msg=ctx.getResources().getString(R.string.upgrade_bullet_frequency);
			if(ProtagonistView.getShootingDelay(ctx) == ProtagonistView.MIN_SHOOTING_FREQ){
				msg = maxMsg;
				maxLevelItem = true;
			}
			break;
		case UPGRADE_GUN:
			title = "Ship Blasters";
			final int gunlvl = getGunLevel(ctx);
			if(gunlvl < maxGunLevel(ctx)-1 ){
				cost = ctx.getResources().getIntArray(R.array.gun_upgrade_costs)[gunlvl+1];
				msg = ctx.getResources().getStringArray(R.array.gun_descriptions)[gunlvl+1];
			}else{
				msg = maxMsg;
				maxLevelItem = true;				
			}
			break;
		case UPGRADE_FRIEND:
			title = "Ally";
			int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0 );
			if(friendLvl < AllyView.MAX_ALLY_LEVEL){
				cost = ctx.getResources().getInteger(R.integer.friend_base_cost) ;
				if(friendLvl < 1){
					msg=ctx.getResources().getString(R.string.upgrade_buy_friend);					
				}else{
					msg=ctx.getResources().getString(R.string.upgrade_friend_level);					
				}
			}else{
				maxLevelItem=true;
				msg = maxMsg;
			}
			break;
		case UPGRADE_SCORE_MULTIPLIER:
			title = "Resources";
			cost = (int) (ctx.getResources().getInteger(R.integer.score_multiplier_base_cost) * 
				Math.pow(5, gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0))) ;
			msg=ctx.getResources().getString(R.string.upgrade_score_multiplier);
			break;
		case UPGRADE_HEAL:
			title = "Repair";
			if(ProtagonistView.getProtagonistCurrentHealth(ctx) == ProtagonistView.getProtagonistMaxHealth(ctx)){
				maxLevelItem=true;
				msg="Ship fully repaired";
			}else{
				final double proportionHealthLeft = ((double)(ProtagonistView.getProtagonistMaxHealth(ctx) - 
						ProtagonistView.getProtagonistCurrentHealth(ctx) ) ) / ProtagonistView.getProtagonistMaxHealth(ctx);
				cost = 	(int) ( proportionHealthLeft * 
						ctx.getResources().getInteger(R.integer.heal_base_cost) * (levelCreator.getLevel())) ;
				cost = Math.min(cost, ctx.getResources().getInteger(R.integer.heal_max_cost));
				msg = ctx.getResources().getString(R.string.upgrade_heal);					
			}
			break;
		}
		if(!maxLevelItem){
			msg+="\n\n"+NumberFormat.getNumberInstance(Locale.US).format(cost);//add cost formatted with commas
		}
		
		AlertDialog.Builder confirmStoreChoice = new AlertDialog.Builder(ctx)
				    .setTitle( title ) 
				    .setMessage( msg );
		
		if(maxLevelItem){
			confirmStoreChoice.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
		     });
		}else{
			final int costCopy=cost;
			
			confirmStoreChoice.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
		     })
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
	        		if(costCopy<=levelCreator.getResourceCount()){
	        			MediaController.playSoundEffect(ctx, MediaController.SOUND_COINS);
		        		StoreUpgradeHandler.applyUpgrade(whichUpgrade,ctx);
		        		
		        		//update Views in the store
		        		levelCreator.setResources(levelCreator.getResourceCount()-costCopy);
		        		levelCreator.saveResourceCount();  
		        		((GameActivityInterface)ctx).resetResourcesTextView();
		    			((GameActivityInterface)ctx).setHealthBars( );
		    			Toast.makeText(ctx.getApplicationContext(),"Purchased!", Toast.LENGTH_SHORT).show();       			
	        		}else{
	        			Toast.makeText(ctx.getApplicationContext(),"Not enough resources", Toast.LENGTH_SHORT).show();
	        		}
	        		dialog.cancel();
		        }
		     });
			
		}
	    confirmStoreChoice.show();
	}
	

	public static void applyUpgrade(final int whichUpgrade,Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		switch(whichUpgrade){
		case UPGRADE_BULLET_DAMAGE:
			final int gunDmg = gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, gunDmg+1);
			break;
		case UPGRADE_DEFENCE:
			final int defence = gameState.getInt(GameActivity.STATE_DEFENCE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_DEFENCE_LEVEL, defence+1);
			editor.putInt(GameActivity.STATE_HEALTH, ProtagonistView.getProtagonistMaxHealth(ctx));
			break;
		case UPGRADE_BULLET_FREQ:
			final int gunFreq = gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_FREQ_LEVEL, gunFreq+1);
			break;
		case UPGRADE_GUN:
			final int gunSet = gameState.getInt(GameActivity.STATE_GUN_CONFIG, -1);
			editor.putInt(GameActivity.STATE_GUN_CONFIG, gunSet+1);
			break;
		case UPGRADE_FRIEND:
			final int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0);
			editor.putInt(GameActivity.STATE_FRIEND_LEVEL, friendLvl+1);
			break;
		case UPGRADE_SCORE_MULTIPLIER:
			final int resourceLvl = gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
			editor.putInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, resourceLvl+1);
			break;
		case UPGRADE_HEAL:
			editor.putInt(GameActivity.STATE_HEALTH, ProtagonistView.getProtagonistMaxHealth(ctx));
			break;
		}
		
		editor.commit();
	}
	

	/**
	 * define the different levels of guns protagonist may have and their damage, frequency, speed, etc
	 */
	
	public static void createProtagonistGunSet(ProtagonistView protag){
		protag.removeAllGuns();

		final int dmg = ProtagonistView.getBulletDamage(protag.getContext());
		final float freq = ProtagonistView.getShootingDelay(protag.getContext());
		final float bulletSpeed = Bullet_Interface.DEFAULT_BULLET_SPEED_Y * ProtagonistView.BULLET_SPEED_MULTIPLIER;
		
		RelativeLayout layout = (RelativeLayout) ( protag.getParent());
		
		switch(StoreUpgradeHandler.getGunLevel(protag.getContext())){
		case -1: //only appears on first level, combined strength of bullets = 0.8x bullet
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int)(dmg*0.8),
					50) );
			break;
		case 0: //combined strength of bullets = 1x bullet
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					dmg,
					50) );
			break;
		case 1: //combined strength of bullets = 1.1x bullet
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.55),
					20) );
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_small_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.55),
					80) );
			break;
		case 2: //combined strength of bullets = 1.2x bullet
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.4),
					20) );
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.4),
					50) );
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.4),
					80) );
			break;
		case 3: //combined strength of bullets = 1.3x bullet
			protag.addGun(new Gun_AngledDualShot(layout,protag,new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.433333),
					50));
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.433333),
					50) );
			break;
		case 4: //combined strength of bullets = 1.4x bullet
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.2),
					20) );
			protag.addGun(new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(		//twice as powerful, half as fast
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_mid_fat_width), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
					R.drawable.bullet_laser_round_green),
					(float) (freq * 2),
					bulletSpeed,
					(int) (dmg * 2),
					50) );
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.2),
					80) );
			break;
		case 5: //combined strength of bullets = 1.5x bullet
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic( //3x as powerful, half as fast = 1.5x combined
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					R.drawable.bullet_laser_round_green),
					freq * 2,
					bulletSpeed,
					dmg * 3,
					50) );
			break;
		case 6: //combined strength of bullets = 1.6x bullet
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.3),
					20) );
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic( //2x as powerful, half as fast = 1x combined
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					R.drawable.bullet_laser_round_green),
					freq * 2,
					bulletSpeed,
					dmg * 2,
					50) );
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.3),
					80) );
			break;
		case 7://combined strength of bullets = 1.6x bullet
			protag.addGun(new Gun_AngledDualShot(layout,protag,new Bullet_Basic(
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
					R.drawable.bullet_laser_round_green),
					freq,
					bulletSpeed,
					(int) (dmg*.3),
					50));
			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic( //2x as powerful, half as fast = 1x combined
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_large_length), 
					R.drawable.bullet_laser_round_green),
					freq * 2,
					bulletSpeed,
					dmg * 2,
					50) );
			break;
//		case 8:
//			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
//					R.drawable.bullet_laser_round_green),
//					freq,
//					bulletSpeed,
//					(int) (dmg*.3),
//					20) );
//			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Duration(		//TODO get duration bullet working for friendlies
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_xskinny_width), 
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_rec_long_height), 
//					R.drawable.bullet_laser_round_red,
//					Bullet_Duration.DEFAULT_BULLET_DURATION,
//					50),
//				5000, 
//				Bullet_Interface.DEFAULT_BULLET_SPEED_Y, 
//				dmg / 70,
//				50) );
//			protag.addGun( new Gun_SingleShotStraight(layout, protag, new Bullet_Basic(
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
//					(int)protag.getContext().getResources().getDimension(R.dimen.bullet_round_xsmall_length), 
//					R.drawable.bullet_laser_round_green),
//					freq,
//					bulletSpeed,
//					(int) (dmg*.3),
//					80) );
//			break;
		}
	}

	//Helper/convenience methods	
	public static int getGunLevel(Context ctx){ 
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_GUN_CONFIG,-1);
	}
	public static int maxGunLevel(Context ctx){
		return Math.min( ctx.getResources().getStringArray(R.array.gun_descriptions).length, //these arrays should be the same length, but just in case here's a check
				ctx.getResources().getIntArray(R.array.gun_upgrade_costs).length );
	}
	public static int getBulletDamageLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
	}
	public static int getDefenceLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_DEFENCE_LEVEL, 0);
	}
	public static int getBulletBulletFreqLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
	}
	
}
