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
        void onCommandFinished(final int cmdId, final int cmdSeq, final IApiResults.ICommon res);
    }

    public interface CIProgressListener {
        void onProgressChanged(final int command, final String percent, HashMap<String, String> map);
    }

    static public int COMMAND_SEQ_BASE = 0x10000000;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //         -- Command definitions --
    //
    public interface ICommand {

        CmdExecRes CommandTest();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          User Admin
       /**
        *   CMD: <CMD_GET_SMS_CODE,>
        *       - userName: login name (cell phone number)
        *   Arguments: <IApiArgs.IArgsGetSmsCode>
        *   Result: <IApiResults.IGetSmsCode>
        */
        CmdExecRes GetSmsCode(String userName);

        /**
         *  Get user info. CMD_GET_USER_INFO
         *      @param uid : user id to fetch
         *      @return
         *  Arguments   : <IApiArgs.IArgsGetUserInfo>
         *  Result      : <IApiResults.IGetUserInfo>
         */
        CmdExecRes GetUserInfo(int uid);

        /**
         *  Get Login User Info.    CMD_GET_LOGIN_USER_INFO
         *      @return
         *  Result      : <IApiResults.IGetUserInfo>
         */
        IApiResults.IGetUserInfo GetLoginUserInfo();

       /**
        *   CMD: CMD_GET_USER_SALT,
        *       - userName: login name (cell phone number)
        *   Result      : <IApiResults.IGetUserSalt>
         *  Arguments   : <IApiArgs.IArgsUserName>
        */
        CmdExecRes GetUserSalt(String userName);

       /**
        *   CMD: <CMD_LOGIN_BY_PASSWORD>
        *       - user  : login name (cell phone number)
        *       - pass  : password
        *       - rand  : rand number
        *       - salt  : the session salt returned by GetUserSalt
        *   Result    : <IApiResults.ILogin>
        *   Arguments : <IApiArgs.IArgsLoginPass>
        */
        CmdExecRes LoginByPassword(String user, String pass, String rand, String salt);

       /**
        *   CMD: <CMD_LOGIN_BY_SMS>
        *       - user    : login name (cell phone number)
        *       - smsCode : the sms code user received
        *   Result    : <IApiResults.ILogin>
        *   Arguments : <IApiArgs.IArgsLoginSms>
        */
        CmdExecRes LoginBySms(String user, String smsCode);

       /**
        *   CMD: <CMD_RELOGIN>
        *       - userName : login name (cell phone number)
        *   Result   : <IApiResults.ILogin>
        *  Arguments : <IApiArgs.IArgsUserName>
        */
        CmdExecRes Relogin(String userName);

       /**
        *   CMD    : <CMD_LOG_OUT>
        *   Result : <IApiResults.ICommon>
        */
        CmdExecRes Logout();

        /**
        *   CMD: <CMD_RESET_LOGIN_PASS>
        *       - user  : target user name(/phone number)
        *       - pass  : new password
        *       - sms   : sms captcha code
        *       - salt  : salt
        *       - rand  : rand
        *   Result    : <IApiResults.ICommon>
        *   Arguments : <IApiArgs.IArgsResetLoginPwd>
        */
        CmdExecRes ResetLoginPass(String user, String pass, String sms, String salt, String rand);

        /**
         *  Get Agent List. <CMD_GET_AGENCY_LIST>
         *      @param begin
         *      @param cnt
         *      @return
         *  Arguments   : <IApiArgs.IArgsFetchList>
         *  Result      : <IApiResults.IResultList(IApiResults.IAgencyInfo)>
         */
        CmdExecRes GetAgencyList(int begin, int cnt);

        /**
         *  Modify Agency Info. <CMD_MODIFY_AGENCY>
         *      @param agency       : agency id
         *      @param rank_pro     : rank of professional
         *      @param rank_att     : renk of attitude
         *      @param begin_year   : working years
         *      @return
         *  Arguments: <IApiArgs.IArgsModifyAgencyInfo>
         *  Result   : <IApiResults.ICommon>
         */
        CmdExecRes MofidyAgency(int agency, int rank_pro, int rank_att, int begin_year);

        /*
        *   CMD: NONE
        *       - url: url for cookie
         */
        String GetCookie(String url);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          House
        /**
         *  get brief house inf. <CMD_GET_BRIEF_PUBLIC_HOUSE_INFO>
         *      @param houseId: house id
         *      @return
         *  Result: <IApiResults.IHouseDigest & IApiResults.IResultList(IApiResults.IHouseTag) & IApiResults.IHouseCertDigestInfo>
         *  Arguments: <IApiArgs.IArgsHouseId>
         */
        CmdExecRes GetBriefPublicHouseInfo(int houseId);

       /**
        *   CMD: <CMD_GET_HOUSE_INFO>
        *       - houseid   : house id
        *       - bBackend  : is it for backend using
        *   Arguments:  <IApiArgs.IArgsGetHouseInfo>
        *   Result:     <IApiResults.IGetHouseInfo, IApiResults.IHouseCertInfo>
        */
        CmdExecRes GetHouseInfo(int houseId, boolean bBackend);

        /**
         *  Commit New House by Landlord. <CMD_COMMIT_HOUSE_BY_OWNER>
         *      @param  houseInfo   : house info
         *      @param  agency      : agency id
         *  Arguments: <IApiArgs.IArgsCommitNewHouseByLandlord>
         *  Result     <IApiResults.IAddRes>
         */
        CmdExecRes CommitHouseByOwner(HouseInfo houseInfo, int agency);

        /**
         *  Amend House Info. <CMD_AMEND_HOUSE>
         *      @param houseInfo : new house info
         *      @return
         *  Result      : <IApiResults.ICommon>
         *  Arguments   : <IApiArgs.IArgsAmendHouseInfo>
         */
        CmdExecRes AmendHouse(HouseInfo houseInfo);

        /**
         *  Recommend or Unrecommend house. <CMD_GET_RECOMMEND_HOUSE>
         *      @param house_id : house id
         *      @param action   : RECOMMEND_HOUSE / UNRECOMMEND_HOUSE
         *      @return
         *  Arguments   : <IApiArgs.IArgsRecommendHouse>
         *  Result      : <IApiResults.ICommon>
         */
        CmdExecRes RecommendHouse(int house_id, int action);

        /**
         *  Set House Cover Image. <CMD_GET_SET_HOUSE_COVER_IMAGE>
         *      @param house_id : house id
         *      @param img_id   : image id
         *  @return
         *  Result      : <IApiResults.ICommon>
         *  Arguments   : <IApiArgs.IArgsSetHouseCoverImage>
         */
        CmdExecRes SetHouseCoverImg(int house_id, int img_id);

        /**
         *  Set House New Price, <CMD_SET_HOUSE_PRICE>
         *      @param house_id         : house id
         *      @param rental_tag       : the tag rental, opened for everyone
         *      @param rental_bottom    : the bottom tental, only valid for landlord and agent
         *      @param bIncPropFee      : if the rental include property fee
         *      @param price_tag        : the tag selling price, opened for everyone
         *      @param price_bottom     : the bottom selling price, only valid for landlord and agent
         *  @return
         *  Result    : <IApiResults.IAddRes>
         *  Arguments : <IApiArgs.IArgsSetHousePrice>
         */
        CmdExecRes SetHousePrice(int house_id, int rental_tag, int rental_bottom,
                                 boolean bIncPropFee, int price_tag, int price_bottom);

        /**
         *  Get House Price History. <CMD_GET_HOUSE_PRICE>
         *      @param house_id : house id
         *      @param begin    : fetch begin position
         *      @param count    : fetch count
         *  @return
         *  Result      : <IApiResults.IResultList(IApiResults.IHousePriceInfo)>
         *  Arguments   : <IApiArgs.IArgsGetHousePriceHist>
         */
        CmdExecRes GetHousePrice(int house_id, int begin, int count);

        /**
         *  Certificat New House.   <CMD_CERTIFY_HOUSE>
         *      @param house_id
         *      @param bPass
         *      @param sCertComment
         *  @return
         *  Result   : <IApiResults.ICommon>
         *  Arguments: <IArgsCertifyHouse>
         */
        CmdExecRes CertificateHouse(int house_id, boolean bPass, String sCertComment);

        /**
         *  Get House Certification History.    CMD_GET_HOUSE_CERTIFY_HIST
         *      @param house_id : house id
         *      @return
         *  Result      : IApiResults.IResultList(IApiResults.IHouseCertInfo)), IApiResults.IHouseCertHist
         *  Arguments   : IApiArgs.IArgsHouseId
         */
        CmdExecRes GetHouseCertHist(int house_id);

        /**
        *   Recommit house certification: <CMD_RECOMMIT_HOUSE_CERTIFICATON>
        *       @param house_id  : house id
        *       @param comments  : recommit comments
        *   Result      : <IApiResults.IResultList(IApiResults.IHouseCertInfo)), IApiResults.IHouseCertHist>
        *   Arguments   : <IApiArgs.IArgsRecommitHouseCertify>
        */
        CmdExecRes RecommitHouseCertification(int house_id, String comments);

        /**
        *   Get behalf house list. <CMD_GET_BEHALF_HOUSE_LIST>
        *       - type  : list type. 0 - all; 1 - to rent; 2 - rented; 3 - to sale; 4 - to approve
        *       - begin : fetch begin position
        *       - cnt   : fetch count. set to 0 mean just want to get the total number
        *   Arguments: <IApiArgs.IArgsGetBehalfList>
        *   Result: <IApiResults.IResultList(IApiResults.IHouseDigest, IApiResults.IResultList(IApiResults.IHouseTag))>
         */
        CmdExecRes GetBehalfHouses(int type, int begin, int cnt);

       /**
        *   Get Digest House Info List <CMD_GET_HOUSE_DIGEST_LIST>
        *       - type  : list type. 0: all house; 1: recommend houses; 2: deducted houses; 3: new houses
        *       - begin : fetch begin position
        *       - cnt   : fetch count. set to 0 mean just want to get the total number
        *       - filter: filter confitions. HouseFilterCondition
        *       - sort  : sort condition list
        *   Arguments: <IApiArgs.IArgsGetHouseDigestList>
        *   Result: <IApiResults.IResultList(IApiResults.IHouseDigest, IApiResults.IResultList(IApiResults.IHouseTag))>
        */
        CmdExecRes GetHouseDigestList(int type, int begin, int cnt, HouseFilterCondition filter, ArrayList<Integer> sort);

        /**
         *  Get House Show time. <CMD_GET_HOUSE_SHOWTIME>
         *      @param house_id
         *  @return
         *  Result      : <IApiResults.IHouseShowtime>
         *  Arguments   : <IApiArgs.IArgsHouseId>
         */
        CmdExecRes GetHouseShowtime(int house_id);

        /**
         *  Set House Show Time.  <CMD_SET_HOUSE_SHOWTIME>
         *      @param house_id : house id
         *      @param pw       : period of working-day
         *      @param pv       : period of vacation, including weekend and vacation
         *      @param pd       : period description
         *      @return
         *  Return      : <IApiResults.ICommon>
         *  Arguments   : <IApiArgs.IArgsSetHouseShowtime>
         */
        CmdExecRes SetHouseShowtime(int house_id, int pw, int pv, String pd);          // ,

       /**
        *   CMD: NONE
        *       - property      : property id
        *       - building_no   : building no for new house
        *       - house_no      : house no for new house
        */
        String GetLandlordHouseSubmitConfirmUrl(int property, String building_no, String house_no);

        /**
         *  Assign house agency <CMD_ASSIGN_HOUSE_AGENT>
         *      @param house : house id
         *      @param agent : agent id
         *      @return
         *  Result: <IApiResults.ICommon>
         *  Arguments; <IApiArgs.IArgsAssignHouseAgency>
         */
        CmdExecRes AssignHouseAgency(int house, int agent);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          Property, Community

        /**
         *  Get Propery List by Name. <CMD_GET_PROPERTY_LIST>
         *      @param      sName   : property name to search
         *      @param      nBegin  : begin position
         *      @param      nCount  : how many items to fetch
         *  Arguments  <IApiArgs.IArgsGetPropertyList>
         *  Result     <IApiResults.IResultList(IApiResults.IPropertyInfo)>
         */
        CmdExecRes GetPropertyListByName(String sName, int nBegin, int nCount);

        /**
         *  Add new Property(Estate) <CMD_ADD_PROPERTY>
         *      @param sName
         *      @param sAddr
         *      @param sDesc
         *  @return
         *  Arguments   : <IApiArgs.IArgsAddProperty>
         *  Result      : <IApiResults.IAddRes>
         */
        CmdExecRes AddProperty(String sName, String sAddr, String sDesc);

        /**
         *  Get Property Info <CMD_GET_PROPERTY_INFO>
         *      @param nPropId
         *  @return
         *  Arguments   : <IApiArgs.IArgsObjId>
         *  Result      : <IApiResults.IPropertyInfo>
         */
        CmdExecRes GetPropertyInfo(int nPropId);

        /**
         *  Modify property Info. <CMD_MODIFY_PROPERTY>
         *      @param nPropId
         *      @param sName
         *      @param sAddr
         *      @param sDesc
         *  @return
         *  Arguments : <IApiArgs.IArgsModifyProperty>
         *  Result    : <IApiResults.ICommon>
         */
        CmdExecRes ModifyPropertyInfo(int nPropId, String sName, String sAddr, String sDesc);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          House Deliverables for rent

        /**
         *  Add Deliverables. <CMD_ADD_DELIVERABLE>
         *      @param sName
         *  @return
         *  Arguments: <IApiArgs.IArgsAddDeliverable>
         *  Results: <IApiResults.IAddRes>
         */
        CmdExecRes AddDeliverable(String sName);

        /**
         *  Get Deliverable List. <CMD_GET_DELIVERABLE_LIST>
         *  @return
         *  Arguments: <None>
         *  Result: <IApiResults.IResultList(IApiResults.IDeliverableItem)>
         */
        CmdExecRes GetDeliverableList();

        /**
         *  Modify Deliveralb. <CMD_EDIT_DELIVERABLE>
         *      @param dev_id
         *      @param sName
         *  @return
         *  Arguments: <IApiArgs.IArgsModifyDeliverable>
         *  Results: <IApiResults.ICommon>
         */
        CmdExecRes ModifyDeliverable(int dev_id, String sName);

        /**
         *  Add House Deliverable. <CMD_ADD_HOUSE_DELIVERABLE>
         *      @param house_id
         *      @param deliverable_id
         *      @param qty
         *      @param sDesc
         *  @return
         *  Arguments: <IApiArgs.IArgsAddHouseDeliverable>
         *  Result: <IApiResults.IAddRes>
         */
        CmdExecRes AddHouseDeliverable(int house_id, int deliverable_id, int qty, String sDesc);   // ,

        /**
         *  Get House Deliverables. <CMD_GET_HOUSE_DELIVERABLES>
         *      @param house_id
         *      @return
         *  Arguments : <IApiArgs.IArgsObjId>
         *  Return    : <IApiResults.IResultList(IApiResults.IDeliverableInfo)>
         */
        CmdExecRes GetHouseDeliverables(int house_id);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          House Facilities

        /**
         *  Add Facility Type. <CMD_ADD_FACILITY_TYPE>
         *      @param sTypeName
         *  @return
         *  Arguments: <IApiArgs.IArgsAddFacilityType>
         *  Result: <IApiResults.IAddRes>
         */
        CmdExecRes AddFacilityType(String sTypeName);

        /**
         *  Modify Facility Type. <CMD_EDIT_FACILITY_TYPE>
         *      @param typeId
         *      @param sTypeName
         *  @return
         *  Arguments: <IApiArgs.IArgsEditFacilityType>
         *  Result: <IApiResults.ICommon>
         */
        CmdExecRes EditFacilityType(int typeId, String sTypeName);

        /**
         *  Get Facility Type List. <CMD_GET_FACILITY_TYPE_LIST>
         *  @return
         *  Arguments: None
         *  Result: <IApiResults.IResultList(IApiResults.IListIdName)>
         */
        CmdExecRes GetFacilityTypeList();

        /**
         *  Add Facility. <CMD_ADD_FACILITY>
         *      @param nType
         *      @param sName
         *      @param sIcon
         *  @return
         *  Arguments: <IApiArgs.IArgsAddFacility>
         *  Result: <IApiResults.IAddRes>
         */
        CmdExecRes AddFacility(int nType, String sName, String sIcon);

       /**
        *  Edit Facility. <CMD_EDIT_FACILITY>
        *    @param id
        *    @param nType
        *    @param sName
        *    @param sIcon
        *  @return
        *  Arguments: <IApiArgs.IArgsEditFacility>
        *  Result: <IApiResults.ICommon>
        */
        CmdExecRes EditFacility(int id, int nType, String sName, String sIcon);

     /**
      *  Get Facility List. <CMD_GET_FACILITY_LIST>
      *    @param nType
      *  @return
      *  Arguments: <IApiArgs.IArgsGetFacilityList>
      *  Result: <IApiResults.IResultList(IApiResults.IFacilityInfo)>
      */
        CmdExecRes GetFacilityList(int nType);

        /**
         *  Add Facility for house. <CMD_ADD_HOUSE_FACILITY>
         *      @param house
         *      @param list
         *  @return
         *  Arguments: <IApiArgs.IArgsAddHouseFacility>
         *  Result: <IApiResults.ICommon>
         */
        CmdExecRes AddHouseFacility(int house, ArrayList<IApiArgs.IFacilityItem> list);

        /**
         *  Get House Facility List. <CMD_GET_HOUSEFACILITY_LIST>
         *      @param house
         *  @return
         *  Arguments: <IApiArgs.IArgsObjId>
         *  Result: <IApiResults.IResultList(IApiResults.IHouseFacilityInfo)>
         */
        CmdExecRes GetHouseFacilityList(int house);

        /**
         *  Edit House Facility. <CMD_EDIT_HOUSE_FACILITY>
         *      @param hfid
         *      @param fid
         *      @param qty
         *      @param desc
         *  @return
         *  Arguments: <IApiArgs.IArgsEditHouseFacility>
         *  Result: <IApiResults.ICommon>
         */
        CmdExecRes EditHouseFacility(int hfid, int fid, int qty, String desc);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          Pictures
        /**
         *  upload new picture to server. <CMD_ADD_PICTURE>
         *      @param house    : house id
         *      @param type     : picture type. PIC_TYPE_MAJOR_xxx + PIC_TYPE_SUB_xxx
         *      @param refId    : reference id, depend what type the picture is. for example. if the picture type is house, then the refId is house id
         *      @param desc     : picture description
         *      @param file     : picture file in local
         *  @return
         *  Result      : <IApiResults.IAddPic>
         *  Arguments   : <IApiArgs.IArgsAddPic>
         */
        CmdExecRes AddPicture(int house, int type, int refId, String desc, String file);

        /**
         *  Delete Picture from server. <CMD_DEL_PICTURE>
         *      @param pic: picture id to delete
         *      @return
         *  Result      : <IApiResults.ICommon>
         *  Arguments   : <IApiArgs.IArgsObjId>
         */
        CmdExecRes DelePicture(int pic);

       /**
        *   Get Picture URL <CMD_GET_PIC_URL>
        *       - pic   : picture id
        *       - size  : picture size. PIC_SIZE_xxx
        *   Result: <IApiResults.IPicUrls>
        *   Arguments: <IApiArgs.IArgsGetPicUrls>
        */
        CmdExecRes GetPicUrls(int pic, int size);

       /**
        *   Get House Pictures. <CMD_GET_HOUSE_PIC_LIST>
        *       - house : house id
        *       - type  : house picture sub-type. PIC_TYPE_SUB_HOUSE_xxx
        *       - size  : picture size. PIC_SIZE_xxx
        *   Arguments: <IApiArgs.IArgsGetXPicLst>
        *   Result: <IApiResults.IResultList(IApiResults.IPicInfo, IApiResults.IPicUrls)>
        */
        CmdExecRes GetHousePics(int house, int type, int size);

       /**
        *   Get User Pictures. <CMD_GET_USER_PIC_LIST>
        *       - user  : user id
        *       - type  : user picture sub-type. PIC_TYPE_SUB_USER__xxx
        *       - size  : picture size. PIC_SIZE_xxx
        *   Arguments: <IApiArgs.IArgsGetXPicLst>
        *   Result: <IApiResults.IResultList(IApiResults.IPicInfo, IApiResults.IPicUrls)>
        */
        CmdExecRes GetUserPics(int user, int type, int size);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          Event

        /**
         *  Get House New Event. <CMD_GET_HOUSE_NEW_EVENTS>
         *  @return
         *  Arguments: <None>
         *  Result: <IApiResults.IResultList(IApiResults.IHouseNewEvent)>
         */
        CmdExecRes GetHouseNewEvent();

        /**
         *  Get Event Process History of house. <CMD_GET_EVENT_PROC_LST>
         *      @param  event_id
         *  @return
         *  Arguments: <IApiArgs.IArgsObjId>
         *  Result: <IApiResults.IResultList(IApiResults.IEventProcInfo)>
         */
        CmdExecRes GetHouseEventProcList(int event_id);

        /**
         *  Modify House Event. <CMD_MODIFY_HOUSE_EVENT>
         *      @param event_id
         *      @param desc
         *  @return
         *  Arguments: <IApiArgs.IArgsModifyHouseEvent>
         *  Result: <IApiResults.ICommon>
         */
        CmdExecRes ModifyHouseEvent(int event_id, String desc);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          System Messages
       /**
        *   Get New Message Count of current user. <CMD_GET_NEW_MSG_CNT>
        *   Arguments: <None>
        *   Result : IApiResults.GetNewMsgCount
        */
        CmdExecRes GetNewMsgCount();

       /**
        *   Get System Message Info. <CMD_GET_MSG_INFO>
        *       - msg_id: message id
        *   Arguments: <IApiArgs.IArgsObjId>
        *   Result: <IApiResults.ISysMsgInfo>
        */
        CmdExecRes GetSysMsgInfo(int msg_id);

        /**
         *  Mark a message readed.  <CMD_READ_NEW_MSG>
         *      @param msg_id: message id
         *  @return
         *  Result      : <IApiResults.ICommon>
         *  Arguments   : <IApiArgs.IArgsObjId>
         */
        CmdExecRes ReadNewMsg(int msg_id);

       /**
        *   Get System Message List. <CMD_GET_SYSTEM_MSG_LST>
        *       - posi_bgn  :   begin position to fetch
        *       - fetch_cnt :   how many records want to fetch
        *       - ido       :   true  - only fetch message id;
        *                       false - fetch whole message info
        *       - nmo       :   true  - only fetch new messages
        *                       false - fetch all messages
        *   Arguments: <IApiArgs.IArgsGetMsgList>
        *   Result : <IApiResults.IResultList(IApiResults.ISysMsgInfo)>
        */
        CmdExecRes GetSysMsgList(int posi_bgn, int fetch_cnt, boolean ido, boolean nmo);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          Appointment APIs
       /**
        *   Make an appointment for house seeing. <CMD_APPOINT_SEE_HOUSE>
        *       - house       : house id
        *       - phone       : contact phone. if user logined, it could be empty
        *       - time_begin  : appointment period begin, like 2017-06-24 09:00
        *       - time_end    : appointment period end, like 2017-06-24 09:30
        *       - desc        : appointment description
        *   Result: <IApiResults.IAddRes>
        *   Arguments: <IApiArgs.IArgsMakeAppointmentSeeHouse>
        */
        CmdExecRes MakeAppointment_SeeHouse(int house, String phone, String time_begin, String time_end, String desc);

        /**
         *  Get Appointment List of house seeing. <>
         *      @param house_id : house id
         *      @param begin    : fetch begin position
         *      @param cnt      : fetch count. set to 0 to get the total number
         *  @return
         *  Arguments: <IApiArgs.IArgsGetHouseSeeAppointmentList>
         *  Result: <IApiResults.IResultList>
         */
        CmdExecRes GetHouseSeeAppointmentList(int house_id, int begin, int cnt);

        /**
         *  Get house list that be appointed house seeing.  <CMD_HOUSE_LST_APPOINT_SEE>
         *      @param begin  : fetch begin position
         *      @param cnt    : fetch count. set to 0 mean just want to get the total number
         *      @return
         *  Arguments   : <IApiArgs.IArgsFetchList>
         *  Result      : <IApiResults.IResultList(IApiResults.IHouseDigest, IApiResults.IResultList(IApiResults.IHouseTag))>
         */
        CmdExecRes GetHouseList_AppointSee(int begin, int cnt);

       /**
        *   Make an Action for an appointment. <CMD_MAKE_APPOINTMENT_ACTION>
        *       - apid      : appointment id
        *       - act       : action type: APPOINTMENT_ACTION_xxxx 2: Confirm; 3: Reschedule; 4: Done; 5: Cancel
        *       - time_begin: appointment period begin, like 2017-06-24 09:00
        *       - time_end  : appointment period end, like 2017-06-24 09:30
        *       - comments  : appointment comments
        *   Arguments: <IApiArgs.IArgsMakeAppointmentAct>
        *   Result : <IApiResults.IAddRes>
        */
        CmdExecRes MakeAppointmentAction(int apid, int act, String time_begin, String time_end, String comments);

        /**
         *  Get Appointment Info. <CMD_GET_APPOINTMENT_INFO>
         *      @param apid: appointment id
         *  @return
         *  Result      : <IApiResults.IAppointmentInfo & IApiResults.IResultList(IApiResults.IAppointmentAct)>
         *  Arguments   : <IApiArgs.IArgsObjId>
         */
        CmdExecRes GetAppointmentInfo(int apid);

        /**
         *  Assign receptionist to a appointment. <CMD_ASSIGN_APPOINTMENT_RECEPTIONIST>
         *      @param aid          : appointment id
         *      @param receptionist: the user id of new receptionist
         *  @return
         *  Result: <IApiResults.ICommon>
         *  Arguments: <IApiArgs.IArgsAssignAppointmentReceptionist>
         */
        CmdExecRes AssignAppointmentReceptionist(int aid, int receptionist);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //          House Watch APIs
        /**
         *  Get house watching list of current user. <CMD_GET_USER_HOUSE_WATCH_LIST>
         *      @param begin : fetch begin position
         *      @param cnt   : fetch count, set to 0 means just want to get the totoal number
         *      @return
         *  Arguments: <IApiArgs.IArgsFetchList>
         *  Result: <IApiResults.IResultList(IApiResults.IHouseDigest, IApiResults.IResultList(IApiResults.IHouseTag))>
         */
        CmdExecRes GetUserHouseWatchList(int begin, int cnt);
    }

    public static class CmdRes {
        final public static int CMD_RES_NOERROR         = 0,
                                CMD_RES_NOT_LOGIN       = 1104,
                                CMD_RES_ALREADY_EXIST   = 1105;
    }
    public static class CmdID {
        // Admin
        final public static int CMD_LOGIN_BY_PASSWORD     = 0x1001,
                                CMD_LOGIN_BY_SMS          = 0x1002,
                                CMD_GET_USER_SALT         = 0x1003,
                                CMD_GET_USER_INFO         = 0x1004,
                                CMD_LOG_OUT               = 0x1005,
                                CMD_RELOGIN               = 0x1006,
                                CMD_GET_SMS_CODE          = 0x1007,
                                CMD_GET_AGENCY_LIST       = 0x1008,
                                CMD_MODIFY_AGENCY         = 0x1009,
                                CMD_GET_LOGIN_USER_INFO   = 0x100A,
                                CMD_RESET_LOGIN_PASS      = 0x100B;

        // House
        final public static int CMD_GET_BRIEF_PUBLIC_HOUSE_INFO   = 0x2001,
                                CMD_GET_HOUSE_DIGEST_LIST         = 0x2002,
                                CMD_GET_BEHALF_HOUSE_LIST         = 0x2003,
                                CMD_GET_HOUSE_INFO                = 0x2004,
                                CMD_COMMIT_HOUSE_BY_OWNER         = 0x2005,
                                CMD_AMEND_HOUSE                   = 0x2006,
                                CMD_CERTIFY_HOUSE                 = 0x2007,
                                CMD_GET_SET_HOUSE_COVER_IMAGE     = 0x2008,
                                CMD_GET_RECOMMEND_HOUSE           = 0x2009,
                                CMD_SET_HOUSE_PRICE               = 0x200A,
                                CMD_GET_HOUSE_PRICE               = 0x200B,
                                CMD_GET_HOUSE_SHOWTIME            = 0x200C,
                                CMD_SET_HOUSE_SHOWTIME            = 0x200D,
                                CMD_GET_HOUSE_CERTIFY_HIST        = 0x200E,
                                CMD_RECOMMIT_HOUSE_CERTIFICATON   = 0x200F,
                                CMD_ASSIGN_HOUSE_AGENT            = 0x2010;

        // Property
        final public static int CMD_GET_PROPERTY_LIST     = 0x3001,
                                CMD_GET_PROPERTY_INFO     = 0x3002,
                                CMD_ADD_PROPERTY          = 0x3003,
                                CMD_MODIFY_PROPERTY       = 0x3004;

        // Deliverables
        final public static int CMD_ADD_DELIVERABLE        = 0x4001,
                                CMD_GET_DELIVERABLE_LIST   = 0x4002,
                                CMD_EDIT_DELIVERABLE       = 0x4003,
                                CMD_ADD_HOUSE_DELIVERABLE  = 0x4004,
                                CMD_GET_HOUSE_DELIVERABLES = 0x4005;

        // Facilities
        final public static int CMD_ADD_FACILITY_TYPE      = 0x5001,
                                CMD_EDIT_FACILITY_TYPE     = 0x5002,
                                CMD_GET_FACILITY_TYPE_LIST = 0x5003,
                                CMD_ADD_FACILITY           = 0x5004,
                                CMD_EDIT_FACILITY          = 0x5005,
                                CMD_GET_FACILITY_LIST      = 0x5006,
                                CMD_ADD_HOUSE_FACILITY     = 0x5007,
                                CMD_GET_HOUSEFACILITY_LIST = 0x5008,
                                CMD_EDIT_HOUSE_FACILITY    = 0x5009;

        // Pictures
        final public static int CMD_ADD_PICTURE            = 0x6001,
                                CMD_DEL_PICTURE            = 0x6002,
                                CMD_GET_PIC_URL            = 0x6003,
                                CMD_GET_HOUSE_PIC_LIST     = 0x6004,
                                CMD_GET_USER_PIC_LIST      = 0x6005;

        // Event. TODO: to be removed, replaced by system message
        final public static int CMD_GET_HOUSE_NEW_EVENTS      = 0x7002,
                                CMD_GET_EVENT_PROC_LST        = 0x7005,
                                CMD_MODIFY_HOUSE_EVENT        = 0x7007;

        // Appointment
        final public static int CMD_APPOINT_SEE_HOUSE               = 0x8001,
                                CMD_APPOINT_HOUSE_SEE_LST           = 0x8002,
                                CMD_HOUSE_LST_APPOINT_SEE           = 0x8003,
                                CMD_MAKE_APPOINTMENT_ACTION         = 0x8004,
                                CMD_GET_APPOINTMENT_INFO            = 0x8005,
                                CMD_ASSIGN_APPOINTMENT_RECEPTIONIST = 0x8006;

        // House watch
        final public static int CMD_GET_USER_HOUSE_WATCH_LIST = 0x9001;

        // System Message
        final public static int CMD_GET_NEW_MSG_CNT         = 0xA001,
                                CMD_GET_MSG_INFO            = 0xA002,
                                CMD_READ_NEW_MSG            = 0xA003,
                                CMD_GET_SYSTEM_MSG_LST      = 0xA004;

        public static int CMD_TEST                   = 0x0001;

        private static HashMap<Integer, String> cmdMap;
        static {
            cmdMap = new HashMap<Integer, String>();
            cmdMap.put(CMD_LOGIN_BY_PASSWORD,   "CMD_LOGIN_BY_PASSWORD");
            cmdMap.put(CMD_LOGIN_BY_SMS,        "CMD_LOGIN_BY_SMS");
            cmdMap.put(CMD_GET_USER_SALT,       "CMD_GET_USER_SALT");
            cmdMap.put(CMD_GET_USER_INFO,       "CMD_GET_USER_INFO");
            cmdMap.put(CMD_LOG_OUT,             "CMD_LOG_OUT");
            cmdMap.put(CMD_RELOGIN,             "CMD_RELOGIN");
            cmdMap.put(CMD_GET_SMS_CODE,        "CMD_GET_SMS_CODE");
            cmdMap.put(CMD_GET_AGENCY_LIST,     "CMD_GET_AGENCY_LIST");
            cmdMap.put(CMD_MODIFY_AGENCY,       "CMD_MODIFY_AGENCY");
            cmdMap.put(CMD_GET_LOGIN_USER_INFO, "CMD_GET_LOGIN_USER_INFO");
            cmdMap.put(CMD_RESET_LOGIN_PASS,    "CMD_RESET_LOGIN_PASS");

            cmdMap.put(CMD_GET_BRIEF_PUBLIC_HOUSE_INFO, "CMD_GET_BRIEF_PUBLIC_HOUSE_INFO");
            cmdMap.put(CMD_GET_HOUSE_DIGEST_LIST,       "CMD_GET_HOUSE_DIGEST_LIST");
            cmdMap.put(CMD_GET_BEHALF_HOUSE_LIST,       "CMD_GET_BEHALF_HOUSE_LIST");
            cmdMap.put(CMD_GET_HOUSE_INFO,              "CMD_GET_HOUSE_INFO");
            cmdMap.put(CMD_COMMIT_HOUSE_BY_OWNER,       "CMD_COMMIT_HOUSE_BY_OWNER");
            cmdMap.put(CMD_AMEND_HOUSE,                 "CMD_AMEND_HOUSE");
            cmdMap.put(CMD_CERTIFY_HOUSE,               "CMD_CERTIFY_HOUSE");
            cmdMap.put(CMD_GET_SET_HOUSE_COVER_IMAGE,   "CMD_GET_SET_HOUSE_COVER_IMAGE");
            cmdMap.put(CMD_GET_RECOMMEND_HOUSE,         "CMD_GET_RECOMMEND_HOUSE");
            cmdMap.put(CMD_SET_HOUSE_PRICE,             "CMD_SET_HOUSE_PRICE");
            cmdMap.put(CMD_GET_HOUSE_PRICE,             "CMD_GET_HOUSE_PRICE");
            cmdMap.put(CMD_GET_HOUSE_SHOWTIME,          "CMD_GET_HOUSE_SHOWTIME");
            cmdMap.put(CMD_SET_HOUSE_SHOWTIME,          "CMD_SET_HOUSE_SHOWTIME");
            cmdMap.put(CMD_GET_HOUSE_CERTIFY_HIST,      "CMD_GET_HOUSE_CERTIFY_HIST");
            cmdMap.put(CMD_RECOMMIT_HOUSE_CERTIFICATON, "CMD_RECOMMIT_HOUSE_CERTIFICATON");
            cmdMap.put(CMD_ASSIGN_HOUSE_AGENT,          "CMD_ASSIGN_HOUSE_AGENT");

            // Property
            cmdMap.put(CMD_GET_PROPERTY_LIST,   "CMD_GET_PROPERTY_LIST");
            cmdMap.put(CMD_GET_PROPERTY_INFO,   "CMD_GET_PROPERTY_INFO");
            cmdMap.put(CMD_ADD_PROPERTY,        "CMD_ADD_PROPERTY");
            cmdMap.put(CMD_MODIFY_PROPERTY,     "CMD_MODIFY_PROPERTY");

            // Deliverables
            cmdMap.put(CMD_ADD_DELIVERABLE,         "CMD_ADD_DELIVERABLE");
            cmdMap.put(CMD_GET_DELIVERABLE_LIST,    "CMD_GET_DELIVERABLE_LIST");
            cmdMap.put(CMD_EDIT_DELIVERABLE,        "CMD_EDIT_DELIVERABLE");
            cmdMap.put(CMD_ADD_HOUSE_DELIVERABLE,   "CMD_ADD_HOUSE_DELIVERABLE");
            cmdMap.put(CMD_GET_HOUSE_DELIVERABLES,  "CMD_GET_HOUSE_DELIVERABLES");

            // Facilities
            cmdMap.put(CMD_ADD_FACILITY_TYPE,       "CMD_ADD_FACILITY_TYPE");
            cmdMap.put(CMD_EDIT_FACILITY_TYPE,      "CMD_EDIT_FACILITY_TYPE");
            cmdMap.put(CMD_GET_FACILITY_TYPE_LIST,  "CMD_GET_FACILITY_TYPE_LIST");
            cmdMap.put(CMD_ADD_FACILITY,            "CMD_ADD_FACILITY");
            cmdMap.put(CMD_EDIT_FACILITY,           "CMD_EDIT_FACILITY");
            cmdMap.put(CMD_GET_FACILITY_LIST,       "CMD_GET_FACILITY_LIST");
            cmdMap.put(CMD_ADD_HOUSE_FACILITY,      "CMD_ADD_HOUSE_FACILITY");
            cmdMap.put(CMD_GET_HOUSEFACILITY_LIST,  "CMD_GET_HOUSEFACILITY_LIST");
            cmdMap.put(CMD_EDIT_HOUSE_FACILITY,     "CMD_EDIT_HOUSE_FACILITY");

            // Pictures
            cmdMap.put(CMD_ADD_PICTURE,         "CMD_ADD_PICTURE");
            cmdMap.put(CMD_DEL_PICTURE,         "CMD_DEL_PICTURE");
            cmdMap.put(CMD_GET_PIC_URL,         "CMD_GET_PIC_URL");
            cmdMap.put(CMD_GET_HOUSE_PIC_LIST,  "CMD_GET_HOUSE_PIC_LIST");
            cmdMap.put(CMD_GET_USER_PIC_LIST,   "CMD_GET_USER_PIC_LIST");

            // Event
            cmdMap.put(CMD_GET_HOUSE_NEW_EVENTS,    "CMD_GET_HOUSE_NEW_EVENTS");
            cmdMap.put(CMD_GET_EVENT_PROC_LST,      "CMD_GET_EVENT_PROC_LST");
            cmdMap.put(CMD_MODIFY_HOUSE_EVENT,      "CMD_MODIFY_HOUSE_EVENT");

            // System Message
            cmdMap.put(CMD_GET_NEW_MSG_CNT,         "CMD_GET_NEW_MSG_CNT");
            cmdMap.put(CMD_GET_MSG_INFO,            "CMD_GET_MSG_INFO");
            cmdMap.put(CMD_READ_NEW_MSG,            "CMD_READ_NEW_MSG");
            cmdMap.put(CMD_GET_SYSTEM_MSG_LST,      "CMD_GET_SYSTEM_MSG_LST");

            // Appointment
            cmdMap.put(CMD_APPOINT_SEE_HOUSE,               "CMD_APPOINT_SEE_HOUSE");
            cmdMap.put(CMD_APPOINT_HOUSE_SEE_LST,           "CMD_APPOINT_HOUSE_SEE_LST");
            cmdMap.put(CMD_HOUSE_LST_APPOINT_SEE,           "CMD_HOUSE_LST_APPOINT_SEE");
            cmdMap.put(CMD_MAKE_APPOINTMENT_ACTION,         "CMD_MAKE_APPOINTMENT_ACTION");
            cmdMap.put(CMD_GET_APPOINTMENT_INFO,            "CMD_GET_APPOINTMENT_INFO");
            cmdMap.put(CMD_ASSIGN_APPOINTMENT_RECEPTIONIST, "CMD_ASSIGN_APPOINTMENT_RECEPTIONIST");

            // House watch
            cmdMap.put(CMD_GET_USER_HOUSE_WATCH_LIST, "CMD_GET_USER_HOUSE_WATCH_LIST");
        }

        public static String GetCmdDesc(int nCmd) {
            if (!cmdMap.containsKey(nCmd)) {
                return "Unnamed command";
            }

            String description = cmdMap.get(nCmd);
            if(description == null) {
                description = "";
            }

            return description;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //         -- API data definitions --
    //
    // used in CommitHouse, AmendHouse
    static public class HouseInfo {
        public int      mHouseId        = 0;
        public int      mPropId         = 0;    // property, community
        public String   mBuilding       = "";   // building number, like 177A栋
        public String   mHouseNo        = "";   // house number, 1505室
        public int      mFloorTotal     = 0;    // total floor
        public int      mFloorThis      = 0;    // house floor
        public int      mLivingrooms    = 0;
        public int      mBedrooms       = 0;
        public int      mBathrooms      = 0;
        public int      mAcreage        = 0;    // acreage, 100 times than actual. 10300 means 103 平米
        public boolean  mForSale        = false;
        public boolean  mForRent        = false;
        public int      mDecorate       = 0;    // decoration. 0 - 毛坯 / 1 - 简装 / 2 - 中等 / 3 - 精装 / 4 - 豪华
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

        protected boolean IsHouseInfoEqual(HouseInfo obj) {
            if (mHouseId != obj.mHouseId || mPropId != obj.mPropId || !mBuilding.equals(obj.mBuilding) ||
                    !mHouseNo.equals(obj.mHouseNo) || mFloorTotal != obj.mFloorTotal || mFloorThis != obj.mFloorThis ||
                    mLivingrooms != obj.mLivingrooms || mBedrooms != obj.mBedrooms || mBathrooms != obj.mBathrooms ||
                    mAcreage != obj.mAcreage || mForSale != obj.mForSale || mForRent != obj.mForRent ||
                    mDecorate != obj.mDecorate || !mBuyDate.equals(obj.mBuyDate)) {
                return false;
            }
            return true;
        }
    }

    // used in GetHouseDigestList
    static public class HouseFilterCondition {

        static public int   FILTER_TYPE_EQ      = 1,    // Less Than..      =
                            FILTER_TYPE_LT      = 2,    // Less Than..      <
                            FILTER_TYPE_LE      = 3,    // Less Equal.      <=
                            FILTER_TYPE_GT      = 4,    // Greater Than.    >
                            FILTER_TYPE_GE      = 5,    // Greater Equal.   >=
                            FILTER_TYPE_BETWEEN = 6,    // Between.         >= && <=
                            FILTER_TYPE_IN      = 7;    // In               IN (...)

        static public int   SORT_PUBLISH_TIME       = 1,    // sort by publish time, from early to late
                            SORT_PUBLISH_TIME_DESC  = 2,    // sort by publish time, from late to early
                            SORT_RENTAL             = 3,    // sort by rental, from low to high
                            SORT_RENTAL_DESC        = 4,    // sort by rental, from high to low
                            SORT_APPOINT_NUMB       = 5,    // sort by appointment number, from low to high
                            SORT_APPOINT_NUMB_DESC  = 6;    // sort by appointment number, from high to low

        public IntegerFilter        mRental;
        public IntegerFilter        mLivingroom;
        public IntegerFilter        mBedroom;
        public IntegerFilter        mBathroom;
        public IntegerFilter        mAcreage;
        public ArrayList<Integer>   tags;
        public IntegerFilter        mProperty;

        public HouseFilterCondition() {
            mRental     = new IntegerFilter();
            mLivingroom = new IntegerFilter();
            mBedroom    = new IntegerFilter();
            mBathroom   = new IntegerFilter();
            mAcreage    = new IntegerFilter();
            tags        = new ArrayList<Integer>();
            mProperty   = new IntegerFilter();
        }

        public class IntegerFilter {
            private String              TAG         = getClass().getSimpleName();
            private int                 mFilterType = -1;
            public  ArrayList<Integer>  mValues     = null;

            IntegerFilter() {
                mValues = new ArrayList<Integer>();
            }

            public int FilterIn(ArrayList<Integer> values) {
                if (null == values || values.isEmpty()) {
                    Log.e(TAG, "value is null or empty");
                    return -1;
                }

                for (int n = 0; n < values.size(); n++) {
                    if (0 == values.get(n)) {
                        Log.e(TAG, "value is zero");
                        return -2;
                    }
                }

                mFilterType = FILTER_TYPE_IN;
                mValues = values;
                return 0;
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
                mValues.clear();
                mValues.add(Val1);
                mValues.add(Val2);
                return 0;
            }

            public int FilterEq(int Val) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = FILTER_TYPE_EQ;
                mValues.clear();
                mValues.add(Val);
                return 0;
            }

            public int FilterLessX(int Val, boolean equal) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = equal ? FILTER_TYPE_LE : FILTER_TYPE_LT;
                mValues.clear();
                mValues.add(Val);
                return 0;
            }

            public int FilterGreatX(int Val, boolean equal) {
                if (Val <= 0) {
                    Log.e(TAG, "Val:" + Val);
                    return -1;
                }

                mFilterType = equal ? FILTER_TYPE_GE : FILTER_TYPE_GT;
                mValues.clear();
                mValues.add(Val);
                return 0;
            }

            public int GetOp() { return mFilterType; }

            public ArrayList<Integer> GetValues() {
                return mValues;
            }

            public String GetValuesString() {
                String strVal = "";

                for (int n = 0; n < mValues.size(); n++) {
                    if (n > 0) {
                        strVal += ",";
                    }
                    strVal += mValues.get(n).toString();
                }

                return strVal;
            }

            public int GetValue(int index) {
                if (null == mValues || mValues.size() < index + 1) {
                    return -1;
                }
                if (index > 0) {
                    if (FILTER_TYPE_BETWEEN != mFilterType && FILTER_TYPE_IN != mFilterType) {
                        return -2;
                    }
                }
                return mValues.get(index);
            }
        }
    }
}
