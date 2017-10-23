package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/9/24.
 */

class ResHouseTitleInfo implements IApiResults.IHouseTitleInfo {

    private int     mHouseId        = 0;
    private String  mProperty       = null;
    private String  mBuildingNo     = null;
    private String  mHouseNo        = null;
    private int     mLivingrooms    = 0;
    private int     mBedroomgs      = 0;
    private int     mBathrooms      = 0;

    public String DebugString() {
        return String.format("House: %d %s %s栋 %s室, %d室 %d厅 %d卫",
                            HouseId(), Property(), BuildingNo(), HouseNo(), Bedrooms(), Livingrooms(), Bathrooms());
    }

    public int parse(JSONObject obj) {
        try {
            mHouseId        = obj.getInt("HouseId");
            mProperty       = obj.getString("Property");
            mBuildingNo     = obj.getString("BuildingNo");
            mHouseNo        = obj.getString("HouseNo");
            mLivingrooms    = obj.getInt("Livingrooms");
            mBedroomgs      = obj.getInt("Bedrooms");
            mBathrooms      = obj.getInt("Bathrooms");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    @Override
    public int HouseId() {
        return mHouseId;
    }

    @Override
    public String Property() {
        return mProperty;
    }

    @Override
    public String BuildingNo() {
        return mBuildingNo;
    }

    @Override
    public String HouseNo() {
        return mHouseNo;
    }

    @Override
    public int Livingrooms() {
        return mLivingrooms;
    }

    @Override
    public int Bedrooms() {
        return mBedroomgs;
    }

    @Override
    public int Bathrooms() {
        return mBathrooms;
    }
}
