package org.gandhar.wallshuffle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		
		WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
		ArrayList<String> retrieved = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("SAVEDATA", new HashSet<String>()));
		
		Random r = new Random();
    	int shuffle = r.nextInt(retrieved.size()-1);

    	Log.d(TAG, ""+shuffle);
		
		try {
			Bitmap bm = decodeFile(retrieved.get(shuffle));
			myWallpaperManager.setBitmap(bm);
			bm.recycle();
			bm=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Service.START_STICKY;
        
	}

private Bitmap decodeFile(String s) throws IOException{
    Bitmap b = null;

    BitmapFactory.Options o = new BitmapFactory.Options();
    o.inJustDecodeBounds = true;

    FileInputStream fis = new FileInputStream(s);
    BitmapFactory.decodeStream(fis, null, o);
    fis.close();
    
    int IMAGE_MAX_SIZE = Math.max(WallpaperManager.getInstance(getApplicationContext()).getDesiredMinimumHeight(),WallpaperManager.getInstance(getApplicationContext()).getDesiredMinimumHeight());
    int scale = 1;
    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
        scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / 
           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
    }
    
    Log.d(TAG, ""+scale);
    BitmapFactory.Options o2 = new BitmapFactory.Options();
    o2.inSampleSize = scale;
    o2.inPurgeable = true;
    fis = new FileInputStream(s);
    b = BitmapFactory.decodeStream(fis, null, o2);
    fis.close();

    return b;
}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}