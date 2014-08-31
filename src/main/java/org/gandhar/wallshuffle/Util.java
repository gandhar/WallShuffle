package org.gandhar.wallshuffle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;


public class Util {

    public static String TAG = "wallshuffle";
    public static int PENDING_INTENT_ID = 1569;


    public static void setWallpaper(int position,Context context){

        final ArrayList<String> wallpapers;
        wallpapers = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("SAVEDATA", new HashSet<String>()));
        String path = wallpapers.get(position);

        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
        Bitmap bm= BitmapFactory.decodeFile(path);
        try {
            myWallpaperManager.setBitmap(bm);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("POSITION", position);
            edit.apply();

        } catch (IOException ioe) {
            Log.d(TAG,"errors" + ioe.getMessage());
        }
        bm.recycle();
        bm=null;
    }

    public static void deleteEntry(int position, Context context){
        Log.d(TAG, "delete entry");
        final ArrayList<String> wallpapers;
        wallpapers = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("SAVEDATA", new HashSet<String>()));
        wallpapers.remove(position);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("SAVEDATA", new HashSet<String>(wallpapers));
        edit.apply();

        Log.d(TAG,"removed"+wallpapers.get(position));
    }

    public static String getPath(int position,Context context){
        final ArrayList<String> wallpapers;
        wallpapers = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("SAVEDATA", new HashSet<String>()));
        return wallpapers.get(position);
    }


    public static void startAlarm(Context context){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(context, WallChangerService.class);
        PendingIntent pintent = PendingIntent.getService(context, PENDING_INTENT_ID, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        int shuffle_time = PreferenceManager.getDefaultSharedPreferences(context).getInt("shuffle_duration", 7200);
        Log.d(TAG,"alarm time is "+PreferenceManager.getDefaultSharedPreferences(context).getInt("shuffle_duration", 7200));
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), shuffle_time*1000, pintent);
    }

    public static void stopAlarm(Context context){
        Intent intent = new Intent(context, WallChangerService.class);
        PendingIntent pintent = PendingIntent.getService(context, PENDING_INTENT_ID, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
    }

    public static boolean isMyServiceRunning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("PENDING_INTENT_STATUS",false);
    }

    public static boolean isCurrent(int position, Context context){
        Log.d(TAG,"you removed the current wallpaper");
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("POSITION", 0)==position;
    }

}
