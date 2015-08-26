package com.bignerdranch.android.criminalintent;

//import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;

public class CrimeActivity extends SingleFragmentActivity {

    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_fragment);

    //    //Work with the fragment hosted by this activity via the fragment manager
    //    FragmentManager fm = getSupportFragmentManager();
    //    //Get the fragment refereneced in activity_fragmentent.xml from the fragment manager
    //    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
    //    //If the fragment does not exist add it to the fragment manager
    //    if (fragment == null) {
    //       fragment = new CrimeFragment();
    //       fm.beginTransaction()
    //               .add(R.id.fragmentContainer, fragment)
    //                .commit();
    //    }

    //}
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
