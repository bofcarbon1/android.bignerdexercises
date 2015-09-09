package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIKME_ID = "crime_id";
    //09-03-15 Added for time challenge
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_DATE = "DialogDate";
    private Crime mCrime;
    private EditText mTitleField;
    private EditText mDetailField;
    private Button mDateButton;
    //09-03-15 Added for time challenge
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private static final String TAG = "CrimeFragment";
    private static final int REQUEST_DATE = 0;
    //09-03-15 Added for time challenge
    private static final int REQUEST_TIME = 1;

    //Create bundle arguments for the fragment
    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIKME_ID, crimeId);
        Log.d(TAG, "Current crimeId: " + crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Retrieve extra using intent with argument bundle to get a crime detail object
        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIKME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Create view using layout, parent view group and not including in the parent layout
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        //Create the edit text variable and event handlers

        //Crime Title
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TBD
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TBD
            }
        });

        //Crime Details
        mDetailField = (EditText)v.findViewById(R.id.crime_detail);
        mDetailField.setText(mCrime.getDetail());
        mDetailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TBD
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TBD
            }
        });

        //Crime Date
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        //08-24-15 Added for challenge one to convert date time stamp to simple date
        String formatedDate = DateFormat.format("EEEE , MMM dd yyyy", mCrime.getDate()).toString();
        //Log.d(TAG, "formatedDate: " + formatedDate);
        //mDateButton.setText(mCrime.getDate().toString());
        updateDate(formatedDate);
        //08-29-15 Added listener to hanlde FragmentManager with dialog and date picker
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                //Set the CrimeFragment to be the target of the DatePickerFragment
                //That way we can get the date from the date picker in that fragment
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //09-03-15 Added Crime Time
        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        String formatedTime = DateFormat.format("HH:mm", mCrime.getTime()).toString();
        Log.d(TAG, "formatedTime: " + formatedTime);
        updateTime(formatedTime);
        //Lstener to handle FragmentManager with dialog and time picker
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getTime());
                //Set the CrimeFragment to be the target of the TimePickerFragment
                //That way we can get the time from the time picker in that fragment
                try {
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    dialog.show(manager, DIALOG_TIME);
                }
                catch (Exception ex)
                {
                    Log.d(TAG, "Error creating dialog : " + ex.getMessage());
                }
            }
        });

        //Crime Solved check box
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "Started onActivityResult. ");

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate(mCrime.getDate().toString());
        }

        //09-03-15 added for time challenge
        if(requestCode == REQUEST_TIME) {
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            String formatedTime = DateFormat.format("HH:mm", mCrime.getTime()).toString();
            //updateTime(mCrime.getTime().toString());
            updateTime(formatedTime);
        }

    }

    private void updateDate(String text) {
        mDateButton.setText(text);
    }

    private void updateTime(String text) {
        mTimeButton.setText(text);
    }

}
