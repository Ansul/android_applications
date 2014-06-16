package com.example.logparser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CompleteTouchActivity extends View {
  private Paint paint = new Paint();
  private Path path = new Path();
  private int crash_protector=0;
  
  public CompleteTouchActivity(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    paint.setAntiAlias(true);
    paint.setStrokeWidth(6f);
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawPath(path, paint);
  }

 
  @Override
  public boolean onTouchEvent(MotionEvent event){
    
		float eventX;
    	float eventY;
    	
    	crash_protector++;
     	if(crash_protector>=2)return true;				//To protect application from crashing
     	
        //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
        for(TouchObject t:parser.parsedData){
	       // TouchObject.display(t);
	        eventX = (float)t.getX();
	        eventY = (float)t.getY();
	        if (t.isDir()){
	        	//System.out.println("Enter in pressed if condition");         DEBUG log
	        	path.addCircle(eventX, eventY,(float) 1,Path.Direction.CW);
	        }
	        else{
	        	path.addCircle(eventX, eventY,(float) 0.5,Path.Direction.CW);
	        }
    	
    	}
    	// Schedules a repaint.
    	invalidate();
    	return true;
  }
}