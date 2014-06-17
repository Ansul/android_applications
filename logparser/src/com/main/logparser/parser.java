package com.main.logparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.kernel.logparser.MainActivity;
import com.main.logparser.TouchObject.TimeStampObject;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.content.res.AssetManager;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class parser extends View{

    public static List<TouchObject> parsedData= new ArrayList<TouchObject>();   //ArrayList object Usage creates outofbounds :(
   
    public final static String TAG = "PARSER";
    		
    @SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	public parser(Context context, AttributeSet attrs)
    {
	   super(context, attrs);
	   com.main.logparser.SingleTouchActivity.parse_flag=true;				//Set parse_flag to restart the activity
	   	parsedData.clear();
        try{
        	
        	//AssetManager manager = context.getAssets();      File path in assets folder of .apk
        	File sdcard = Environment.getExternalStorageDirectory();       //Read from sdcard
        	File file; 
        	
        	if((file = new File(sdcard,"main.log")) == null){
        		MainActivity.fileExistflag=false;
        		return;
        	}
        	
        	MainActivity.fileExistflag=true;
        		
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	
        	/*System.out.println(timedate[1]+" "+timedate[2]+"\n");*/                		
            String Strline;
            int Finger;
            String[] touchtoken,mtimedate,coordinate;
            
            Log.v(TAG,"File Opened\n");
            //Read File Line By Line
            while ((Strline = br.readLine()) != null)   {
            	try{
            	if(Strline.contains("[InputReader][Touch]") && Strline.contains("y:") && Strline.contains("x:")){
            		
            		//Log.v(TAG,Strline);									
            		
            		touchtoken=parseLine(Strline);
            		mtimedate=touchtoken[0].split("\\s+");
            		coordinate=touchtoken[3].split("\\s+");
            		
            		if((touchtoken[2].toLowerCase()).contains("down")){

            			//System.out.println("If press condition entered");         DEBUG LOG            			
            			//Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
            			//System.out.println(Finger);								DEBUG LOG
            			//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            			
            			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[0]).intValue(),
                    	Float.valueOf(mtimedate[1])),Float.valueOf(coordinate[2]).intValue(),
                    	Float.valueOf(coordinate[3]).intValue(),Float.valueOf(coordinate[0]).intValue(),0));
            		}
                    	
            		else if ((touchtoken[2].toLowerCase()).contains("move")){ 
            			
            			//System.out.println("If release condition entered");		DEBUG LOG            			
                		//Finger=Float.valueOf((touchtoken[1].substring(touchtoken[1].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            		
            			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[0]).intValue(),
                        Float.valueOf(mtimedate[1])),Float.valueOf(coordinate[2]).intValue(),
                        Float.valueOf(coordinate[3]).intValue(),Float.valueOf(coordinate[0]).intValue(),1));
            		
            		}
            		
            		else if ((touchtoken[2].toLowerCase()).contains("up")){ 
            			       			
            			//System.out.println("If release different condition entered");		DEBUG LOG            			
                		//Finger=Float.valueOf((touchtoken[2].substring(touchtoken[2].indexOf("<")+1)).replaceAll(">","")).intValue();
                		//System.out.println(Finger);								DEBUG LOG
                		//System.out.println(timedate[1]+" "+timedate[2]+"\n");		DEBUG LOG
            		
            			parsedData.add(new TouchObject(new TimeStampObject(Float.valueOf(mtimedate[0]).intValue(),
                        Float.valueOf(mtimedate[1])),Float.valueOf(coordinate[2]).intValue(),
                        Float.valueOf(coordinate[3]).intValue(),Float.valueOf(coordinate[0]).intValue(),2));
            		
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
    		
    		//input=input.trim();
    		input=input.replaceAll("[]:xy-]", "");					//input=input.replaceAll("[\\]:xyz-]", "");
    		//input = input.substring(input.indexOf("/")+1);
    		String[] touchtokens = input.split("[\\[]");
    		
    		/*System.out.println(input);					//DEBUG LOG
    	    
    		for(String token : touchtokens){
    	        System.out.println(token+ " ");
    	    } 		*/
    		return touchtokens;
    		
    }
    
    
    public void displayTouchList(){
    	for(TouchObject t:parsedData){
	       Log.v(TAG,TouchObject.display(t));
	       
    	}
    }
}