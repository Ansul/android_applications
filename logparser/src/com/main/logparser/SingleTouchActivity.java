package com.main.logparser;

import java.util.ArrayList;
import java.util.List;

import com.kernel.logparser.MainActivity;
import com.kernel.logparser.parser;
import com.main.logparser.TouchObject.TimeStampObject;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SingleTouchActivity extends View {
  private Paint paint = new Paint();
  private Paint navibarpaint=new Paint();
  private Path path=new Path();
  private Context mContext;									//Passing context within class for TOAST notification
  private int mColor;										//Color for the particular path
  									
  private float[] TimeDiff=new float[2];
  
  public static int count=0;									//Storing the number of event displayed
  public static boolean parse_flag; 			
  
  private static TouchObject[][] mPressRelease=new TouchObject[10][2];    /*Storing PRESS & RELEASE events based on 
  																		  index for same finger */
  
  List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>(); //Different color for each path


  
  public SingleTouchActivity(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext=context;
    
    paint.setAntiAlias(true);
    paint.setStrokeWidth(3f);
    //paint.setColor(Color.BLACK);   					Default color BLACK
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    
    navibarpaint.setAntiAlias(true);
    navibarpaint.setStrokeWidth(7f);
	//navibarpaint.setColor(Color.argb(50, 55, 55, 155));   				
    navibarpaint.setColor(Color.argb(128,255,176,0));
    navibarpaint.setStyle(Paint.Style.FILL);
    navibarpaint.setTextSize(paint.getTextSize() * mContext.getResources().getDisplayMetrics().density*2);  
   
  }

  @Override
  protected void onDraw(Canvas canvas) {	 
	  	   
	  for (Pair<Path,Integer> path_clr : path_color_list ){

		   paint.setColor(path_clr.second);
		  canvas.drawPath( path_clr.first, paint);
		  
	  }
	  
	  if(0==TimeDiff[0])
		  canvas.drawText(String.format("%.3f", TimeDiff[1])+ " sec", (float) (getWidth()-(.35*getWidth())), (float) 0.04 * getHeight(), navibarpaint);
	  else
		  canvas.drawText(Integer.toString((int)TimeDiff[0])+ " days", (float) (getWidth()-(.35*getWidth())), (float) 0.04 * getHeight(), navibarpaint);
	  
  }

  
  @Override
  public boolean onTouchEvent(MotionEvent event){
    
	    //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
	  	
	  	float eventX, eventY ,lcdTouch=1;	  	
    	TouchObject currentObject;
  
    	if(count == parser.parsedData.size()){
    		if(MainActivity.gToast!=null){
    		
			MainActivity.gToast.cancel();
		}
		MainActivity.gToast=Toast.makeText(mContext,"Log Finished", Toast.LENGTH_SHORT);
		MainActivity.gToast.show();
    	}
   
    	if(parse_flag && count>0){				//Check the value, if parse_flag set start activity again or continue
  			path_color_list.clear();
  			invalidate();
  			count=0;
    		parse_flag=false;
    	}
    	
    	try{
    		
    	
    		if(event.getAction() == MotionEvent.ACTION_DOWN && count<com.main.logparser.parser.parsedData.size()){
    			
    			//System.out.println("Enter in event.getAction() if condition");  
    			currentObject=com.main.logparser.parser.parsedData.get(count);
    			Log.v(com.main.logparser.parser.TAG,count+". "+ TouchObject.display(currentObject));
    			eventX = currentObject.getX();
    			eventY = currentObject.getY();

    	    	if(null != MainActivity.ratio && (MainActivity.ratio.toString().length())>=1)
    		    	lcdTouch=Float.valueOf(MainActivity.ratio.toString());
    			if(lcdTouch!=1){
    				eventX/= lcdTouch;
    				eventY/= lcdTouch;
    			}
    			
    		    if (currentObject.isDir()==0){
    				
    				//System.out.println("Enter in pressed if condition");         DEBUG log
    		    	//path=new Path();
    		    	mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
    		    	
    				path.addCircle((float)eventX,  (float)eventY,(float) 3,Path.Direction.CW);
    				if(MainActivity.gToast!=null){
    		    		
    					MainActivity.gToast.cancel();
    				}
    				MainActivity.gToast=Toast.makeText(mContext,(int)eventX+ ", "+(int)eventY+ ", "+" (PRESS) "+" <"+currentObject.getFinger()+">", 
    						Toast.LENGTH_LONG);    
    				MainActivity.gToast.show();
    				
    				path.moveTo(eventX, eventY);
    				mPressRelease[currentObject.getFinger()][0]=currentObject;
    				TimeDiff[1]=TimeDiff[0]=0f;
    			}
    			
    			else if(currentObject.isDir()==1){
    				if(mPressRelease[currentObject.getFinger()][0]==null)mPressRelease[currentObject.getFinger()][0]=new TouchObject(new TimeStampObject(0,0),0,0,currentObject.getFinger(),0);
    				//System.out.println("Enter in released if condition");         DEBUG log
    				mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
    				path.addCircle(eventX,  eventY,(float) 2,Path.Direction.CW);
    				
    				if(MainActivity.gToast!=null){
    		    		
    					MainActivity.gToast.cancel();
    				}
    				MainActivity.gToast=Toast.makeText(mContext,(int)eventX+ ", "+(int)eventY+ " (MOVE)"+" <"+currentObject.getFinger()+">", Toast.LENGTH_LONG);
    				MainActivity.gToast.show();
    				
    				mPressRelease[currentObject.getFinger()][1]=currentObject;
    				
    				if(mPressRelease[currentObject.getFinger()][1].getT().mmdd==mPressRelease[currentObject.getFinger()][0].getT().mmdd){
	    				TimeDiff[0]=0;
	    				timeDiff(mPressRelease[currentObject.getFinger()][1].getT().time,mPressRelease[currentObject.getFinger()][0].getT().time);
	    			}
	    			else{
	    				TimeDiff[0]=mPressRelease[currentObject.getFinger()][1].getT().mmdd-mPressRelease[currentObject.getFinger()][0].getT().mmdd;
	    				TimeDiff[1]=0;
	    			}
    				
    				if(mPressRelease[currentObject.getFinger()][0]!=null){
    					path.moveTo(mPressRelease[currentObject.getFinger()][0].getX()/lcdTouch, mPressRelease[currentObject.getFinger()][0].getY()/lcdTouch);
    					mPressRelease[currentObject.getFinger()][0]=mPressRelease[currentObject.getFinger()][1];
    				}
    				else
    					path.moveTo(0,0);
    				path.lineTo(eventX, eventY);
    				
    			}
    			else{
    				if(mPressRelease[currentObject.getFinger()][0]==null)mPressRelease[currentObject.getFinger()][0]=new TouchObject(new TimeStampObject(0,0),0,0,currentObject.getFinger(),0);
    				mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
    				path.addCircle(eventX,  eventY,(float) 1,Path.Direction.CW);
    				
    				if(MainActivity.gToast!=null){
    		    		
    					MainActivity.gToast.cancel();
    				}
    				MainActivity.gToast=Toast.makeText(mContext,(int)eventX+ ", "+(int)eventY+ " (RELEASE)"+" <"+currentObject.getFinger()+">", Toast.LENGTH_LONG);
    				MainActivity.gToast.show();
    				
    				mPressRelease[currentObject.getFinger()][1]=currentObject;
    				
    				if(mPressRelease[currentObject.getFinger()][1].getT().mmdd==mPressRelease[currentObject.getFinger()][0].getT().mmdd){
	    				TimeDiff[0]=0;
	    				timeDiff(mPressRelease[currentObject.getFinger()][1].getT().time,mPressRelease[currentObject.getFinger()][0].getT().time);
	    			}
	    			else{
	    				TimeDiff[0]=mPressRelease[currentObject.getFinger()][1].getT().mmdd-mPressRelease[currentObject.getFinger()][0].getT().mmdd;
	    				TimeDiff[1]=0;
	    			}
    				
    				if(mPressRelease[currentObject.getFinger()][0]!=null)
    					path.moveTo(mPressRelease[currentObject.getFinger()][0].getX()/lcdTouch, mPressRelease[currentObject.getFinger()][0].getY()/lcdTouch);    					
    				else
    					path.moveTo(0,0);
    				path.lineTo(eventX, eventY);
    				
    			}
    			
    			count++;
    		}
    	}
    	catch(Exception e){
    		Log.v(com.main.logparser.parser.TAG,"Exception Occured\n");
    		e.printStackTrace();
    	}
    	// Schedules a repaint.
    	invalidate();
    	return true;
  	}

  	void timeDiff(double current,double previous){
		//System.out.println(current+", "+previous);				DEBUG LOG
		
		if(0==current-previous){
			TimeDiff[1] = 0;
			return ;
		}
		
		int curTi,preTi,count;
		float curTf,preTf;
		
		try {
			curTi = preTi = count = 0;
			curTf = (float) (current - (long) current);
			preTf = (float) (previous - (long) previous);			
			while (current != 0) {
				curTi += (int) ((int) (current % 100) * (Math.pow(60, count)));
				current = current / 100;
				count++;
			}
			count = 0;
			while (previous != 0) {
				preTi += (int) ((int) (previous % 100) * (Math.pow(60, count)));
				previous = previous / 100;
				count++;
			}
			//System.out.println(curTf+", "+preTf);						DEBUG LOG
			//System.out.println(curTi+", "+preTi);						DEBUG LOG
			
			if(curTi==preTi)
				TimeDiff[1]=(curTf-preTf);
			else if(curTf==preTf)
				TimeDiff[1]=(curTi-preTi);
			else
				TimeDiff[1]= ((float) ((float)curTi + curTf) - (float) ((float)preTi + preTf));
			
			//System.out.println(TimeDiff[1]);				
			return;
			
		} catch (Exception e) {
			Log.v(com.main.logparser.parser.TAG,"Math Exception Occured\n");
  		e.printStackTrace();
		}
		
	}

  
  @SuppressWarnings({ "rawtypes", "unchecked" })
	private int colorpicker(int finger) {
			switch(finger){
				case 0: path=new Path();
						path_color_list.add( new Pair(path,Color.BLACK));
						mColor=Color.BLACK;
					break;
				case 1: path=new Path();
						path_color_list.add( new Pair(path,Color.BLUE));
						mColor=Color.BLUE;
						break;
				case 2: path=new Path();
						path_color_list.add( new Pair(path,Color.GREEN));
						mColor=Color.GREEN;
						break;
				case 3: path=new Path();
						path_color_list.add( new Pair(path,Color.MAGENTA));
						mColor=Color.MAGENTA;
						break;
				case 4: path=new Path();
						path_color_list.add( new Pair(path,Color.RED));
						mColor=Color.RED;
						break;
				case 5: path=new Path();
						path_color_list.add( new Pair(path,Color.rgb(51,181,229)));
						mColor=Color.CYAN;
						break;
				case 6: path=new Path();
						path_color_list.add( new Pair(path,Color.LTGRAY));
						mColor=Color.LTGRAY;
						break;
				case 7: path=new Path();
						path_color_list.add( new Pair(path,Color.rgb(255,187,56)));
						mColor=Color.DKGRAY;
						break;
				case 8: path=new Path();
						path_color_list.add( new Pair(path,Color.rgb(153,51,204)));
						mColor=Color.GRAY;
						break;
				case 9: path=new Path();
						path_color_list.add( new Pair(path,Color.YELLOW));
						mColor=Color.YELLOW;
						break;
				
				default: path=new Path();
						path_color_list.add( new Pair(path,Color.BLACK)); 
						mColor=Color.BLACK; 				
			}
			return mColor;
	}

  
}