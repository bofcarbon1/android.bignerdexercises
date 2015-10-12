package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity
    implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    private static final String TAG = "CrimeListActivity";

    //9-11-15 Added for Ch 12 Challenge 1 - delete and return to list
    ///Create an intent to use to target an activity to get back to the list activity
    public static Intent newIntent(Context packagContext) {
        Intent intent = new Intent(packagContext, CrimeListActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    //10-10-15 Ch 17 Table/Phone support
    @Override
    protected int getLayoutResId() {
        Log.d(TAG, "getLayoutResId started");
        //return R.layout.activity_twopane;
        //Use the reference alias refs.xml to resolve layout
        return R.layout.activity_masterdetail;
    }

    //10-10-15 Ch 17 Table/Phone support Callbacks fragment to activity
    //Determine which fragment to put in the detail_fragment_container
    //based on the devise tablet or phone
    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null ) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    //10-10-15 Ch 17 Table/Phone support Callbacks fragment to activity
    //When one of the fragments had a crime modified on tablet or phone
    // based on the detail_fragment_container update the database
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment
                = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();;
    }

}
