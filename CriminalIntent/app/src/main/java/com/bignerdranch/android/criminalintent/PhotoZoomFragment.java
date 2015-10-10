package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URL;

public class PhotoZoomFragment extends DialogFragment {

    private static final String TAG = "PhotoZoomFragment";
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final String ARG_FILE = "file";
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    //The newInstance is a  type of static factory method
    //allowing us to initialize and setup a new Fragment without
    // having to call its constructor and additional setter methods.
    //We use it here for I don't know why perhaps a photo image or not
    public static PhotoZoomFragment newInstance(File file) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE, file);

        PhotoZoomFragment fragment = new PhotoZoomFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.fragment_zoom_photo, null);

        mPhotoView = (ImageView) v.findViewById(R.id.zoom_photo);
        mPhotoFile = (File) getArguments().getSerializable(ARG_FILE); // CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        Log.d(TAG, "mPhotoFile: " + mPhotoFile);

        updatePhotoView();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setCancelable(true)
                .setTitle(R.string.pinch_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .create();
    }

    //10-04-15 Added Ch 16 Taking pictures w/ intents
    private void updatePhotoView() {
        Log.d(TAG, "PhotoZoomFragment : updatePhotoView started");
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            Log.d(TAG, "mPhotoFile was null or does not exist: " + mPhotoFile);
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}



