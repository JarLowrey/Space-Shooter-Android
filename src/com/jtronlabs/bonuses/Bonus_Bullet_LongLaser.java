package com.jtronlabs.bonuses;

import android.content.Context;

import com.jtronlabs.to_the_moon.R;
import com.jtronlabs.to_the_moon.bullets.Bullet_LaserLong;
import com.jtronlabs.to_the_moon_interfaces.GameObjectInterface;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Bonus_Bullet_LongLaser extends BonusView implements GameObjectInterface{
	
	public static double FREQ_DECREASE_RATIO=1.1;
	
	public Bonus_Bullet_LongLaser(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
//		//scale xml drawable to appropriate size and set as image drawable
//		int width=(int)context.getResources().getDimension(R.dimen.bonus_laser_long_width);
//		int height=(int)context.getResources().getDimension(R.dimen.bonus_laser_long_width);
//
//		Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser1_friendly);
//		Bitmap b = Bitmap.createScaledBitmap(original, width, height, false);
//		Drawable drawable = new BitmapDrawable(context.getResources(), b);
//		
//		
//		
//		Resources res = this.getResources();
//		Drawable drawable;
//		try {
//			drawable = Drawable.createFromXml(res, res.getXml(R.drawable.laser1_friendly));
//		} catch (NotFoundException | XmlPullParserException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//				new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 
//				(int)context.getResources().getDimension(R.dimen.bonus_laser_long_width), 
//				(int)context.getResources().getDimension(R.dimen.bonus_laser_long_height), true));
		
		//I gave up on giving an xml drawable a width and height
		this.setImageResource(R.drawable.laser_long_img);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		theBenefitter.setBulletType(new Bullet_LaserLong());
	}
}
