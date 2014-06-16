package com.example.logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.example.logparser.TouchObject.TimeStampObject;

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
    		
    public parser(Context context, AttributeSet attrs)
    {
	   super(context, attrs);
	   	SingleTouchActivity.parse_flag=true;				//Set parse_flag to restart the activity
	   	parsedData.clear();
        try{
        	
        	//AssetManager manager = context.getAssets();      File path in assets folder of .apk
        	File sdcard = Environment.getExternalStorageDirectory();       //Read from sdcard
        	File file = new File(sdcard,"kernel.log");  
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	
        	/*System.out.println(timedate[1]+" "+timedate[2]+"\n");*/                		
            String Strline;
            int Finger;
            String[] touchtoken,mtimedate;
            
            Log.v(TAG,"File Opened\n");
            //Read File Line By Line
            while ((Strline = br.readLine()) != null)   {
            	
            	if(Strline.contains("[Touch]") && Strline.contains("y[") && Strline.contains("x[")){
            		
            		//Log.v(TAG,Strline);
            		
            		touchtoken=parseLine(Strline);
            		if((touchtoken[1].toLowerCase()).contains("press")){

            			//System.out.println("If press condition entered");         DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
            			Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
            			//System.out.println(Finger);								DEBUG LOG
            			//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            			
            			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
                    	Float.valueOf(mtimedate[2])),Float.valueOf(touchtoken[2]).intValue(),
                    	Float.valueOf(touchtoken[3]).intValue(),Finger,PRESSED,Float.valueOf(touchtoken[4]).intValue()));
            		}
                    	
            		else if ((touchtoken[1].toLowerCase()).contains("release") && touchtoken[1].contains("<")){ 
            			
            			//System.out.println("If release condition entered");		DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
                		Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            		
                		parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
            			Float.valueOf(mtimedate[2])),Float.valueOf(touchtoken[2]).intValue(),
            			Float.valueOf(touchtoken[3]).intValue(),Finger,RELEASED));
            		
            		}
            		
            		else if ((touchtoken[1].toLowerCase()).contains("release") && touchtoken[2].contains("<")){ 
            			       			
            			//System.out.println("If release different condition entered");		DEBUG LOG
            			mtimedate=touchtoken[0].split("\\s+");
                		Finger=Float.valueOf((touchtoken[2].substring(touchtoken[2].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            		
                		parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[1]).intValue(),
            			Float.valueOf(mtimedate[2])),Float.valueOf(touchtoken[3]).intValue(),
            			Float.valueOf(touchtoken[4]).intValue(),Finger,RELEASED));
            		
            		}
            		else continue;
            	}
            }
            br.close();
            
            //displayTouchList();	
        
        }catch (Exception e){
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
    		input = input.substring(input.indexOf("/")+1);
    		String[] touchtokens = input.split("[\\[]");
    		
    		/*System.out.println(input);					DEBUG LOG
    	    
    		for(String token : touchtokens){
    	        System.out.println(token+ " ");
    	    } 		*/
    		return touchtokens;
    		
    }
    
    
    public void displayTouchList(){
    	for(TouchObject t:parsedData){
	        TouchObject.display(t);
	       
    	}
    }
}