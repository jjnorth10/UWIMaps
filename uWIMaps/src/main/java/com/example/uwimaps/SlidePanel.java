package com.example.uwimaps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SlidePanel extends LinearLayout {
	private Paint innerPaint,borderPaint;

	public SlidePanel(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public SlidePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public SlidePanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public void init(){
		 innerPaint = new Paint();
	     innerPaint.setARGB(100, 25, 25, 75); //gray
	     innerPaint.setAntiAlias(true);
	 
	     borderPaint = new Paint();
	     borderPaint.setARGB(255, 255, 255, 255);
	     borderPaint.setAntiAlias(true);
	     borderPaint.setStyle(Style.STROKE);
	     borderPaint.setStrokeWidth(5);
	}
	
	public void setInnerPaint(Paint innerPaint) {
        this.innerPaint = innerPaint;
    }
 
    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }
 
    @Override
    protected void dispatchDraw(Canvas canvas) {
 
        RectF drawRect = new RectF();
        drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
 
        canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
        canvas.drawRoundRect(drawRect, 5, 5, borderPaint);
 
        super.dispatchDraw(canvas);
    }

}
