package bonuses;

import interfaces.MovingObject;
import interfaces.Shooter;
import levels.LevelSystem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.jtronlabs.to_the_moon.R;

public class Bonus_ScoreView extends BonusView implements MovingObject{
	
	public Bonus_ScoreView(Context context,float positionX,float positionY) {
		super(context,positionX,positionY);	
		
		//set image src size
		Drawable dr = this.getResources().getDrawable(R.drawable.resources);
		Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
		int newLength = this.getResources().getDimensionPixelOffset(R.dimen.bonus_img_len);
		Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, newLength, newLength, true));
		
		this.setImageDrawable(d);
	}
	
	public void applyBenefit(Shooter theBenefitter){
		LevelSystem.incrementScore(500);
	}
}
