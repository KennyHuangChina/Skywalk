package com.kjs.skywalk.communicationlibrary;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kenny on 2017/2/24.
 */

public class IApiResults {
    public interface ICommon {
        int     GetErrCode();
        String  GetErrDesc();
        String  DebugString();
    }

    public interface IGetUserSalt {
        String GetSalt();
        String GetRandom();
    }

    public interface IAgencyInfo {
        int     Id();       // agency id
        String  Name();     // agency name
        String  Phone();    // agency phone number
        String  Portrait(); // agency head portrait picture URL
        int     Sex();      // agency sex. USER_SEX_XXX
        String  IdNo();     // agency ID card
        int     RankProf(); // professional rank. 0 ~ 50 (0.0 ~ 5.0)
        int     RankAtti(); // attitude rank. 0 ~50 (0.0 ~ 5.0)
    }

    public interface IAppointment {
        int     AppointId();        // appointment id
        int     AppointType();      // appointment type
        String  TypeDesc();         // appointment type description
        int     HouseId();          // which house has been subscribed
        String  Phone();            // contact phone
        String  AppointDesc();      // appointment description
        String  Subscriber();       // who made the subscription
        Date    AppointTimeBgn();   // appointment time begin
        Date    AppointTimeEnd();   // appointment time end
        Date    SubscribeTime();    // subscribe time
        Date    CloseTime();        // close time
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
        int     GetHouseId();
        String  GetProperty();
        String  GetPropertyAddr();
        int     GetBedrooms();
        int     GetLivingrooms();
        int     GetBathrooms();
        int     GetAcreage();
        int     GetRental();
        int     GetPricing();
        boolean IsRentalIncPropFee();
        int     GetCoverImage();
        String  GetCoverImageUrlS();
        String  GetCoverImageUrlM();
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
        int     GetUserId();
        String  GetName();
        String  GetPhoneNo();
        String  GetIdNo();
        String  GetHead();
        Boolean IsAdmin();
        Boolean IsAgent();
    }

    public interface IResetPassword {
        String GetUser();
    }

    public interface IGetHouseInfo {
        int     HouseId();      // house id
        int     ProId();        // property id which the house belong to
        String  BuildingNo();   // the building number the house belong to
        int     FloorTotal();   // total floors
        int     Floorthis();    // exact floor the house resident
        String  FloorDesc();    // floor description
        String  HouseNo();      // exact house number. like house 1305#
        int     Bedrooms();     // how many bedrooms whitin house
        int     Livingrooms();  // how many living rooms within house
        int     Bathrooms();    // how many bathrooms within house
        int     Acreage();      // house acreage, 100x than real value. for example 11537 mean 115.37 M^2
        int     Decorate();     // decoration. ref to HOUSE_DECORATE_XXXX
        int     Agency();       // house agency id
        int     Landlord();     // house landlord id
        Date    SubmitTime();   // house submit time
        boolean ForSale();      // is house for sale
        boolean ForRent();      // is house for rent
        int     RentStat();     // rent state. HOUSE_RENT_xxx
        String  DecorateDesc();
        String  BuyDate();
        String  ModifyDate();
    }

    public interface IHouseCertDigestInfo {
        int     CertStat();     // certification stat. HOUSE_CERT_STAT_XXX
        Date    CertTime();
        String  CertDesc();
    }

    public interface IHouseCertInfo {
        int     CertId();
        int     UserId();
        String  UserName();
        String  UserPhone();
        Date    CertTime();
        int     CertStat();     // certification stat. HOUSE_CERT_STAT_XXX
        String  CertDesc();
    }

    public interface IHouseCertHist {
        int     Operations();
    }

    // TODO: maybe should be removed
    public interface IHousePriceInfo {
        int     Id();
        int     RentalTag();
        int     RentalMin();
        boolean RentalIncPropFee();
        int     SellingPriceTag();
        int     SellingPriceMin();
        String  Who();
        String  When();
    }

    public interface IHouseShowtime {
        int     HouseId();  // house id
        int     PeriodW();  // Period for working day: 1 - mornint; 2 - afternoon; 3 - night
        int     PeriodV();  // Period for weekend and vacation: 1 - mornint; 2 - afternoon; 3 - night
        String  Desc();     // description when period is 4
        String  Who();      // who set the showtime
        String  When();     // when the showtime's been set
    }

    public interface IAddRes {
        int GetId();
    }

    public interface IListIdName {
        int GetId();
        String GetName();
    }

    public interface IDeliverableItem {
        int     GetId();
        String  GetName();
        int     GetPic();
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
        String GetIcon();
    }

    public interface IHouseFacilityInfo {
        int GetId();
        String GetName();
        String GetType();
        String GetIcon();
        int GetQty();
        String GetDesc();
    }

    // command: CMD_ADD_PICTURE
    public interface IAddPic extends IAddRes {
        String GetPicChecksum();    // get the checksum of picture uploaded
    }

