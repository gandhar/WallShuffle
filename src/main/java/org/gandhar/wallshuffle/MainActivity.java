package org.gandhar.wallshuffle;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;


public class MainActivity extends Activity {
     
     
    public static String TAG = "wallshuffle";
    public static int PENDING_INTENT_ID = 1569;
    private Menu menu;

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
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("PENDING_INTENT_STATUS",false);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        invalidateOptionsMenu();
	    return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("tshuffle_duration", R.id.t7200)).setChecked(true);

        MenuItem playmenuitem = menu.findItem(R.id.play);
        if(isMyServiceRunning()){
            playmenuitem.setIcon(R.drawable.ic_action_pause);
            playmenuitem.setTitle("Pause");
        }
        else{
            playmenuitem.setIcon(R.drawable.ic_action_play);
            playmenuitem.setTitle("Play");
        }

        return true;
    }


    public void startAlarm(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(getApplicationContext(), WallChangerService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int shuffle_time = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("shuffle_duration", 7200);
        Log.d(TAG,"alarm time is "+PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("shuffle_duration", 7200));
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), shuffle_time*1000, pintent);
    }

    public void stopAlarm(){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(getApplicationContext(), WallChangerService.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
    }


    public boolean changeTime(MenuItem item){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();

        switch (item.getItemId()) {
            case R.id.t900:
                edit.putInt("shuffle_duration", 900);
                edit.putInt("tshuffle_duration", R.id.t900);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t900).setChecked(true);
                return true;
            case R.id.t1800:
                edit.putInt("shuffle_duration", 1800);
                edit.putInt("tshuffle_duration", R.id.t1800);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t1800).setChecked(true);
                return true;
            case R.id.t3600:
                edit.putInt("shuffle_duration", 3600);
                edit.putInt("tshuffle_duration", R.id.t3600);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t3600).setChecked(true);
                return true;
            case R.id.t7200:
                edit.putInt("shuffle_duration", 7200);
                edit.putInt("tshuffle_duration", R.id.t7200);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t7200).setChecked(true);
                return true;
            case R.id.t14400:
                edit.putInt("shuffle_duration", 14400);
                edit.putInt("tshuffle_duration", R.id.t14400);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t14400).setChecked(true);
                return true;
            case R.id.t28800:
                edit.putInt("shuffle_duration", 28800);
                edit.putInt("tshuffle_duration", R.id.t28800);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t28800).setChecked(true);
                return true;
            case R.id.t43200:
                edit.putInt("shuffle_duration", 43200);
                edit.putInt("tshuffle_duration", R.id.t43200);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t43200).setChecked(true);
                return true;
            case R.id.t186400:
                edit.putInt("shuffle_duration", 186400);
                edit.putInt("tshuffle_duration", R.id.t186400);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t186400).setChecked(true);
                return true;
            case R.id.t172800:
                edit.putInt("shuffle_duration", 172800);
                edit.putInt("tshuffle_duration", R.id.t172800);
                edit.commit();
                unCheckAll();
                if (isMyServiceRunning())
                    startAlarm();
                menu.findItem(R.id.t172800).setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void unCheckAll(){
        menu.findItem(R.id.t900).setChecked(false);
        menu.findItem(R.id.t1800).setChecked(false);
        menu.findItem(R.id.t14400).setChecked(false);
        menu.findItem(R.id.t172800).setChecked(false);
        menu.findItem(R.id.t186400).setChecked(false);
        menu.findItem(R.id.t28800).setChecked(false);
        menu.findItem(R.id.t3600).setChecked(false);
        menu.findItem(R.id.t43200).setChecked(false);
        menu.findItem(R.id.t7200).setChecked(false);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
            case R.id.play:
                MenuItem playmenuitem = menu.findItem(R.id.play);

                if(isMyServiceRunning()){
                    stopAlarm();
                    playmenuitem.setIcon(R.drawable.ic_action_play);
                    Log.d(TAG,"service stopped");

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("PENDING_INTENT_STATUS",false);
                    edit.commit();
                }

                else{
                    startAlarm();
                    playmenuitem.setIcon(R.drawable.ic_action_pause);
                    Log.d(TAG,"service started");

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("PENDING_INTENT_STATUS",true);
                    edit.commit();

                }
                return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}