package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/6/21.
 */

class ResGetHouseShowtime extends ResBase implements IApiResults.IHouseShowtime {
    private int     mHouse      = 0;
    private int     mPeriodW    = 0;
    private int     mPeriodV    = 0;
    private String  mDesc       = null;
    private String  mWho        = null;
    private String  mWhen       = null;

    ResGetHouseShowtime(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  House: " + HouseId() + "\n";
        mString += "  Period working day: " + PeriodW() + ", vacation: " + PeriodV() + "\n";
        mString += "  Desc: " + Desc() + "\n";
        mString += "  Who: " + Who() + "\n";
        mString += "  When: " + When() + "\n";

        return mString;
    }

    protected int parseResult(JSONObject obj) {

        try {
            mHouse      = obj.getInt("Id");
            mPeriodW    = obj.getInt("Period1");
            mPeriodV    = obj.getInt("Period2");
            mDesc       = obj.getString("Desc");
            mWho        = obj.getString("Who");
            mWhen       = obj.getString("When");

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int HouseId() {
        return mHouse;
    }

    @Override
    public int PeriodW() {
        return mPeriodW;
    }

    @Override
    public int PeriodV() {
        return mPeriodV;
    }

    @Override
    public String Desc() {
        return mDesc;
    }

    @Override
    public String Who() {
        return mWho;
    }

    @Override
    public String When() {
        return mWhen;
    }
}
