package org.gandhar.wallshuffle;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

 
public class MainActivity extends Activity {
     
     
    public static String TAG = "wallshuffle";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
    
        ArrayList<String> wallpapers;

        
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()).isEmpty()){ 
        	wallpapers = new ArrayList<String>();
            Log.d(TAG,"array created, first run?");
        } 
        else {
        	wallpapers = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));
        	Log.d(TAG,"loaded old list of walls");
        }
        
        Log.d(TAG,"current wallpapers"+wallpapers.size());
        Log.d(TAG, "started");
        
        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction()) && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
    	    ArrayList<Parcelable> list = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
    	    
    	    for (Parcelable parcel : list) {
    	    	Uri uri = (Uri) parcel;
    	    	String sourcepath=getPath(uri);
    	    	if(wallpapers.contains(sourcepath))
    	    		continue;
    	    	else
    	    		wallpapers.add(sourcepath);
    	    }       
        }
        
        Log.d(TAG,"current wallpapers"+wallpapers.size());
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("SAVEDATA", new HashSet<String>(wallpapers));
        edit.commit();
        
        ArrayList<String> retrieved = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));
        Log.d(TAG,retrieved.toString());
        
        GridView gv = (GridView) findViewById(R.id.gridView1);
        gv.setAdapter(new SampleGridViewAdapter(this));
        
        
        Button button = (Button) findViewById(R.id.button1);
        
        if(isMyServiceRunning())
        	button.setText("Stop Service");
        else
        	button.setText("Start Service");
        
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button button = (Button) findViewById(R.id.button1);
				
				Calendar cal = Calendar.getInstance();
		        Intent intent = new Intent(getApplicationContext(), WallChangerService.class);
		        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
		        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		        
		        if(isMyServiceRunning()){
		        	alarm.cancel(pintent);
		        	button.setText("Start Service");
		        	Log.d(TAG,"service stopped");
		        }
		        else{
		        	alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 2*60*1000, pintent); 
		        	button.setText("Stop Service");
		        	Log.d(TAG,"service started");
		        }
		        
			}
 
		});
       
  	}
    
    
	public  String getPath(Uri selectedImage) {
    	String[] filePathColumn = { MediaStore.Images.Media.DATA };
    	Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
    	cursor.moveToFirst();
    	int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    	String picturePath = cursor.getString(columnIndex);
    	cursor.close();
    	return picturePath;
    	
    }
        
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (WallChangerService.class.getName().equals(service.service.getClassName())){
	        	Log.d(TAG, "service is running");
	        	return true;
	        }
	    }
	    Log.d(TAG, "service is not rnning");
		return false;
	}
}