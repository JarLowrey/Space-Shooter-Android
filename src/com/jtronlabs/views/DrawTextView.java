package com.jtronlabs.views;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jtronlabs.new_proj.R;
import com.jtronlabs.new_proj.R.color;
import com.jtronlabs.new_proj.R.dimen;
import com.jtronlabs.new_proj.R.raw;

public class DrawTextView extends SurfaceView implements SurfaceHolder.Callback
{
	Paint strokePaint = new Paint();
	Paint textPaint = new Paint();

	private boolean surfaceCreated=false;
	
//	private String txt;
	private float wordSize,outlineSize;
	
	private Typeface myFont;
	private int wordColor,outlineColor;
	private String prevWord=null;
	
	public DrawTextView(Context context,AttributeSet attr) {
		super(context,attr);
		
		myFont= getFontFromRes(context,R.raw.roboto_condensed_bold);
		wordSize=context.getResources().getDimension(R.dimen.title_size);
		outlineSize=context.getResources().getDimension(R.dimen.title_outline_size);
		wordColor=context.getResources().getColor(R.color.light_blue);
		outlineColor=context.getResources().getColor(R.color.dark_yellow);
	    
		setZOrderOnTop(true);
		getHolder().addCallback(this);
		getHolder().setFormat(PixelFormat.TRANSPARENT);
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
 
	@Override
	public void surfaceCreated(SurfaceHolder hold) {
		surfaceCreated=true;
		if(prevWord!=null){
			drawText(prevWord);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawText(String word){
		if(surfaceCreated){
			Canvas canvas = getHolder().lockCanvas();
			
			strokePaint.setColor(outlineColor);
			strokePaint.setTextAlign(Paint.Align.CENTER);
			strokePaint.setTextSize(wordSize);
			strokePaint.setTypeface(myFont);
			strokePaint.setStyle(Paint.Style.STROKE);
			strokePaint.setStrokeWidth(outlineSize);
			
			textPaint.setColor(Color.WHITE);
			textPaint.setTextAlign(Paint.Align.CENTER);
			textPaint.setTextSize(wordSize);
			
			textPaint.setTypeface(myFont);
			canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
			
			float wid=getWidth()/2;
			float strokeHeight=(getHeight()-strokePaint.ascent()-strokePaint.descent())/2;
			float txtHeight=(getHeight()-textPaint.ascent()-textPaint.descent())/2;
			canvas.drawText(word,wid,strokeHeight,strokePaint);
			canvas.drawText(word,wid,txtHeight,textPaint);
			
			getHolder().unlockCanvasAndPost(canvas);
		}
		prevWord=word;
	}
	
//	@Override
//	  public boolean onSetAlpha(int alpha) {
//		strokePaint.setAlpha(alpha);
//		textPaint.setAlpha(alpha);
//	    return true;
//	  }
	
	//http://stackoverflow.com/questions/7610355/font-in-android-library
	private Typeface getFontFromRes(Context ctx,int resource)
	{ 
	    Typeface tf = null;
	    InputStream is = null;
	    try {
	        is = getResources().openRawResource(resource);
	    }
	    catch(NotFoundException e) {
	        Log.e("font", "Could not find font in resources!");
	    }

	    String outPath = ctx.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

	    try
	    {
	        byte[] buffer = new byte[is.available()];
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

	        int l = 0;
	        while((l = is.read(buffer)) > 0)
	            bos.write(buffer, 0, l);

	        bos.close();

	        tf = Typeface.createFromFile(outPath);

	        // clean up
	        new File(outPath).delete();
	    }
	    catch (IOException e)
	    {
	        Log.e("font", "Error reading in font!");
	        return null;
	    }

	    Log.d("font", "Successfully loaded font.");

	    return tf;      
	}

}
