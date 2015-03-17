package bonuses;

import interfaces.MovingViewInterface;
import interfaces.Shooter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import interfaces.GameActivityInterface;

import com.jtronlabs.to_the_moon.R;

public class Bonus_ScoreView extends BonusView implements MovingViewInterface{
	
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
		((GameActivityInterface) this.getContext()).incrementScore(1000);
	}
}
