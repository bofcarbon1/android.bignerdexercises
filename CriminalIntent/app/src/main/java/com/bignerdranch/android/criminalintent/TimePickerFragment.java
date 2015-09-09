package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment  extends DialogFragment {

    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME =
            "com.bignerdranch.android.criminalintent.time";
    private static final String TAG = "TimePickerFragment";

    //The newInstance is a  type of static factory method
    //allowing us to initialize and setup a new Fragment without
    // having to call its constructor and additional setter methods.
    //We use it to stash a time in a bundle to pass between fragments
    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        Log.d(TAG, "Created fragment for time picker: ");
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        Log.d(TAG, "time : " + time);
        int hour = 0;
        int minute = 0;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            hour = calendar.get(calendar.HOUR);
            Log.d(TAG, "hour : " + hour);
            minute = calendar.get(calendar.MINUTE);
            Log.d(TAG, "minute : " + minute);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Error getting hour/minutes : " + ex.getMessage());
        }

        View v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog_time, null);

        try {
            mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
            mTimePicker.setIs24HourView(true);
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Error setting mTimePicker : " + ex.getMessage());
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick started. ");
                                try {
                                    int hour = mTimePicker.getCurrentHour();
                                    int minute = mTimePicker.getCurrentMinute();
                                    //Build the time string and return it to the dialog
                                    Date time = new GregorianCalendar(0, 0, 0, hour, minute).getTime();
                                    sendResult(Activity.RESULT_OK, time);
                                } catch (Exception ex) {
                                    Log.d(TAG, "Error creating view : " + ex.getMessage());
                                }
                            }
                        })

                .create();

    }

    private void sendResult(int resultCode, Date time) {

        if (getTargetFragment() == null) {
            Log.d(TAG, "getTargetFragment was null.");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        Log.d(TAG, "time: " + time);
        try {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Error getTargetFragment message is : " + ex.getMessage());
        }
        Log.d(TAG, "getTargetFragment executed. " );
    }
}

