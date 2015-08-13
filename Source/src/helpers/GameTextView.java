package helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jtronlabs.space_shooter.R;

public class GameTextView extends TextView{
	
	public GameTextView(Context context) {
		super(context);
		setFont( context.getString(R.string.bit_font_bold) );
	}
	public GameTextView(Context context,String fontName) {
		super(context);
		setFont(fontName);
	}
	
	public GameTextView(Context context, AttributeSet attrs){
		super(context,attrs);
		String font = context.getString(R.string.bit_font_bold);
		
		TypedArray typedArr = context.obtainStyledAttributes(attrs,R.styleable.helpers_GameTextView);
		for(int i=0;i<typedArr.getIndexCount();i++){
			int attr = typedArr.getIndex(i);
			switch(attr){
			case R.styleable.helpers_GameTextView_custom_font:
				font = typedArr.getString(attr);
				break;
			}
		}
		
		setFont(font);
	}
	
	public void setFont(String fontName){
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), fontName);
		setTypeface(font);
	}
}
 