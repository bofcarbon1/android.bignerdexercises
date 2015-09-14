package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

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

}
