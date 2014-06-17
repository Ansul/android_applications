package com.kernel.logparser;


import java.util.ArrayList;
import java.util.List;

import com.kernel.logparser.TouchObject.TimeStampObject;

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
  private Path path = new Path();
  private int crash_protector=0;
  private Paint navibarpaint=new Paint();
  private Context mContext;									//Passing context within class for TOAST notification
  private int mColor;										//Color for the particular path
  private int mPalm;										//To check if Palm event has occured
  private int mKeyPress;										//To check if KeyPress event has occured
  private float[] TimeDiff=new float[2];
  public static int mSize;
  TouchObject currentObject;
  public static int count=0;									//Storing the number of event displayed
 
  private static TouchObject[][] mPressRelease=new TouchObject[14][2];    /*Storing PRESS & RELEASE events based on 
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
    mPalm=0;
    mKeyPress=0;
    count=0;
  }

  @Override
  protected void onDraw(Canvas canvas) {
	  
	  for (Pair<Path,Integer> path_clr : path_color_list ){
		  //Log.v(parser.TAG,path_clr.second.toString());
		
			  paint.setColor(path_clr.second);
			  canvas.drawPath(path_clr.first, paint);
		 
	  }
	  
	/*  if(0==TimeDiff[0])
		  canvas.drawText(String.format("%.3f", TimeDiff[1])+ " sec", (float) (getWidth()-(.35*getWidth())), 30, navibarpaint);
	  else
		  canvas.drawText(Integer.toString((int)TimeDiff[0])+ " days", (float) (getWidth()-(.35*getWidth())), 30, navibarpaint);
	  
	 */ if(1==mPalm || 2==mPalm){				 
			 canvas.drawRect((float) (getWidth()/2- .04*getWidth()), 5,(float) ((float) getWidth()/2+.04*getWidth()), 45, navibarpaint);
	  }
	  if(mKeyPress>0){
		  switch(mKeyPress){		  
		  	case 11:canvas.drawText("BACK ", 10, (float) 0.04 * getHeight(), navibarpaint);
		  		break;
		  	case 12:canvas.drawText("HOME ", 10, (float) 0.04 * getHeight(), navibarpaint);
		  		break;
		  	case 13:canvas.drawText("MENU ", 10, (float) 0.04 * getHeight(), navibarpaint);
		  		break;
		  }
	  }
  }
  
 
  @SuppressWarnings("deprecation")
