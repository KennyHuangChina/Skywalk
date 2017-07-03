package com.kjs.skywalk.communicationlibrary;

import android.text.TextUtils;
import android.util.Log;

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

        int CommandTest();

        /* User Admin */
        int GetSmsCode(String userName);                                        // CMD_GET_SMS_CODE,        IApiResults.IGetSmsCode
        int GetUserInfo(int uid);                                               // CMD_GET_USER_INFO,       IApiResults.IGetUserInfo
        int GetUserSalt(String userName);                                       // CMD_GET_USER_SALT,       IApiResults.IGetUserSalt
        int LoginByPassword(String user, String pass, String rand, String salt);// CMD_LOGIN_BY_PASSWORD,   IApiResults.ILogin
        int LoginBySms(String user, String smsCode);                            // CMD_LOGIN_BY_SMS,        IApiResults.ILogin
        int Relogin(String userName);                                           // CMD_RELOGIN,             IApiResults.ILogin
        int Logout();                                                           // CMD_LOG_OUT,             IApiResults.ICommon
        int GetAgencyList(int begin, int cnt);                                  // CMD_GET_AGENCY_LIST,     IApiResults.IResultList(IApiResults.IAgencyInfo)
        int MofidyAgency(int agency, int rank_pro, int rank_att, int begin_year);// CMD_MODIFY_AGENCY,       IApiResults.ICommon

        /* House */
        int GetBriefPublicHouseInfo(int houseId);                               // CMD_GET_BRIEF_PUBLIC_HOUSE_INFO, IApiResults.IHouseDigest & IApiResults.IResultList(IApiResults.IHouseTag)
        int GetHouseInfo(int houseId, boolean bBackend);                        // CMD_GET_HOUSE_INFO,              IApiResults.IGetHouseInfo
        int CommitHouseByOwner(HouseInfo houseInfo, int agency);                // CMD_COMMIT_HOUSE_BY_OWNER,       IApiResults.IAddRes
        int AmendHouse(HouseInfo houseInfo);                                    // CMD_AMEND_HOUSE,                 IApiResults.ICommon
        int RecommendHouse(int house_id, int action);                           // CMD_GET_RECOMMEND_HOUSE,         IApiResults.ICommon
        int SetHouseCoverImg(int house_id, int img_id);                         // CMD_GET_SET_HOUSE_COVER_IMAGE,   IApiResults.ICommon
        int SetHousePrice(int house_id, int rental_tag, int rental_bottom, boolean bIncPropFee,
                          int price_tag, int price_bottom);                     // CMD_SET_HOUSE_PRICE,             IApiResults.IAddRes
        int GetHousePrice(int house_id, int begin, int count);                  // CMD_GET_HOUSE_PRICE,             IApiResults.IResultList(IApiResults.IHousePriceInfo)
        int CertificateHouse(int house_id, boolean bPass, String sCertComment); // CMD_CERTIFY_HOUSE,               IApiResults.ICommon
        int GetBehalfHouses(int type, int begin, int cnt);                      // CMD_GET_BEHALF_HOUSE_LIST,       TODO:
        /*
        *   CMD     : CMD_GET_HOUSE_DIGEST_LIST
        *   Result  : IApiResults.IResultList(IApiResults.IHouseDigest, IApiResults.IResultList(IApiResults.IHouseTag))
        *   type    : list type. 0: all house; 1: recommend houses; 2: deducted houses; 3: new houses
        *   begin   : fetch begin position
        *   cnt     : fetch count. set to 0 mean just want to get the total number
        *   filter  : filter confitions. HouseFilterCondition
        *   sort    : sort condition list
         */
        int GetHouseDigestList(int type, int begin, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort);

        int GetHouseShowtime(int house_id);                                     // CMD_GET_HOUSE_SHOWTIME,          IApiResults.IHouseShowtime
        int SetHouseShowtime(int house_id, int pw, int pv, String pd);          // CMD_SET_HOUSE_SHOWTIME,          IApiResults.ICommon

        /* Property, Community */
        int GetPropertyListByName(String sName, int nBegin, int nCount);                // CMD_GET_PROPERTY_LIST,   IApiResults.IResultList(IApiResults.IPropertyInfo)
        int AddProperty(String sName, String sAddr, String sDesc);                      // CMD_ADD_PROPERTY,        IApiResults.IAddRes
        int GetPropertyInfo(int nPropId);                                               // CMD_GET_PROPERTY_INFO,   IApiResults.IPropertyInfo
        int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc);  // CMD_MODIFY_PROPERTY,     IApiResults.ICommon

        /* House Deliverables for rent */
        int AddDeliverable(String sName);                                                   // CMD_ADD_DELIVERABLE,         IApiResults.IAddRes
        int GetDeliverableList();                                                           // CMD_GET_DELIVERABLE_LIST,    IApiResults.IResultList(IApiResults.IDeliverableItem)
        int ModifyDeliverable(int dev_id, String sName);                                    // CMD_EDIT_DELIVERABLE,        IApiResults.ICommon
        int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc);   // CMD_ADD_HOUSE_DELIVERABLE,   IApiResults.IAddRes
        int GetHouseDeliverables(int house_id);                                             // CMD_GET_HOUSE_DELIVERABLES,  IApiResults.IResultList(IApiResults.IDeliverableInfo)

        /* House Facilities */
        int AddFacilityType(String sTypeName);                          // CMD_ADD_FACILITY_TYPE,       IApiResults.IAddRes
        int EditFacilityType(int typeId, String sTypeName);             // CMD_EDIT_FACILITY_TYPE,      IApiResults.ICommon
        int GetFacilityTypeList();                                      // CMD_GET_FACILITY_TYPE_LIST,  IApiResults.IResultList(IApiResults.IListIdName)

        int AddFacility(int nType, String sName, String sIcon);         // CMD_ADD_FACILITY,            IApiResults.IAddRes
        int EditFacility(int id, int nType, String sName, String sIcon);// CMD_EDIT_FACILITY,           IApiResults.ICommon
        int GetFacilityList(int nType);                                 // CMD_GET_FACILITY_LIST,       IApiResults.IResultList(IApiResults.IFacilityInfo)

        int AddHouseFacility(int house, ArrayList<FacilityItem> list);  // CMD_ADD_HOUSE_FACILITY,      IApiResults.ICommon
        int GetHouseFacilityList(int house);                            // CMD_GET_HOUSEFACILITY_LIST,  IApiResults.IResultList(IApiResults.IHouseFacilityInfo)
        int EditHouseFacility(int hfid, int fid, int qty, String desc); // CMD_EDIT_HOUSE_FACILITY,     IApiResults.ICommon

        // Pictures
        int AddPicture(int house, int type, int refId,
                       String desc, String file);                       // CMD_ADD_PICTURE,         IApiResults.IAddRes
        int DelePicture(int pic);                                       // CMD_DEL_PICTURE,         IApiResults.ICommon
        int GetPicUrls(int pic, int size);                              // CMD_GET_PIC_URL,         IApiResults.IPicUrls
        int GetHousePics(int house, int type);                          // CMD_GET_HOUSE_PIC_LIST,  IApiResults.IResultList(IApiResults.IHousePicInfo)

        // Event
        int GetNewEventCount();                                 // CMD_GET_NEW_EVENT_CNT,       IApiResults.INewEventCount
        int GetHouseNewEvent();                                 // CMD_GET_HOUSE_NEW_EVENTS,    IApiResults.IResultList(IApiResults.IHouseNewEvent)
        int ReadNewEvent(int event_id);                         // CMD_READ_NEW_EVENT,          IApiResults.ICommon
        int GetHouseEventInfo(int event_id);                    // CMD_GET_EVENT_INFO,          IApiResults.IHouseEventInfo
        int GetHouseEventProcList(int event_id);                // CMD_GET_EVENT_PROC_LST,      IApiResults.IResultList(IApiResults.IEventProcInfo)
        int GetHouseEventList(int house_id, int stat, int type, // CMD_GET_HOUSE_EVENT_LST,     IApiResults.IResultList(IApiResults.IHouseEventInfo)
                              int posi_bgn, int fetch_cnt, boolean bIDO);
        int ModifyHouseEvent(int event_id, String desc);        // CMD_MODIFY_HOUSE_EVENT,      IApiResults.ICommon

        // Appointment APIs
        /*
        *   CMD         : CmdMakeAppointment_SeeHouse
        *   Result      : IApiResults.IAddRes
        *   house       : house id
        *   phone       : contact phone. if user logined, it could be empty
        *   time_begin  : appointment period begin, like 2017-06-24 09:00
        *   time_end    : appointment period end, like 2017-06-24 09:30
        *   desc        : appointment description
         */
        int MakeAppointment_SeeHouse(int house, String phone, String time_begin, String time_end, String desc);

        /*
            CMD     : CMD_APPOINT_HOUSE_SEE_LST
            Result  : IApiResults.IResultList(IApiResults.IAppointment)
            house_id: house id
            begin   : fetch begin position
            cnt     : fetch count. set to 0 to get the total number
         */
        int GetHouseSeeAppointmentList(int house_id, int begin, int cnt);
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
        public static int CMD_GET_AGENCY_LIST       = 0x1008;
        public static int CMD_MODIFY_AGENCY         = 0x1009;

        // House
        public static int CMD_GET_BRIEF_PUBLIC_HOUSE_INFO   = 0x2001;
        public static int CMD_GET_HOUSE_DIGEST_LIST         = 0x2002;
        public static int CMD_GET_BEHALF_HOUSE_LIST         = 0x2003;
        public static int CMD_GET_HOUSE_INFO                = 0x2004;
        public static int CMD_COMMIT_HOUSE_BY_OWNER         = 0x2005;
        public static int CMD_AMEND_HOUSE                   = 0x2006;
        public static int CMD_CERTIFY_HOUSE                 = 0x2007;
        public static int CMD_GET_SET_HOUSE_COVER_IMAGE     = 0x2008;
        public static int CMD_GET_RECOMMEND_HOUSE           = 0x2009;
        public static int CMD_SET_HOUSE_PRICE               = 0x200A;
        public static int CMD_GET_HOUSE_PRICE               = 0x200B;
        public static int CMD_GET_HOUSE_SHOWTIME            = 0x200C;
        public static int CMD_SET_HOUSE_SHOWTIME            = 0x200D;

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
        public static int CMD_GET_HOUSE_NEW_EVENTS      = 0x7002;
        public static int CMD_READ_NEW_EVENT            = 0x7003;
        public static int CMD_GET_EVENT_INFO            = 0x7004;
        public static int CMD_GET_EVENT_PROC_LST        = 0x7005;
        public static int CMD_GET_HOUSE_EVENT_LST       = 0x7006;
        public static int CMD_MODIFY_HOUSE_EVENT        = 0x7007;

        // Appointment
        public static int CMD_APPOINT_SEE_HOUSE         = 0x8001;
        public static int CMD_APPOINT_HOUSE_SEE_LST     = 0x8002;

        public static int CMD_TEST                   = 0x0001;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //         -- API data definitions --
    //
    // used in CommitHouse, AmendHouse
    static public class HouseInfo {
        public int      mHouseId        = 0;
        public int      mPropId         = 0;    // property, community
        public String   mBuilding       = "";   // building number, like 177A�?
        public String   mHouseNo        = "";   // house number, 1505�?
        public int      mFloorTotal     = 0;    // total floor
        public int      mFloorThis      = 0;    // house floor
        public int      mLivingrooms    = 0;
        public int      mBedrooms       = 0;
        public int      mBathrooms      = 0;
        public int      mAcreage        = 0;    // acreage, 100 times than actual. 10300 means 103 平米
        public boolean  mForSale        = false;
        public boolean  mForRent        = false;
        public int      mDecorate       = 0;    // decoration. 0 - 毛坯 / 1 - 简�?/ 2 - 中等 / 3 - 精装 / 4 - 豪华
        public String   mBuyDate        = "";   // exact date of buying this house

        public HouseInfo() {
        }

        public HouseInfo(int hid, int pid, String bld, String hno, int ftotal, int fthis, int lr,
                         int bedr, int bathr, int acre, boolean sale, boolean rent, int decorate, String buy_date) {
            mHouseId        = hid;
            mPropId         = pid;
            mBuilding       = bld;
            mHouseNo        = hno;
            mFloorTotal     = ftotal;
            mFloorThis      = fthis;
            mLivingrooms    = lr;
            mBedrooms       = bedr;
            mBathrooms      = bathr;
            mAcreage        = acre;
            mForSale        = sale;
            mForRent        = rent;
            mDecorate       = decorate;
            mBuyDate        = buy_date;
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
            if (mBuilding.isEmpty()) {
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
            if (mDecorate < 0 || mDecorate > 4) {
                Log.e(TAG, "Invalid decoration:" + mDecorate);
                return false;
            }

            if (mBuyDate.isEmpty()) {
                Log.e(TAG, "buy date not set");
                return false;
            }

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

    // used in GetHouseDigestList
    static public class HouseFilterCondition {

        static public int   FILTER_TYPE_EQ      = 1,    // Less Than..      =
                            FILTER_TYPE_LT      = 2,    // Less Than..      <
                            FILTER_TYPE_LE      = 3,    // Less Equal.      <=
                            FILTER_TYPE_GT      = 4,    // Greater Than.    >
                            FILTER_TYPE_GE      = 5,    // Greater Equal.   >=
                            FILTER_TYPE_BETWEEN = 6;    // Between.         >= && <=
        static public int   SORT_PUBLISH_TIME       = 1,    // sort by publish time, from early to late
                            SORT_PUBLISH_TIME_DESC  = 2,    // sort by publish time, from late to early
                            SORT_RENTAL             = 3,    // sort by rental, from low to high
                            SORT_RENTAL_DESC        = 4,    // sort by rental, from high to low
                            SORT_APPOINT_NUMB       = 5,    // sort by appointment number, from low to high
                            SORT_APPOINT_NUMB_DESC  = 6;    // sort by appointment number, from high to low

        public IntegerFilter mRental;
        public IntegerFilter mLivingroom;
        public IntegerFilter mBedroom;
        public IntegerFilter mBathroom;
        public IntegerFilter mAcreage;
        public ArrayList<Integer> tags;

        public HouseFilterCondition() {
            mRental     = new IntegerFilter();
            mLivingroom = new IntegerFilter();
            mBedroom    = new IntegerFilter();
            mBathroom   = new IntegerFilter();
            mAcreage    = new IntegerFilter();
            tags        = new ArrayList<Integer>();
        }

        public class IntegerFilter {
            private String TAG = getClass().getSimpleName();
            private int mVal1       = -1;
            private int mVal2       = -1;
            private int mFilterType = -1;

            IntegerFilter() {
            }

            public int FilterBetween(int Val1, int Val2) {
                if (Val1 < 0) {
                    Log.e(TAG, "Val1:" + Val1);
                    return -1;
                }
                if (Val2 < 0) {
                    Log.e(TAG, "Val2:" + Val2);
                    return -1;
                }
                if (Val2 <= Val1) {
                    Log.e(TAG, String.format("Val2(%d) <= Val1(%d)", Val2, Val1));
                    return -1;
                }

                mFilterType = FILTER_TYPE_BETWEEN;
                mVal1       = Val1;
                mVal2       = Val2;
                return 0;
            }

            public int FilterEq(int Val) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = FILTER_TYPE_EQ;
                mVal1       = Val;
                return 0;
            }

            public int FilterLessX(int Val, boolean equal) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = equal ? FILTER_TYPE_LE : FILTER_TYPE_LT;
                mVal1       = Val;
                return 0;
            }

            public int FilterGreatX(int Val, boolean equal) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = equal ? FILTER_TYPE_GE : FILTER_TYPE_GT;
                mVal1   = Val;
                return 0;
            }

            public int GetOp() { return mFilterType; }

            public int GetValue1() { return mVal1; }

            public int GetValue2() {
                if (FILTER_TYPE_BETWEEN != mFilterType) {
                    return -1;
                }
                return mVal2;
            }
        }
    }
}
