package com.kernel.logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.kernel.logparser.TouchObject.TimeStampObject;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.content.res.AssetManager;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class parser extends View{

    public static List<TouchObject> parsedData= new ArrayList<TouchObject>();   //ArrayList object Usage creates outofbounds :(
    
	private final Boolean PRESSED=true;
    private final Boolean RELEASED=false;
    
    public final static String TAG = "PARSER";
    		
    @SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	public parser(Context context, AttributeSet attrs)
    {
	   super(context, attrs);
	   	SingleTouchActivity.parse_flag=true;				//Set parse_flag to restart the activity
	   	parsedData.clear();
        try{
        	
        	//AssetManager manager = context.getAssets();      File path in assets folder of .apk
        	File sdcard = Environment.getExternalStorageDirectory();       //Read from sdcard
        	File file; 
        	
        	if((file = new File(sdcard,"kernel.log")) == null){
        		MainActivity.fileExistflag=false;
        		return;
        	}
        	
        	MainActivity.fileExistflag=true;
        		
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	
        	/*System.out.println(timedate[1]+" "+timedate[2]+"\n");*/                		
            String Strline;
            int Finger;
            String[] touchtoken,mtimedate;
            
            Log.v(TAG,"File Opened\n");
            //Read File Line By Line
            
            while ((Strline = br.readLine()) != null)   {
            	try{
            	if(Strline.contains("[Touch]")){
            		
            		if( Strline.contains("y[") && Strline.contains("x[")){
            		
            		touchtoken=parseLine(Strline);
            		
            		if((touchtoken[1].toLowerCase()).contains("press")){

            			//System.out.println("If press condition entered");         DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
            			Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
            			//System.out.println(Finger);								DEBUG LOG
            			//System.out.println(mtimedate[1]+" "+mtimedate[2]+"\n");		DEBUG LOG
            			
            			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                    	Double.valueOf(mtimedate[2])),Float.valueOf(touchtoken[2]).intValue(),
                    	Float.valueOf(touchtoken[3]).intValue(),Finger,PRESSED,Float.valueOf(touchtoken[4]).intValue()));
            		}
                    	
            		else if ((touchtoken[1].toLowerCase()).contains("release") && touchtoken[1].contains("<")){ 
            			
            			//System.out.println("If release condition entered");		DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
                		Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(mtimedate[1]+" "+mtimedate[2]+"\n");		DEBUG LOG
            		
                		parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                		Double.valueOf(mtimedate[2])),Float.valueOf(touchtoken[2]).intValue(),
            			Float.valueOf(touchtoken[3]).intValue(),Finger,RELEASED));
            		
            		}
            		
            		else if ((touchtoken[1].toLowerCase()).contains("release") && touchtoken[2].contains("<")){ 
            			       			
            			//System.out.println("If release different condition entered");		DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
                		Finger=Float.valueOf((touchtoken[2].substring(touchtoken[2].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            		
                		parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                		Double.valueOf(mtimedate[2])),Float.valueOf(touchtoken[3]).intValue(),
            			Float.valueOf(touchtoken[4]).intValue(),Finger,RELEASED));
            		
            		}
            		else continue;
            	}
            	else if(Strline.contains("Palm")){
            			touchtoken=parseLine(Strline);
            			
            			if((touchtoken[1].toLowerCase()).contains("detected")){

                			//System.out.println("If press condition entered");         DEBUG LOG
                			mtimedate=touchtoken[0].split("\\s+");
                			//System.out.println(Finger);								DEBUG LOG
                			//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
                			
                			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                			Double.valueOf(mtimedate[2])),0,0,10,PRESSED));
                		}
                        	
                		else if ((touchtoken[1].toLowerCase()).contains("released")){ 
                			
                			//System.out.println("If release condition entered");		DEBUG LOG
                			mtimedate=touchtoken[0].split("\\s+");      
                    		//System.out.println(Finger);								DEBUG LOG
                    		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
                		
                    		parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                    		Double.valueOf(mtimedate[2])),0,0,10,RELEASED));
                		
                		}
                		else continue;
            		}
            		else if(Strline.contains("KEY[")){
            			//Log.v(TAG,Strline);
            			touchtoken=parseLine(Strline);
            			if(touchtoken[2].contains("BACK"))Finger=1;
        				else if(touchtoken[2].contains("HOME"))Finger=2;
        				else Finger=3;
            			
            			if((touchtoken[2].toLowerCase()).contains("pressed")){
            				mtimedate=touchtoken[0].split("\\s+");      
            				parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                            Double.valueOf(mtimedate[2])),0,0,Finger+10,PRESSED));
            			}
            			else{
            				mtimedate=touchtoken[0].split("\\s+");      
            				parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                            Double.valueOf(mtimedate[2])),0,0,Finger+10,RELEASED));
            			}
                	
            		} 		
            		
            	  }
            	}catch(ArrayIndexOutOfBoundsException e){
                	Log.v(TAG,"Array Out of Bounds Exception: Continuing to next data");
                	e.printStackTrace();
                //	parsedData.remove(parsedData.size()-1);
                	continue;
            	}
            }
            br.close();
            
            //displayTouchList();	
        
        }
        catch (Exception e){
        	Log.v(TAG,"Invalid Data Or File Missing\n");
            e.printStackTrace();
        }
    }


    public static String[] parseLine(String input) throws NullPointerException, ParseException{
    		if(input == null){
    			throw new NullPointerException();
             }
    		
    		input=input.trim();
    		input=input.replaceAll("[\\]:xyz-]", "");
    		input=input.replaceAll("area","");
    		input = input.substring(input.indexOf("/")+1);
    		String[] touchtokens = input.split("[\\[]");
    		
    		/*System.out.println(input);					DEBUG LOG
    	    
    		for(String token : touchtokens){
    			if (token == null || token.trim().equals(""))token="0";
    	        System.out.println(token+ " ");
      	    } 		*/
    		return touchtokens;
    		
    }
    
    
    public static void displayTouchList(){
    	for(TouchObject t:parsedData){
	        Log.v(TAG,TouchObject.display(t));
	       
    	}
    }
}