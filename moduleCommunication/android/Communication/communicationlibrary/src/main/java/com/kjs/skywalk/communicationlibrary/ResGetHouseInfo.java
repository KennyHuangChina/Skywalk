package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/3/1.
 */

class ResGetHouseInfo extends ResBase implements IApiResults.IGetHouseInfo {
    private int mHouseId;      // house id
    private int mProId;        // property id which the house belong to
    private int mBuildingNo;   // the building number the house belong to
    private int mFloorTotal;   // total floors
    private int mFloorThis;    // exact floor the house resident
    private String mHouseNo;   // exact house number. like house 1305#
    private int mBedrooms;     // how many bedrooms whitin house
    private int mLivingrooms;  // how many living rooms within house
    private int mBathrooms;    // how many bathrooms within house
    private int mAcreage;      // house acreage, 100x than real value. for example 11537 mean 115.37 m2

    ResGetHouseInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  hosue id: " + mHouseId + "\n";
        mString += "  property: " + mProId + "\n";
        mString += "  building: " + mBuildingNo + "\n";
        mString += "  floor: " + mFloorThis + "/" + mFloorTotal + "\n";
        mString += "  house no: " + mHouseNo + "\n";
        mString += "  bedrooms: " + mBedrooms + "\n";
        mString += "  living rooms: " + mLivingrooms + "\n";
        mString += "  bathrooms: " + mBathrooms + "\n";
        mString += "  acreage: " + mAcreage / 100 + "." + mAcreage % 100 + "\n";

        return mString;
    }

    protected int parseResult(JSONObject obj) {
        try {
            JSONObject jHouse = obj.getJSONObject("HouseInfo");

            mHouseId = jHouse.getInt("Id");
            mProId  = jHouse.getInt("Property");
            mBuildingNo = jHouse.getInt("BuildingNo");
            mFloorTotal = jHouse.getInt("FloorTotal");
            mFloorThis = jHouse.getInt("FloorThis");
            mHouseNo = jHouse.getString("HouseNo");
            mBedrooms = jHouse.getInt("Bedrooms");
            mLivingrooms = jHouse.getInt("Livingrooms");
            mBathrooms = jHouse.getInt("Bathrooms");
            mAcreage = jHouse.getInt("Acreage");

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
    public int ProId() {
        return mProId;
    }

    @Override
    public int BuildingNo() {
        return mBuildingNo;
    }

    @Override
    public int FloorTotal() {
        return mFloorTotal;
    }

    @Override
    public int Floorthis() {
        return mFloorThis;
    }

    @Override
    public String HouseNo() {
        return mHouseNo;
    }

    @Override
    public int Bedrooms() {
        return mBedrooms;
    }

    @Override
    public int Livingrooms() {
        return mLivingrooms;
    }

    @Override
    public int Bathrooms() {
        return mBathrooms;
    }

    @Override
    public int Acreage() {
        return mAcreage;
    }
}
