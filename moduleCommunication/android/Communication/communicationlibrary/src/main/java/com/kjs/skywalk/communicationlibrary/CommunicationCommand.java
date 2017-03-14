package com.kjs.skywalk.communicationlibrary;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationCommand {
    // Admin
    public static String CC_LOG_IN_BY_PASSWORD                  = "CommandLogInByPassword";
    public static String CC_LOG_IN_BY_SMS                       = "CommandLogInBySms";
    public static String CC_GET_USER_SALT                       = "CommandGetUserSalt";
    public static String CC_GET_USER_INFO                       = "CommandGetUserInfo";
    public static String CC_LOG_OUT                             = "CommandLogOut";
    public static String CC_RELOGIN                             = "CommandRelogin";
    public static String CC_GET_SMS_CODE                        = "CommandGetSmsCode";

    // House
    public static String CC_GET_BRIEF_PUBLIC_HOUSE_INFO         = "CommandGetBriefPublicHouseInfo";
    public static String CC_GET_HOUSE_LIST                      = "CommandGetHouseList";
    public static String CC_GET_BEHALF_HOUSE_LIST               = "CommandGetBehalfHouseList";
    public static String CC_GET_GET_HOUSE_INFO                  = "CommandGetHouseInfo";
    public static String CC_GET_COMMIT_HOUSE_BY_OWNER           = "CommandCommitHouseByOwner";
    public static String CC_GET_AMEND_HOUSE                     = "CommandAmendHouseInfo";
    public static String CC_GET_CERT_HOUSE                      = "CommandCertificateHouse";
    public static String CC_GET_SET_HOUSE_COVER_IMAGE           = "CommandSetHouseCoverImage";
    public static String CC_GET_RECOMMEND_HOUSE                 = "CommandRecommendHouse";

    // Property
    public static String CC_GET_PROPERTY_LIST                   = "CommandGetPropertyList";
    public static String CC_GET_GET_PROPERTY_INFO               = "CommandGetPropertyInfo";
    public static String CC_ADD_PROPERTY                        = "CommandAddProperty";
    public static String CC_MODIFY_PROPERTY                     = "CommandModifyProperty";

    // Deliverables
    public static String CC_ADD_DELIVERABLE                     = "CommandAddDeliverable";
    public static String CC_GET_DELIVERABLE_LIST                = "CommandGetDeliverableList";
    public static String CC_ADD_HOUSE_DELIVERABLE               = "CommandAddHouseDeliver";
    public static String CC_GET_HOUSE_DELIVERABLES              = "CommandGetHouseDelivers";

    // Facilities
    public static String CC_ADD_FACILITY_TYPE                   = "CommandAddFacilityType";
    public static String CC_GET_FACILITY_TYPE_LIST              = "CommandGetFacilityTypeList";
    public static String CC_ADD_FACILITY                        = "CommandAddFacility";
    public static String CC_GET_FACILITY_LIST                   = "CommandGetFacilityList";

    public static String CC_TEST                                = "CommandTest";
}
