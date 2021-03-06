package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIKME_ID = "crime_id";
    //09-03-15 Added for time challenge
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_DATE = "DialogDate";
    //10-07-15 Ch 16 Challenge #1
    private static final String DIALOG_ZOON = "DialogZoom";
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
    //09-11-15 Added for Ch 12 Challenge 1 delete
    private static final int REQUEST_DELETE = 3;
    //09-30-15 Added for Ch 15 Implicit Intents
    private Button mReportButton;
    private static final int REQUEST_CONTACT = 4;
    private Button mSuspectButton;
    //10-01/-15 Added for Ch 15 Challenge #2 phone suspect
    private static final int REQUEST_SUSPECT_PHONE = 5;
    private Button mSuspectPhoneButton;
    private static final int REQUEST_CALL_SUSPECT = 6;
    private Button mCallSuspectButton;
    //10-04-15 Added for Ch 16 Taking Pictures with intents
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private static final int REQUEST_PHOTO = 7;
    //10-07-15 Ch 16 Challenge #1 photo zoom
    private static final int REQUEST_PHOTO_ZOOM = 8;
    //10-08-15 Ch 16 Challenge #2 Global Layout listener
    private int photoLayoutWidth = 0;
    private int photoLayoutHeight = 0;
    //10-10-15 Ch 17 Tablet/Phone support Callbacks fragment/activity
    private Callbacks mCallbacks;

    //10-10-15 Ch 17 Tablet/Phone support Callbacks fragment/activity
    //Required interface for hosting activities
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    //Create bundle arguments for the fragment
    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIKME_ID, crimeId);
        //Log.d(TAG, "Current crimeId: " + crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    //The new version of this uses context and when
    //done with Ch 17 may want to try to convert it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //9-11-15 Added Ch 12 Challenge 1
        setHasOptionsMenu(true);

        //Retrieve extra using intent with argument bundle to get a crime detail object
        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIKME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        //10-04-15 Added for Ch 16 Taking Pictures with intents
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);

    }

    //9-14-15 Added Ch 13 SQ:ote database usage
    @Override
    public void onPause() {
        super.onPause();;

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    //10-10-15 Ch 17 Tablet/Phone support Callbacks fragment and activity
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
                //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                //Update when text is changed in title
                updateCrime();
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
                //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                //Update when text is changed in title
                updateCrime();
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
        //Log.d(TAG, "formatedTime: " + formatedTime);
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
                } catch (Exception ex) {
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
                //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                //Update when text is changed in title
                updateCrime();
            }
        });

        //09-30-15 Added for Ch 15 Implicit Intents

        mReportButton = (Button)  v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //10-01-15 Ch 15 Challenge #1 using ShareCompat.IntentBuilider
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder
                        .from(getActivity());
                builder.setType("text/plain");
                builder.setSubject(getString(R.string.crime_report_subject));
                builder.setText(getCrimeReport());
                builder.startChooser();
                //Intent i = new Intent(Intent.ACTION_SEND);
                //i.setType("text/plain");
                //i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                //i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                //i = Intent.createChooser(i, getString(R.string.send_report));
                //startActivity(i);
            }
        });

        //Getting content from the ContactsContact.Contact database
        final Intent pickContentContact =
                new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //The code below used for test only to trigger the PackagManager check to fail - disable button
        //pickContentContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContentContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        //10-01-15 Ch 15 Challenge #2 Call the suspect
        //Getting content from the ContactsContact.Contact database
        final Intent pickContentPhone =
                new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        //The code below used for test only to trigger the PackagManager check to fail - disable button
        //pickContentContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectPhoneButton = (Button) v.findViewById(R.id.suspect_phone);
        mSuspectPhoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContentPhone, REQUEST_SUSPECT_PHONE);
            }
        });

        if (mCrime.getSuspectPhone() != null) {
            mSuspectPhoneButton.setText(mCrime.getSuspectPhone());
        }

        //10-03-15 Ch 15 Challenge #2 Call the suspect
        mCallSuspectButton = (Button)  v.findViewById(R.id.call_suspect);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + mCrime.getSuspectPhone()));
            startActivity(i);
            }
        });

        //If the users device does not have a contact option disable the suspect button
        //to avoid an app crash
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContentContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        //If the users device does not have a phone option disable the
        // call suspect button to avoid an app crash
        if (packageManager.resolveActivity(pickContentPhone,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectPhoneButton.setEnabled(false);
        }

        //10-04-15 Added for Ch 16 Taking Pictures w/ Intents
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivityForResult(captureImage, REQUEST_PHOTO);
                }
        });

        mPhotoView = (ImageView)  v.findViewById(R.id.crime_photo);
        updatePhotoView();

        //Ch 16 Challenge #1
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                PhotoZoomFragment dialog = PhotoZoomFragment
                        .newInstance(mPhotoFile);
                //Set the CrimeFragment to be the target of the PhotoZoomFragment
                //That way we can get zoom on our suspect photo
                try {
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_PHOTO_ZOOM);
                    dialog.show(manager, DIALOG_ZOON);
                } catch (Exception ex) {
                    Log.d(TAG, "Error creating dialog : " + ex.getMessage());
                }
            }
        });

        //Return the view
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
            String formatedDate = DateFormat.format("EEEE , MMM dd yyyy", mCrime.getDate()).toString();
            //updateDate(mCrime.getDate().toString());
            //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
            //Update when text is changed in title
            updateCrime();
            updateDate(formatedDate);
        }

        //09-03-15 added for time challenge
        if(requestCode == REQUEST_TIME) {
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            String formatedTime = DateFormat.format("HH:mm", mCrime.getTime()).toString();
            //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
            //Update when text is changed in title
            updateCrime();
            updateTime(formatedTime);
        }

        //09-30-15 added for Ch 15 Implied Intents
        if (requestCode != REQUEST_DATE && requestCode != REQUEST_TIME) {
            if (requestCode == REQUEST_CONTACT && data != null) {
                Uri contactUri = data.getData();
                //Specify which values you want your query to return values for
                String[] queryFields = new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts._ID
                };
                //Perform your query - the contactUri is like a 'where'
                //clause here
                Cursor c = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
                try {
                    //Check that you actually got results
                    if (c.getCount() == 0 ) {
                        return;
                    }
                    //Extract the first column of the first row of data
                    //which is your suspect's name
                    c.moveToFirst();
                    String suspect = c.getString(0);
                    //10-01-15 - Ch 15 Challenge #2 get suspect ID to use in suspect phone query later
                    mCrime.setSuspectID(c.getString(1));
                    mCrime.setSuspect(suspect);
                    //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                    //Update when text is changed in title
                    updateCrime();
                    mSuspectButton.setText(suspect);
                }
                catch (Exception ex) {
                    Log.d(TAG, "onActivityResult Contact Error using content resolver : " + ex.getMessage());
                }
                finally {
                    c.close();
                }

            }
        }

        //10-01-15 added for Ch 15 Implied Intents, Challenge #2 call suspect
        if (requestCode != REQUEST_DATE
            && requestCode != REQUEST_TIME
            && requestCode != REQUEST_CONTACT) {
            if (requestCode == REQUEST_SUSPECT_PHONE && data != null) {
                Uri contactUri = data.getData();
                //Specify which values you want your query to return values for
                String[] queryFields = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                //Perform your query - the contactUri is like a 'where'
                //clause here
                Cursor c = getActivity().getContentResolver()
                        .query(contactUri, queryFields,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{mCrime.getSuspectID()},
                                 null);
                try {
                    //Check that you actually got results
                    if (c.getCount() == 0 ) {
                        return;
                    }
                    //Extract the first column of the first row of data
                    //which is your suspect's phone number
                    c.moveToFirst();
                    String suspectphone = c.getString(0);
                    mCrime.setSuspectPhone(suspectphone);
                    //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                    //Update when text is changed in title
                    updateCrime();
                    mSuspectPhoneButton.setText(suspectphone);
                }
                catch (Exception ex) {
                    Log.d(TAG, "onActivityResult Call Suspect Error using content resolver : " + ex.getMessage());
                }
                finally {
                    c.close();
                }
            }
        }

        //10-04-15 added for Ch 16 Taking pictures with  Implied Intents
        if (requestCode != REQUEST_DATE
                && requestCode != REQUEST_TIME
                && requestCode != REQUEST_CONTACT
                && requestCode != REQUEST_SUSPECT_PHONE) {
            if (requestCode == REQUEST_PHOTO) {
                //10-10-15 Ch 17 Table/Phone support Callbacks fragment/activity
                //Update when text is changed in title
                updateCrime();
                //Ch 16 Challenge #2 will move when this happens to a listener
                //was not done. Never did understand how to do this or why
                //you have to use a view tree to get the phot object size
                updatePhotoView();
            }
        }

    }

    private void updateDate(String text) {
        mDateButton.setText(text);
    }

    private void updateTime(String text) {
        mTimeButton.setText(text);
    }

    //9-11-15 Added Ch 12 Challenge 1
    //Handle the creation of the menu when a callback is done
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                //Log.d(TAG, "started menu_item_delete_crime.");
                //Get the persisted id of the crime
                UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIKME_ID);
                //Remove the crime
                CrimeLab.get(getActivity()).deleteCrime(crimeId);
                //Start a new intent back to the CrimeListActivity
                Intent intent = CrimeListActivity
                            .newIntent(getActivity());
                //This will clear any previous
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //09-30-15 Added for Ch 15 Implicit Intents
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(),
                dateString, solvedString, suspect);

        return report;

    }

    //10-04-15 Added Ch 16 Taking pictures w/ intents
    private void updatePhotoView () {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //10-10-16 CH 17 Tablet/Phone support Callbacks fragment/activity
    //Update crime via Callbacks
    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

}
