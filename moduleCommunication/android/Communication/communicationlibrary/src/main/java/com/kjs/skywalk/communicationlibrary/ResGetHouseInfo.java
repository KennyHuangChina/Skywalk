package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/3/1.
 */

class ResGetHouseInfo extends ResBase implements IApiResults.IGetHouseInfo {
    private int     mHouseId;       // house id
    private int     mProId;         // property id which the house belong to
    private String  mBuildingNo;    // the building number the house belong to
    private int     mFloorTotal;    // total floors
    private int     mFloorThis;     // exact floor the house resident
    private String  mFloorDesc;     // floor description
    private String  mHouseNo;       // exact house number. like house 1305#
    private int     mBedrooms;      // how many bedrooms whitin house
    private int     mLivingrooms;   // how many living rooms within house
    private int     mBathrooms;     // how many bathrooms within house
    private int     mAcreage;       // house acreage, 100x than real value. for example 11537 mean 115.37 m^2 (Square Meter)
    private int     mDecorate;      // decorate id
    private String  mDecoration;    // decoration description
    private String  mBuyDate;       // buy date
    private String  mModifyDate;    // last modify date
    private int     mAgency;        // house agent id

    ResGetHouseInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  hosue id: " + HouseId() + "\n";
        mString += "  property: " + ProId() + "\n";
        mString += "  building: " + BuildingNo() + "\n";
        mString += "  floor: " + Floorthis() + "/" + FloorTotal() + "\n";
        mString += "  house no: " + HouseNo() + "\n";
        mString += "  bedrooms: " + Bedrooms() + "\n";
        mString += "  living rooms: " + Livingrooms() + "\n";
        mString += "  bathrooms: " + Bathrooms() + "\n";
        mString += "  acreage: " + Acreage() / 100 + "." + Acreage() % 100 + "\n";
        mString += "  decoration: (" + Decorate() + ")" + DecorateDesc() + "\n";
        mString += "  buy date: " + BuyDate() + "\n";
        mString += "  last modify date: " + ModifyDate() + "\n";
        mString += "  Agency: " + Agency() + "\n";

        return mString;
    }

    protected int parseResult(JSONObject obj) {
        try {
            JSONObject jHouse = obj.getJSONObject("HouseInfo");

            mHouseId        = jHouse.getInt("Id");
            mProId          = jHouse.getInt("Property");
            mBuildingNo     = jHouse.getString("BuildingNo");
            mFloorTotal     = jHouse.getInt("FloorTotal");
            mFloorThis      = jHouse.getInt("FloorThis");
            mHouseNo        = jHouse.getString("HouseNo");
            mBedrooms       = jHouse.getInt("Bedrooms");
            mLivingrooms    = jHouse.getInt("Livingrooms");
            mBathrooms      = jHouse.getInt("Bathrooms");
            mAcreage        = jHouse.getInt("Acreage");
            mDecorate       = jHouse.getInt("Decoration");
            mBuyDate        = jHouse.getString("BuyDate");
            mModifyDate     = jHouse.getString("ModifyDate");
            mAgency         = jHouse.getInt("Agency");

            if (mBuildingNo.isEmpty() || mHouseNo.isEmpty()) {
                if (mFloorThis == mFloorTotal + 1) {
                    mFloorDesc = "低层";
                } else if (mFloorThis == mFloorTotal + 2) {
                    mFloorDesc = "中层";
                } else if (mFloorThis == mFloorTotal + 3) {
                    mFloorDesc = "高层";
                }
            }

            switch (mDecorate) {
                case 0:
                    mDecoration = "毛坯";
                    break;
                case 1:
                    mDecoration = "简装";
                    break;
                case 2:
                    mDecoration = "中档装修";
                    break;
                case 3:
                    mDecoration = "精装修";
                    break;
                case 4:
                    mDecoration = "豪华装修";
                    break;
            }

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
    public String BuildingNo() {
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
    public String FloorDesc() {
        return mFloorDesc;
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

    @Override
    public int Decorate() {
        return mDecorate;
    }

    @Override
    public int Agency() {
        return mAgency;
    }

    @Override
    public String DecorateDesc() {
        return mDecoration;
    }

    @Override
    public String BuyDate() {
        return mBuyDate;
    }

    @Override
    public String ModifyDate() {
        return mModifyDate;
    }
}
