package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import database.CrimeDbSchema.CrimeTable;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";
    private static final String TAG = "CrimeBaseHelper";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase  db) {

        try {
            db.execSQL("create table " + CrimeTable.NAME + "(" +
                            " _id integer primary key autoincrement, " +
                            CrimeTable.Cols.UUID + ", " +
                            CrimeTable.Cols.TITLE + ", " +
                            CrimeTable.Cols.DATE + ", " +
                            CrimeTable.Cols.SOLVED +
                            ")"
            );
        }
        catch (Exception ex)
        {
            Log.d(TAG, "Error onCreate SQLiteDatabase : " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
