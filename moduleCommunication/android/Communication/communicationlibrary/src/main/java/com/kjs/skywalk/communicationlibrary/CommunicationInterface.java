package com.kjs.skywalk.communicationlibrary;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationInterface {
    public interface CICommandListener {
        void onCommandFinished(final int command, final IApiResults.ICommon res);
    }

    public interface CIProgressListener {
        void onProgressChanged(final int command, final String percent, HashMap<String, String> map);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //         -- Command definitions --
    //
    public interface ICommand {
        // user admin
        int GetSmsCode(String userName);
        int GetUserInfo(int uid);
        int GetUserSalt(String userName);
        int LoginByPassword(String user, String pass, String rand, String salt);
        int LoginBySms(String user, String smsCode);
        int Logout();
        int Relogin(String userName);
        int CommandTest();

        // house
        int GetBriefPublicHouseInfo(int houseId);
        int GetHouseInfo(int houseId, boolean bPrivtInfo);
        int CommitHouseByOwner(HouseInfo houseInfo, int agency);
        int AmendHouse(HouseInfo houseInfo);
        int RecommendHouse(int house_id, int action);
        int SetHouseCoverImg(int house_id, int img_id);
        int CertificateHouse(int house_id, boolean bPass, String sCertComment);
        int GetBehalfHouses(int type, int begin, int cnt);
        int GetHouseList(int type, int begin, int cnt);

        // Property, community
        int GetPropertyListByName(String sName, int nBegin, int nCount);
        int AddProperty(String sName, String sAddr, String sDesc);
        int GetPropertyInfo(int nPropId);
        int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc);

        // house deliverables for rent
        int AddDeliverable(String sName);
        int GetDeliverableList();
        int ModifyDeliverable(int dev_id, String sName);
        int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc);
        int GetHouseDeliverables(int house_id);

        // house facilities
        int AddFacilityType(String sTypeName);
        int EditFacilityType(int typeId, String sTypeName);
        int GetFacilityTypeList();

        int AddFacility(int nType, String sName);
        int EditFacility(int id, int nType, String sName);
        int GetFacilityList(int nType);

        int AddHouseFacility(int house, ArrayList<FacilityItem> list);
        int GetHouseFacilityList(int house);
        int EditHouseFacility(int hfid, int fid, int qty, String desc);

        // Pictures
        int AddPicture(int house, int type, String desc, String file);
        int DelePicture(int pic);
        int GetPicUrls(int pic, int size);
        int GetHousePics(int house, int type);

        // Event
        int GetNewEventCount();
        int GetHouseNewEvent();
    }

    public static class CmdID {
        // Admin
        public static int CMD_LOGIN_BY_PASSWORD     = 0x1001;
        public static int CMD_LOGIN_BY_SMS          = 0x1002;
        public static int CMD_GET_USER_SALT         = 0x1003;
        public static int CMD_GET_USER_INFO         = 0x1004;
        public static int CMD_LOG_OUT               = 0x1005;
        public static int CMD_RELOGIN               = 0x1006;
        public static int CMD_GET_SMS_CODE          = 0x1007;

        // House
        public static int CMD_GET_BRIEF_PUBLIC_HOUSE_INFO   = 0x2001;
        public static int CMD_GET_HOUSE_LIST                = 0x2002;
        public static int CMD_GET_BEHALF_HOUSE_LIST         = 0x2003;
        public static int CMD_GET_HOUSE_INFO                = 0x2004;
        public static int CMD_COMMIT_HOUSE_BY_OWNER         = 0x2005;
        public static int CMD_AMEND_HOUSE                   = 0x2006;
        public static int CMD_CERTIFY_HOUSE                 = 0x2007;
        public static int CMD_GET_SET_HOUSE_COVER_IMAGE     = 0x2008;
        public static int CMD_GET_RECOMMEND_HOUSE           = 0x2009;

        // Property
        public static int CMD_GET_PROPERTY_LIST     = 0x3001;
        public static int CMD_GET_PROPERTY_INFO     = 0x3002;
        public static int CMD_ADD_PROPERTY          = 0x3003;
        public static int CMD_MODIFY_PROPERTY       = 0x3004;

        // Deliverables
        public static int CMD_ADD_DELIVERABLE        = 0x4001;
        public static int CMD_GET_DELIVERABLE_LIST   = 0x4002;
        public static int CMD_EDIT_DELIVERABLE       = 0x4003;
        public static int CMD_ADD_HOUSE_DELIVERABLE  = 0x4004;
        public static int CMD_GET_HOUSE_DELIVERABLES = 0x4005;

        // Facilities
        public static int CMD_ADD_FACILITY_TYPE      = 0x5001;
        public static int CMD_EDIT_FACILITY_TYPE     = 0x5002;
        public static int CMD_GET_FACILITY_TYPE_LIST = 0x5003;
        public static int CMD_ADD_FACILITY           = 0x5004;
        public static int CMD_EDIT_FACILITY          = 0x5005;
        public static int CMD_GET_FACILITY_LIST      = 0x5006;
        public static int CMD_ADD_HOUSE_FACILITY     = 0x5007;
        public static int CMD_GET_HOUSEFACILITY_LIST = 0x5008;
        public static int CMD_EDIT_HOUSE_FACILITY    = 0x5009;

        // Pictures
        public static int CMD_ADD_PICTURE            = 0x6001;
        public static int CMD_DEL_PICTURE            = 0x6002;
        public static int CMD_GET_PIC_URL            = 0x6003;
        public static int CMD_GET_HOUSE_PIC_LIST     = 0x6004;

        // Event
        public static int CMD_GET_NEW_EVENT_CNT         = 0x7001;
        public static int CMD_GET_HOUSE_NEW_EVENT_CNT   = 0x7002;

        public static int CMD_TEST                   = 0x0001;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //         -- API data definitions --
    //
    // used in CommitHouse, AmendHouse
    static public class HouseInfo {
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

        public boolean CheckHoseInfo(String TAG, boolean bAdd) {
            // mHouseId could be zero, cause it maybe a "Add" operation
            if (mHouseId < 0 || (!bAdd && 0 == mHouseId)) {
                Log.e(TAG, "mHouseId:" + mHouseId);
                return false;
            }
            if (mPropId <= 0) {
                Log.e(TAG, "mPropId:" + mPropId);
                return false;
            }
            if (mBuilding <= 0) {
                Log.e(TAG, "mBuilding:" + mBuilding);
                return false;
            }
            if (TextUtils.isEmpty(mHouseNo)) {
                // Kenny; house no could be empty for townhouse or villa
//                Log.e(TAG, "No house no specified");
//                return false;
            }
            if (mFloorTotal < 1) {
                Log.e(TAG, "mFloorTotal:" + mFloorTotal);
                return false;
            }
            if (mFloorThis < 1 || mFloorThis > mFloorTotal) {
                Log.e(TAG, "mFloorThis:" + mFloorThis);
                return false;
            }
            if (mLivingrooms <= 0 && mBedrooms <= 0 && mBathrooms <= 0) {
                Log.e(TAG, "Invalid livingroom:" + mLivingrooms + ", bedroom:" + mBedrooms + ", bathroom:" + mBathrooms);
                return false;
            }
            // acreage value is 100 times than actual acreage
            if (mAcreage < 100){
                Log.e(TAG, "Invalid acreage:" + mAcreage);
                return false;
            }
//            mSale
//            mRent

            return true;
        }
    }

    // used in AddHouseFacilities
    static public class FacilityItem {
        public int      mFId    = 0;    // facility id
        public int      mQty    = 0;    // facility quantity
        public String   mDesc   = "";   // facility description

        public FacilityItem(int id, int qty, String desc) {
            mFId = id;
            mQty = qty;
            mDesc = desc;
        }
    }
}
