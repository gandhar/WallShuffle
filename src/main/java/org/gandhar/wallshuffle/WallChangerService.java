package org.gandhar.wallshuffle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class WallChangerService extends Service {
	public static String TAG = "wallshuffle";
	
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		
		Thread t = new Thread("MyService(" + startId + ")") {
	         @Override
	         public void run() {
	             _onStartCommand(intent, flags, startId);
	             stopSelf();
	         }
	     };
	     t.start();
		return startId;
        
	}
	
public int _onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d(TAG, "change wallpaper");
		
		ArrayList<String> wallpapers = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));
		
		Random r = new Random();
    	int shuffle = r.nextInt(wallpapers.size()-1);
        Util.setWallpaper(shuffle,getBaseContext());
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}