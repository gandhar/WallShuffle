package org.gandhar.wallshuffle;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class SampleGridViewAdapter extends BaseAdapter {
  private final Context context;
  public static String TAG = "wallshuffle";
  
  
  public SampleGridViewAdapter(Context context) {
    this.context = context;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
	ArrayList<String> retrieved = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("SAVEDATA", new HashSet<String>()));
    SquaredImageView view = (SquaredImageView) convertView;
    if (view == null) {
      view = new SquaredImageView(context);
      view.setScaleType(CENTER_CROP);
    }

    File f = new File(retrieved.get(position));
    Picasso.with(context)
    .load(f)
    .fit()
    .centerCrop()
    .into(view);
    
    return view;
  }

  @Override public int getCount() {
	  ArrayList<String> retrieved = new ArrayList<String>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet("SAVEDATA", new HashSet<String>()));
    return retrieved.size();
  }

  @Override public long getItemId(int position) {
    return position;
  }

@Override
public Object getItem(int position) {
	// TODO Auto-generated method stub
	return null;
}
}