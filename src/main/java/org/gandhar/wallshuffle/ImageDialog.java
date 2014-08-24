package org.gandhar.wallshuffle;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageDialog extends DialogFragment {

    public static String POSITIONINARRAY = "sourcepath";
    public static String TAG = "wallshuffle";

    public interface ImageDialogListener {
        public void refreshTheThing();
    }

    // Use this instance of the interface to deliver action events
    ImageDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ImageDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ImageDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog, null);
        builder.setView(v);

        ImageView imageView1 = (ImageView)v.findViewById(R.id.imageOptions);
        final int position = getArguments().getInt(POSITIONINARRAY);
        final String sourcepath = Util.getPath(position,getActivity());
        imageView1.setImageBitmap(BitmapFactory.decodeFile(sourcepath));

        Button setWall = (Button)v.findViewById(R.id.setWall);
        setWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"set wallpaper");
                Util.setWallpaper(sourcepath,getActivity());
                Toast.makeText(getActivity(), "wallpaper changed", Toast.LENGTH_LONG).show();
            }
        });

        Button removeWall = (Button)v.findViewById(R.id.removeWall);
        removeWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"set wallpaper");
                Util.deleteEntry(position, getActivity());
                mListener.refreshTheThing();
                if(Util.isMyServiceRunning(getActivity())){
                    Util.startAlarm(getActivity());
                }
                Toast.makeText(getActivity(), "removed", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        return builder.create();
    }



}