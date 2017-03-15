package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/3/14.
 */

public class HouseInfo {
    public int mHouseId = 0;
    public int mPropId = 0;
    public int mBuilding = 0;
    public String mHouseNo = "";
    public int mFloorTotal = 0;
    public int mFloorThis = 0;
    public int mLivingrooms = 0;
    public int mBedrooms = 0;
    public int mBathrooms = 0;
    public int mAcreage = 0;
    public boolean mForSale = false;
    public boolean mForRent = false;

    public HouseInfo() {
    }

    public HouseInfo(int hid, int pid, int bld, String hno, int ftotal, int fthis, int lr,
                     int bedr, int bathr, int acre, boolean sale, boolean rent) {
        mHouseId = hid;
        mPropId = pid;
        mBuilding = bld;
        mHouseNo = hno;
        mFloorTotal = ftotal;
        mFloorThis = fthis;
        mLivingrooms = lr;
        mBedrooms = bedr;
        mBathrooms = bathr;
        mAcreage = acre;
        mForSale = sale;
        mForRent = rent;
    }
}
