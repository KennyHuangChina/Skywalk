package com.kjs.skywalk.communicationlibrary;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationInterface {
    public interface CICommandListener {
        void onCommandFinished(final String command, final IApiResults.ICommon res);
    }

    public interface CIProgressListener {
        void onProgressChanged(final String command, final String percent, HashMap<String, String> map);
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
}
