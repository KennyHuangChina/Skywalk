package com.kjs.skywalk.communicationlibrary;

import java.util.ArrayList;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

/**
 * Created by kenny on 2017/12/27.
 */

public class IApiArgs {
    public interface IArgsBase {
        boolean checkArgs();
        boolean isEqual(IArgsBase arg2);
    }

    /******************************************************************************************/
    public interface IArgsGetHouseDigestList extends IArgsBase {
        int                     getType();
        int                     getBeginPosi();
        int                     getFetchCnt();
        HouseFilterCondition    getFilters();
        ArrayList<Integer>      getSorts();
    }

    // command: GetHouseDigestList
    public static int   GET_HOUSE_DIG_LST_TYPE_BEGIN        = 0,
                        GET_HOUSE_DIG_LST_TYPE_ALL          = 0,
                        GET_HOUSE_DIG_LST_TYPE_RECOMMEND    = 1,
                        GET_HOUSE_DIG_LST_TYPE_DEDUCTED     = 2,
                        GET_HOUSE_DIG_LST_TYPE_NEW          = 3,
                        GET_HOUSE_DIG_LST_TYPE_END          = GET_HOUSE_DIG_LST_TYPE_NEW;

    /******************************************************************************************/
    public interface IArgsGetBehalfList extends IArgsBase {
        int                     getType();
        int                     getBeginPosi();
        int                     getFetchCnt();
    }
    // command: CmdGetBehalfHouses
    static public int   AGENT_HOUSE_ALL         = 0,     // all houses
                        AGENT_HOUSE_TO_RENT     = 1,     // agent houses waiting for renting
                        AGENT_HOUSE_RENTED      = 2,     // agent houses already rented
                        AGENT_HOUSE_TO_SALE     = 3,     // agent houses waiting for selling
                        AGENT_HOUSE_TO_APPROVE  = 4,     // agent houses waiting for approving or certification
                        AGENT_HOUSE_END         = AGENT_HOUSE_TO_APPROVE;
    /******************************************************************************************/
    public interface IArgsGetXPicLst extends IArgsBase {
        int getXId();
        int getSubType();   // picture sub-type, ref to PIC_TYPE_SUB_HOUSE_xxx
        int getSize();      // picture size, ref to PIC_SIZE_xxx
    }

    static public int   PIC_TYPE_MAJOR_User     = 100,
                        PIC_TYPE_MAJOR_House    = 200,
                        PIC_TYPE_MAJOR_Rental   = 300;

    // picture sub-type of user
    static public int   PIC_TYPE_SUB_USER_Begin         = 1,
                        PIC_TYPE_SUB_USER_IDCard        = 1,    // User's ID card
                        PIC_TYPE_SUB_USER_Headportrait  = 2,    // user's head-portrait
                        PIC_TYPE_SUB_USER_End           = PIC_TYPE_SUB_USER_Headportrait;

    // picture sub-type of house
    static public int   PIC_TYPE_SUB_HOUSE_BEGIN        = 0,    // all type
                        PIC_TYPE_SUB_HOUSE_FLOOR_PLAN   = 1,    // house plan
                        PIC_TYPE_SUB_HOUSE_FURNITURE    = 2,    // house furnitures
                        PIC_TYPE_SUB_HOUSE_APPLIANCE    = 3,    // house appliances
                        PIC_TYPE_SUB_HOUSE_OwnershipCert= 4,    // owernship certification
                        PIC_TYPE_SUB_HOUSE_RealMap      = 5,    // house real map
                        PIC_TYPE_SUB_HOUSE_END          = PIC_TYPE_SUB_HOUSE_RealMap;

    // picture sub-type of rental
    static public int   PIC_TYPE_SUB_RENTAL_Begin   = 1,
                        PIC_TYPE_SUB_RENTAL_End     = PIC_TYPE_SUB_RENTAL_Begin;
    // picture size
    static public int   PIC_SIZE_ALL        = 0,
                        PIC_SIZE_Small      = 2,
                        PIC_SIZE_Moderate   = 3,
                        PIC_SIZE_Large      = 4;

    /******************************************************************************************/
    public interface IArgsGetSmsCode extends IArgsBase {
        String getUserName();
    }

    /******************************************************************************************/
    public interface IArgsGetHouseInfo extends IArgsBase {
        int     getHouseId();
        boolean isBackendUse();
    }

    /******************************************************************************************/
    public interface IArgsGetMsgList extends IArgsBase {
        int     getBegin();
        int     getFetchCnt();
        boolean isIdOnly();
        boolean isNewMsgOnly();
    }

    /******************************************************************************************/
    public interface IArgsGetPropertyList extends IArgsBase {
        String  getName();
        int     getBegin();
        int     getFetchCnt();
    }

    /******************************************************************************************/
    public interface IArgsGetAgentList extends IArgsBase {
        int     getBegin();
        int     getFetchCnt();
    }

    /******************************************************************************************/
    public interface IArgsCommitNewHouseByLandlord extends IArgsBase {
        HouseInfo   getHouseInfo();
        int         getAgent();
    }

    /******************************************************************************************/
    public interface IArgsSetHousePrice extends IArgsBase {
        int     getHouse();
        int     getRentalTag();
        int     getRentalMin();
        boolean includePropertyFee();
        int     getSellingPriceTag();
        int     getSellingPriceMin();
    }

    /******************************************************************************************/
    public interface IArgsAddPic extends IArgsBase {
        int         getHouse();
        int         getType();      // PIC_TYPE_MAJOR_xxx + sub-type(PIC_TYPE_SUB_USER_xxx, PIC_TYPE_SUB_HOUSE_xxx)
        int         getObjId();
        String      getFile();
        String      getDesc();
    }

    /******************************************************************************************/
    public interface IArgsDelePic extends IArgsBase {
        int getPicId();
    }

    /******************************************************************************************/
    public interface IArgsGetUserInfo extends IArgsBase {
        int getUsrId();
    }
}
