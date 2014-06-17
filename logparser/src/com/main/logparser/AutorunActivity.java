package com.main.logparser;

import java.util.ArrayList;
import java.util.List;

import com.kernel.logparser.MainActivity;
import com.kernel.logparser.SingleTouchActivity;
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

public class AutorunActivity extends View {
  private Paint paint = new Paint();
  private Paint navibarpaint=new Paint();
  private int crash_protector=0;
  private Path path=new Path();
  private Context mContext;									//Passing context within class for TOAST notification
  private int mColor;										//Color for the particular path
  private Float rate=1f;
  
  public static int mSize;
  									
  //private float[] TimeDiff=new float[2];
  
  public static int count;									//Storing the number of event displayed
			
  
  private static TouchObject[][] mPressRelease=new TouchObject[10][2];    /*Storing PRESS & RELEASE events based on 
  																		  index for same finger */
  
  List<Pair<Path, Integer>> path_color_list = new ArrayList<Pair<Path,Integer>>(); //Different color for each path


  
  public AutorunActivity(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext=context;
    
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2f);
    //paint.setColor(Color.BLACK);   					Default color BLACK
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    
    navibarpaint.setAntiAlias(true);
    navibarpaint.setStrokeWidth(7f);
	//navibarpaint.setColor(Color.argb(50, 55, 55, 155));   				
    navibarpaint.setColor(Color.argb(128,255,176,0));
    navibarpaint.setStyle(Paint.Style.FILL);
    navibarpaint.setTextSize(paint.getTextSize() * mContext.getResources().getDisplayMetrics().density*2);  
    setKeepScreenOn(true);
    count=0;
   
  }

  @Override
  protected void onDraw(Canvas canvas) {	 
	  	   
	  for (Pair<Path,Integer> path_clr : path_color_list ){

		   paint.setColor(path_clr.second);
		  canvas.drawPath( path_clr.first, paint);
		  
	  }
	  /*
	  if(0==TimeDiff[0])
		  canvas.drawText(String.format("%.3f", TimeDiff[1])+ " sec", (float) (getWidth()-(.35*getWidth())), 30, navibarpaint);
	  else
		  canvas.drawText(Integer.toString((int)TimeDiff[0])+ " days", (float) (getWidth()-(.35*getWidth())), 30, navibarpaint);
	  */
	  //if(count == mSize-1)canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
  }

  
  @SuppressWarnings("deprecation")
