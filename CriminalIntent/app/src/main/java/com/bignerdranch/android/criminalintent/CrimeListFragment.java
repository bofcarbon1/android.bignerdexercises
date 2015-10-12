package com.bignerdranch.android.criminalintent;

import android.app.Activity;
//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;
//import java.util.UUID;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final String TAG = "CrimeListFragment";
    private static final String ARG_CRIKME_ID = "crime_id";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private int mCurrentPosition = 0;
    private static final String KEY_INDEX = "index";
    private boolean mSubtitleVisible;
    //10-10-15 Ch 17 Table/Phone Support Callbacks fragments to activity
    private Callbacks mCallbacks;

    //10-10-15 Ch 17 Table/Phone Support Callbacks fragments to activity
    //Required interface for hosting activities
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    //The new version of this uses context and when
    //done with Ch 17 may want to try to convert it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    //Tell fragment manager that fragment should receive a call to a menu on callback
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Create view using layout, parent view group and not including in the parent layout
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Get the persisted subtitle visible state before updating the view UI
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //10-10-15 Ch 17 Tablet/Phone support Callbacks fragment and activity
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //Handle the creation of the menu when a callback is done
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);

                //10-10-15 Ch 17 Tablet/Phone support & Callbacks fragment to activity
                //Intent intent = CrimePagerActivity
                //        .newIntent(getActivity(), crime.getId());
                //startActivity(intent);
                //Use the CallBacks to resolve the fragment/activity for the device UI
                updateUI();
                mCallbacks.onCrimeSelected(crime);

                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimelab = CrimeLab.get(getActivity());
        int crimeCount = crimelab.getCrimes().size();
        //9-13-15 Ch 13 - Challenge #2 adjust sub title based on size
        //String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
    //Made this method public so it could be invoked from CrimeListActivity
    //Realod the list of crimes in RecyclerView
    public void updateUI () {

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            //Load list of crimes through fragment adapter for first time
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            //Reload list of crimes for existing fragment adapter
            //08-27-15 Chapter 10 Challenge #1
            //Currently the statement below reloads all of crime objects in array
            //mAdapter.notifyDataSetChanged();
            //Reload the crime view but only for the position ID of the changed layout object

            //9-14-15 Ch 13 SQLite
            mAdapter.setCrimes(crimes);

            mAdapter.notifyItemChanged(mCurrentPosition);
        }

        //update the subtitle when returning back to list
        updateSubtitle();

    }

    //This is an inner class that implements a ViewHolder
    private class CrimeHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDetailTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder (View itemView) {

            super(itemView);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDetailTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_detail_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_checkbox);

        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDetailTextView.setText(mCrime.getDetail());
            String formatedDate = DateFormat.format("EEEE , MMM dd yyyy", mCrime.getDate()).toString();
            String formatedTime = DateFormat.format("HH:mm", mCrime.getTime()).toString();
            formatedDate = formatedDate + " @ " + formatedTime;
            updateDate(formatedDate);
            mDateTextView.setText(formatedDate);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //Replace the date text (used in formatting)
        private void updateDate(String text)  {
           mDateTextView.setText(text);
        }

        //Listen for a click on any of the list items then
        //start the crime activity (crime details)
        @Override
        public void onClick(View v) {

            //10-10-15 Ch 17 Tablet/Phone support & Callbacks fragment to activity
            //08-29-15 modified to use CrimePagerActivity to page through crimes
            //Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            //mCurrentPosition = getLayoutPosition();
            //Log.d(TAG, "position: " + mCurrentPosition);
            //startActivity(intent);
            //Use the CallBacks to resolve the fragment/activity listerner passing the crime obj
            mCallbacks.onCrimeSelected(mCrime);

        }

    }

    //This is an inner class that implements an Adapter for ViewHolder CrimeHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;
        private CrimeAdapter mAdapter;

        public CrimeAdapter (List<Crime> crimes) {

            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            //holder.mTitleTextView.setText(crime.getTitle());
            holder.bindCrime(crime);

        }

        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

    }

}
