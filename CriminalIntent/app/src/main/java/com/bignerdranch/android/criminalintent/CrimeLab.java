package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//CrimeLab works at the model level with fragments
//and fragment adapters to manage the crime data
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    private static final String TAG = "CrimeLab";

    //Add a new crime
    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    //9-11-15 Added Ch 12 Challenge 1
    //Delete a crime finding the index that
    //matches the id of the crime delete request
    public void deleteCrime(UUID id) {
        //Log.d(TAG, "started deleteCrime. " );
        for (int i = 0; i < mCrimes.size(); i++) {
            if(mCrimes.get(i).getId().equals(id)) {
                //Remove the crime object from the List object
                mCrimes.remove(i);
                break;
            }
        }
    }

    //This static usage makes it a singleton class
    //it gets instantiated once per session
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        //for(int i = 0; i < 100; i++) {
        //    Crime crime = new Crime();
        //    crime.setTitle("Crime #" + i);
        //    crime.setSolved(i % 2 == 0 ); // every onther one
        //    mCrimes.add(crime);
        //}
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {

        for (Crime crime : mCrimes) {
            if(crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
