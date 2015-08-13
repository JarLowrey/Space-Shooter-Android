package helpers;

import enemies.EnemyView;
import friendlies.AllyView;
import friendlies.ProtagonistView;
import guns.Gun_AngledDualShot;
import guns.Gun_SingleShotStraight;
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
import bullets.Bullet_Interface;

import com.jtronlabs.space_shooter.GameActivity;
import com.jtronlabs.space_shooter.R;

public class StoreUpgradeHandler {

	public static void confirmUpgradeDialog(final int upgradeLayoutId, final Context ctx, final LevelSystem levelCreator){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);

		final int 	level = levelCreator.getLevel();
		final String message = getUpgradeMessage(ctx,upgradeLayoutId);
		final int cost = getUpgradeCost(ctx, upgradeLayoutId);
		final int playerScore = gameState.getInt(GameActivity.STATE_RESOURCES, 0);
		
		AlertDialog.Builder confirmStoreChoice = new GameAlertDialogBuilder(ctx)
				    .setTitle( getUpgradeTitle(ctx,upgradeLayoutId) ) 
				    .setMessage( getUpgradeMessage(ctx,upgradeLayoutId) );
		
		
		if(message.equals(ctx.getResources().getString(R.string.upgrade_max_level)) ||
				message.equals( ctx.getResources().getString(R.string.upgrade_repair_message_full) )){
			confirmStoreChoice.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
		     });
		}else{			
			confirmStoreChoice.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
		     })
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
	        		if(cost <= playerScore){
	        			MediaController.playSoundEffect(ctx, MediaController.SOUND_COINS);
		        		StoreUpgradeHandler.applyUpgrade(upgradeLayoutId,ctx,cost,level,playerScore);
		        		
		        		//update Views in the store
		        		int newScore = Math.max(playerScore - cost, 0);//if repairAttempt, then don't decrement the full amount
		        		levelCreator.setResources(newScore);
		        		levelCreator.saveResourceCount();  
		        		((GameActivityInterface)ctx).resetResourcesGameTextView();
		    			((GameActivityInterface)ctx).setHealthBars( );
		    			((GameActivityInterface)ctx).setStoreItemsMessages();
		    			((GameActivityInterface)ctx).setStoreItemsTitles();
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
	

	private static void applyUpgrade(final int whichUpgrade,Context ctx, int cost,int level, int playerScore){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		SharedPreferences.Editor editor = gameState.edit();
		
		switch(whichUpgrade){
		case R.id.buy_inc_bullet_dmg_layout:
			final int gunDmg = gameState.getInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_DAMAGE_LEVEL, gunDmg+1);
			break;
		case R.id.buy_inc_defense_layout:
			final int defense = gameState.getInt(GameActivity.STATE_DEFENSE_LEVEL, 0);
			editor.putInt(GameActivity.STATE_DEFENSE_LEVEL, defense+1);
			editor.putInt(GameActivity.STATE_HEALTH, ProtagonistView.getProtagonistMaxHealth(ctx));
			break;
		case R.id.buy_inc_bullet_freq_layout:
			final int gunFreq = gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
			editor.putInt(GameActivity.STATE_BULLET_FREQ_LEVEL, gunFreq+1);
			break;
		case R.id.buy_new_gun_layout:
			final int gunSet = gameState.getInt(GameActivity.STATE_GUN_CONFIG, -1);
			editor.putInt(GameActivity.STATE_GUN_CONFIG, gunSet+1);
			break;
		case R.id.buy_inc_friend_layout:
			final int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0);
			editor.putInt(GameActivity.STATE_FRIEND_LEVEL, friendLvl+1);
			break;
		case R.id.buy_inc_score_weight_layout:
			final int resourceLvl = gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0);
			editor.putInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, resourceLvl+1);
			break;
		case R.id.buy_repair_layout:
			cost = Math.min(playerScore, cost); //allow user to repair less than the full health bar
			final int amtToHeal = (int)( ( ( (double)cost ) / getRepairCost(ctx,level) ) * 
					( ProtagonistView.getProtagonistMaxHealth(ctx) - 
							ProtagonistView.getProtagonistCurrentHealth(ctx) )
							);
			final int finalHealth = ProtagonistView.getProtagonistCurrentHealth(ctx) + amtToHeal;
			editor.putInt(GameActivity.STATE_HEALTH, finalHealth);
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
	public static int getdefenseLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_DEFENSE_LEVEL, 0);
	}
	public static int getBulletBulletFreqLevel(Context ctx){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);
		return gameState.getInt(GameActivity.STATE_BULLET_FREQ_LEVEL, 0);
	}
	
	private static int getRepairCost(Context ctx,int level){
		int cost; 
		
		final double proportionHealthLeft = ((double)(ProtagonistView.getProtagonistMaxHealth(ctx) - 
				ProtagonistView.getProtagonistCurrentHealth(ctx) ) ) / ProtagonistView.getProtagonistMaxHealth(ctx);
		cost = 	(int) ( proportionHealthLeft * 
				ctx.getResources().getInteger(R.integer.heal_base_cost) * level) ;
		cost = Math.min(cost, ctx.getResources().getInteger(R.integer.heal_max_cost));
		
		return cost;
	}
	
	public static String getUpgradeTitle(Context ctx, int upgradeLayoutId){
		String title = "";
		
		switch(upgradeLayoutId){
		case R.id.buy_inc_bullet_dmg_layout:
			title = ctx.getResources().getString(R.string.upgrade_inc_bullet_dmg_title);
			break;
		case R.id.buy_repair_layout:
			title = ctx.getResources().getString(R.string.upgrade_repair_title);
			break;
		case R.id.buy_inc_bullet_freq_layout:
			title = ctx.getResources().getString(R.string.upgrade_inc_bullet_freq_title);
			break;
		case R.id.buy_inc_defense_layout:
			title = ctx.getResources().getString(R.string.upgrade_inc_defense_title);
			break;
		case R.id.buy_inc_score_weight_layout:
			title = ctx.getResources().getString(R.string.upgrade_inc_score_weight_title);
			break;
		case R.id.buy_new_gun_layout:
			title = ctx.getResources().getString(R.string.upgrade_new_gun_title);
			break;
		case R.id.buy_inc_friend_layout:
			title = ctx.getResources().getString(R.string.upgrade_inc_friend_title);
			break;
		}
		
		return title;
	}
	
	public static String getUpgradeMessage(Context ctx, int upgradeLayoutId){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);

		String message = "";
		int playerScore = gameState.getInt(GameActivity.STATE_RESOURCES, 0),
				level = gameState.getInt(GameActivity.STATE_LEVEL, 0),
				cost = getUpgradeCost(ctx,upgradeLayoutId);
		boolean maxLevelItem = false,
				usePlayerScore = false,
				addCostToMessage = true;

		switch(upgradeLayoutId){
		case R.id.buy_inc_bullet_dmg_layout:
			message = ctx.getResources().getString(R.string.upgrade_bullet_damage);
			
			final int damageScalingFactor = ProtagonistView.getBulletDamage(ctx) / ProtagonistView.DEFAULT_BULLET_DAMAGE;
			maxLevelItem = damageScalingFactor == EnemyView.MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR;
			break;
		case R.id.buy_repair_layout:
			if(ProtagonistView.getProtagonistCurrentHealth(ctx) == ProtagonistView.getProtagonistMaxHealth(ctx)){
				message=ctx.getResources().getString(R.string.upgrade_repair_message_full);
				addCostToMessage = false;
			}else{				
				if (playerScore < cost && playerScore != 0)	{ 
					message = ctx.getResources().getString(R.string.upgrade_repair_message_partial); 
					usePlayerScore = true;
				} else if(playerScore == 0){
					message = ctx.getResources().getString(R.string.upgrade_repair_message_no_score); 
				}else{
					message = ctx.getResources().getString(R.string.upgrade_repair_message_default); 
				}
			}
			break;
		case R.id.buy_inc_bullet_freq_layout:
			message = ctx.getResources().getString(R.string.upgrade_bullet_frequency);
			maxLevelItem = ProtagonistView.getShootingDelay(ctx) == ProtagonistView.MIN_SHOOTING_FREQ;
			break;
		case R.id.buy_inc_defense_layout:
			message = ctx.getResources().getString(R.string.upgrade_defense);
			break;
		case R.id.buy_inc_score_weight_layout:
			message = ctx.getResources().getString(R.string.upgrade_score_multiplier);
			break;
		case R.id.buy_new_gun_layout:
			final int gunlvl = getGunLevel(ctx);
			if(gunlvl < maxGunLevel(ctx)-1 ){
				message = ctx.getResources().getStringArray(R.array.gun_descriptions)[gunlvl+1];
			}else{
				maxLevelItem = true;				
			}
			break;
		case R.id.buy_inc_friend_layout:
			int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0 );
			if(friendLvl < AllyView.MAX_ALLY_LEVEL){
				cost = ctx.getResources().getInteger(R.integer.friend_base_cost) ;
				if(friendLvl < 1){
					message = ctx.getResources().getString(R.string.upgrade_buy_friend);					
				}else{
					message = ctx.getResources().getString(R.string.upgrade_friend_level);					
				}
			}else{
				maxLevelItem=true;
			}
			break;
		}
		
		if(maxLevelItem){
			message = ctx.getResources().getString(R.string.upgrade_max_level);
		}
		
		if(usePlayerScore){
			message+="\n\n"+NumberFormat.getNumberInstance(Locale.US).format(playerScore);
		}else if(addCostToMessage){
			message+="\n\n"+NumberFormat.getNumberInstance(Locale.US).format(cost);
		}
		
		return message;
	}
	
	public static int getUpgradeCost(Context ctx,int upgradeLayoutId){
		SharedPreferences gameState = ctx.getSharedPreferences(GameActivity.GAME_STATE_PREFS, 0);

		int cost=0,
			level = gameState.getInt(GameActivity.STATE_LEVEL, 0),
			playerScore = gameState.getInt(GameActivity.STATE_RESOURCES, 0);
	
		switch(upgradeLayoutId){
		case R.id.buy_inc_bullet_dmg_layout:
			cost = 	(int) (ctx.getResources().getInteger(R.integer.inc_bullet_damage_base_cost) 
					* Math.pow((getBulletDamageLevel(ctx)+1),2)) ;
			final int damageScalingFactor = ProtagonistView.getBulletDamage(ctx) / ProtagonistView.DEFAULT_BULLET_DAMAGE;
			if( damageScalingFactor == EnemyView.MAXIMUM_ENEMY_HEALTH_SCALING_FACTOR){
				cost = -1;
			}
			break;
		case R.id.buy_inc_defense_layout:
			cost = (int) (ctx.getResources().getInteger(R.integer.inc_defense_base_cost) 
					* Math.pow((getdefenseLevel(ctx)+1),2)) ;
			break;
		case R.id.buy_inc_bullet_freq_layout:
			cost = (int) (ctx.getResources().getInteger(R.integer.inc_bullet_frequency_base_cost) 
					* Math.pow((getBulletBulletFreqLevel(ctx)+1),2)) ;
			if(ProtagonistView.getShootingDelay(ctx) == ProtagonistView.MIN_SHOOTING_FREQ){
				cost = -1;
			}
			break;
		case R.id.buy_new_gun_layout:
			final int gunlvl = getGunLevel(ctx);
			if(gunlvl < maxGunLevel(ctx)-1 ){
				cost = ctx.getResources().getIntArray(R.array.gun_upgrade_costs)[gunlvl+1];
			}else{
				cost  = -1;				
			}
			break;
		case R.id.buy_inc_friend_layout:
			int friendLvl = gameState.getInt(GameActivity.STATE_FRIEND_LEVEL, 0 );
			if(friendLvl < AllyView.MAX_ALLY_LEVEL){
				cost = ctx.getResources().getInteger(R.integer.friend_base_cost);
			}else{
				cost = -1;
			}
			break;
		case R.id.buy_inc_score_weight_layout:
			cost = (int) (ctx.getResources().getInteger(R.integer.score_multiplier_base_cost) * 
				Math.pow(5, gameState.getInt(GameActivity.STATE_RESOURCE_MULTIPLIER_LEVEL, 0))) ;
			break;
		case R.id.buy_repair_layout:
			if(ProtagonistView.getProtagonistCurrentHealth(ctx) == ProtagonistView.getProtagonistMaxHealth(ctx)){
				cost = -1;
			}else{
				cost = getRepairCost(ctx,level);
				
				if (playerScore < cost && playerScore != 0)	{ 
					cost = playerScore;
				} else if(playerScore == 0){
					cost = -1;
				}
			}
			break;
		}
		return cost;
	}
}
