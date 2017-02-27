package com.kjs.skywalk.communicationlibrary;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/24.
 */

public class IApiResults {
    public interface ICommon {
        int GetErrCode();
        String GetErrDesc();
        String DebugString();
    }

    public interface IGetUserSalt {
        String GetSalt();
        String GetRandom();
    }

    public interface IPropertyInfo {
        int GetId();
        String GetName();
        String GetAddress();
        String GetDesc();
    }

    public interface IResultList {
        int GetTotalNumber();
        int GetFetchedNumber();
        ArrayList<Object> GetList();
    }

    public interface IHouseDigest {
        int GetHouseId();
        String GetProperty();
        String GetPropertyAddr();
        int GetBedrooms();
        int GetLivingrooms();
        int GetBathrooms();
        int GetAcreage();
        int GetRental();
        int GetPricing();
        int GetCoverImage();
    }

    public interface IHouseTag {
        int GetId();
        String GetName();
    }

    public interface ILogin {
        String GetSession();
    }

    public interface IGetSmsCode {
        String GetSmsCode();
    }

    public interface IGetUserInfo {
        int GetId();
        String GetName();
        String GetPhoneNo();
        String GetIdNo();
        String GetHead();
        String GerRole();
        String GetRoleDesc();
    }
}
