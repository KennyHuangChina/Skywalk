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

    // House & Property
    public static String CC_GET_BRIEF_PUBLIC_HOUSE_INFO         = "CommandGetBriefPublicHouseInfo";
    public static String CC_GET_HOUSE_LIST                      = "CommandGetHouseList";
    public static String CC_GET_GET_HOUSE_INFO                  = "CommandGetHouseInfo";
    public static String CC_GET_PROPERTY_LIST                   = "CommandGetPropertyList";
    public static String CC_GET_GET_PROPERTY_INFO               = "CommandGetPropertyInfo";


    public static String CC_TEST                                = "CommandTest";
}
