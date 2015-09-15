package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private String mDetail;
    private Date mDate;
    private Date mTime;
    private boolean mSolved;

    public Crime() {
        //Generate unique identifier
        //9-14-15 Changed to use SQLite
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
        //mDate = new Date();
        //mTime = new Date();
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
        mTime = new Date();
    }

    public String getTitle() {

        return mTitle;
    }

    public void setTitle(String title) {

        mTitle = title;
    }

    public UUID getId() {

        return mId;
    }

    public Date getDate() {

        return mDate;
    }

    public void setDate(Date date) {

        mDate = date;
    }

    public boolean isSolved() {

        return mSolved;
    }

    public void setSolved(boolean solved)
    {
        mSolved = solved;
    }

    public String getDetail()
    {
        return mDetail;
    }

    public void setDetail(String detail)
    {
        mDetail = detail;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time)
    {
        mTime = time;
    }

}
