package com.kernel.logparser;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//@SuppressLint("DrawAllocation")
public class CompleteTouchActivity extends View {
  private Paint paint = new Paint();
  private Path path = new Path();
  private int crash_protector=0;
  
 /* Canvas tempcanvas=new Canvas();
  Bitmap tempBmp = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
  */
  public CompleteTouchActivity(Context context, AttributeSet attrs) {
    super(context, attrs);
   
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2f);
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
    
		crash_protector++;
     	if(crash_protector>1)return true;				//To protect application from crashing
     	else
     		displayLogEvent();     	
     	
     	return true;
  }
  
  private boolean displayLogEvent(){
     	
	  	float eventX;
    	float eventY;
    	float lcdTouch=1;	
    	// parser.displayTouchList();	
    	if(null != MainActivity.ratio && (MainActivity.ratio.toString().length())>=1)
	    	lcdTouch=Float.valueOf(MainActivity.ratio.toString());
    
        //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
        for(TouchObject t:parser.parsedData){        
	       // TouchObject.display(t);
	        eventX = t.getX();
	        eventY = t.getY();
	        
	    	if(lcdTouch!=1){
				eventX/= lcdTouch;
				eventY/= lcdTouch;
			}
	    	
	        if (t.isDir()){
	        	//System.out.println("Enter in pressed if condition");         DEBUG log
	        	path.addCircle(eventX, eventY,(float) 3,Path.Direction.CW);
	        }
	        else{
	        	path.addCircle(eventX, eventY,(float) 1,Path.Direction.CW);
	        }
    	
    	}
    	// Schedules a repaint.
    	invalidate();
    	return true;
  }
}