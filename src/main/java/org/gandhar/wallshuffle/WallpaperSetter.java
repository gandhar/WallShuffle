package org.gandhar.wallshuffle;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class WallpaperSetter extends IntentService {

    public static String TAG = "wallshuffle";

    public WallpaperSetter(){
        super("WallpaperSetter");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        int position = workIntent.getIntExtra("POSITION",0);
        Util.setWallpaper(position,getBaseContext());
        Log.d(TAG, "wallpaper set");
        Toast.makeText(getBaseContext(),"Wallpaper Changed",Toast.LENGTH_LONG).show();
    }
}