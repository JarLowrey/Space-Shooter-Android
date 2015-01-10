package com.jtronlabs.to_the_moon.guns;
  
import android.content.Context;

import com.jtronlabs.to_the_moon.bullets.Bullet;
import com.jtronlabs.to_the_moon.bullets.BulletView;
import com.jtronlabs.to_the_moon_interfaces.Shooter;

public class Gun_StraightSingleShot extends Gun {
	
	public Gun_StraightSingleShot(Context context,Shooter theShooter,Bullet bulletType) {
		super(context,theShooter,bulletType);
	}
	public boolean shoot(){
		//create one bullet at center of shooter
		BulletView bulletMid = myBulletType.getBullet(ctx, shooter);
		
		//add bullets to layout
		shooter.getMyScreen().addView(bulletMid,1);
		
		return false;
	}
	
}
