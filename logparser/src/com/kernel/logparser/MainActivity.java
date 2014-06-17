package com.kernel.logparser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	 private Context mContext;
	 public static Toast gToast;											//Global Toast
	 public static boolean fileExistflag=false;
		 
	 private static boolean parsefinishflag=false;							//To check if parsing process has finished
	 private boolean parsestartedflag=false;								//To check if kernel.log is parsed
	 
	 public static Editable z=null;
	 public static Editable ratio=null;
	 public static Editable rate=null;
	 
	 public static int repeat;
	 private static boolean logSelect;
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() <1) {
		    	//finish();
		    	if(gToast!=null){
		    		gToast.cancel();
		    	}
		    	repeat=0;
		    	AutorunActivity.mSize=0;
		    	com.main.logparser.AutorunActivity.mSize=0;
		    	if(SingleTouchActivity.count==parser.parsedData.size())SingleTouchActivity.count=0;
		    	if(com.main.logparser.SingleTouchActivity.count==com.main.logparser.parser.parsedData.size())com.main.logparser.SingleTouchActivity.count=0;
		    	setContentView(R.layout.activity_main);
		    	if(logSelect){
		    		RadioButton rb1 = (RadioButton) findViewById(R.id.radio_kernel);
		    		rb1.setChecked(true);
		    	}
		    	else{  	
		    		RadioButton rb1 = (RadioButton) findViewById(R.id.radio_main);
		    		rb1.setChecked(true);
		    	}
		    }
		    else if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >=1){
		    	new AlertDialog.Builder(this)
				.setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						MainActivity.super.onBackPressed();
					}
				}).create().show();
		    }
		   
		    else if(keyCode == KeyEvent.KEYCODE_MENU){
		    	 if(logSelect){
		    new AlertDialog.Builder(this)
			.setMessage("Menu")
			.setNegativeButton("Pressure",new DialogInterface.OnClickListener() {
			
				public void onClick(DialogInterface arg0, int arg1) {
					LayoutInflater li = LayoutInflater.from(mContext);
					View promptsView = li.inflate(R.layout.prompts, null);
	 
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
	 
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView).setTitle("User Defined Pressure");
	 
					final EditText userInput = (EditText) promptsView.findViewById(R.id.userInputValue);
					alertDialogBuilder					
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
						z=userInput.getText();
					    }
					})
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					}).create().show();					
				}
		    })
			.setPositiveButton("Touch:LCD",new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					LayoutInflater li = LayoutInflater.from(mContext);
					View promptsView = li.inflate(R.layout.prompts, null);
	 
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
	 
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView).setTitle("User Defined Touch:LCD");
	 
					final EditText userInput = (EditText) promptsView
							.findViewById(R.id.userInputValue);
					alertDialogBuilder					
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
						ratio=userInput.getText();
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  }).create().show();	
				}
			}).create().show();
		   }
		    	 else{
		    		  new AlertDialog.Builder(this)
		  			.setMessage("Menu")
		  			.setNegativeButton("Autorun Rate",new DialogInterface.OnClickListener() {

		  				public void onClick(DialogInterface arg0, int arg1) {
		  					LayoutInflater li = LayoutInflater.from(mContext);
		  					View promptsView = li.inflate(R.layout.prompts, null);
		  	 
		  					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		  	 
		  					// set prompts.xml to alertdialog builder
		  					alertDialogBuilder.setView(promptsView).setTitle("User Defined Rate");
		  	 
		  					final EditText userInput = (EditText) promptsView.findViewById(R.id.userInputValue);
		  					alertDialogBuilder					
		  					.setPositiveButton("OK",
		  					  new DialogInterface.OnClickListener() {
		  					    public void onClick(DialogInterface dialog,int id) {
		  						// get user input and set it to result
		  						// edit text
		  						rate=userInput.getText();
		  					    }
		  					})
		  					.setNegativeButton("Cancel",
		  					  new DialogInterface.OnClickListener() {
		  					    public void onClick(DialogInterface dialog,int id) {
		  						dialog.cancel();
		  					    }
		  					}).create().show();					
		  				}
		  		    })
		  			.setPositiveButton("Touch:LCD",new DialogInterface.OnClickListener() {

		  				public void onClick(DialogInterface arg0, int arg1) {
		  					LayoutInflater li = LayoutInflater.from(mContext);
		  					View promptsView = li.inflate(R.layout.prompts, null);
		  	 
		  					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		  	 
		  					// set prompts.xml to alertdialog builder
		  					alertDialogBuilder.setView(promptsView).setTitle("User Defined Touch:LCD");
		  	 
		  					final EditText userInput = (EditText) promptsView
		  							.findViewById(R.id.userInputValue);
		  					alertDialogBuilder					
		  					.setPositiveButton("OK",
		  					  new DialogInterface.OnClickListener() {
		  					    public void onClick(DialogInterface dialog,int id) {
		  						// get user input and set it to result
		  						// edit text
		  						ratio=userInput.getText();
		  					    }
		  					  })
		  					.setNegativeButton("Cancel",
		  					  new DialogInterface.OnClickListener() {
		  					    public void onClick(DialogInterface dialog,int id) {
		  						dialog.cancel();
		  					    }
		  					  }).create().show();	
		  				}
		  			}).create().show();
		    	 }
	    }
		return true;
	}
	 
	
	 
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	mContext=this;
    	setContentView(R.layout.activity_main);  
    	   
    }

    public void onRadioButtonClicked(View view) {
        
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_main:
                if (checked){
                   logSelect=false;
                   fileExistflag=false;
                   parsefinishflag=false;							
                   parsestartedflag=false;
                   AutorunActivity.count=0;
                   com.main.logparser.AutorunActivity.count=0;
                }
                break;
            case R.id.radio_kernel:
                if (checked){
                	 logSelect=false;
                	fileExistflag=false;
                	parsefinishflag=false;							
                	parsestartedflag=false;
                	logSelect=true;
                	AutorunActivity.count=0;
                    com.main.logparser.AutorunActivity.count=0;
                }
                break;
        }
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
    }
    
    public void parsestep(View view) { 
    	repeat=0;
    	if(gToast!=null){
    		gToast.cancel();
    	}
    	    	
    	if(!parsestartedflag){
    		gToast=Toast.makeText(mContext," Please Parse/Reset the file status", Toast.LENGTH_LONG);
			gToast.show();
    	}
    	else{
    		 if(logSelect)
    		setContentView(new SingleTouchActivity(this, null));
    		 else
    			 setContentView(new com.main.logparser.SingleTouchActivity(this, null));
    	}
	}
    
    public void autorun(View view) { 
    	repeat=0;
    	if(gToast!=null){
    		gToast.cancel();
    	}
    	
    	if(!parsestartedflag){
    		gToast=Toast.makeText(mContext," Please Parse/Reset the file status", Toast.LENGTH_LONG);
			gToast.show();
    	}
    	else{
    		if (parsefinishflag){
    			if(logSelect)
    				setContentView(new AutorunActivity(this,null));
    			 else
        			 setContentView(new com.main.logparser.AutorunActivity(this, null));
        	}
    		else 
    			gToast=Toast.makeText(mContext," Please wait..Parsing in Process... :D", Toast.LENGTH_LONG);
    			gToast.show();
    	}
	}
    
    public void parse(View view) {
    	parsefinishflag=false;
    	parsestartedflag=true;
    	if(z!=null)
    	 z=null;
    	if(ratio!=null)
    	 ratio=null;
    	if(rate!=null)
    	 rate=null;
    	 
    	if(gToast!=null){
    		gToast.cancel();
    	}
    	gToast=Toast.makeText(mContext," Parsing Started...", Toast.LENGTH_LONG);
    	gToast.show();
    	
    	parseTask task=new parseTask();
    	task.execute();
    	
  	}
    
    public void parsefull(View view) {
    	if(gToast!=null){
    		gToast.cancel();
    	}
    	
    	if(!parsestartedflag){
    		gToast=Toast.makeText(mContext," Please Parse/Reset the file status", Toast.LENGTH_LONG);
			gToast.show();
    	}
    	else{
    		if (parsefinishflag){
    			if(logSelect)
    				setContentView(new CompleteTouchActivity(this,null));
    			else
        			setContentView(new com.main.logparser.CompleteTouchActivity(this, null));
        	}
    		else 
    			gToast=Toast.makeText(mContext," Please wait..Parsing in Process... :D", Toast.LENGTH_LONG);
    			gToast.show();
    	}
    		
  	}    

    
    public void parsegroup(View view) {  
    	if(gToast!=null){
    		gToast.cancel();
    	}
    	
    	if(!parsestartedflag){
    		gToast=Toast.makeText(mContext," Please Parse/Reset the file status", Toast.LENGTH_LONG);
			gToast.show();
    	}
    	else{
    		if(logSelect)
    			setContentView(new GroupedTouchActivity(this, null));
    		else
    			setContentView(new com.main.logparser.GroupedTouchActivity(this, null));
    	}
	}
    
    private class parseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
        
        if(logSelect)
        	new parser(mContext,null);
        else
			new com.main.logparser.parser(mContext, null);
        
         /* try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }*/
          return null;
        }

        @SuppressLint("ShowToast")
		@Override
        protected void onPostExecute(String result) {
        	
        	if(gToast!=null){
        		gToast.cancel();
        	}
        	if(!fileExistflag)
        		gToast=Toast.makeText(mContext,"File Not found", Toast.LENGTH_LONG);
        	else
        		gToast=Toast.makeText(mContext," Log Parsing Finished...", Toast.LENGTH_LONG);
        	
        	gToast.show();
        	
        	parsefinishflag=true;
          /*menuItem.collapseActionView();
          menuItem.setActionView(null);*/
        }
      };
    
    
}