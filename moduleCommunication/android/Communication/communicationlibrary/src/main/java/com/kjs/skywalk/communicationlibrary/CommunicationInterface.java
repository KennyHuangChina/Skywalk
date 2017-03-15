package com.kjs.skywalk.communicationlibrary;

import org.json.JSONObject;

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

    public interface ICommand {
        int GetSmsCode(String userName);
        int GetUserInfo(int uid);
        int GetUserSalt(String userName);
        int LoginByPassword(String user, String pass, String rand, String salt);
        int LoginBySms(String user, String smsCode);
        int Logout();
        int Relogin(String userName);
        int CommandTest();

        int GetBriefPublicHouseInfo(int houseId);
        int GetHouseInfo(int houseId);
        int CommitHouseByOwner(HouseInfo houseInfo, int agency);
        int AmendHouse(HouseInfo houseInfo);
        int RecommendHouse(int house_id, int action);
        int SetHouseCoverImg(int house_id, int img_id);
        int CertificateHouse(int house_id, boolean bPass, String sCertComment);
        int GetBehalfHouses(int type, int begin, int cnt);
        int GetHouseList(int type, int begin, int cnt);

        int GetPropertyListByName(String sName, int nBegin, int nCount);
        int AddProperty(String sName, String sAddr, String sDesc);
        int GetPropertyInfo(int nPropId);
        int ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc);

        int AddDeliverable(String sName);
        int GetDeliverableList();
        int AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc);
        int GetHouseDeliverables(int house_id);

        int AddFacilityType(String sTypeName);
        int GetFacilityTypeList();
        int AddFacility(int nType, String sName);
        int GetFacilityList(int nType);
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
        public static int CMD_ADD_HOUSE_DELIVERABLE  = 0x4003;
        public static int CMD_GET_HOUSE_DELIVERABLES = 0x4004;

        // Facilities
        public static int CMD_ADD_FACILITY_TYPE      = 0x5001;
        public static int CMD_GET_FACILITY_TYPE_LIST = 0x5002;
        public static int CMD_ADD_FACILITY           = 0x5003;
        public static int CMD_GET_FACILITY_LIST      = 0x5004;

        public static int CMD_TEST                   = 0x0001;
    }
}
