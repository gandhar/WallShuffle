package org.gandhar.wallshuffle;

import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
	

    public static String TAG = "wallshuffle";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		Log.d(TAG, "something changed" + arg1);
		
		boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("com.my.package.MY_UNIQUE_ACTION"),PendingIntent.FLAG_NO_CREATE) != null);

		if (alarmUp)
		{
		    Log.d("myTag", "Alarm is already active");
		}
		
		/*
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	    	if (org.gandhar.wallshuffle.WallChangerService.class.equals(service.service.getClassName())){
	    		Calendar cal = Calendar.getInstance();
	    		Intent intent = new Intent(getApplicationContext(), WallChangerService.class);
	    		PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
	    		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    		int shuffle_time = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shuffle_duration", null));
	    		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), shuffle_time*60*1000, pintent);
	    		Log.d(TAG, "service restarted with "+shuffle_time);
	    	}
	    }*/
	}
    
}