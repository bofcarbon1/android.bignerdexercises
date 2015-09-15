package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeCursorWrapper;
import database.CrimeDbSchema;
import database.CrimeDbSchema.CrimeTable;

//CrimeLab works at the model level with fragments
//and fragment adapters to manage the crime data
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    //9-14-15 Changed to use SQLite
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "CrimeLab";

    //9-14-15 Changed to use SQLite
    //Query the database, convert using cursor wrapper then return as object
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query (
                CrimeTable.NAME,
                null, // Columns - selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //havingBy
                null  //orderBy
            );

        return new CrimeCursorWrapper(cursor);
    }

   //This static usage makes it a singleton class
    //it gets instantiated once per session
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime) {

        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();
        //9-14-15 Changed to use SQLite
        //mCrimes = new ArrayList<>();

        //for(int i = 0; i < 100; i++) {
        //    Crime crime = new Crime();
        //    crime.setTitle("Crime #" + i);
        //    crime.setSolved(i % 2 == 0 ); // every onther one
        //    mCrimes.add(crime);
        //}
    }

    public List<Crime> getCrimes() {
        //9-14-15 Changed to use SQLite
        //return mCrimes;
        //return new ArrayList<>();

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {

        //9-14-15 Changed to use SQLite
        //for (Crime crime : mCrimes) {
        //    if(crime.getId().equals(id)) {
        //        return crime;
        //    }
        //}

        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
            );

        try {
            if(cursor.getCount() == 0 ) {
                return null;
            }

            cursor.moveToFirst();

            return cursor.getCrime();

        } finally {
            cursor.close();
        }

    }

    //Add a new crime
    public void addCrime(Crime c) {
        //9-14-15 Changed to use SQLite
        //mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    //9-14-15 Changed to use SQLite
    public void updateCrime(Crime crime) {

        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]  {uuidString});

    }

    //9-11-15 Added Ch 12 Challenge 1
    //Delete a crime finding the index that
    //matches the id of the crime delete request
    public void deleteCrime(UUID id) {
        //Log.d(TAG, "started deleteCrime. " );
        //9-14-15 Changed to use SQLite
        String uuidString = id.toString();
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});

        //for (int i = 0; i < mCrimes.size(); i++) {
        //    if(mCrimes.get(i).getId().equals(id)) {
        //        //Remove the crime object from the List object
        //        mCrimes.remove(i);
        //        break;
        //    }
        //}
    }

}