@Override
  public boolean onTouchEvent(MotionEvent event){
    
	 
    	   Thread AutorunThread=new Thread(new Runnable() {
    	   @Override
    	   public void run() {
    		   do {
    				  
    			   try {
    				   if(count >= (mSize-1)){
	                       	  return ;
	 					}	    					   
 				  
    				   if(count==mSize-2){
    					  	System.out.println("Time Differnce last");
    					   Thread.sleep(1000);
    				   }
    				   else{
    					   
    					   	long Diff;
    					   	int pre=count;
    					   	int cur=count+1;
    					   	double current=parser.parsedData.get(cur).getT().time;
    					   	double previous=parser.parsedData.get(pre).getT().time;
    					   	int curTi,preTi,countloop;
    					   	float curTf,preTf;
      				     
    					  /* 	TouchObject.displayObject(parser.parsedData.get(pre));
    					   	TouchObject.displayObject(parser.parsedData.get(cur));
    				   	   */
    				   	   	if((current)==(previous))Diff= 0;
    						
    							
    							
    						curTi = preTi = countloop = 0;
    						curTf = (float) (current - (long) current);
    						preTf = (float) (previous - (long) previous);	
    						 				
    						while (current != 0) {
    							curTi += (int) ((int) (current % 100) * (Math.pow(60, countloop)));
    							current = current / 100;
    							countloop++;
    						}
    						countloop = 0;
    						while (previous != 0) {
    							preTi += (int) ((int) (previous % 100) * (Math.pow(60, countloop)));
    							previous = previous / 100;
    							countloop++;
    						}
    							
    								
    								
    				   	   	Diff=(long) ((((float)curTi + curTf) -  ((float)preTi + preTf))*1000);
    				   	   	if(Diff>300000 || Diff<0){
    				   	   	 
    				   	   			Diff=300000;    	
    				   	   		} 
    				   	   		System.out.println("Time Differnce: "+Diff+", "+count);

    				   	   		Thread.sleep(Diff);
    				   	}
    			    	int prevcount = count;		   
    				   ((MainActivity)mContext).runOnUiThread(new Runnable() {
    					   
    		           	  
    				   @Override
    				   public void run() {
                    	  //System.out.println("OnTouchObjectEvent called\n");				DEBUG log
    					   if(count >= (mSize-1)){
	 	                       	  return ;
	 	 					   }
	    					   
    					  int pressure=0;	
    					  float lcdTouch=1,eventX,eventY;
                  	  	 
                  	  	  if(null != MainActivity.z && MainActivity.z.toString().length()>=1)
                  	  		  pressure=Double.valueOf(MainActivity.z.toString()).intValue();
                  	  	  if(null != MainActivity.ratio && MainActivity.ratio.toString().length()>=1)
                  	  		  lcdTouch= Float.valueOf(MainActivity.ratio.toString());
                  	  	currentObject=parser.parsedData.get(count);   
                  	  
                  	  	  Log.v(parser.TAG,count +". " +TouchObject.display(currentObject));
            			
                  	  	  if(10==currentObject.getFinger()){
            					
            				    
            				    if (currentObject.isDir()){
            				    	mPalm=1;
            				    /*	if(MainActivity.gToast!=null){
            				    	
                		    				MainActivity.gToast.cancel();
            				    	}
            				    	
            				    	MainActivity.gToast=Toast.makeText(mContext," PALM PRESSED ",Toast.LENGTH_LONG);    
            				    	MainActivity.gToast.show();*/
            				    	mPressRelease[10][0]=currentObject;
            				    	//TimeDiff[1]=TimeDiff[0]=0f;
            				    }
            				    else{
            				    	mPalm=2;
            				    	/*if(MainActivity.gToast!=null){
                				    	
            		    				MainActivity.gToast.cancel();
            				    	}
        				    	
            				    	MainActivity.gToast=Toast.makeText(mContext," PALM RELEASED ",Toast.LENGTH_LONG);    
            				    	MainActivity.gToast.show();*/
            				    	mPressRelease[10][1]=currentObject;
            				    	if(mPressRelease[10][0]==null)mPressRelease[currentObject.getFinger()][0]=new TouchObject(new TimeStampObject(0,0),0,0,10,true,0);
                					
            				    /*	if(mPressRelease[10][1].getT().mmdd==mPressRelease[10][0].getT().mmdd){
                	    				TimeDiff[0]=0;
                	    				timeStampDiff(mPressRelease[10][1].getT().time,mPressRelease[10][0].getT().time);
                	    			}
                	    			else{
                	    				TimeDiff[0]=mPressRelease[10][1].getT().mmdd-mPressRelease[10][0].getT().mmdd;
                	    				TimeDiff[1]=0;
                	    			}*/
            				    	mPalm=0;
            				    }
                  	  	  }
                  	  	  else if(currentObject.getFinger()>10){
            					if(currentObject.isDir()){
            					
            				    	/*if(MainActivity.gToast!=null){
            				    	
                		    				MainActivity.gToast.cancel();
            				    	}*/
            				    	if(currentObject.getFinger()==11){
            				    		//MainActivity.gToast=Toast.makeText(mContext," BACK KEY PRESSED ",Toast.LENGTH_LONG);
            				    		mKeyPress=11;
            				    		mPressRelease[11][0]=currentObject;}
            				    	else if(currentObject.getFinger()==12){
            				    		//MainActivity.gToast=Toast.makeText(mContext," HOME KEY PRESSED ",Toast.LENGTH_LONG);
            				    		mKeyPress=12;
            				    		mPressRelease[12][0]=currentObject;}
            				    	else{
            				    		//MainActivity.gToast=Toast.makeText(mContext," MENU KEY PRESSED ",Toast.LENGTH_LONG);
            				    		mKeyPress=13;
            				    		mPressRelease[13][0]=currentObject;}
            				    	//MainActivity.gToast.show();    				    	
            				    	
            				    	//TimeDiff[1]=TimeDiff[0]=0f;
            					}
            					else{
            						/*if(MainActivity.gToast!=null){
                				    	
            		    				MainActivity.gToast.cancel();
            						}*/
            						if(currentObject.getFinger()==11){
            							//MainActivity.gToast=Toast.makeText(mContext," BACK KEY RELEASED ",Toast.LENGTH_LONG);
            							mKeyPress=11;
            							mPressRelease[11][1]=currentObject;
                				    	if(mPressRelease[11][0]==null)mPressRelease[11][0]=new TouchObject(new TimeStampObject(0,0),0,0,11,true,0);
                    				/*	
                				    	if(mPressRelease[11][1].getT().mmdd==mPressRelease[11][0].getT().mmdd){
                    	    				TimeDiff[0]=0;
                    	    				timeStampDiff(mPressRelease[11][1].getT().time,mPressRelease[11][0].getT().time);
                    	    			}
                    	    			else{
                    	    				TimeDiff[0]=mPressRelease[11][1].getT().mmdd-mPressRelease[11][0].getT().mmdd;
                    	    				TimeDiff[1]=0;
                    	    			}*/
                				    	mKeyPress=0;
                				    }
            						else if(currentObject.getFinger()==12){
            							//MainActivity.gToast=Toast.makeText(mContext," HOME KEY RELEASED ",Toast.LENGTH_LONG);
            							mKeyPress=12;
            							mPressRelease[12][1]=currentObject;
                				    	if(mPressRelease[12][0]==null)mPressRelease[12][0]=new TouchObject(new TimeStampObject(0,0),0,0,12,true,0);
                    				/*	
                				    	if(mPressRelease[12][1].getT().mmdd==mPressRelease[12][0].getT().mmdd){
                    	    				TimeDiff[0]=0;
                    	    				timeStampDiff(mPressRelease[12][1].getT().time,mPressRelease[12][0].getT().time);
                    	    			}
                    	    			else{
                    	    				TimeDiff[0]=mPressRelease[12][1].getT().mmdd-mPressRelease[12][0].getT().mmdd;
                    	    				TimeDiff[1]=0;
                    	    			}*/
                				    	mKeyPress=0;
                				    }
            						else{
            							//MainActivity.gToast=Toast.makeText(mContext," MENU KEY RELEASED ",Toast.LENGTH_LONG);
            							mKeyPress=13;
            							mPressRelease[13][1]=currentObject;
                				    	if(mPressRelease[13][0]==null)mPressRelease[13][0]=new TouchObject(new TimeStampObject(0,0),0,0,13,true,0);
                    					
                				    	/*if(mPressRelease[13][1].getT().mmdd==mPressRelease[13][0].getT().mmdd){
                    	    				TimeDiff[0]=0;
                    	    				timeStampDiff(mPressRelease[13][1].getT().time,mPressRelease[13][0].getT().time);
                    	    			}
                    	    			else{
                    	    				TimeDiff[0]=mPressRelease[13][1].getT().mmdd-mPressRelease[13][0].getT().mmdd;
                    	    				TimeDiff[1]=0;
                    	    			}*/
                				    	mKeyPress=0;
                				    }
            						//MainActivity.gToast.show();  
            						
            					}
            				
                  	  	  }
                  	  	  else if(currentObject.getFinger()<10){
            				if(pressure>0 && currentObject.getPressure()>pressure){
            					mPressRelease[currentObject.getFinger()][0].setPressure(-1);            					
            					count++; 
                				return;
                			}
            				
            				eventX = currentObject.getX();
                			eventY = currentObject.getY();
                			if(lcdTouch!=1){
                				eventX/= lcdTouch;
                				eventY/= lcdTouch;
                			}
                			
            				if (currentObject.isDir()){
            				
            					//System.out.println("Enter in pressed if condition");         DEBUG log
            					mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
            		    	
            					path.addCircle(eventX,  eventY,(float) 8,Path.Direction.CW);
            					/*if(MainActivity.gToast!=null){
            		    		
            						MainActivity.gToast.cancel();
            					}
            					MainActivity.gToast=Toast.makeText(mContext,currentObject.getX()+ ", "+currentObject.getY()+ ", "+currentObject.getPressure()+" (PRESS) "+" <"+currentObject.getFinger()+">", 
            						Toast.LENGTH_LONG);    
            					MainActivity.gToast.show();*/
            				
            					path.moveTo(eventX, eventY);
            					mPressRelease[currentObject.getFinger()][0]=currentObject;
            					/*TimeDiff[1]=TimeDiff[0]=0f;*/
            				}
            			
            				else{
            					if(mPressRelease[currentObject.getFinger()][0]==null)mPressRelease[currentObject.getFinger()][0]=new TouchObject(new TimeStampObject(0,0),0,0,currentObject.getFinger(),true,0);
            					
            					if(-1==mPressRelease[currentObject.getFinger()][0].getPressure()){
            						count++;
            						return;
            					}
            					//System.out.println("Enter in released if condition");         DEBUG log
            					mColor=colorpicker(currentObject.getFinger());						//Decide the color before traversing the path
            					path.addCircle(eventX, eventY,(float) 3,Path.Direction.CW);
            				
            					/*if(MainActivity.gToast!=null){
            		    		
            						MainActivity.gToast.cancel();
            					}
            					MainActivity.gToast=Toast.makeText(mContext,currentObject.getX()+ ", "+currentObject.getY()+ " (RELEASE)"+" <"+currentObject.getFinger()+">", Toast.LENGTH_LONG);
            					MainActivity.gToast.show();*/
            				
            					mPressRelease[currentObject.getFinger()][1]=currentObject;
            					/*if(mPressRelease[currentObject.getFinger()][1].getT().mmdd==mPressRelease[currentObject.getFinger()][0].getT().mmdd){
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
                  	  	  }
                  	    
                  	  /*	if(MainActivity.gToast!=null){
        		    		
    						MainActivity.gToast.cancel();
    					}
    					MainActivity.gToast=Toast.makeText(mContext,count+" / "+mSize, Toast.LENGTH_SHORT);
    					MainActivity.gToast.show();*/
                  	  	count++;
                  	  	//System.out.println(count+ "In UI Thread");
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
    	   crash_protector++;

    	      if(crash_protector>1)return true;				//To protect application from crashing
    	      else{ 
    	    	   mSize=parser.parsedData.size();
    	    	   if(AutorunThread.isAlive())
      	    		   AutorunThread.destroy();
    	    	   AutorunThread.start();
    	      }
    	      return true;
     	}
 
/*

	long timeDiff(double current,double previous){
		
		if((current)==(previous)){
			System.out.println("Same Time");
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
			return (long) ((((float)curTi + curTf) -  ((float)preTi + preTf))*1000);
	}*/
	
	void timeStampDiff(double current,double previous){
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
  