    // command: CmdGetPictureUrls
    public interface IPicUrls {
        int     GetPicId();
        String  GetSmallPicture();
        String  GetMiddlePicture();
        String  GetLargePicture();
    }

    // command: GetHousePicList, GetUserPicList
    public interface IPicInfo {
        int     GetId();
        String  GetDesc();
        int     GetType();
        String  GetChecksum();
    }

    // command: GetNewMsgCount
    public interface INewMsgCount {
        int     GetNewMsgCount();
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

    /* Two cases:
            1. BuildingNo & HouseNo return empty or null, mean current login user have no permission to read this privacy info
                property + bedrooms + livingrooms + bathrooms. for example, 蝶湖湾 3室2厅2卫
            2. BuildingNo & HouseNo return real info. property + building no + house no. for example, 蝶湖湾 171栋 1501室
    */
    interface IHouseTitleInfo {
        int     HouseId();      // house id
        String  Property();     // property name
        String  BuildingNo();   // the building number the house belong to
        String  HouseNo();      // exact house number. like house 1305#
        int     Livingrooms();  // how many livingrooms
        int     Bedrooms();     // how many bedrooms
        int     Bathrooms();    // bathrooms
    }

    // command: CMD_GET_MSG_INFO, CmdGetHouseEventList
    public interface ISysMsgInfo extends IHouseTitleInfo {
        int     MsgId();        // message id
        int     MsgType();      // message type. SYS_MSG_TYPE_XXX
        int     RefId();        // reference id, depend on what the msg_typs is
        int     MsgPriority();  // message priority. SYS_MSG_PRIORITY_xxx
        String  Receiver();     // people who the event send to
        String  CreateTime();   // exact time when the event created
        String  ReadTime();     // exact time when the event get readed
        String  MsgBody();      // message text
    }

    // command: CmdGetHouseEventProcList
    public interface IEventProcInfo {
        int     Id();           // event proc id
        String  User();         // user name
        String  Time();         // proc time
        String  Operation();    // proc operation
        String  Desc();         // proc description
    }

    public interface IAppointmentInfo extends IHouseTitleInfo {
        Date    ScheduleDate();
        Date    ScheduleBeginTime();
        Date    ScheduleEndTime();
        String  Subscriber();
        String  SubscriberPhone();
        String  Receptionist();
        String  ReceptionistPhone();
        String  AppointmentDesc();
        Date    SubscribeTime();
        int     Operations();
    }

    public interface IAppointmentAct {
        int     Id();
        int     Act();      // Ref to HOUSESEE_APPOINTMENT_ACT_xxx
        String  Who();
        Date    When();
        Date    PeriodBegin();
        Date    PeriodEnd();
        String  Comment();
    }

    // System Message Type
    public static final int SYS_MSG_TYPE_HOUSE_CERTIFICATION        = 1,    // house certification
                            SYS_MSG_TYPE_APPOINTMENT_HOUSE_SEEING   = 2;    // appointment for house seeing

    //System Message Priority
    public static final int SYS_MSG_PRIORITY_INFO       = 0,
                            SYS_MSG_PRIORITY_WARNING    = 1,
                            SYS_MSG_PRIORITY_ERROR      = 2;

    // certification stat. 1: wait for certification; 2: Certification passed; 3: Certification failed
    public static final int HOUSE_CERT_STAT_WAIT    = 1,    // wait for certification
                            HOUSE_CERT_STAT_PASSED  = 2,    // certification passed
                            HOUSE_CERT_STAT_FAILED  = 3;    // certification failed

    // user sex. 0 - male / 1 - female
    public static final int USER_SEX_MALE   = 0,
                            USER_SEX_FEMALE = 1;

    // Rent stat: 1: wait for rent, 2: rented, 3: Due, open for ordering
    public static final int HOUSE_RENT_WAIT     = 1,    // wait for rent
                            HOUSE_RENT_RENTED   = 2,    // rented
                            HOUSE_RENT_DUE      = 3;    // Due, open for ordering

    //  House decoration
    public static final int HOUSE_DECORATE_Workblank= 0,
                            HOUSE_DECORATE_Simple   = 1,
                            HOUSE_DECORATE_Medium   = 2,
                            HOUSE_DECORATE_Fine     = 3,
                            HOUSE_DECORATE_Luxury   = 4;

    // House seeing appointment actions
    public static final int HOUSESEE_APPOINTMENT_ACT_Submit         = 1,    // submit new appointment
                            HOUSESEE_APPOINTMENT_ACT_Confirm        = 2,    // confirm the schedule
                            HOUSESEE_APPOINTMENT_ACT_Reschedule     = 3,    // reschedule
                            HOUSESEE_APPOINTMENT_ACT_Done           = 4,    // appointment finished
                            HOUSESEE_APPOINTMENT_ACT_Cancel         = 5,    // appointment canceled
                            HOUSESEE_APPOINTMENT_ACT_AssignAgency   = 6;    // assign agency to handle the appointment
}