@Override
  public boolean onTouchEvent(MotionEvent event){
	  
	 
	    	   Thread AutorunThread=new Thread(new Runnable(){
	    	   @Override
	    	   public void run() {
	    		  
	    		   
	    		   do {
	    			   try {
	    				   if(count >= (mSize-1)){
	    					 
	 	                       	return ;
	 	 					}	    					   
	    				  
	    				   Float factor = (float) 1;
	    					  if(null != MainActivity.rate && MainActivity.rate.toString().length()>=1)
	    						  factor= Float.valueOf(MainActivity.rate.toString());
	    					  if(factor>3)factor=(float) 5;
	    					  
	    				   if(count==mSize-2)
	    					   Thread.sleep(1000);
	    				   else{
	    					   
	    					   long Diff;
	    					   int pre=count;
	    					   int cur=count+1;
	    					 /*  TouchObject.displayObject(com.main.logparser.parser.parsedData.get(pre));
	    					   TouchObject.displayObject(com.main.logparser.parser.parsedData.get(cur));*/
	    					   Diff=timeDiff(com.main.logparser.parser.parsedData.get(cur).getT().time,com.main.logparser.parser.parsedData.get(pre).getT().time);
	    				       				
	    					   if(count==0)	Diff=100;
	    					   if(Diff>300000 || Diff<0){
	    						  
	    					   		Diff=300000;    	
	    					   }   				   
	    					  // System.out.println("Time Differnce: "+Diff+", "+count);
	    				  
	    					   Thread.sleep((long) (Diff/factor));
	    				   }
	    			    	int prevcount = count;	
	    			    	
	    				   ((MainActivity)mContext).runOnUiThread(new Runnable() {
	    					   
	    				   @Override
	    				   public void run() {	
	    					   
	    					   //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
	    					   if(count >= (mSize-1)){
	 	                       	  return ;
	 	 					   }
	    					   
	    					   float lcdTouch=1,eventX, eventY;	  
	    						
	                		   if(null != MainActivity.ratio && MainActivity.ratio.toString().length()>=1)
		                   	  	  lcdTouch= Float.valueOf(MainActivity.ratio.toString());
	                			  
	    					   TouchObject currentObject;
	    					   currentObject=com.main.logparser.parser.parsedData.get(count);
	    					   Log.v(parser.TAG,count +". " +TouchObject.display(currentObject));
	    						eventX = currentObject.getX();
	                			eventY = currentObject.getY();
	                		
	                			if(lcdTouch!=1){
	                				eventX/= lcdTouch;
	                				eventY/= lcdTouch;
	                			}
	                			
	    					    if (currentObject.isDir()==0){
    				
	    						   //System.out.println("Enter in pressed if condition");         DEBUG log
	    						   //path=new Path();
	    						   mColor=colorpicker(currentObject.getFinger());	//Decide the color before traversing the path
    		    	
	    						   path.addCircle(eventX, eventY,(float) 8,Path.Direction.CW);
	    						   path.moveTo(eventX, eventY);
	    						   mPressRelease[currentObject.getFinger()][0]=currentObject;
	    						   //TimeDiff[1]=TimeDiff[0]=0f;
	    					   }
    			
	    					   else if(currentObject.isDir()==1){
	    						   if(mPressRelease[currentObject.getFinger()][0]==null)mPressRelease[currentObject.getFinger()][0]=new TouchObject(new TimeStampObject(0,0),0,0,currentObject.getFinger(),0);
	    						   //System.out.println("Enter in released if condition");         DEBUG log
	    						   mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
	    						   path.addCircle(eventX, eventY,(float) 3,Path.Direction.CW);
    				
	    						   mPressRelease[currentObject.getFinger()][1]=currentObject;
    				
	    						   /*if(mPressRelease[currentObject.getFinger()][1].getT().mmdd==mPressRelease[currentObject.getFinger()][0].getT().mmdd){
	    							   TimeDiff[0]=0;
	    							   timeStampDiff(mPressRelease[currentObject.getFinger()][1].getT().time,mPressRelease[currentObject.getFinger()][0].getT().time);
	    						   }
	    						   else{
	    							   TimeDiff[0]=mPressRelease[currentObject.getFinger()][1].getT().mmdd-mPressRelease[currentObject.getFinger()][0].getT().mmdd;
	    							   TimeDiff[1]=0;
	    						   }*/
    				
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
	    						   path.addCircle(eventX, eventY,(float) 2,Path.Direction.CW);
    				
	    					
	    						   mPressRelease[currentObject.getFinger()][1]=currentObject;
    				
	    						 /*  if(mPressRelease[currentObject.getFinger()][1].getT().mmdd==mPressRelease[currentObject.getFinger()][0].getT().mmdd){
	    							   TimeDiff[0]=0;
	    							   timeStampDiff(mPressRelease[currentObject.getFinger()][1].getT().time,mPressRelease[currentObject.getFinger()][0].getT().time);
	    						   }
	    						   else{
	    							   TimeDiff[0]=mPressRelease[currentObject.getFinger()][1].getT().mmdd-mPressRelease[currentObject.getFinger()][0].getT().mmdd;
	    							   TimeDiff[1]=0;
	    						   }*/
    				
	    						   if(mPressRelease[currentObject.getFinger()][0]!=null)
	    							   path.moveTo(mPressRelease[currentObject.getFinger()][0].getX()/lcdTouch, mPressRelease[currentObject.getFinger()][0].getY()/lcdTouch);    					
	    						   else
	    							   path.moveTo(0,0);
	    						   path.lineTo(eventX, eventY);
    				
	    					   }
	    					  /* if(MainActivity.gToast!=null){
	    				    		
	    	    					MainActivity.gToast.cancel();
	    						}
	    	    				MainActivity.gToast=Toast.makeText(mContext,count+" / "+mSize, Toast.LENGTH_SHORT);
	    	    				MainActivity.gToast.show();*/
	    	    				++count;
	    	    				invalidate();
	    				   }
	    				  });
	    				   while(prevcount == count) {
							   Thread.sleep(100);
						   }
	    			   } catch (Exception e) {
	    				   e.printStackTrace();
	    			   	}  
	    		   }while(count<mSize); 
	    		   
	    	   }
	    	   });
	    	
	    	   if(count == mSize-1){
	    		   if(AutorunThread.isAlive())
	  	    		   AutorunThread.destroy();
	      		
	      			if(MainActivity.gToast!=null){
			    		
						MainActivity.gToast.cancel();
					}
					MainActivity.gToast=Toast.makeText(mContext,"Log Finished", Toast.LENGTH_SHORT);
					MainActivity.gToast.show();
	    	   }
	    	   if(SingleTouchActivity.parse_flag && count>0){				//Check the value, if parse_flag set start activity again or continue
	      			SingleTouchActivity.parse_flag=false;
	      			crash_protector=0; 
	      			count=0;
	    	   }
	    	
	    	   if(null != MainActivity.rate && MainActivity.rate.toString().length() >=1)
		 	  	  	rate= Float.valueOf(MainActivity.rate.toString());
			 
			  crash_protector++;

    	      if(crash_protector>1)return true;				//To protect application from crashing
    	      else{ 
    	    	   mSize=com.main.logparser.parser.parsedData.size();
    	    	   if(AutorunThread.isAlive())
      	    		   AutorunThread.destroy();
    	    	   AutorunThread.start();
    	      }
    	      return true;
  	}
    	
  long timeDiff(double current,double previous){
		
		if((current)==(previous)){
			
			return 0;
		}
		
		int curTi,preTi,count;
		float curTf,preTf;
		curTi = preTi = count = 0;
		curTf = (float) (current - (long) current);
		preTf = (float) (previous - (long) previous);	
		
		try {  				
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
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return (long) ((((float)curTi + curTf) - ((float)preTi + preTf))*1000*rate);
}

  /*	void timeStampDiff(double current,double previous){
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
			Log.v(parser.TAG,"Math Exception Occured\n");
  		e.printStackTrace();
		}
		
	}
*/
  
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