package com.example.logparser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		    	setContentView(R.layout.activity_main);
		        return true;
		    }
		     
		    return true;
	}
	 
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	 Button btn1 = (Button) findViewById(R.id.button3);
    	    btn1.setOnClickListener(new OnClickListener() {

    	        @Override
    	        public void onClick(View v) {
    	        	 Intent intent = new Intent();
    	             setResult(RESULT_OK, intent);
    	             finish();
    	             return ;
    	        }
    	    });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
    }
    
    public void parsestep(View view) {      
    	setContentView(new SingleTouchActivity(this, null));
	}
    
    public void parse(View view) {
    	setContentView(new parser(this,null));
  	}
    
    public void parsefull(View view) {
    	setContentView(new CompleteTouchActivity(this,null));
  	}    

    public void parsegroup(View view) {      
    	setContentView(new GroupedTouchActivity(this, null));
	}
    
}