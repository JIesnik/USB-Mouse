package com.sns.usbmouse;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.sns.usbmouse.PropActivity;
import com.sns.usbmouse.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MouseActivity extends Activity  {
    /** Called when the activity is first created. */
   // @Override

    Context parent;
    PowerManager.WakeLock wl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);      
        parent = getApplicationContext();
        wl = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    };
    
    public void onStart() {
    	super.onStart();
        setContentView(R.layout.main);
        
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("hideBar", true))
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showClock", true))
        	findViewById(R.id.dClock).setVisibility(0);
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("scrollBar", false))
        	findViewById(R.id.scrollBar).setVisibility(0);
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("showButtons", false)) {
        	findViewById(R.id.btnRMB).setVisibility(0);
        	findViewById(R.id.btnLMB).setVisibility(0);
        }
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("lowBright", true))
        	getWindow().getAttributes().screenBrightness = 0.1f;
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("noSleep", true)) 
        	wl.acquire();
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        FrameLayout ss = (FrameLayout) findViewById(R.id.scrollBar);
        
        switch(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("style", "1"))) {
        case 1:
        	ll.setBackgroundResource(R.drawable.tp_alu);
        	break;
        case 2:
    		ll.setBackgroundResource(R.drawable.tp_carbon);
    		break;
        }
        

        ll.setOnTouchListener(new View.OnTouchListener() {
            int x;
    		int y;
            boolean oldsecindex;
    		boolean md;
			public boolean onTouch(View v, MotionEvent event) {
        		int Action=event.getAction();
        		switch(Action){
        		case MotionEvent.ACTION_DOWN:
                	x = (int) event.getX();
                	y = (int) event.getY();
                	oldsecindex =  md = false;
                	break;
                case MotionEvent.ACTION_MOVE:
                	if(event.getPointerCount() == 2) md = true;
                	break;
                case MotionEvent.ACTION_UP:
                    if(!md) {
                    	if((event.getEventTime()-event.getDownTime()) < 100) Log.v("SNSUM", "SNSTAP");
                    }
                    else {
                    	if((event.getEventTime()-event.getDownTime()) < 100) Log.v("SNSUM", "SNSDTAP");
                    }
                	break;
        		}
        		if(event.getPointerCount() == 1) {
        			if((event.getEventTime()-event.getDownTime()) >= 100) {
        				if(!oldsecindex) {
        					Float k = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(parent).getString("kSwype", "1"));

        					Log.v("SNSUM", "SNSCOORD" + ":" + 
            					((int)((event.getX()-(x+event.getX())/2)*k) + ":" + ((int)((event.getY()-(y+event.getY())/2)*k))));
        				}
        				else {
        					oldsecindex = false;
        				}
        			}
        		}
        		if(event.getPointerCount() == 2) {
        			if((event.getEventTime()-event.getDownTime()) >= 100) {
        				Float k = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(parent).getString("kScroll", "1"));

        				Log.v("SNSUM", "SNSSCOORD" + ":" + ((int)((event.getY())-y)*k));
        				oldsecindex = true;
        			}
        		}
        		x = (int)event.getX();
        		y = (int)event.getY();
    		
        		return true;
        	}
        });
        
        ss.setOnTouchListener(new View.OnTouchListener() {
        	int x,y;
			public boolean onTouch(View v, MotionEvent event) {
			Float k = Float.parseFloat(PreferenceManager.getDefaultSharedPreferences(parent).getString("kScroll", "1"));

			Log.v("SNSUM", "SNSSCOORD" + ":" + ((int)((event.getY()-(y+event.getY())/2)*-k)));
    		x = (int)event.getX();
    		y = (int)event.getY();
			return true;
			}
        });
        
        Button lmb = (Button) findViewById(R.id.btnLMB);
        lmb.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	Log.v("SNSUM", "SNSTAP");
            }
        });
        
        Button rmb = (Button) findViewById(R.id.btnRMB);
        rmb.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	Log.v("SNSUM", "SNSDTAP");
            }
        });
    };
    
    public void onStop() {
    	super.onStop();
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("noSleep", true)) 
        	wl.release();
    }
   
    boolean pressed = false;
    public boolean onSearchRequested() {
    	if(pressed) {
    		Log.v("SNSUM", "SNSUP");
    		pressed = false;
    	}
    	else {
    		Log.v("SNSUM", "SNSDOWN");
    		pressed = true;
    	}
        return false;
     } 

	//////////////// ***MENU*** \\\\\\\\\\\\\\\\
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuProperties:
			startActivity(new Intent(MouseActivity.this, PropActivity.class));
			return true;
		case R.id.menuAbout:
			AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
			aboutDialog.setTitle(getString(R.string.about_title)).setMessage(getString(R.string.app_name) + " " + getString(R.string.app_version) + "\n\n� SNSteam, " + "2010 - " + (new SimpleDateFormat("yyyy").format(new Date()))).setNeutralButton(R.string.btn_ok, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) {dialog.cancel();}}).create().show();
			return true;
		case R.id.menuExit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}