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
        int GetTagId();
        String GetName();
    }

    public interface ILogin {
        String GetSession();
    }

    public interface IGetSmsCode {
        String GetSmsCode();
    }

    public interface IGetUserInfo {
        int GetUserId();
        String GetName();
        String GetPhoneNo();
        String GetIdNo();
        String GetHead();
        String GerRole();
        String GetRoleDesc();
    }

    public interface IGetHouseInfo {
        int HouseId();      // house id
        int ProId();        // property id which the house belong to
        int BuildingNo();   // the building number the house belong to
        int FloorTotal();   // total floors
        int Floorthis();    // exact floor the house resident
        String HouseNo();   // exact house number. like house 1305#
        int Bedrooms();     // how many bedrooms whitin house
        int Livingrooms();  // how many living rooms within house
        int Bathrooms();    // how many bathrooms within house
        int Acreage();      // house acreage, 100x than real value. for example 11537 mean 115.37 m2
    }

    public interface IAddRes {
        int GetId();
    }

    public interface IListIdName {
        int GetId();
        String GetName();
    }

    public interface IDeliverableInfo {
        int GetId();
        String GetName();
        int GetQty();
        String GetDesc();
    }

    public interface IFacilityInfo {
        int GetId();
        String GetName();
        String GetType();
    }

    public interface IHouseFacilityInfo {
        int GetId();
        String GetName();
        String GetType();
        int GetQty();
        String GetDesc();
    }

    // command: CmdGetPictureUrls
    public interface IPicUrls {
        String GetSmallPicture();
        String GetMiddlePicture();
        String GetLargePicture();
    }

    // command: GetHousePicList
    public interface IHousePicInfo {
        int     GetId();
        String  GetDesc();
        int     GetType();
        String  GetChecksum();
    }

    // command: GetNewEventCount
    public interface INewEventCount {
        int     GetNewEventCount();
    }

    // command: CmdGetHouseNewEvent
    public interface IHouseNewEvent {
        int     GetHouseId();
        String  GetProperty();
        int     GetBuildingNo();
        String  GetHouseNo();
        int     GetCoverImageId();
        int     GetNewEventCount();
        String  GetEvenTime();
        String  GetDescription();
    }
}
