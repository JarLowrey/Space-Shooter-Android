package helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class GameTextView extends TextView{

	public static final String BIT_FONT_BOLD = "8_bit_operator_plus_8_bold.ttf",
			BIT_FONT_REGULAR = "8_bit_operator_plus_8_regular.ttf",
			SPACE_AGE = "space_age_0.ttf";
	
	public GameTextView(Context context) {
		super(context);
		setFont(BIT_FONT_BOLD);
	}
	public GameTextView(Context context,String fontName) {
		super(context);
		setFont(fontName);
	}
	
	public GameTextView(Context context, AttributeSet attrs){
		super(context,attrs);
		setFont(BIT_FONT_BOLD);
	}
	
	private void setFont(String fontName){
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), fontName);
		setTypeface(font);
	}
}
 