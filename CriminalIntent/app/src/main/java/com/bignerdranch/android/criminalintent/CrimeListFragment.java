package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final String TAG = "CrimeListFragment";
    private static final String ARG_CRIKME_ID = "crime_id";
    private int mCurrentPosition = 0;
    private static final String KEY_INDEX = "index";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Create view using layout, parent view group and not including in the parent layout
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ) );

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //Realod the list of crimes in RecyclerView
    private void updateUI () {

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
            mAdapter.notifyItemChanged(mCurrentPosition);
        }

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
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //Listen for a click on any of the list items then
        //start the crime activity (crime details)
        @Override
        public void onClick(View v) {

            //08-29-15 modified to use CrimePagerActivity to page through crimes
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            mCurrentPosition = getLayoutPosition();
            //Log.d(TAG, "position: " + mCurrentPosition);
            startActivity(intent);

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
        public int getItemCount() {
            return mCrimes.size();
        }


    }


}
