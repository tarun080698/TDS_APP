package com.example.tds.Model;

import java.util.Date;

public class Data {
    private String mName, mDesc, mId, mDate;

    public Data(){}

    public Data(String mName, String mDesc, String mId, String mDate) {
        this.mName = mName;
        this.mDesc = mDesc;
        this.mId = mId;
        this.mDate = mDate;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
