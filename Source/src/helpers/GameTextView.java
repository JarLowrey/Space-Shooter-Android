package helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class GameTextView extends TextView{

	public static final String BIT_FONT = "8_bit_operator_plus_8_bold.ttf";
	
	public GameTextView(Context context) {
		super(context);
		setFont();
	}
	
	public GameTextView(Context context, AttributeSet attrs){
		super(context,attrs);
		setFont();
	}
	
	private void setFont(){
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), BIT_FONT);
		setTypeface(font);
	}
}
 