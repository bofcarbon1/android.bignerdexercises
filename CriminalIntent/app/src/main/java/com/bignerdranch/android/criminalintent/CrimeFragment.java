package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIKME_ID = "crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private EditText mDetailField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private static final String TAG = "CrimeFragment";

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

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        //08-24-15 Added for challenge one to convert date time stamp to simple date
        String formatedDate = DateFormat.format("EEEE , MMM dd yyyy", mCrime.getDate()).toString();
        //Log.d(TAG, "formatedDate: " + formatedDate);
        //mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setText(formatedDate);
        mDateButton.setEnabled(false);

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

}
