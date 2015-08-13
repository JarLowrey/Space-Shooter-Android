package helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ScrollView;

import com.jtronlabs.space_shooter.MainActivity;
import com.jtronlabs.space_shooter.R;

public class GameAlertDialogBuilder extends AlertDialog.Builder{
	
	private final int PADDING = (int) (10 * MainActivity.getScreenDens());
	
	public GameAlertDialogBuilder(Context ctx) {
		super(ctx);
	} 
	
	@Override
	public AlertDialog.Builder setTitle(CharSequence title){
		GameTextView titleView = new GameTextView(getContext());
		titleView.setPadding(PADDING, PADDING, PADDING, PADDING);
		titleView.setTextSize(30);//30dp
	    
		titleView.setText(title);
		this.setCustomTitle(titleView);
		return this;
	}
	
	@Override
	public AlertDialog.Builder setMessage(CharSequence title){				
		ScrollView scrollPane = new ScrollView( getContext());
		scrollPane.setPadding(PADDING, PADDING, PADDING, PADDING);
		 
		GameTextView messageView = new GameTextView(getContext(),getContext().getString(R.string.bit_font_bold));
		messageView.setFont(getContext().getResources().getString(R.string.bit_font_regular));
		messageView.setTextSize(20);//20dp
		messageView.setText(title);
		
		scrollPane.addView(messageView);
		this.setView(scrollPane);
		
		return this;
	}
}
