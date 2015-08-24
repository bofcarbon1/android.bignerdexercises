package com.bignerdranch.android.criminalintent;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
//import android.os.Bundle;

public class CrimeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        //Work with the fragment hosted by this activity via the fragment manager
        FragmentManager fm = getSupportFragmentManager();
        //Get the fragment refereneced in activity_crime.xml from the fragment manager
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        //If the fragment does not exist add it to the fragment manager
        if (fragment == null) {
           fragment = new CrimeFragment();
           fm.beginTransaction()
                   .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


    }


}
