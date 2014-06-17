package com.kernel.logparser;

import java.util.ArrayList;
import java.util.List;

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

public class GroupedTouchActivity extends View {
  private Paint paint = new Paint();
  private Path path=new Path();
  private Context mContext;									//Passing context within class for TOAST notification
  private int mColor;										//Color for the particular path
  
  public static int count;									//Storing the number of event displayed
  public static boolean parse_flag; 						//Storing the status for RESET ON/OFF
  
  private static TouchObject[][] mPressRelease=new TouchObject[10][2];    /*Storing PRESS & RELEASE events based on 
  																		  index for same finger */
  
  List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>(); //Different color for each path
  
  public GroupedTouchActivity(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    mContext=context;
    paint.setAntiAlias(true);
    paint.setStrokeWidth(3f);
    //paint.setColor(Color.BLACK);   					Default color BLACK
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);

  }

  @Override
  protected void onDraw(Canvas canvas) {	 
	  	   
	  for (Pair<Path,Integer> path_clr : path_color_list ){
		  if(path_clr.second==mColor){
		   paint.setColor(path_clr.second);
		  canvas.drawPath( path_clr.first, paint);
		  }
	  }
  }

  
  @Override
  public boolean onTouchEvent(MotionEvent event){
    
	    //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
	  	
	  	float eventX, eventY;	  	
    	TouchObject temp;
    	if(parse_flag && count>0){				//Check the value, if parse_flag set start activity again or continue
    		count=0;
    		parse_flag=false;
    	}
      
    	try{
    		
    	
    		if(event.getAction() == MotionEvent.ACTION_DOWN && count<parser.parsedData.size()){
    			
    			//System.out.println("Enter in event.getAction() if condition");  
    			temp=parser.parsedData.get(count);
    			TouchObject.display(temp);
    			eventX = temp.getX();
    			eventY = temp.getY();
    			    			
    		    if (temp.isDir()){
    				
    				//System.out.println("Enter in pressed if condition");         DEBUG log
    		    	//path=new Path();
    		    	mColor=colorpicker(temp.getFinger());						//Decide the color before traversing the path
    		    	
    				path.addCircle((float)eventX,  (float)eventY,(float) 3,Path.Direction.CW);
    				MainActivity.gToast.cancel();
    				MainActivity.gToast=Toast.makeText(mContext,eventX+ ", "+eventY+ ", "+temp.getPressure()+" (PRESS) "+" <"+temp.getFinger()+">", 
    						Toast.LENGTH_LONG);    
    				MainActivity.gToast.show();
    				path.moveTo(eventX, eventY);
    				mPressRelease[temp.getFinger()][0]=temp;
    			}
    			
    			else{
    				
    				//System.out.println("Enter in released if condition");         DEBUG log
    				mColor=colorpicker(temp.getFinger());						//Decide the color before traversing the path
    				path.addCircle((float)eventX,  (float)eventY,(float) 0.6,Path.Direction.CW);
    				MainActivity.gToast.cancel();
    				MainActivity.gToast=Toast.makeText(mContext,eventX+ ", "+eventY+ " (RELEASE)"+" <"+temp.getFinger()+">", Toast.LENGTH_LONG);
    				MainActivity.gToast.show();
    				
    				mPressRelease[temp.getFinger()][1]=temp;
    				if(mPressRelease[temp.getFinger()][0]!=null)
    					path.moveTo(mPressRelease[temp.getFinger()][0].getX(), mPressRelease[temp.getFinger()][0].getY());
    				else
    					path.moveTo(0,0);
    				path.lineTo(eventX, eventY);
    			}
    			count++;
    		}
    	}
    	catch(Exception e){
    		Log.v(parser.TAG,"Path Exception Occured\n");
    		e.printStackTrace();
    	}
    	// Schedules a repaint.
    	invalidate();
    	return true;
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